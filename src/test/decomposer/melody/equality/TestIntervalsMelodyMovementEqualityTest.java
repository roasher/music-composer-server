package decomposer.melody.equality;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.B4;
import static jm.constants.Pitches.CS4;
import static jm.constants.Pitches.D4;
import static jm.constants.Pitches.D5;
import static jm.constants.Pitches.DF4;
import static jm.constants.Pitches.DS4;
import static jm.constants.Pitches.E4;
import static jm.constants.Pitches.EF4;
import static jm.constants.Pitches.F4;
import static jm.constants.Pitches.F5;
import static jm.constants.Pitches.FF4;
import static jm.constants.Pitches.FS4;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import decomposer.melody.equality.melodymovement.IntervalsMelodyMovementEqualityTest;
import jm.music.data.Note;
import model.melody.Melody;
import model.melody.MelodyMovement;

public class TestIntervalsMelodyMovementEqualityTest {

	IntervalsMelodyMovementEqualityTest intervalsMelodyMovementEqualityTest = new IntervalsMelodyMovementEqualityTest();
	EqualityTest equalityTest = new EqualNumberOfNotesRequired( intervalsMelodyMovementEqualityTest );

	@Test
	public void testCase1() {
		Melody testMelody1 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( DS4, QUARTER_NOTE ),
			new Note( E4, EIGHTH_NOTE )
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 1 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 2 );

		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase2() {
		Melody testMelody1 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( FS4, EIGHTH_NOTE ),
			new Note( E4, QUARTER_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( F5, EIGHTH_NOTE )
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 2 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 11 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 2 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 12 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase3() {
		Melody testMelody1 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( FF4, EIGHTH_NOTE ),
			new Note( DS4, QUARTER_NOTE ),
			new Note( E4, EIGHTH_NOTE )
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 1 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 10 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 2 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 3 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase4() {
		Melody testMelody1 = new Melody(
			new Note( DF4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( D5, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( E4, QUARTER_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( B4, EIGHTH_NOTE )
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 1 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 11 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 2 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 11 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase7() {
		Melody testMelody1 = new Melody(
			new Note( 64, 4 )
		);
		Melody testMelody2 = new Melody(
			new Note( 52, 1.5 ),
			new Note( 64, 1.5 ),
			new Note( 65, 1 )
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 0 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 0 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 777 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 89 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		}

	@Test
	public void testCase8() {
		Melody testMelody1 = new Melody(
			new Note( 64, 0.25 )
		);
		Melody testMelody2 = new Melody(
			new Note( 52, 0.25 )
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 0 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 0 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 777 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 89 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

	}

	@Test
	public void testCase9() {
		Melody testMelody1 = new Melody(
			new Note( 60, 0.5 ),
			new Note( 62, 0.5 ),
			new Note( 64, 0.5 ),
			new Note( 65, 0.5 ),
			new Note( 67, 0.5 ),
			new Note( 69, 0.5 ),
			new Note( -2147483648, 0.5 )
		);
		Melody testMelody2 = new Melody(
			new Note( 57, 0.5 ),
			new Note( 59, 0.5 ),
			new Note( 60, 0.5 ),
			new Note( 62, 0.5 ),
			new Note( 64, 0.5 ),
			new Note( 65, 0.5 ),
			new Note( -2147483648, 0.5 )
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 0 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 0 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 3 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 1 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void getEqualityMetricTest() {
		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 0 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 0 );
		MelodyMovement melodyMovement0 = new MelodyMovement();
		melodyMovement0.setPitchIntervals( Arrays.asList( 1, 1, 1 ) );
		MelodyMovement melodyMovement1 = new MelodyMovement();
		melodyMovement1.setPitchIntervals( Arrays.asList( -1, 1, 2 ) );
		MelodyMovement melodyMovement2 = new MelodyMovement();
		melodyMovement2.setPitchIntervals( Arrays.asList( 1, 1, -3 ) );
		assertEquals( 1, intervalsMelodyMovementEqualityTest.getEqualityMetric( melodyMovement0, melodyMovement0 ), 0 );
		assertEquals( 1./3, intervalsMelodyMovementEqualityTest.getEqualityMetric( melodyMovement0, melodyMovement1 ), 0 );
		assertEquals( 2./3, intervalsMelodyMovementEqualityTest.getEqualityMetric( melodyMovement0, melodyMovement2 ), 0 );
		assertEquals( 1./3, intervalsMelodyMovementEqualityTest.getEqualityMetric( melodyMovement1, melodyMovement2 ), 0 );

		MelodyMovement melodyMovement01 = new MelodyMovement();
		melodyMovement01.setPitchIntervals( Arrays.asList( 1, -2, 1, 0 ) );
		MelodyMovement melodyMovement11 = new MelodyMovement();
		melodyMovement11.setPitchIntervals( Arrays.asList( 4, -2, 1, 12 ) );
		assertEquals( 0.5, intervalsMelodyMovementEqualityTest.getEqualityMetric( melodyMovement01, melodyMovement11 ), 0 );
	}
}
