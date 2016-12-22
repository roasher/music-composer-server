package ru.pavelyurkin.musiccomposer.core.decomposer.melody.equality;

import ru.pavelyurkin.musiccomposer.core.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.FragmentationMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melody.DifferentNumberOfNotesRequired;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.junit.Test;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestFragmentationMelodyMovementEquality {
	FragmentationMelodyMovementEquality fragmentationMelodyMovementEquality = new FragmentationMelodyMovementEquality();
	Equality equality = new DifferentNumberOfNotesRequired( fragmentationMelodyMovementEquality );

	@Test
	public void testCase1() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ) }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			//                        new Note( REST, EIGHTH_NOTE ),
			new Note( C4, EIGHTH_NOTE ),
			new Note( AS3, EIGHTH_NOTE ),
			new Note( B3, EIGHTH_NOTE ) }
		);

		fragmentationMelodyMovementEquality.setMaxNumberOfDeletedIntervals( 1 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		fragmentationMelodyMovementEquality.setMaxNumberOfDeletedIntervals( 0 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase2() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ) }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( C4, EIGHTH_NOTE ),
			new Note( AS3, EIGHTH_NOTE ),
			new Note( B3, EIGHTH_NOTE ),
			//                        new Note( REST, EIGHTH_NOTE )
		  }
		);

		fragmentationMelodyMovementEquality.setMaxNumberOfDeletedIntervals( 1 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		fragmentationMelodyMovementEquality.setMaxNumberOfDeletedIntervals( 0 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase3() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ) }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( REST, EIGHTH_NOTE ),
			new Note( C4, EIGHTH_NOTE ),
			new Note( AS3, EIGHTH_NOTE ),
			new Note( BF3, EIGHTH_NOTE ),
			new Note( C4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ) }
		);

		fragmentationMelodyMovementEquality.setMaxNumberOfDeletedIntervals( 10 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		fragmentationMelodyMovementEquality.setMaxNumberOfDeletedIntervals( 20 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );
	}

}
