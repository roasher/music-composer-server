package ru.pavelyurkin.musiccomposer.core.service.decomposer.melody.equality;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.A5;
import static jm.constants.Pitches.B4;
import static jm.constants.Pitches.B5;
import static jm.constants.Pitches.BF0;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.CS4;
import static jm.constants.Pitches.D4;
import static jm.constants.Pitches.D5;
import static jm.constants.Pitches.E4;
import static jm.constants.Pitches.E5;
import static jm.constants.Pitches.EF4;
import static jm.constants.Pitches.F4;
import static jm.constants.Pitches.G5;
import static jm.constants.Pitches.GS4;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jm.music.data.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.service.equality.melody.Equality;

public class KeyEqualityTest extends AbstractSpringTest {

  @Autowired
  @Qualifier("keyEquality")
  private Equality test;

  @Test
  public void testCase1() {
    Melody testMelody1 = new Melody(
        new Note(C4, SIXTEENTH_NOTE),
        new Note(B4, SIXTEENTH_NOTE),
        new Note(D5, SIXTEENTH_NOTE),
        new Note(G5, SIXTEENTH_NOTE),
        new Note(B5, QUARTER_NOTE),
        new Note(A5, DOTTED_EIGHTH_NOTE),
        new Note(B5, SIXTEENTH_NOTE),
        new Note(G5, DOTTED_QUARTER_NOTE),
        new Note(D5, EIGHTH_NOTE),
        new Note(B4, EIGHTH_NOTE),
        new Note(A4, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(C4, SIXTEENTH_NOTE),
        new Note(GS4, SIXTEENTH_NOTE)
    );

    assertFalse(test.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase2() {
    Melody testMelody1 = new Melody(
        new Note(C4, SIXTEENTH_NOTE),
        new Note(B4, SIXTEENTH_NOTE),
        new Note(D5, SIXTEENTH_NOTE),
        new Note(G5, SIXTEENTH_NOTE),
        new Note(B5, QUARTER_NOTE),
        new Note(A5, DOTTED_EIGHTH_NOTE),
        new Note(B5, SIXTEENTH_NOTE),
        new Note(G5, DOTTED_QUARTER_NOTE),
        new Note(D5, EIGHTH_NOTE),
        new Note(B4, EIGHTH_NOTE),
        new Note(A4, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(C4, SIXTEENTH_NOTE),
        new Note(D4, SIXTEENTH_NOTE),
        new Note(E5, SIXTEENTH_NOTE),
        new Note(G5, SIXTEENTH_NOTE)
    );

    assertTrue(test.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase3() {
    Melody testMelody1 = new Melody(
        new Note(C4, SIXTEENTH_NOTE),
        new Note(CS4, SIXTEENTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(D4, SIXTEENTH_NOTE)
    );

    assertFalse(test.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase4() {
    Melody testMelody1 = new Melody(
        new Note(C4, SIXTEENTH_NOTE),
        new Note(BF0, SIXTEENTH_NOTE),
        new Note(D4, SIXTEENTH_NOTE),
        new Note(F4, SIXTEENTH_NOTE),
        new Note(E4, SIXTEENTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(EF4, SIXTEENTH_NOTE)
    );

    assertFalse(test.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase5() {
    Melody testMelody1 = new Melody(
        new Note(60, 0.5),
        new Note(62, 0.5),
        new Note(64, 0.5),
        new Note(65, 0.5),
        new Note(67, 0.5),
        new Note(69, 0.5),
        new Note(-2147483648, 1)
    );
    Melody testMelody2 = new Melody(
        new Note(57, 0.5),
        new Note(59, 0.5),
        new Note(60, 0.5),
        new Note(62, 0.5),
        new Note(64, 0.5),
        new Note(65, 0.5),
        new Note(-2147483648, 1)
    );

    assertTrue(test.test(testMelody1, testMelody2));
  }
}
