package ru.pavelyurkin.musiccomposer.core.utils;

import jm.music.data.Note;
import jm.music.data.Rest;
import junit.framework.Assert;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isOneOf;
import static ru.pavelyurkin.musiccomposer.core.utils.ModelUtils.trimToTime;

/**
 * Created by Pavel Yurkin on 20.07.14.
 */
public class ModelUtilsTest {

	@Test
    public void intervalPatternCalculatedNoMatterNotesOrder() throws Exception {
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
        intervalPattern.add( 2 );
        intervalPattern.add( 2 );
        intervalPattern.add( 0 );
        intervalPattern.add( 6 );
        intervalPattern.add( 19 );
        intervalPattern.add( 11 );

        Assert.assertEquals( intervalPattern, ModelUtils.retrieveIntervalPattern( pitches ) );
    }

	@Test
	public void intervalPatternIsCalculatedWithoutRests() throws Exception {
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
		intervalPattern.add( 2 );
		intervalPattern.add( 27 );
		intervalPattern.add( 11 );

		Assert.assertEquals( intervalPattern, ModelUtils.retrieveIntervalPattern( pitches ) );
	}

	@Test
	public void getEmptyIntervalPatternIfAllRests() throws Exception {
		List< Integer > pitches = new ArrayList< Integer >( );
		pitches.add( Note.REST );
		pitches.add( Note.REST );
		pitches.add( Note.REST );

		assertThat( ModelUtils.retrieveIntervalPattern( pitches ), is( Collections.emptyList()) );
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
		Assert.assertEquals( ModelUtils.getNoteNameByPitch( 60 ), "C4" );
		Assert.assertEquals( ModelUtils.getNoteNameByPitch( 62 ), "D4" );
		Assert.assertEquals( ModelUtils.getNoteNameByPitch( 64 ), "E4" );
		Assert.assertEquals( ModelUtils.getNoteNameByPitch( 65 ), "F4" );
		assertThat( ModelUtils.getNoteNameByPitch( 66 ), isOneOf("F#4", "Gb4") );
	}

	@Test
	public void testTrimToTimeSingleMelody() {
		InstrumentPart melody = new InstrumentPart(
				new Rest( WHOLE_NOTE ),
				new Note( C4, HALF_NOTE),
				new Note( CS4, EIGHTH_NOTE )
		);

		assertThat( trimToTime( melody, 2, 6.25 ), is( new InstrumentPart(
				new Rest( 2 ),
				new Note( C4, 2 ),
				new Note( CS4, 0.25 )
		) ) );

		InstrumentPart melody1 = new InstrumentPart(
				new Note( C4, 0.5 ),
				new Note( DF4, 0.1 ),
				new Note( D4, 0.3 ),
				new Note( EF4, 0.1 ),
				new Note( FF4, 0.5 )
		);
		assertEquals( new InstrumentPart( new Note( C4, 0.5 ) ), trimToTime( melody1, 0.0, 0.5 ) );
		assertEquals( new InstrumentPart(
				new Note( DF4, 0.1 ),
				new Note( D4, 0.3 ),
				new Note( EF4, 0.1 )
		), trimToTime( melody1, 0.5, 1 ) );

		assertEquals( new InstrumentPart( new Note( FF4, 0.5 ) ), trimToTime( melody1, 1, 1.5 ) );

		InstrumentPart melody2 = new InstrumentPart(
				new Note( F5, HALF_NOTE ),
				new Note( EF5, QUARTER_NOTE ),
				new Note( D5, QUARTER_NOTE ),
				new Note( G5, QUARTER_NOTE ),
				new Note( A5, QUARTER_NOTE )
		);

		assertEquals( new InstrumentPart( new Note( F5, 1.0 )  ), trimToTime( melody2, 0.0, 1.0 ) );
	}

	@Test
	public void testTrimToTimeChords() throws Exception {
		InstrumentPart melody = new InstrumentPart( Arrays.asList(
				new NewMelody( new Note( 1, 1 ), new Note( 2, 1 ) ),
				new Chord( Arrays.asList( 3, 4 ), 1 ),
				new NewMelody( new Note( 5, 1 ) ),
				new Chord( Arrays.asList( 6, 7 ), 1 )
		), 1 );

		assertThat( trimToTime( melody, 0, 2.5 ), is( new InstrumentPart(
				Arrays.asList(
						new NewMelody( new Note( 1, 1 ), new Note( 2, 1 ) ),
						new Chord( Arrays.asList( 3, 4 ), 0.5 )
				), 1 )
		) );

		assertThat( trimToTime( melody, 2.5, 3.5 ), is( new InstrumentPart(
				Arrays.asList(
						new Chord( Arrays.asList( 3, 4 ), 0.5 ),
						new NewMelody( new Note( 5, 0.5 ) )
				), 1 )
		) );

		assertThat( trimToTime( melody, 3.5, 4.5 ), is( new InstrumentPart(
				Arrays.asList(
						new NewMelody( new Note( 5, 0.5 ) ),
						new Chord( Arrays.asList( 6, 7 ), 0.5 )
				), 1 )
		) );
	}
}
