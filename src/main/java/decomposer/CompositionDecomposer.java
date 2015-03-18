package decomposer;

import composer.MusicBlockProvider;
import decomposer.form.FormDecomposer;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Melody;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import utils.Recombinator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class analyses and decomposes the composition, creating MusicBlocks
 * Created by Pavel Yurkin on 21.07.14.
 */
@Component
public class CompositionDecomposer implements ApplicationContextAware {

    private ApplicationContext applicationContext;

	@Autowired
	private FormDecomposer formDecomposer;

	@Autowired
	private Recombinator recombinator;

    @Autowired
    private MusicBlockProvider musicBlockProvider;

	/**
	 * Decomposes composition into music block list
	 * @param composition
	 * @param rhythmValue
	 * @return
	 */
    public List<MusicBlock> decomposeIntoMusicBlocks(Composition composition, double rhythmValue) {
		// analyzing form
		List< List< Melody > > melodyBlockList = formDecomposer.decompose( composition, rhythmValue );
		// recombining result melodies into lexicon
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
        List<MusicBlock> musicBlockList = decomposeIntoMusicBlocks( composition, rhythmValue );
		List<ComposeBlock> composeBlockList = getComposeBlocks( musicBlockList );
        Lexicon lexicon = new Lexicon( composeBlockList );
        return lexicon;
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
	 * Decomposes compositions into lexicon
	 * @param compositionList
	 * @param rhythmValue
	 * @return
	 */
	public Lexicon decompose ( List< Composition > compositionList, double rhythmValue ) {
		List< MusicBlock > musicBlockList = new ArrayList<>();
		for ( Composition composition : compositionList ) {
			musicBlockList.addAll( decomposeIntoMusicBlocks(composition, rhythmValue) );
		}
		List<ComposeBlock> composeBlockList = getComposeBlocks( musicBlockList );
		Lexicon lexicon = new Lexicon( composeBlockList );
        return lexicon;
	}


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
