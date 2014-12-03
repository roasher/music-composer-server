package decomposer.melody.equality;

import jm.music.data.Note;
import model.melody.Melody;
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

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 0 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase2() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, DOTTED_EIGHTH_NOTE ),
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
			new Note( E4, DOTTED_EIGHTH_NOTE )
		  }
		);

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testMelody2, testMelody1 ) );
	}

	@Test
	public void testCase3() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, DOTTED_EIGHTH_NOTE ),
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
			new Note( E4, DOTTED_EIGHTH_NOTE )
		  }
		);

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equalityTest.test( testMelody2, testMelody1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equalityTest.test( testMelody2, testMelody1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testMelody2, testMelody1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase4() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, DOTTED_EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, QUARTER_NOTE ) }
		);
		Melody testMelody2 = new Melody(
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
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.75 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.75 );
		assertTrue( equalityTest.test( testMelody2, testMelody1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.74 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.74 );
		assertFalse( equalityTest.test( testMelody2, testMelody1 ) );
	}

	@Test
	public void testCase5() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( D4, WHOLE_NOTE ),
			new Note( EF4, WHOLE_NOTE ),
			new Note( CS4, QUARTER_NOTE ),
			new Note( D4, QUARTER_NOTE ),
			new Note( E4, WHOLE_NOTE ),
			new Note( F4, WHOLE_NOTE ) }
		);
		Melody testMelody2 = new Melody(
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
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.49 );
		assertFalse( equalityTest.test( testMelody1, testMelody2 ) );

		// inverse
		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equalityTest.test( testMelody2, testMelody1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testMelody2, testMelody1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equalityTest.test( testMelody2, testMelody1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equalityTest.test( testMelody2, testMelody1 ) );

		rhytmEqualityTest.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhytmEqualityTest.setMaxRhythmDeviationSteps( 0.49 );
		assertFalse( equalityTest.test( testMelody2, testMelody1 ) );
	}

    @Test
    public void testCase6() {
        Melody testMelody1 = new Melody(
                new Note[] {
                        new Note( REST, 1 ),
                        new Note( 72, 0.25 ),
                        new Note( 74, 0.25 ),
                        new Note( 76, 0.25 ),
                        new Note( 77, 0.25 ),
                        new Note( REST, 1 ) }
        );
        Melody testMelody2 = new Melody(
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
        assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

    }
}
