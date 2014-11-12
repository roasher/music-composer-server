package decomposer.analyzer.melody.equality;

import decomposer.analyzer.melody.equality.melodymovement.ContourMelodyMovementEqualityTest;
import jm.music.data.Note;
import jm.util.View;
import model.Melody;
import org.junit.Test;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestContourEqualityTest {

	private ContourMelodyMovementEqualityTest contourMelodyMovementEqualityTest = new ContourMelodyMovementEqualityTest();
	private EqualityTest equalityTest = new EqualNumberOfNotesRequired( contourMelodyMovementEqualityTest );

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

		View.notate( testMelody1 );
		View.notate( testMelody2 );

		contourMelodyMovementEqualityTest.setMaxNumberOfDiffDirectionIntervals(4);
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );

		contourMelodyMovementEqualityTest.setMaxNumberOfDiffDirectionIntervals(0);
		assertTrue(equalityTest.test( testMelody1, testMelody2 ));
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

		View.notate( testMelody1 );
		View.notate( testMelody2 );

		contourMelodyMovementEqualityTest.setMaxNumberOfDiffDirectionIntervals(2);
		assertTrue( equalityTest.test( testMelody1, testMelody2 ) );
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

        View.notate( testMelody1 );
        View.notate( testMelody2 );

        contourMelodyMovementEqualityTest.setMaxNumberOfDiffDirectionIntervals(0);
        assertFalse(equalityTest.test( testMelody1, testMelody2 ));

        contourMelodyMovementEqualityTest.setMaxNumberOfDiffDirectionIntervals(1);
        assertTrue(equalityTest.test( testMelody1, testMelody2 ));
    }
    
//	ic static void main( String... args ) {
//		TestContourEqualityTest testContourEqualityTest = new TestContourEqualityTest();
//		testContourEqualityTest.testCase1();
//		testContourEqualityTest.testCase2();
//	}
}