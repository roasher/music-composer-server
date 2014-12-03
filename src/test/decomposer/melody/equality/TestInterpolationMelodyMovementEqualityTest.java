package decomposer.melody.equality;

import decomposer.melody.equality.melodymovement.InterpolationMelodyMovementEqualityTest;
import jm.music.data.Note;
import model.melody.Melody;
import org.junit.Ignore;
import org.junit.Test;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE_TRIPLET;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestInterpolationMelodyMovementEqualityTest {

	InterpolationMelodyMovementEqualityTest interpolationMelodyMovementEqualityTest = new InterpolationMelodyMovementEqualityTest();
	EqualityTest equalityTest = new DifferentNumberOfNotesRequired( interpolationMelodyMovementEqualityTest );

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
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( BF4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( G4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( GS4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ) }
		);

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 2 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 1 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase2() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ) }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( BF4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( G4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( GS4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( BF4, SIXTEENTH_NOTE_TRIPLET ) }
		);

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 20 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 20 );
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
			new Note( G4, EIGHTH_NOTE ),
			new Note( EF5, DOTTED_EIGHTH_NOTE ),
			new Note( CS3, EIGHTH_NOTE ),
			new Note( G4, EIGHTH_NOTE ) }
		);

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 20 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 20 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Ignore
	// problem in this test
    @Test
    public void testCase4() {
        Melody testMelody1 = new Melody(
                new Note[] {
                        new Note( 69, 1 ),
                        new Note( 68, 0.5 ),
                        new Note( 66, 0.5 ),
                        new Note( 64, 1 ),
                        new Note( 74, 0.5 ),
                        new Note( 72, 0.5 ),
                        new Note( 71, 1 ),
                        new Note( 68, 0.5 ),
                        new Note( 69, 0.5 ),
                }
        );
        Melody testMelody2 = new Melody(
                new Note[] {
                        new Note( 72, 0.5 ),
                        new Note( 67, 0.25 ),
                        new Note( 67, 0.25 ),
                        new Note( 76, 0.5 ),
                        new Note( 74, 0.5 ),
                        new Note( 71, 0.5 ),
                        new Note( 72, 1.5 ),
                        new Note( 74, 0.5 ),
                        new Note( 76, 0.5 ),
                        new Note( 72, 0.5 ),
                }
        );

//		View.notate( testMelody1 );
//		View.notate( testMelody2 );
//		suspend();

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 2 );
        assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

    }
}
