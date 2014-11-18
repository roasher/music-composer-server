package decomposer.analyzer.melody.equality;

import decomposer.analyzer.melody.equality.melodymovement.OrderMelodyMovementEqualityTest;
import jm.music.data.Note;
import model.Melody;
import org.junit.Test;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.G4;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestOrderMelodyMovementEqualityTest {

	OrderMelodyMovementEqualityTest orderMelodyMovementEqualityTest = new OrderMelodyMovementEqualityTest();
	EqualityTest equalityTest = new EqualNumberOfNotesRequired( orderMelodyMovementEqualityTest );

	@Test
	public void testCase1() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 0 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase2() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE )
		  }
		);

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 1 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 0 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase3() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 2 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase4() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE )
		  }
		);

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 2 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 1 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase5() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( G4, EIGHTH_NOTE ),
			new Note( A4, EIGHTH_NOTE ),
		  }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( A4, EIGHTH_NOTE ),
			new Note( G4, EIGHTH_NOTE ),
		  }
		);

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 3 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 2 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );
	}

}
