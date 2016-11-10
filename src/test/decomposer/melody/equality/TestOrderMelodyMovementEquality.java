package decomposer.melody.equality;

import decomposer.melody.equality.melodymovement.OrderMelodyMovementEquality;
import jm.music.data.Note;
import model.melody.Melody;
import org.junit.Test;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.G4;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestOrderMelodyMovementEquality {

	OrderMelodyMovementEquality orderMelodyMovementEquality = new OrderMelodyMovementEquality();
	Equality equality = new EqualNumberOfNotesRequired( orderMelodyMovementEquality );

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

		orderMelodyMovementEquality.setMaxNumberOfIntervalsHavingSwappedNotes( 0 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );
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

		orderMelodyMovementEquality.setMaxNumberOfIntervalsHavingSwappedNotes( 1 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		orderMelodyMovementEquality.setMaxNumberOfIntervalsHavingSwappedNotes( 0 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );
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

		orderMelodyMovementEquality.setMaxNumberOfIntervalsHavingSwappedNotes( 2 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );
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

		orderMelodyMovementEquality.setMaxNumberOfIntervalsHavingSwappedNotes( 2 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		orderMelodyMovementEquality.setMaxNumberOfIntervalsHavingSwappedNotes( 1 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );
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

		orderMelodyMovementEquality.setMaxNumberOfIntervalsHavingSwappedNotes( 3 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		orderMelodyMovementEquality.setMaxNumberOfIntervalsHavingSwappedNotes( 2 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );
	}

}
