package ru.pavelyurkin.musiccomposer.core.decomposer.melody.equality;

import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.ContourMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.equality.melody.EqualNumberOfNotesRequired;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.junit.Test;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestContourMelodyMovementEquality {

	private ContourMelodyMovementEquality contourMelodyMovementEquality = new ContourMelodyMovementEquality();
	private Equality equality = new EqualNumberOfNotesRequired( contourMelodyMovementEquality );

	@Test
	public void testCase1() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( G4, SIXTEENTH_NOTE ),
			new Note( B4, SIXTEENTH_NOTE ),
			new Note( DS5, SIXTEENTH_NOTE ),
			new Note( G5, SIXTEENTH_NOTE ),
			new Note( B5, QUARTER_NOTE ),
			new Note( AS5, DOTTED_EIGHTH_NOTE ),
			new Note( B5, SIXTEENTH_NOTE ),
			new Note( G5, DOTTED_QUARTER_NOTE ),
			new Note( DS5, EIGHTH_NOTE ),
			new Note( B4, EIGHTH_NOTE ),
			new Note( AS4, EIGHTH_NOTE ),
		  }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( G4, SIXTEENTH_NOTE ),
			new Note( DS5, SIXTEENTH_NOTE ),
			new Note( G5, SIXTEENTH_NOTE ),
			new Note( B5, SIXTEENTH_NOTE ),
			new Note( DS6, QUARTER_NOTE ),
			new Note( C6, DOTTED_EIGHTH_NOTE ),
			new Note( DS6, SIXTEENTH_NOTE ),
			new Note( B5, DOTTED_QUARTER_NOTE ),
			new Note( G5, EIGHTH_NOTE ),
			new Note( DS5, EIGHTH_NOTE ),
			new Note( B4, EIGHTH_NOTE ),
		  }
		);

//		View.notate( testMelody1 );
//		View.notate( testMelody2 );
//		suspend();

		contourMelodyMovementEquality.setMaxNumberOfDiffDirectionIntervals(4);
		assertTrue( equality.test( testMelody1, testMelody2 ) );

		contourMelodyMovementEquality.setMaxNumberOfDiffDirectionIntervals(0);
		assertTrue( equality.test( testMelody1, testMelody2 ));
	}

	@Test
	public void testCase2() {
		Melody testMelody1 = new Melody(
		  new Note[] {
			new Note( G4, SIXTEENTH_NOTE ),
			new Note( B4, SIXTEENTH_NOTE ),
			new Note( DS5, SIXTEENTH_NOTE ),
			new Note( G5, SIXTEENTH_NOTE ),
			new Note( B5, QUARTER_NOTE ),
			new Note( AS5, DOTTED_EIGHTH_NOTE ),
			new Note( B5, SIXTEENTH_NOTE ),
			new Note( G5, DOTTED_QUARTER_NOTE ),
			new Note( DS5, EIGHTH_NOTE ),
			new Note( B4, EIGHTH_NOTE ),
			new Note( AS4, EIGHTH_NOTE ),
		  }
		);
		Melody testMelody2 = new Melody(
		  new Note[] {
			new Note( G3, SIXTEENTH_NOTE ),
			new Note( G4, SIXTEENTH_NOTE ),
			new Note( DS5, SIXTEENTH_NOTE ),
			new Note( G5, SIXTEENTH_NOTE ),
			new Note( B5, QUARTER_NOTE ),
			new Note( AS5, DOTTED_EIGHTH_NOTE ),
			new Note( B5, SIXTEENTH_NOTE ),
			new Note( G5, DOTTED_QUARTER_NOTE ),
			new Note( DS5, EIGHTH_NOTE ),
			new Note( B4, EIGHTH_NOTE ),
			new Note( AS4, EIGHTH_NOTE ),
		  }
		);

//		View.notate( testMelody1 );
//		View.notate( testMelody2 );
//		suspend();

		contourMelodyMovementEquality.setMaxNumberOfDiffDirectionIntervals(2);
		assertTrue( equality.test( testMelody1, testMelody2 ) );
	}

    @Test
    public void testCase5() {
        Melody testMelody1 = new Melody(
                new Note[] {
                        new Note( G4, SIXTEENTH_NOTE ),
                        new Note( B4, SIXTEENTH_NOTE ),
                        new Note( DS5, SIXTEENTH_NOTE ),
                        new Note( G5, SIXTEENTH_NOTE ),
                        new Note( B5, QUARTER_NOTE ),
                        new Note( AS5, DOTTED_EIGHTH_NOTE ),
                        new Note( B5, SIXTEENTH_NOTE ),
                        new Note( G5, DOTTED_QUARTER_NOTE ),
                        new Note( DS5, EIGHTH_NOTE ),
                        new Note( B4, EIGHTH_NOTE ),
                        new Note( AS4, EIGHTH_NOTE ),
                }
        );
        Melody testMelody2 = new Melody(
                new Note[] {
                        new Note( G3, SIXTEENTH_NOTE ),
                        new Note( G4, SIXTEENTH_NOTE ),
                        new Note( DS5, SIXTEENTH_NOTE ),
                        new Note( G5, SIXTEENTH_NOTE ),
                        new Note( B5, QUARTER_NOTE ),
                        new Note( AS5, DOTTED_EIGHTH_NOTE ),
                        new Note( B5, SIXTEENTH_NOTE ),
                        new Note( G5, DOTTED_QUARTER_NOTE ),
                        new Note( DS5, EIGHTH_NOTE ),
                        new Note( E5, EIGHTH_NOTE ),
                        new Note( AS4, EIGHTH_NOTE ),
                }
        );

//        View.notate( testMelody1 );
//        View.notate( testMelody2 );
//		  suspend();

        contourMelodyMovementEquality.setMaxNumberOfDiffDirectionIntervals(0);
        assertFalse( equality.test( testMelody1, testMelody2 ));

        contourMelodyMovementEquality.setMaxNumberOfDiffDirectionIntervals(1);
        assertTrue( equality.test( testMelody1, testMelody2 ));
    }
}