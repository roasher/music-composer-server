package decomposer;

import composer.MusicBlockProvider;
import database.LexiconDAO;
import decomposer.form.FormDecomposer;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.composition.CompositionInfo;
import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.Recombinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class analyses and decomposes the composition, creating MusicBlocks
 * Created by Pavel Yurkin on 21.07.14.
 */
@Component
public class CompositionDecomposer {

	@Autowired
	private FormDecomposer formDecomposer;

	@Autowired
	private Recombinator recombinator;

    @Autowired
    private MusicBlockProvider musicBlockProvider;

	@Autowired
	private LexiconDAO lexiconDAO;

	private Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Decomposes composition into music block list
	 * @param composition
	 * @param rhythmValue
	 * @return
	 */
    public List<MusicBlock> decomposeIntoMusicBlocks(Composition composition, double rhythmValue) {
		// analyzing form
		List< List< Melody > > melodyBlockList = formDecomposer.decompose( composition, rhythmValue );
		// recombining result melodies into composeBlockList
		List< List< Melody > > recombineList = recombinator.recombine( melodyBlockList );
		// filling composition information
		List< MusicBlock > lexiconMusicBlocks = new ArrayList< MusicBlock >();
		for ( int melodyBlockNumber = 0; melodyBlockNumber < recombineList.size(); melodyBlockNumber ++ ) {
            MusicBlock musicBlock = new MusicBlock(recombineList.get(melodyBlockNumber), composition.getCompositionInfo());
            // binding with previous Music Block
            if (melodyBlockNumber != 0) {
                MusicBlock previousMusicBlock = lexiconMusicBlocks.get(melodyBlockNumber - 1);
                musicBlock.setPrevious(previousMusicBlock);
                previousMusicBlock.setNext(musicBlock);
            }
            lexiconMusicBlocks.add(musicBlock);
        }
		return lexiconMusicBlocks;
    }

    public Lexicon decompose( Composition composition, double rhythmValue ) {
		List<Composition> compositionList = new ArrayList<>(  );
		compositionList.add( composition );
        return decompose( compositionList, rhythmValue );
    }

    /**
     * Wraps Music Blocks into Compose Blocks
     * @param musicBlockList
     * @return
     */
    public List<ComposeBlock> getComposeBlocks( List<MusicBlock> musicBlockList ) {

		class ComposeBlockWrapper {
			ComposeBlock composeBlock;
			List<MusicBlock> possibleNextMusicBlocks;
			ComposeBlockWrapper( ComposeBlock composeBlock, List<MusicBlock> possibleNextMusicBlocks ) {
				this.composeBlock = composeBlock; this.possibleNextMusicBlocks = possibleNextMusicBlocks;
			}
		}

        List<ComposeBlockWrapper> composeBlockWrapperList = new ArrayList<>();
		List<ComposeBlock> composeBlockList = new ArrayList<>(  );
        for ( MusicBlock musicBlock : musicBlockList ) {
            List< MusicBlock > possibleNextMusicBlockList = musicBlockProvider.getAllPossibleNextVariants( musicBlock, musicBlockList );
            ComposeBlockWrapper composeBlockWrapper = new ComposeBlockWrapper( new ComposeBlock( musicBlock ), possibleNextMusicBlockList );
            composeBlockWrapperList.add( composeBlockWrapper );
			composeBlockList.add( composeBlockWrapper.composeBlock );
        }
		for ( ComposeBlockWrapper composeBlockWrapper : composeBlockWrapperList ) {
			for ( MusicBlock musicBlock : composeBlockWrapper.possibleNextMusicBlocks ) {
				for ( ComposeBlockWrapper anotherComposeBlockWrapper : composeBlockWrapperList ) {
					// Using "==" is legal
					if ( musicBlock == anotherComposeBlockWrapper.composeBlock.getMusicBlock() ) {
						composeBlockWrapper.composeBlock.getPossibleNextComposeBlocks().add( anotherComposeBlockWrapper.composeBlock );
						anotherComposeBlockWrapper.composeBlock.getPossiblePreviousComposeBlocks().add( composeBlockWrapper.composeBlock );
					}
				}
//				musicBlock.getPossiblePreviousComposeBlocks().add( composeBlockWrapper );
			}
		}
        return composeBlockList;
    }

	/**
	 * Decomposes compositions into composeBlockList
	 * @param compositionList
	 * @param rhythmValue
	 * @return
	 */
	public Lexicon decompose ( List< Composition > compositionList, double rhythmValue ) {
		logger.info( "Getting blocks from database" );
		Lexicon dataBaseLexicon = lexiconDAO.fetch();

		logger.info( "Deleting all blocks, build from other than input list compositions" );
		trimToCompositions( dataBaseLexicon.getComposeBlockList(), compositionList );

		logger.info( "Combining blocks from new compositions" );
		List< MusicBlock > musicBlockList = new ArrayList<>();
		for ( Composition composition : compositionList ) {
			if ( !dataBaseLexicon.getCompositionsInLexicon().contains( composition.getCompositionInfo() ) ) {
				logger.info( "Fetching blocks from composition: {}", composition.getCompositionInfo() );
				musicBlockList.addAll( decomposeIntoMusicBlocks( composition, rhythmValue ) );
			}
		}
		List<ComposeBlock> composeBlockList = getComposeBlocks( musicBlockList );

		logger.info( "Combining blocks from compositions and blocks from database" );
		List<ComposeBlock> combinedComposeBlockList = union( dataBaseLexicon.getComposeBlockList(), composeBlockList );

		Lexicon lexicon = new Lexicon( combinedComposeBlockList );
        return lexicon;
	}

	/**
	 * Deletes from composeBlockList all blocks that does not belong to compositions in composition list
	 * @param composeBlockList
	 * @param compositionList
	 * @return
	 */
	private void trimToCompositions ( List<ComposeBlock> composeBlockList, List<Composition> compositionList ) {
		for ( Iterator<ComposeBlock> composeBlockIterator = composeBlockList.iterator(); composeBlockIterator.hasNext(); ) {
			ComposeBlock composeBlock = composeBlockIterator.next();

			if ( !isFromCompositionList( composeBlock, compositionList ) ) {
				composeBlockIterator.remove();
			} else {
				// Deleting all non convenient possible next/previous
				for ( Iterator<ComposeBlock> possibleNextComposeBlockIterator = composeBlock.getPossibleNextComposeBlocks().iterator(); possibleNextComposeBlockIterator.hasNext(); ) {
					ComposeBlock possibleNextComposeBlock = possibleNextComposeBlockIterator.next();
					if ( !isFromCompositionList( possibleNextComposeBlock, compositionList ) ) {
						possibleNextComposeBlockIterator.remove();
					}
				}
				for ( Iterator<ComposeBlock> possiblePreviousComposeBlockIterator = composeBlock.getPossiblePreviousComposeBlocks().iterator(); possiblePreviousComposeBlockIterator.hasNext(); ) {
					ComposeBlock possiblePreviousComposeBlock = possiblePreviousComposeBlockIterator.next();
					if ( !isFromCompositionList( possiblePreviousComposeBlock, compositionList ) ) {
						possiblePreviousComposeBlockIterator.remove();
					}
				}
			}
		}
	}

	/**
	 * Finds if input block's original composition is in composition list
	 * @param composeBlock
	 * @param compositionList
	 * @return
	 */
	private boolean isFromCompositionList( ComposeBlock composeBlock, List<Composition> compositionList ) {
		boolean fromCompositionList = false;
		for ( Composition composition : compositionList ) {
			if ( composeBlock.getMusicBlock().getCompositionInfo().equals( composition.getCompositionInfo() ) ) {
				fromCompositionList = true;
				break;
			}
		}
		return fromCompositionList;
	}

	private boolean fromComposition( ComposeBlock composeBlock, CompositionInfo compositionInfo ) {
		return composeBlock.getMusicBlock().getCompositionInfo().equals( compositionInfo );
	}

	/**
	 * Unions but Changes both input Lists!!!
	 * TODO think of need to impl cloning ?
	 * @param firstComposeBlockList
	 * @param secondComposeBlockList
	 * @return
	 */
	private List<ComposeBlock> union( List<ComposeBlock> firstComposeBlockList, List<ComposeBlock> secondComposeBlockList ) {
		List<ComposeBlock> union = new ArrayList<>( firstComposeBlockList );
		union.addAll( secondComposeBlockList );
		// adding the possible next/previous
		for ( ComposeBlock firstComposeBlock : firstComposeBlockList ) {
			for ( ComposeBlock secondComposeBlock : secondComposeBlockList ) {
				if ( musicBlockProvider.canSubstitute( firstComposeBlock.getMusicBlock(), secondComposeBlock.getMusicBlock() ) ) {
					// We are assuming that first members of possiblePrevious and possibleNext list is taken from the original composition
					if ( firstComposeBlock.getPossiblePreviousComposeBlocks().size() > 0 &&
					  !firstComposeBlock.getPossiblePreviousComposeBlocks().get( 0 ).getPossibleNextComposeBlocks().contains( secondComposeBlock ) ) {
						firstComposeBlock.getPossiblePreviousComposeBlocks().get( 0 ).getPossibleNextComposeBlocks().add( secondComposeBlock );
					}
					if ( firstComposeBlock.getPossibleNextComposeBlocks().size() > 0 &&
					  !firstComposeBlock.getPossibleNextComposeBlocks().get( 0 ).getPossiblePreviousComposeBlocks().contains( secondComposeBlock ) ) {
						firstComposeBlock.getPossibleNextComposeBlocks().get( 0 ).getPossiblePreviousComposeBlocks().add( secondComposeBlock );
					}
					if ( secondComposeBlock.getPossiblePreviousComposeBlocks().size() > 0 &&
					  !secondComposeBlock.getPossiblePreviousComposeBlocks().get( 0 ).getPossibleNextComposeBlocks().contains( firstComposeBlock ) ) {
						secondComposeBlock.getPossiblePreviousComposeBlocks().get( 0 ).getPossibleNextComposeBlocks().add( firstComposeBlock );
					}
					if ( secondComposeBlock.getPossibleNextComposeBlocks().size() > 0 &&
					  !secondComposeBlock.getPossibleNextComposeBlocks().get( 0 ).getPossiblePreviousComposeBlocks().contains( firstComposeBlock ) ) {
						secondComposeBlock.getPossibleNextComposeBlocks().get( 0 ).getPossiblePreviousComposeBlocks().add( firstComposeBlock );
					}
				}
			}
		}
		return union;
	}
}
