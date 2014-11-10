package decomposer.analyzer.melody.equality;

import jm.music.data.Note;
import model.Signature;
import org.junit.Test;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.F4;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestInversionEqualityTest {

	InversionMelodyMovementEqualityTest inversionMelodyMovementEqualityTest = new InversionMelodyMovementEqualityTest();
	EqualityTest equalityTest = new EqualNumberOfNotesRequired( inversionMelodyMovementEqualityTest );

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
			new Note( E4, SIXTEENTH_NOTE ),
			new Note( DS4, SIXTEENTH_NOTE ),
			new Note( F4, SIXTEENTH_NOTE ),
			new Note( E4, SIXTEENTH_NOTE ) }
		);

		inversionMelodyMovementEqualityTest.setMaxNumberOfInvertedIntervals( 3 );

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
			new Note( D5, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( E4, SIXTEENTH_NOTE ),
			new Note( DS4, SIXTEENTH_NOTE ),
			new Note( F4, SIXTEENTH_NOTE ),
			new Note( E4, SIXTEENTH_NOTE ),
			new Note( E3, SIXTEENTH_NOTE ) }
		);

		inversionMelodyMovementEqualityTest.setMaxNumberOfInvertedIntervals( 3 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		inversionMelodyMovementEqualityTest.setMaxNumberOfInvertedIntervals( 4 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase3() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( E4, SIXTEENTH_NOTE ),
			new Note( DS4, SIXTEENTH_NOTE ),
			new Note( F4, SIXTEENTH_NOTE ) }
		);

		inversionMelodyMovementEqualityTest.setMaxNumberOfInvertedIntervals( 3 );

		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

}