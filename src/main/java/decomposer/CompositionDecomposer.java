package decomposer;

import com.sun.javafx.charts.Legend;
import decomposer.form.FormDecomposer;
import jm.music.data.Score;
import model.Lexicon;
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

	/**
	 * Decomposes compostition into music block list
	 * @param composition
	 * @param rhythmValue
	 * @return
	 */
    public Lexicon decompose( Composition composition, double rhythmValue ) {
		// analyzing form
		List< List< Melody > > melodyBlockList = formDecomposer.decompose( composition, rhythmValue );
		// recombining result melodies into lexicon
		List< List< Melody > > recombineList = recombinator.recombine( melodyBlockList );
		// filling composition information
		List< MusicBlock > lexiconMusicBlocks = new ArrayList< MusicBlock >();
		for ( int melodyBlockNumber = 0; melodyBlockNumber < recombineList.size(); melodyBlockNumber ++ ) {
			MusicBlock musicBlock = new MusicBlock( recombineList.get( melodyBlockNumber ), composition.getCompositionInfo() );
			// binding with previous Music Block
			if ( melodyBlockNumber != 0 ) {
				MusicBlock previousMusicBlock = lexiconMusicBlocks.get( melodyBlockNumber - 1 );
				musicBlock.setPrevious( previousMusicBlock );
				previousMusicBlock.setNext( musicBlock );
			}
			lexiconMusicBlocks.add( musicBlock );
		}
		return new Lexicon( lexiconMusicBlocks );
    }

	/**
	 * Decomposes compositions into music block list
	 * @param compositionList
	 * @param rhythmValue
	 * @return
	 */
	public Lexicon decompose ( List< Composition > compositionList, double rhythmValue ) {
		List< MusicBlock > musicBlockList = new ArrayList<>();
		for ( Composition composition : compositionList ) {
			musicBlockList.addAll( decompose( composition, rhythmValue ).getMusicBlockList() );
		}
		return new Lexicon( musicBlockList );
	}

}
