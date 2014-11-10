package decomposer.analyzer.melody.equality;

import jm.music.data.Note;
import model.Signature;
import org.junit.Test;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.B4;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestIntervalsEqualityTest {

	IntervalsMelodyMovementEqualityTest intervalsMelodyMovementEqualityTest = new IntervalsMelodyMovementEqualityTest();
	EqualityTest equalityTest = new EqualNumberOfNotesRequired( intervalsMelodyMovementEqualityTest );

	@Test
	public void testCase1() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( DS4, QUARTER_NOTE ),
			new Note( E4, EIGHTH_NOTE ) }
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 1 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 2 );

		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase2() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( FS4, EIGHTH_NOTE ),
			new Note( E4, QUARTER_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( F5, EIGHTH_NOTE ) }
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 2 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 11 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 2 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 12 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase3() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( FF4, EIGHTH_NOTE ),
			new Note( DS4, QUARTER_NOTE ),
			new Note( E4, EIGHTH_NOTE ) }
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 1 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 10 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 2 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 3 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase4() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( DF4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( D5, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( E4, QUARTER_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( B4, EIGHTH_NOTE ) }
		);

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 1 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 11 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		intervalsMelodyMovementEqualityTest.setMaxNumberOfShiftedIntervals( 2 );
		intervalsMelodyMovementEqualityTest.setMaxShift( 11 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

}
