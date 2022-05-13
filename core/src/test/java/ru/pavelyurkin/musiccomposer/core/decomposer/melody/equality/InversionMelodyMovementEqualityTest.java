package ru.pavelyurkin.musiccomposer.core.decomposer.melody.equality;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Pitches.CS4;
import static jm.constants.Pitches.D4;
import static jm.constants.Pitches.D5;
import static jm.constants.Pitches.DS4;
import static jm.constants.Pitches.E3;
import static jm.constants.Pitches.E4;
import static jm.constants.Pitches.EF4;
import static jm.constants.Pitches.F4;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import jm.music.data.Note;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.equality.melody.EqualNumberOfNotesRequired;
import ru.pavelyurkin.musiccomposer.core.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.InversionMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

public class InversionMelodyMovementEqualityTest {

  InversionMelodyMovementEquality inversionMelodyMovementEquality = new InversionMelodyMovementEquality();
  Equality equality = new EqualNumberOfNotesRequired(inversionMelodyMovementEquality);

  @Test
  public void testCase1() {
    Melody testMelody1 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(EF4, DOTTED_EIGHTH_NOTE),
        new Note(CS4, EIGHTH_NOTE),
        new Note(D4, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(E4, SIXTEENTH_NOTE),
        new Note(DS4, SIXTEENTH_NOTE),
        new Note(F4, SIXTEENTH_NOTE),
        new Note(E4, SIXTEENTH_NOTE)
    );

    inversionMelodyMovementEquality.setMaxNumberOfInvertedIntervals(3);

    assertTrue(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase2() {
    Melody testMelody1 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(EF4, DOTTED_EIGHTH_NOTE),
        new Note(CS4, EIGHTH_NOTE),
        new Note(D4, EIGHTH_NOTE),
        new Note(D5, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(E4, SIXTEENTH_NOTE),
        new Note(DS4, SIXTEENTH_NOTE),
        new Note(F4, SIXTEENTH_NOTE),
        new Note(E4, SIXTEENTH_NOTE),
        new Note(E3, SIXTEENTH_NOTE)
    );

    inversionMelodyMovementEquality.setMaxNumberOfInvertedIntervals(3);
    assertFalse(equality.test(testMelody1, testMelody2));

    inversionMelodyMovementEquality.setMaxNumberOfInvertedIntervals(4);
    assertTrue(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase3() {
    Melody testMelody1 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(EF4, DOTTED_EIGHTH_NOTE),
        new Note(CS4, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(E4, SIXTEENTH_NOTE),
        new Note(DS4, SIXTEENTH_NOTE),
        new Note(F4, SIXTEENTH_NOTE)
    );

    inversionMelodyMovementEquality.setMaxNumberOfInvertedIntervals(3);

    assertTrue(equality.test(testMelody1, testMelody2));
  }

}