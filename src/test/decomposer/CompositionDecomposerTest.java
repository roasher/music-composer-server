package decomposer;

import com.sun.swing.internal.plaf.metal.resources.metal;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.music.data.Note;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Form;
import model.melody.Melody;
import org.junit.Test;
import org.mockito.internal.matchers.LessOrEqual;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static jm.JMC.*;

public class CompositionDecomposerTest extends AbstractSpringTest {

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Autowired
	private CompositionLoader compositionLoader;

	@Test
	public void getLexiconTest() {
		String fileName = "AABC1_with_base.mid";
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\simpleMelodies\\" + fileName ) );
		Lexicon lexicon = compositionDecomposer.decompose( composition, WHOLE_NOTE );

		int expectedMusicBlocksNumber = 8 + 8 + 3 + 3;
		assertEquals( lexicon.getMusicBlockList().size(), expectedMusicBlocksNumber );
		// length check
		double rhythmValue = 0;
		for ( int musicBlockNumber = 0; musicBlockNumber < expectedMusicBlocksNumber; musicBlockNumber ++ ) {
			// Music Block ckecks
			MusicBlock musicBlock = lexicon.getMusicBlockList().get( musicBlockNumber );
			if ( musicBlockNumber != expectedMusicBlocksNumber - 1 ) {
				assertEquals( musicBlock.getNext(), lexicon.getMusicBlockList().get( musicBlockNumber + 1 ) );
			} else {
				assertEquals( musicBlock.getNext(), null );
			}

			if ( musicBlockNumber != 0 ) {
				assertEquals( musicBlock.getPrevious(), lexicon.getMusicBlockList().get( musicBlockNumber - 1 ) );
			} else {
				assertEquals( musicBlock.getPrevious(), null );
			}

			assertEquals( musicBlock.getCompositionInfo().getTitle(), fileName );

			// Melody ckecks
			List<Melody> melodyList = musicBlock.getMelodyList();
			assertEquals( melodyList.size(), 2 );

			Melody firstMelody = melodyList.get( 0 );
			Melody secondMelody = melodyList.get( 1 );
			assertEquals( firstMelody.getNoteArray().length, 1 );
			assertEquals( secondMelody.getNoteArray().length, 1 );
			assertEquals( firstMelody.getStartTime(), rhythmValue );
			assertEquals( secondMelody.getStartTime(), rhythmValue );
			rhythmValue += firstMelody.getNote( 0 ).getRhythmValue();

			Note firstNote = firstMelody.getNote( 0 );
			Note secondNote = secondMelody.getNote( 0 );
			// Note checks
			if ( musicBlockNumber < 8 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( musicBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'A' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( musicBlockNumber >= 8 && musicBlockNumber < 8 + 8 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( musicBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'A' ) );
				assertEquals( secondMelody.getForm(), new Form( 'B' ) );
			} else if ( musicBlockNumber == 8 + 8 || musicBlockNumber == 8 + 8 + 1 ) {
				assertEquals( firstNote.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( secondNote.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( musicBlock.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'B' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( musicBlockNumber == 8 + 8 + 2 ) {
				assertEquals( firstNote.getRhythmValue(), QUARTER_NOTE );
				assertEquals( secondNote.getRhythmValue(), QUARTER_NOTE );
				assertEquals( musicBlock.getRhythmValue(), QUARTER_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'B' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( musicBlockNumber == 8 + 8 + 3 || musicBlockNumber == 8 + 8 + 4 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( musicBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'C' ) );
				assertEquals( secondMelody.getForm(), new Form( 'C' ) );
			} else {
				assertEquals( firstNote.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( secondNote.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( musicBlock.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'C' ) );
				assertEquals( secondMelody.getForm(), new Form( 'C' ) );
			}
		}
	}

	@Test
	public void test() {
		// TODO разобраться с импортом. Проблема если части заканчиваются не одномоментно
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Another Phoenix (midi)_2.mid" ) );
		Lexicon lexicon = compositionDecomposer.decompose( composition, JMC.WHOLE_NOTE );
	}

}
