package decomposer.melody.equality;

import ru.pavelyurkin.musiccomposer.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.equality.melody.EqualNumberOfNotesRequired;
import ru.pavelyurkin.musiccomposer.equality.melody.RhythmEquality;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
import org.junit.Test;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class RhythmEqualityTest {

	RhythmEquality rhythmEquality = new RhythmEquality();
	Equality equality = new EqualNumberOfNotesRequired( rhythmEquality );

	@Test
	public void testCase1() {
		Melody testMelody1 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE )
		);

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 0 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase2() {
		Melody testMelody1 = new Melody(
			new Note( D4, DOTTED_EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( E4, DOTTED_EIGHTH_NOTE )
		);

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equality.test( testMelody2, testMelody1 ) );
	}

	@Test
	public void testCase3() {
		Melody testMelody1 = new Melody(
			new Note( D4, DOTTED_EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( E4, DOTTED_EIGHTH_NOTE )
		);

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equality.test( testMelody2, testMelody1 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equality.test( testMelody2, testMelody1 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equality.test( testMelody2, testMelody1 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase4() {
		Melody testMelody1 = new Melody(
			new Note( D4, DOTTED_EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( E4, EIGHTH_NOTE ),
			new Note( F4, QUARTER_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, EIGHTH_NOTE ),
			new Note( EF4, DOTTED_EIGHTH_NOTE ),
			new Note( CS4, EIGHTH_NOTE ),
			new Note( D4, EIGHTH_NOTE ),
			new Note( F4, EIGHTH_NOTE ),
			new Note( E4, SIXTEENTH_NOTE )
		);

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhythmEquality.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.75 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.75 );
		assertTrue( equality.test( testMelody2, testMelody1 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.74 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.74 );
		assertFalse( equality.test( testMelody2, testMelody1 ) );
	}

	@Test
	public void testCase5() {
		Melody testMelody1 = new Melody(
			new Note( D4, WHOLE_NOTE ),
			new Note( EF4, WHOLE_NOTE ),
			new Note( CS4, QUARTER_NOTE ),
			new Note( D4, QUARTER_NOTE ),
			new Note( E4, WHOLE_NOTE ),
			new Note( F4, WHOLE_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, DOTTED_QUARTER_NOTE ),
			new Note( EF4, EIGHTH_NOTE ),
			new Note( CS4, SIXTEENTH_NOTE ),
			new Note( D4, SIXTEENTH_NOTE ),
			new Note( E4, QUARTER_NOTE ),
			new Note( F4, EIGHTH_NOTE )
		);

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhythmEquality.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.49 );
		assertFalse( equality.test( testMelody1, testMelody2 ) );

		// inverse
		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0 );
		assertFalse( equality.test( testMelody2, testMelody1 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 1 );
		rhythmEquality.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equality.test( testMelody2, testMelody1 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
		rhythmEquality.setMaxRhythmDeviationSteps( 1 );
		assertFalse( equality.test( testMelody2, testMelody1 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
		assertTrue( equality.test( testMelody2, testMelody1 ) );

		rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 3 );
		rhythmEquality.setMaxRhythmDeviationSteps( 0.49 );
		assertFalse( equality.test( testMelody2, testMelody1 ) );
	}

    @Test
    public void testCase6() {
        Melody testMelody1 = new Melody(
				new Note( REST, 1 ),
                new Note( 72, 0.25 ),
                new Note( 74, 0.25 ),
                new Note( 76, 0.25 ),
                new Note( 77, 0.25 ),
                new Note( REST, 1 )
        );
        Melody testMelody2 = new Melody(
                new Note( REST, 1 ),
                new Note( 72, 0.25 ),
                new Note( 74, 0.25 ),
                new Note( 76, 0.25 ),
                new Note( 77, 0.25 ),
                new Note( F4, 1 )
        );

        rhythmEquality.setMaxNumberOfRhythmicallyDifferentNotes( 2 );
        rhythmEquality.setMaxRhythmDeviationSteps( 0.5 );
        assertTrue( equality.test( testMelody1, testMelody2 ) );

    }
}