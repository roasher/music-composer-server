package ru.pavelyurkin.musiccomposer.core.service.decomposer.melody.equality;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE_TRIPLET;
import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.BF4;
import static jm.constants.Pitches.CS3;
import static jm.constants.Pitches.CS4;
import static jm.constants.Pitches.D4;
import static jm.constants.Pitches.EF4;
import static jm.constants.Pitches.EF5;
import static jm.constants.Pitches.G4;
import static jm.constants.Pitches.GS4;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jm.music.data.Note;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.service.equality.melody.DifferentNumberOfNotesRequired;
import ru.pavelyurkin.musiccomposer.core.service.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.service.equality.melodymovement.InterpolationMelodyMovementEquality;

public class TestInterpolationMelodyMovementEquality {

  InterpolationMelodyMovementEquality interpolationMelodyMovementEquality = new InterpolationMelodyMovementEquality();
  Equality equality = new DifferentNumberOfNotesRequired(interpolationMelodyMovementEquality);

  @Test
  public void testCase1() {
    Melody testMelody1 = new Melody(
        new Note[] {
            new Note(D4, EIGHTH_NOTE),
            new Note(EF4, DOTTED_EIGHTH_NOTE),
            new Note(CS4, EIGHTH_NOTE),
            new Note(D4, EIGHTH_NOTE)}
    );
    Melody testMelody2 = new Melody(
        new Note[] {
            new Note(A4, SIXTEENTH_NOTE_TRIPLET),
            new Note(BF4, SIXTEENTH_NOTE_TRIPLET),
            new Note(G4, SIXTEENTH_NOTE_TRIPLET),
            new Note(A4, SIXTEENTH_NOTE_TRIPLET),
            new Note(GS4, SIXTEENTH_NOTE_TRIPLET),
            new Note(A4, SIXTEENTH_NOTE_TRIPLET)}
    );

    interpolationMelodyMovementEquality.setMaxNumberOfAddedIntervals(2);
    assertTrue(equality.test(testMelody1, testMelody2));

    interpolationMelodyMovementEquality.setMaxNumberOfAddedIntervals(1);
    assertFalse(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase2() {
    Melody testMelody1 = new Melody(
        new Note[] {
            new Note(D4, EIGHTH_NOTE),
            new Note(EF4, DOTTED_EIGHTH_NOTE),
            new Note(CS4, EIGHTH_NOTE),
            new Note(D4, EIGHTH_NOTE),
            new Note(D4, EIGHTH_NOTE)}
    );
    Melody testMelody2 = new Melody(
        new Note[] {
            new Note(A4, SIXTEENTH_NOTE_TRIPLET),
            new Note(BF4, SIXTEENTH_NOTE_TRIPLET),
            new Note(G4, SIXTEENTH_NOTE_TRIPLET),
            new Note(A4, SIXTEENTH_NOTE_TRIPLET),
            new Note(GS4, SIXTEENTH_NOTE_TRIPLET),
            new Note(BF4, SIXTEENTH_NOTE_TRIPLET)}
    );

    interpolationMelodyMovementEquality.setMaxNumberOfAddedIntervals(20);
    assertFalse(equality.test(testMelody1, testMelody2));

    interpolationMelodyMovementEquality.setMaxNumberOfAddedIntervals(20);
    assertFalse(equality.test(testMelody1, testMelody2));
  }

  @Test
  public void testCase3() {
    Melody testMelody1 = new Melody(
        new Note[] {
            new Note(D4, EIGHTH_NOTE),
            new Note(EF4, DOTTED_EIGHTH_NOTE),
            new Note(CS4, EIGHTH_NOTE),
            new Note(D4, EIGHTH_NOTE)}
    );
    Melody testMelody2 = new Melody(
        new Note[] {
            new Note(G4, EIGHTH_NOTE),
            new Note(EF5, DOTTED_EIGHTH_NOTE),
            new Note(CS3, EIGHTH_NOTE),
            new Note(G4, EIGHTH_NOTE)}
    );

    interpolationMelodyMovementEquality.setMaxNumberOfAddedIntervals(20);
    assertFalse(equality.test(testMelody1, testMelody2));

    interpolationMelodyMovementEquality.setMaxNumberOfAddedIntervals(20);
    assertFalse(equality.test(testMelody1, testMelody2));
  }

  @Disabled
  // problem in this test
  @Test
  public void testCase4() {
    Melody testMelody1 = new Melody(
        new Note[] {
            new Note(69, 1),
            new Note(68, 0.5),
            new Note(66, 0.5),
            new Note(64, 1),
            new Note(74, 0.5),
            new Note(72, 0.5),
            new Note(71, 1),
            new Note(68, 0.5),
            new Note(69, 0.5),
        }
    );
    Melody testMelody2 = new Melody(
        new Note[] {
            new Note(72, 0.5),
            new Note(67, 0.25),
            new Note(67, 0.25),
            new Note(76, 0.5),
            new Note(74, 0.5),
            new Note(71, 0.5),
            new Note(72, 1.5),
            new Note(74, 0.5),
            new Note(76, 0.5),
            new Note(72, 0.5),
        }
    );

    //		View.notate( testMelody1 );
    //		View.notate( testMelody2 );
    //		suspend();

    interpolationMelodyMovementEquality.setMaxNumberOfAddedIntervals(2);
    assertFalse(equality.test(testMelody1, testMelody2));

  }
}
