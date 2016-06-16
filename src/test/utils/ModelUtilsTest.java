package utils;

import static jm.constants.Durations.DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.C0;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.CS4;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static utils.ModelUtils.getNoteNameByPitch;
import static utils.ModelUtils.areParallel;
import static utils.ModelUtils.retrieveIntervalPattern;
import static utils.ModelUtils.trimToTime;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jm.music.data.Note;
import jm.music.data.Rest;
import model.melody.Melody;

/**
 * Created by Pavel Yurkin on 20.07.14.
 */
public class ModelUtilsTest {

	@Test
    public void testGetIntervalPattern() throws Exception {
        List< Integer > pitches = new ArrayList< Integer >( );
        // 60 62 64 64 70 89 100
        pitches.add( 100 );
        pitches.add( 60 );
        pitches.add( 62 );
        pitches.add( 89 );
        pitches.add( 64 );
        pitches.add( 64 );
        pitches.add( 70 );
		pitches.add( Note.REST );

        List< Integer > intervalPattern = new ArrayList< Integer >( pitches.size() - 1 );
        // REST <REST> 60 <2> 62 <2> 64 <0> 64 <6> 70 <19> 89 <11> 100
		intervalPattern.add( Note.REST );
        intervalPattern.add( 2 );
        intervalPattern.add( 2 );
        intervalPattern.add( 0 );
        intervalPattern.add( 6 );
        intervalPattern.add( 19 );
        intervalPattern.add( 11 );

        assertEquals( intervalPattern, retrieveIntervalPattern( pitches ) );
    }

	@Test
	public void testGetIntervalPattern1() throws Exception {
		List< Integer > pitches = new ArrayList< Integer >( );
		// 60 62 89 100
		pitches.add( 100 );
		pitches.add( Note.REST );
		pitches.add( 60 );
		pitches.add( 62 );
		pitches.add( Note.REST );
		pitches.add( 89 );
		pitches.add( Note.REST );

		List< Integer > intervalPattern = new ArrayList< Integer >( pitches.size() - 1 );
		// REST <REST> 60 <2> 62 <2> 64 <0> 64 <6> 70 <19> 89 <11> 100
		intervalPattern.add( Note.REST );
		intervalPattern.add( Note.REST );
		intervalPattern.add( Note.REST );
		intervalPattern.add( 2 );
		intervalPattern.add( 27 );
		intervalPattern.add( 11 );

		assertEquals( intervalPattern, retrieveIntervalPattern( pitches ) );
	}

	@Test
	public void testGetIntervalPatternAllRests() throws Exception {
		List< Integer > pitches = new ArrayList< Integer >( );
		pitches.add( Note.REST );
		pitches.add( Note.REST );
		pitches.add( Note.REST );

		List< Integer > intervalPattern = new ArrayList< Integer >( pitches.size() - 1 );
		// REST <REST> 60 <2> 62 <2> 64 <0> 64 <6> 70 <19> 89 <11> 100
		intervalPattern.add( Note.REST );
		intervalPattern.add( Note.REST );

		assertEquals( intervalPattern, retrieveIntervalPattern( pitches ) );
	}

	@Test
	public void testSumAllRhythmValues() {
		List<Note> slice10 =  new ArrayList<>();
		slice10.add( new Note( C0, QUARTER_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );

		assertEquals( ModelUtils.sumAllRhytmValues( slice10 ), HALF_NOTE );
	}

	@Test
	public void testGetNoteNameByPitch() {
		assertEquals( getNoteNameByPitch( 60 ), "C4" );
		assertEquals( getNoteNameByPitch( 62 ), "D4" );
	}

	@Test
	public void testTrimToTime() {
		Melody melody = new Melody(
				new Rest( WHOLE_NOTE ),
				new Note( C4, HALF_NOTE),
				new Note( CS4, EIGHTH_NOTE )
		);
		Melody trimmedMelody = trimToTime( melody, 2, 6.25 );

		Melody etalonMelody = new Melody(
				new Rest( 2 ),
				new Note( C4, 2 ),
				new Note( CS4, 0.25 )
		);

		assertEquals( etalonMelody, trimmedMelody );
	}

	@Test
	public void isParallelTest() throws Exception {
		Melody etalonMelody = new Melody(
				new Rest(QUARTER_NOTE), new Note( 60, SIXTEENTH_NOTE ), new Note( 70, EIGHTH_NOTE ), new Note( 80, HALF_NOTE )
		);
		assertTrue( areParallel( etalonMelody, etalonMelody ) );

		Melody melody1 = new Melody(
				new Rest(QUARTER_NOTE), new Note( 60 - 5, SIXTEENTH_NOTE ), new Note( 70 - 5, EIGHTH_NOTE ), new Note( 80 - 5, HALF_NOTE )
		);
		// second melody all pitches minus 5. pass.
		assertTrue( areParallel( etalonMelody, melody1 ) );

		Melody melody2 = new Melody(
				new Rest(DOTTED_QUARTER_NOTE), new Note( 60 - 5, SIXTEENTH_NOTE ), new Note( 70 - 5, EIGHTH_NOTE ), new Note( 80 - 5, HALF_NOTE )
		);
		// second melody first rest wrong rhythm value. fail.
		assertFalse( areParallel( etalonMelody, melody2 ) );

		Melody melody3 = new Melody(
				new Rest(QUARTER_NOTE), new Note( 60 - 5, SIXTEENTH_NOTE ), new Note( 70 - 5, EIGHTH_NOTE ), new Note( 80 - 4, HALF_NOTE )
		);
		// second melody last note -4 instead of -5. fail.
		assertFalse( areParallel( etalonMelody, melody3 ) );

		Melody melody00 = new Melody(
				new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note( 60, SIXTEENTH_NOTE ), new Rest(QUARTER_NOTE)
		);
		Melody melody01 = new Melody(
				new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note( 60 - 7, SIXTEENTH_NOTE ), new Rest(QUARTER_NOTE)
		);
		// both melodies - all rests except one note. pass what ever pitches are
		assertTrue( areParallel( melody00, melody01 ) );

		Melody melody10 = new Melody(
				new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note( 60, SIXTEENTH_NOTE ), new Rest(QUARTER_NOTE), new Note( 70, SIXTEENTH_NOTE )
		);
		Melody melody11 = new Melody(
				new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note( 60 - 7, SIXTEENTH_NOTE ), new Rest(QUARTER_NOTE), new Note( 70 - 7, SIXTEENTH_NOTE )
		);
		// rests except note in the middle and last note. Those notes are parallel. pass.
		assertTrue( areParallel( melody10, melody11 ) );

		Melody melody20 = new Melody(
				new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note( 60, SIXTEENTH_NOTE ), new Rest(QUARTER_NOTE), new Note( 70, SIXTEENTH_NOTE )
		);
		Melody melody21 = new Melody(
				new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note( 60 - 6, SIXTEENTH_NOTE ), new Rest(QUARTER_NOTE), new Note( 70 - 7, SIXTEENTH_NOTE )
		);
		// rests except non-parallel notes. fail
		assertFalse( areParallel( melody20, melody21 ) );

		Melody melody30 = new Melody(
				new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE)
		);
		Melody melody31 = new Melody(
				new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note( 70 - 7, SIXTEENTH_NOTE )
		);
		// first melody is rests - second has one note. fail.
		assertFalse( areParallel( melody30, melody31 ) );
	}
}
