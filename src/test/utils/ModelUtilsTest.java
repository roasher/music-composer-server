package utils;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.C0;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.CS4;
import static junit.framework.Assert.assertEquals;
import static utils.ModelUtils.getNoteNameByPitch;
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
}
