package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import static jm.constants.Durations.DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.jupiter.api.Test;

class NewMelodyTest {

  @Test
  public void testEquality2() {
    Note[] notes1 = new Note[] {
        new Note(76, 0.5),
        new Note(74, 0.5),
        new Note(71, 0.5),
        new Note(69, 0.25),
        new Note(67, 0.25),
        new Note(69, 1),
        new Note(71, 0.25),
        new Note(72, 0.25)
    };
    NewMelody melody1 = new NewMelody(notes1);

    Note[] notes2 = new Note[] {
        new Note(76, 0.5),
        new Note(74, 0.5),
        new Note(71, 0.5),
        new Note(69, 0.25),
        new Note(67, 0.25),
        new Note(69, 1),
        new Note(71, 0.25),
        new Note(72, 0.25),
    };
    NewMelody melody2 = new NewMelody(notes2);

    Note[] notes3 = new Note[] {
        new Note(76, 0.5),
        new Note(74, 0.5),
        new Note(71, 0.5),
        new Note(69, 0.25),
        new Note(67, 0.25),
        new Note(69, 1.),
        new Note(71, 0.25),
        new Note(72, 0.25),
    };
    NewMelody melody3 = new NewMelody(notes3);

    assertEquals(melody1, melody2);
    assertEquals(melody1, melody3);
  }

  @Test
  public void isParallelTest() throws Exception {
    NewMelody etalonMelody = new NewMelody(
        new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Note(70, EIGHTH_NOTE), new Note(80, HALF_NOTE)
    );
    assertTrue(etalonMelody.isParallelTo(etalonMelody));

    NewMelody melody1 = new NewMelody(
        new Rest(QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 5, HALF_NOTE)
    );
    // second melody all pitches minus 5. pass.
    assertTrue(etalonMelody.isParallelTo(melody1));

    NewMelody melody2 = new NewMelody(
        new Rest(DOTTED_QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 5, HALF_NOTE)
    );
    // second melody first rest wrong rhythm value. fail.
    assertFalse(etalonMelody.isParallelTo(melody2));

    NewMelody melody3 = new NewMelody(
        new Rest(QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 4, HALF_NOTE)
    );
    // second melody last note -4 instead of -5. fail.
    assertFalse(etalonMelody.isParallelTo(melody3));

    NewMelody melody00 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE)
    );
    NewMelody melody01 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 7, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE)
    );
    // both melodies - all rests except one note. pass what ever pitches are
    assertTrue(melody00.isParallelTo(melody01));

    NewMelody melody10 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70, SIXTEENTH_NOTE)
    );
    NewMelody melody11 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 7, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // rests except note in the middle and last note. Those notes are parallel. pass.
    assertTrue(melody10.isParallelTo(melody11));

    NewMelody melody20 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70, SIXTEENTH_NOTE)
    );
    NewMelody melody21 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 6, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // rests except non-parallel notes. fail
    assertFalse(melody20.isParallelTo(melody21));

    NewMelody melody30 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Rest(QUARTER_NOTE)
    );
    NewMelody melody31 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // first melody is rests - second has one note. fail.
    assertFalse(melody30.isParallelTo(melody31));
  }


}