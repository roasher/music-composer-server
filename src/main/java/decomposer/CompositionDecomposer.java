package decomposer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import composer.MusicBlockProvider;
import decomposer.form.FormDecomposer;
import model.BlockMovement;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.composition.CompositionInfo;
import model.melody.Melody;
import persistance.dao.LexiconDAO;
import utils.Recombinator;

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
	@Qualifier( "lexiconDAO_database" )
	private LexiconDAO lexiconDAO;

	private Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Decomposes composition into music block list
	 *
	 * @param composition
	 * @param rhythmValue
	 * @return
	 */
	public List<MusicBlock> decomposeIntoMusicBlocks( Composition composition, double rhythmValue ) {
		// analyzing form
		List<List<Melody>> melodyBlockList = formDecomposer.decompose( composition, rhythmValue );
		// recombining result melodies into composeBlockList
		List<List<Melody>> recombineList = recombinator.recombine( melodyBlockList );
		// filling composition information
		List<MusicBlock> lexiconMusicBlocks = new ArrayList<MusicBlock>();
		for ( int melodyBlockNumber = 0; melodyBlockNumber < recombineList.size(); melodyBlockNumber++ ) {
			MusicBlock musicBlock = new MusicBlock( recombineList.get( melodyBlockNumber ), composition.getCompositionInfo() );
			// binding with previous Music Block
			if ( melodyBlockNumber != 0 ) {
				MusicBlock previousMusicBlock = lexiconMusicBlocks.get( melodyBlockNumber - 1 );
				BlockMovement blockMovement = new BlockMovement( previousMusicBlock.getMelodyList(), musicBlock.getMelodyList() );
				musicBlock.setBlockMovementFromPreviousToThis( blockMovement );
				previousMusicBlock.setBlockMovementFromThisToNext( blockMovement );
			}
			lexiconMusicBlocks.add( musicBlock );
		}
		return lexiconMusicBlocks;
	}

	public Lexicon decompose( Composition composition, double rhythmValue ) {
		List<Composition> compositionList = new ArrayList<>();
		compositionList.add( composition );
		return decompose( compositionList, rhythmValue );
	}

	/**
	 * Wraps Music Blocks into Compose Blocks
	 *
	 * @param musicBlockList
	 * @return
	 */
	public Lexicon getComposeBlocks( List<MusicBlock> musicBlockList ) {

		List<ComposeBlock> composeBlocks = new ArrayList<>();
		Map<Integer, List<Integer>> possibleNextMusicBlockNumbers = musicBlockProvider.getAllPossibleNextVariants( musicBlockList );

		for ( int musicBlockNumber = 0; musicBlockNumber < musicBlockList.size(); musicBlockNumber++ ) {
			composeBlocks.add( new ComposeBlock( musicBlockList.get( musicBlockNumber ) ) );
		}

		for ( int composeBlockNumber = 0; composeBlockNumber < composeBlocks.size(); composeBlockNumber++ ) {
			ComposeBlock composeBlock = composeBlocks.get( composeBlockNumber );
			for ( int musicBlockNumber : possibleNextMusicBlockNumbers.get( composeBlockNumber ) ) {
				ComposeBlock possibleNextComposeBlock = composeBlocks.get( musicBlockNumber );
				composeBlock.getPossibleNextComposeBlocks().add( possibleNextComposeBlock );
				// we should check if we need to add previous at first place
				if ( composeBlockNumber + 1 != musicBlockNumber ) {
					possibleNextComposeBlock.getPossiblePreviousComposeBlocks().add( composeBlock );
				} else {
					possibleNextComposeBlock.getPossiblePreviousComposeBlocks().add( 0, composeBlock );
				}
			}
		}

		Lexicon lexicon = new Lexicon( composeBlocks, possibleNextMusicBlockNumbers );
		return lexicon;
	}

	/**
	 * Decomposes compositions into composeBlockList
	 *
	 * @param compositionList
	 * @param rhythmValue
	 * @return
	 */
	public Lexicon decompose( List<Composition> compositionList, double rhythmValue ) {
		logger.info( "Getting persisted blocks" );
		Lexicon dataBaseLexicon = lexiconDAO.fetch();

		logger.info( "Deleting all blocks, build from other than input list compositions" );
		trimToCompositions( dataBaseLexicon.getComposeBlockList(), compositionList );

		logger.info( "Combining blocks from new compositions" );
		List<MusicBlock> musicBlockList = new ArrayList<>();
		for ( Composition composition : compositionList ) {
			if ( !dataBaseLexicon.getCompositionsInLexicon().contains( composition.getCompositionInfo() ) ) {
				logger.info( "Fetching blocks from composition: {}", composition.getCompositionInfo() );
				musicBlockList.addAll( decomposeIntoMusicBlocks( composition, rhythmValue ) );
			}
		}
		Lexicon lexiconFromComposition = getComposeBlocks( musicBlockList );

		logger.info( "Combining blocks from compositions and blocks persistance" );
		Lexicon combinedLexicon = union( dataBaseLexicon, lexiconFromComposition );

		return combinedLexicon;
	}

	/**
	 * Deletes from composeBlockList all blocks that does not belong to compositions in composition list
	 *
	 * @param composeBlockList
	 * @param compositionList
	 * @return
	 */
	private void trimToCompositions( List<ComposeBlock> composeBlockList, List<Composition> compositionList ) {
		for ( Iterator<ComposeBlock> composeBlockIterator = composeBlockList.iterator(); composeBlockIterator.hasNext(); ) {
			ComposeBlock composeBlock = composeBlockIterator.next();

			if ( !isFromCompositionList( composeBlock, compositionList ) ) {
				composeBlockIterator.remove();
			} else {
				// Deleting all non convenient possible next/previous
				for ( Iterator<ComposeBlock> possibleNextComposeBlockIterator = composeBlock.getPossibleNextComposeBlocks().iterator(); possibleNextComposeBlockIterator
						.hasNext(); ) {
					ComposeBlock possibleNextComposeBlock = possibleNextComposeBlockIterator.next();
					if ( !isFromCompositionList( possibleNextComposeBlock, compositionList ) ) {
						possibleNextComposeBlockIterator.remove();
					}
				}
				for ( Iterator<ComposeBlock> possiblePreviousComposeBlockIterator = composeBlock.getPossiblePreviousComposeBlocks().iterator(); possiblePreviousComposeBlockIterator
						.hasNext(); ) {
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
	 *
	 * @param composeBlock
	 * @param compositionList
	 * @return
	 */
	private boolean isFromCompositionList( ComposeBlock composeBlock, List<Composition> compositionList ) {
		boolean fromCompositionList = false;
		for ( Composition composition : compositionList ) {
			if ( composeBlock.getCompositionInfo().equals( composition.getCompositionInfo() ) ) {
				fromCompositionList = true;
				break;
			}
		}
		return fromCompositionList;
	}

	private boolean fromComposition( ComposeBlock composeBlock, CompositionInfo compositionInfo ) {
		return composeBlock.getCompositionInfo().equals( compositionInfo );
	}

	/**
	 * Unions but Changes both input Lists!!!
	 * TODO think of need to impl cloning ?
	 * TODO write tests
	 *
	 * @return
	 */
	private Lexicon union( Lexicon firstLexicon, Lexicon secondLexicon ) {
		if ( secondLexicon.getComposeBlockList().isEmpty() )
			return firstLexicon;
		if ( firstLexicon.getComposeBlockList().isEmpty() )
			return secondLexicon;
		Map<Integer, List<Integer>> unionMap = unionMaps( firstLexicon.getPossibleNextMusicBlockNumbers(), secondLexicon.getPossibleNextMusicBlockNumbers() );
		// adding the possible next/previous
		List<ComposeBlock> firstComposeBlocks = firstLexicon.getComposeBlockList();
		List<ComposeBlock> secondComposeBlocks = secondLexicon.getComposeBlockList();
		for ( int firstComposeBlockNumber = 1; firstComposeBlockNumber < firstComposeBlocks.size(); firstComposeBlockNumber++ ) {
			ComposeBlock firstComposeBlock = firstComposeBlocks.get( firstComposeBlockNumber );
			for ( int secondComposeBlockNumber = 1; secondComposeBlockNumber < secondComposeBlocks.size(); secondComposeBlockNumber++ ) {
				ComposeBlock secondComposeBlock = secondComposeBlocks.get( secondComposeBlockNumber );

				if ( musicBlockProvider.nextAreSubstitutable( firstComposeBlocks.get( firstComposeBlockNumber - 1 ).getMusicBlock(),
						secondComposeBlocks.get( secondComposeBlockNumber - 1 ).getMusicBlock() ) ) {
					// We are assuming that first members of possiblePrevious and possibleNext list is taken from the original composition
					if ( firstComposeBlock.getPossiblePreviousComposeBlocks().size() > 0 ) {
						ComposeBlock originalPreviousFirst = firstComposeBlock.getPossiblePreviousComposeBlocks().get( 0 );
						if ( !secondComposeBlock.getPossiblePreviousComposeBlocks().contains( originalPreviousFirst ) ) {
							secondComposeBlock.getPossiblePreviousComposeBlocks().add( originalPreviousFirst );
						}
						if ( !originalPreviousFirst.getPossibleNextComposeBlocks().contains( secondComposeBlock ) ) {
							originalPreviousFirst.getPossibleNextComposeBlocks().add( secondComposeBlock );
							int originalPreviousFirstNumber = unionMap.get( firstComposeBlockNumber ).get( 0 );
							unionMap.get( originalPreviousFirstNumber ).add( secondComposeBlockNumber );
						}
					}

					if ( secondComposeBlock.getPossiblePreviousComposeBlocks().size() > 0 ) {
						ComposeBlock originalPreviousSecond = secondComposeBlock.getPossiblePreviousComposeBlocks().get( 0 );
						if ( !firstComposeBlock.getPossiblePreviousComposeBlocks().contains( originalPreviousSecond ) ) {
							firstComposeBlock.getPossiblePreviousComposeBlocks().add( originalPreviousSecond );
						}
						if ( !originalPreviousSecond.getPossibleNextComposeBlocks().contains( firstComposeBlock ) ) {
							originalPreviousSecond.getPossibleNextComposeBlocks().add( firstComposeBlock );
							int originalPreviousSecondNumber = unionMap.get( secondComposeBlockNumber ).get( 0 );
							unionMap.get( originalPreviousSecondNumber ).add( firstComposeBlockNumber );
						}
					}
				}
			}
		}
		List<ComposeBlock> union = new ArrayList<>( firstComposeBlocks );
		union.addAll( secondComposeBlocks );
		return new Lexicon( union, unionMap );
	}

	private Map<Integer, List<Integer>> unionMaps( Map<Integer, List<Integer>> firstMap, Map<Integer, List<Integer>> secondMap ) {
		Map<Integer, List<Integer>> union = new HashMap<>( firstMap );
		for ( Map.Entry<Integer, List<Integer>> mapEntry : secondMap.entrySet() ) {
			union.put( mapEntry.getKey() + firstMap.size(), mapEntry.getValue() );
		}
		return union;
	}

	public void setLexiconDAO( LexiconDAO lexiconDAO ) {
		this.lexiconDAO = lexiconDAO;
	}

	public LexiconDAO getLexiconDAO() {
		return lexiconDAO;
	}
}
