package decomposer.analyzer.melody.equality;

import jm.music.data.Note;
import model.Signature;
import org.junit.Test;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE_TRIPLET;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestInterpolationEqualityTest {

	InterpolationMelodyMovementEqualityTest interpolationMelodyMovementEqualityTest = new InterpolationMelodyMovementEqualityTest();
	EqualityTest equalityTest = new DifferentNumberOfNotesRequired( interpolationMelodyMovementEqualityTest );

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
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( BF4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( G4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( GS4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ) }
		);

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 2 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 1 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );
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
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( BF4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( G4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( A4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( GS4, SIXTEENTH_NOTE_TRIPLET ),
			new Note( BF4, SIXTEENTH_NOTE_TRIPLET ) }
		);

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 20 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 20 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );
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
			new Note( G4, EIGHTH_NOTE ),
			new Note( EF5, DOTTED_EIGHTH_NOTE ),
			new Note( CS3, EIGHTH_NOTE ),
			new Note( G4, EIGHTH_NOTE ) }
		);

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 20 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 20 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );
	}

    @Test
    public void testCase4() {
        Signature testSignature1 = new Signature(
                new Note[] {
                        new Note( 69, 1 ),
                        new Note( 68, 0.5 ),
                        new Note( 66, 0.5 ),
                        new Note( 64, 1 ),
                        new Note( REST, 2 ),
                        new Note( 74, 0.5 ),
                        new Note( 72, 0.5 ),
                        new Note( REST, 1 ),
                        new Note( 71, 1 ),
                        new Note( REST, 1 ),
                        new Note( 68, 0.5 ),
                        new Note( 69, 0.5 ),
                }
        );
        Signature testSignature2 = new Signature(
                new Note[] {
                        new Note( 72, 0.5 ),
                        new Note( 67, 0.25000000000000083 ),
                        new Note( 67, 0.25000000000000083 ),
                        new Note( 76, 0.5000000000000009 ),
                        new Note( 74, 0.5000000000000009 ),
                        new Note( 71, 0.5000000000000009 ),
                        new Note( 72, 1.4999999999999991 ),
                        new Note( 74, 0.49999999999999906 ),
                        new Note( 76, 0.49999999999999906 ),
                        new Note( 72, 0.49999999999999906 ),
                }
        );

        interpolationMelodyMovementEqualityTest.setMaxNumberOfAddedIntervals( 2 );
        assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

    }
}
