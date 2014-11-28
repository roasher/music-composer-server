package utils;

import jm.music.data.Note;
import jm.util.Read;
import jm.util.Write;
import model.composition.Composition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static utils.Utils.rhythmValues;

/**
 * Created by pyurkin on 25.11.14.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = "classpath:spring.configuration.xml" )
public class CompositionLoaderTest {

	Logger logger = LoggerFactory.getLogger( getClass() );

	@Autowired
	private CompositionLoader compositionLoader;

	@Test
	public void correctInstrumentNumberTest() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\testCases\\quartets\\2.Scarecrow's song (midi).mid" ) );
		assertEquals( composition.getPartArray().length, 4 );
	}

	@Test
	public void loadScarecrow() {
		File file = new File( "src\\test\\decomposer\\form\\testCases\\quartets\\2.Scarecrow's song (midi).mid" );

		Composition compositionAsIs = new Composition(  );
		Read.midi( compositionAsIs, file.getAbsolutePath() );
		Composition compositionWithRoundedRhythm = compositionLoader.getComposition( file );

		assertEquals( Utils.roundRhythmValue( compositionAsIs.getEndTime() ) , compositionWithRoundedRhythm.getEndTime() );
		assertEquals( compositionAsIs.getPartArray().length, compositionWithRoundedRhythm.getPartArray().length );
		for ( int instrumentNumber = 0; instrumentNumber < compositionAsIs.getPartArray().length; instrumentNumber++ ) {
//			assertEquals( Utils.roundRhythmValue( compositionAsIs.getPart( instrumentNumber ).getEndTime() ), compositionWithRoundedRhythm.getPart( instrumentNumber ).getEndTime() );
			List<Note> firstNoteList = Arrays.asList( compositionAsIs.getPart( instrumentNumber ).getPhrase( 0 ).getNoteArray() );
			List<Note> secondNoteList = Arrays.asList( compositionWithRoundedRhythm.getPart( instrumentNumber ).getPhrase( 0 ).getNoteArray() );
			noteRounding( firstNoteList, secondNoteList );
		}

		Write.midi( compositionWithRoundedRhythm, "Scarecrow output.mid" );
		assertTrue( false );
		// Testing only by ear
	}

	private void noteRounding( List<Note> firstNoteList, List<Note> secondNoteList ) {
		logger.info( "------------------------ Logging new note lists ------------------------");
		assertEquals( firstNoteList.size(), secondNoteList.size() );
		for ( int currentNoteNumber = 0; currentNoteNumber < firstNoteList.size(); currentNoteNumber++ ) {
			Note firstNote = firstNoteList.get( currentNoteNumber );
			Note secondNote = secondNoteList.get( currentNoteNumber );
			assertEquals( firstNote.getPitch(), secondNote.getPitch() );

			double firstNoteRhythmValue = firstNote.getRhythmValue();
			double secondNoteRhythmValue = secondNote.getRhythmValue();
			double diff = firstNoteRhythmValue - secondNoteRhythmValue;

//			if( Math.abs( diff ) > 0.05 ) {
				logger.info( "№{} , pitch {}, first {}, second {}, diff = {}", currentNoteNumber, firstNoteList.get( currentNoteNumber ).getPitch(), firstNoteRhythmValue, secondNoteRhythmValue, firstNoteRhythmValue - secondNoteRhythmValue  );
//			}
		}
	}
}
