package decomposer;

import decomposer.form.FormDecomposer;
import jm.music.data.Score;
import model.MusicBlock;
import model.composition.Composition;
import model.composition.CompositionInfo;
import model.melody.Melody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.Recombinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public List<MusicBlock > decompose( Composition composition, double rhythmValue ) {
		// analyzing form
		List< List< Melody > > melodyBlockList = formDecomposer.decompose( composition, rhythmValue );
		// recombining result melodies into lexicon
		List< List< Melody > > recombineList = recombinator.recombine( melodyBlockList );
		// filling composition information
		List< MusicBlock > lexicon = new ArrayList< MusicBlock >();
		for ( int melodyBlockNumber = 0; melodyBlockNumber < recombineList.size(); melodyBlockNumber ++ ) {
			MusicBlock musicBlock = new MusicBlock( recombineList.get( melodyBlockNumber ), composition.getCompositionInfo() );
			// binding with previous Music Block
			if ( melodyBlockNumber != 0 ) {
				MusicBlock previousMusicBlock = lexicon.get( melodyBlockNumber - 1 );
				musicBlock.setPrevious( previousMusicBlock );
				previousMusicBlock.setNext( musicBlock );
			}
			lexicon.add( musicBlock );
		}
		return lexicon;
    }

}
