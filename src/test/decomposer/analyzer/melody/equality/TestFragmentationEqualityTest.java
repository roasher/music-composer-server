package decomposer.analyzer.melody.equality;

import decomposer.analyzer.melody.equality.melodymovement.FragmentationMelodyMovementEqualityTest;
import jm.music.data.Note;
import model.Melody;
import org.junit.Test;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestFragmentationEqualityTest {
	FragmentationMelodyMovementEqualityTest fragmentationMelodyMovementEqualityTest = new FragmentationMelodyMovementEqualityTest();
	EqualityTest equalityTest = new DifferentNumberOfNotesRequired( fragmentationMelodyMovementEqualityTest );

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

		fragmentationMelodyMovementEqualityTest.setMaxNumberOfDeletedIntervals( 1 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		fragmentationMelodyMovementEqualityTest.setMaxNumberOfDeletedIntervals( 0 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );
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

		fragmentationMelodyMovementEqualityTest.setMaxNumberOfDeletedIntervals( 1 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		fragmentationMelodyMovementEqualityTest.setMaxNumberOfDeletedIntervals( 0 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );
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

		fragmentationMelodyMovementEqualityTest.setMaxNumberOfDeletedIntervals( 10 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		fragmentationMelodyMovementEqualityTest.setMaxNumberOfDeletedIntervals( 20 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );
	}

}
