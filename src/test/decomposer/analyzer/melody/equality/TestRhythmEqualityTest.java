package decomposer.analyzer.melody.equality;

import jm.music.data.Note;
import model.Signature;
import org.junit.Test;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestRhythmEqualityTest {

	RhytmEqualityTest rhytmEqualityTest = new RhytmEqualityTest();
	EqualityTest equalityTest = new EqualNumberOfNotesRequired( rhytmEqualityTest );

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

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 0 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase2() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, DOTTED_EIGHTH_NOTE ),
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
			new Note( E4, DOTTED_EIGHTH_NOTE )
		  }
		);

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testSignature2, testSignature1 ) );
	}

	@Test
	public void testCase3() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, DOTTED_EIGHTH_NOTE ),
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
			new Note( E4, DOTTED_EIGHTH_NOTE )
		  }
		);

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equalityTest.test( testSignature2, testSignature1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equalityTest.test( testSignature2, testSignature1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testSignature2, testSignature1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase4() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, DOTTED_EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, QUARTER_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( E4, SIXTEENTH_NOTE )
		  }
		);

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.75 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.75 );
		assertTrue( equalityTest.test( testSignature2, testSignature1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.74 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.74 );
		assertFalse( equalityTest.test( testSignature2, testSignature1 ) );
	}

	@Test
	public void testCase5() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( D4, WHOLE_NOTE ),
			new Note( EF4, WHOLE_NOTE ),
			new Note( CS4, QUARTER_NOTE ),
			new Note( D4, QUARTER_NOTE ),
			new Note( E4, WHOLE_NOTE ),
			new Note( F4, WHOLE_NOTE ) }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( D4, DOTTED_QUARTER_NOTE ),
			new Note( EF4, EIGHTH_NOTE ),
			new Note( CS4, SIXTEENTH_NOTE ),
			new Note( D4, SIXTEENTH_NOTE ),
			new Note( E4, QUARTER_NOTE ),
			new Note( F4, EIGHTH_NOTE ) }
		);

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.49 );
		assertFalse( equalityTest.test( testSignature1, testSignature2 ) );

		// inverse
		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equalityTest.test( testSignature2, testSignature1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testSignature2, testSignature1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testSignature2, testSignature1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testSignature2, testSignature1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.49 );
		assertFalse( equalityTest.test( testSignature2, testSignature1 ) );
	}

    @Test
    public void testCase6() {
        Signature testSignature1 = new Signature(
                new Note[] {
                        new Note( REST, 1 ),
                        new Note( 72, 0.25 ),
                        new Note( 74, 0.25 ),
                        new Note( 76, 0.25 ),
                        new Note( 77, 0.25 ),
                        new Note( REST, 1 ) }
        );
        Signature testSignature2 = new Signature(
                new Note[] {
                        new Note( REST, 1 ),
                        new Note( 72, 0.25 ),
                        new Note( 74, 0.25 ),
                        new Note( 76, 0.25 ),
                        new Note( 77, 0.25 ),
                        new Note( F4, 1 ) }
        );

        rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
        rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
        assertTrue( equalityTest.test( testSignature1, testSignature2 ) );

    }
}
