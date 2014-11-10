package decomposer.analyzer.melody.equality;

import jm.music.data.Note;
import model.Signature;
import org.junit.Test;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.G4;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestOrderEqualityTest {

	OrderMelodyMovementEqualityTest orderMelodyMovementEqualityTest = new OrderMelodyMovementEqualityTest();
	EqualityTest equalityTest = new EqualNumberOfNotesRequired( orderMelodyMovementEqualityTest );

	@Test
	public void testCase1() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 0 );
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
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
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
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 0 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase3() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 2 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase4() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
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
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 1 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase5() {
		Signature testSignature1 = new Signature(
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
		Signature testSignature2 = new Signature(
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
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

		orderMelodyMovementEqualityTest.setMaxNumberOfIntervalsHavingSwappedNotes( 2 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );
	}

}
