package ru.pavelyurkin.musiccomposer.core.service.decomposer.melody.equality;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.B4;
import static jm.constants.Pitches.CS4;
import static jm.constants.Pitches.D4;
import static jm.constants.Pitches.D5;
import static jm.constants.Pitches.DF4;
import static jm.constants.Pitches.DS4;
import static jm.constants.Pitches.E4;
import static jm.constants.Pitches.EF4;
import static jm.constants.Pitches.F4;
import static jm.constants.Pitches.F5;
import static jm.constants.Pitches.FF4;
import static jm.constants.Pitches.FS4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import jm.music.data.Note;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.model.melody.MelodyMovement;
import ru.pavelyurkin.musiccomposer.core.service.equality.melody.EqualNumberOfNotesRequired;
import ru.pavelyurkin.musiccomposer.core.service.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.service.equality.melodymovement.IntervalsMelodyMovementEquality;

public class TestIntervalsMelodyMovementEquality {

  IntervalsMelodyMovementEquality intervalsMelodyMovementEquality = new IntervalsMelodyMovementEquality();
  Equality equality = new EqualNumberOfNotesRequired(intervalsMelodyMovementEquality);

  @Test
  public void testCase1() {
    Melody testMelody1 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(EF4, DOTTED_EIGHTH_NOTE),
        new Note(CS4, EIGHTH_NOTE),
        new Note(D4, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(F4, EIGHTH_NOTE),
        new Note(DS4, QUARTER_NOTE),
        new Note(E4, EIGHTH_NOTE)
    );

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(1);
    intervalsMelodyMovementEquality.setMaxShift(2);

    assertTrue(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase2() {
    Melody testMelody1 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(EF4, DOTTED_EIGHTH_NOTE),
        new Note(CS4, EIGHTH_NOTE),
        new Note(D4, EIGHTH_NOTE),
        new Note(D4, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(FS4, EIGHTH_NOTE),
        new Note(E4, QUARTER_NOTE),
        new Note(F4, EIGHTH_NOTE),
        new Note(F5, EIGHTH_NOTE)
    );

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(2);
    intervalsMelodyMovementEquality.setMaxShift(11);
    assertFalse(equality.test(testMelody1, testMelody2));

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(2);
    intervalsMelodyMovementEquality.setMaxShift(12);
    assertTrue(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase3() {
    Melody testMelody1 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(EF4, DOTTED_EIGHTH_NOTE),
        new Note(CS4, EIGHTH_NOTE),
        new Note(D4, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(FF4, EIGHTH_NOTE),
        new Note(DS4, QUARTER_NOTE),
        new Note(E4, EIGHTH_NOTE)
    );

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(1);
    intervalsMelodyMovementEquality.setMaxShift(10);
    assertFalse(equality.test(testMelody1, testMelody2));

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(2);
    intervalsMelodyMovementEquality.setMaxShift(3);
    assertTrue(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase4() {
    Melody testMelody1 = new Melody(
        new Note(DF4, EIGHTH_NOTE),
        new Note(EF4, DOTTED_EIGHTH_NOTE),
        new Note(CS4, EIGHTH_NOTE),
        new Note(D4, EIGHTH_NOTE),
        new Note(D5, EIGHTH_NOTE)
    );
    Melody testMelody2 = new Melody(
        new Note(D4, EIGHTH_NOTE),
        new Note(E4, EIGHTH_NOTE),
        new Note(E4, QUARTER_NOTE),
        new Note(F4, EIGHTH_NOTE),
        new Note(B4, EIGHTH_NOTE)
    );

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(1);
    intervalsMelodyMovementEquality.setMaxShift(11);
    assertFalse(equality.test(testMelody1, testMelody2));

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(2);
    intervalsMelodyMovementEquality.setMaxShift(11);
    assertTrue(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase7() {
    Melody testMelody1 = new Melody(
        new Note(64, 4)
    );
    Melody testMelody2 = new Melody(
        new Note(52, 1.5),
        new Note(64, 1.5),
        new Note(65, 1)
    );

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(0);
    intervalsMelodyMovementEquality.setMaxShift(0);
    assertFalse(equality.test(testMelody1, testMelody2));

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(777);
    intervalsMelodyMovementEquality.setMaxShift(89);
    assertFalse(equality.test(testMelody1, testMelody2));

  }

  @Test
  public void testCase8() {
    Melody testMelody1 = new Melody(
        new Note(64, 0.25)
    );
    Melody testMelody2 = new Melody(
        new Note(52, 0.25)
    );

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(0);
    intervalsMelodyMovementEquality.setMaxShift(0);
    assertTrue(equality.test(testMelody1, testMelody2));

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(777);
    intervalsMelodyMovementEquality.setMaxShift(89);
    assertTrue(equality.test(testMelody1, testMelody2));

  }

  @Test
  public void testCase9() {
    Melody testMelody1 = new Melody(
        new Note(60, 0.5),
        new Note(62, 0.5),
        new Note(64, 0.5),
        new Note(65, 0.5),
        new Note(67, 0.5),
        new Note(69, 0.5),
        new Note(-2147483648, 0.5)
    );
    Melody testMelody2 = new Melody(
        new Note(57, 0.5),
        new Note(59, 0.5),
        new Note(60, 0.5),
        new Note(62, 0.5),
        new Note(64, 0.5),
        new Note(65, 0.5),
        new Note(-2147483648, 0.5)
    );

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(0);
    intervalsMelodyMovementEquality.setMaxShift(0);
    assertFalse(equality.test(testMelody1, testMelody2));

    intervalsMelodyMovementEquality.setMaxNumberOfShiftedIntervals(3);
    intervalsMelodyMovementEquality.setMaxShift(1);
    assertTrue(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void getEqualityMetricTest() {
    // logic tests
    MelodyMovement melodyMovement0 = new MelodyMovement();
    melodyMovement0.setPitchIntervals(Arrays.asList(1, 1, 1));
    MelodyMovement melodyMovement1 = new MelodyMovement();
    melodyMovement1.setPitchIntervals(Arrays.asList(-1, 1, 2));
    MelodyMovement melodyMovement2 = new MelodyMovement();
    melodyMovement2.setPitchIntervals(Arrays.asList(1, 1, -3));
    assertEquals(1, intervalsMelodyMovementEquality.getEqualityMetric(melodyMovement0, melodyMovement0), 0);
    assertEquals((3 - (1. + 0 + 0.5)) / 3, intervalsMelodyMovementEquality
        .getEqualityMetric(melodyMovement0, melodyMovement1), 0);
    assertEquals((3 - (0 + 0 + 1.)) / 3, intervalsMelodyMovementEquality
        .getEqualityMetric(melodyMovement0, melodyMovement2), 0);
    assertEquals((3 - (1 + 0 + 1.)) / 3, intervalsMelodyMovementEquality
        .getEqualityMetric(melodyMovement1, melodyMovement2), 0);

    MelodyMovement melodyMovement01 = new MelodyMovement();
    melodyMovement01.setPitchIntervals(Arrays.asList(1, -2, 1, 0));
    MelodyMovement melodyMovement11 = new MelodyMovement();
    melodyMovement11.setPitchIntervals(Arrays.asList(4, -2, 1, 12));
    assertEquals((4 - (1 + 0 + 0 + 1)) / 4., intervalsMelodyMovementEquality
        .getEqualityMetric(melodyMovement01, melodyMovement11), 0);

    // comparative tests
    MelodyMovement melodyMovement20 = new MelodyMovement();
    melodyMovement20.setPitchIntervals(Arrays.asList(1, 1, 1));
    MelodyMovement melodyMovement21 = new MelodyMovement();
    melodyMovement21.setPitchIntervals(Arrays.asList(1, 1, 2));
    MelodyMovement melodyMovement22 = new MelodyMovement();
    melodyMovement22.setPitchIntervals(Arrays.asList(1, 1, 3));
    assertTrue(intervalsMelodyMovementEquality.getEqualityMetric(melodyMovement20, melodyMovement21)
               > intervalsMelodyMovementEquality
                   .getEqualityMetric(melodyMovement20, melodyMovement22));

    MelodyMovement melodyMovement30 = new MelodyMovement();
    melodyMovement30.setPitchIntervals(Arrays.asList(1, 1, 1, 1));
    MelodyMovement melodyMovement31 = new MelodyMovement();
    melodyMovement31.setPitchIntervals(Arrays.asList(1, 1, 2, 2));
    MelodyMovement melodyMovement32 = new MelodyMovement();
    melodyMovement32.setPitchIntervals(Arrays.asList(1, 1, 1, 12));
    assertTrue(intervalsMelodyMovementEquality.getEqualityMetric(melodyMovement30, melodyMovement31)
               == intervalsMelodyMovementEquality
                   .getEqualityMetric(melodyMovement30, melodyMovement32));
  }
}
