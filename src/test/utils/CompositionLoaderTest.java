package utils;

import helper.AbstractSpringTest;
import jm.music.data.Note;
import jm.music.data.Phrase;
import jm.util.Read;
import model.composition.Composition;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by pyurkin on 25.11.14.
 */
public class CompositionLoaderTest extends AbstractSpringTest {

	Logger logger = LoggerFactory.getLogger( getClass() );

	@Autowired
	private CompositionLoader compositionLoader;
	@Autowired
	private RhythmValueHandler rhythmValueHandler;

	@Test
	public void correctInstrumentNumberTest() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" ) );
		assertEquals( composition.getPartArray().length, 4 );
	}

	@Test
//	@Ignore
	public void loadScarecrow() {
		File file = new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" );

		Composition compositionAsIs = new Composition(  );
		Read.midi( compositionAsIs, file.getAbsolutePath() );
		Composition compositionWithRoundedRhythm = compositionLoader.getComposition( file );

		assertEquals( rhythmValueHandler.roundRhythmValue( compositionAsIs.getEndTime() ), compositionWithRoundedRhythm.getEndTime() );
		assertEquals( compositionAsIs.getPartArray().length, compositionWithRoundedRhythm.getPartArray().length );
		for ( int instrumentNumber = 0; instrumentNumber < compositionAsIs.getPartArray().length; instrumentNumber++ ) {
//			assertEquals( Utils.roundRhythmValue( compositionAsIs.getPart( instrumentNumber ).getEndTime() ), compositionWithRoundedRhythm.getPart( instrumentNumber ).getEndTime() );
			Phrase firstNoteList = compositionAsIs.getPart( instrumentNumber ).getPhrase( 0 );
			Phrase secondNoteList = compositionWithRoundedRhythm.getPart( instrumentNumber ).getPhrase( 0 );
			noteRounding( firstNoteList, secondNoteList, compositionAsIs.getTempo() );
		}

//		Write.midi( compositionWithRoundedRhythm, "Scarecrow output.mid" );
		assertTrue( false );
		// Testing only by ear
	}

	private void noteRounding( Phrase firstPhrase, Phrase secondPhrase, double tempo ) {
		logger.info( "------------------------ Logging new note lists ------------------------");
		assertEquals( firstPhrase.size(), secondPhrase.size() );
//		Calendar calendar = Calendar.getInstance();
		for ( int currentNoteNumber = 0; currentNoteNumber < firstPhrase.size(); currentNoteNumber++ ) {
			Note firstNote = firstPhrase.getNoteArray()[ currentNoteNumber ];
			Note secondNote = secondPhrase.getNoteArray()[ currentNoteNumber ];
			assertEquals( firstNote.getPitch(), secondNote.getPitch() );

			double time = firstNote.getMyPhrase().getNoteStartTime( currentNoteNumber );
			double firstNoteRhythmValue = firstNote.getRhythmValue();
			double secondNoteRhythmValue = secondNote.getRhythmValue();
			double diff = firstNoteRhythmValue - secondNoteRhythmValue;

			if( Math.abs( diff ) > 0.05 ) {
				logger.info( "{}, №{} , pitch {}, first {}, second {}, diff = {}", time, currentNoteNumber, firstNote.getPitch(), firstNoteRhythmValue, secondNoteRhythmValue, firstNoteRhythmValue - secondNoteRhythmValue  );
			}
		}
	}
}
