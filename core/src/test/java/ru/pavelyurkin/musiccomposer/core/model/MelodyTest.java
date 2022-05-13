package ru.pavelyurkin.musiccomposer.core.model;

import static jm.constants.Durations.DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.Assert;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Created by night wish on 11.10.14.
 */
public class MelodyTest {

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
    Melody melody1 = new Melody(notes1);

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
    Melody melody2 = new Melody(notes2);

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
    Melody melody3 = new Melody(notes3);

    assertEquals(melody1, melody2);
    assertEquals(melody1, melody3);
  }

  @Test
  public void isParallelTest() throws Exception {
    Melody etalonMelody = new Melody(
        new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Note(70, EIGHTH_NOTE), new Note(80, HALF_NOTE)
    );
    assertTrue(etalonMelody.isParallelTo(etalonMelody));

    Melody melody1 = new Melody(
        new Rest(QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 5, HALF_NOTE)
    );
    // second melody all pitches minus 5. pass.
    assertTrue(etalonMelody.isParallelTo(melody1));

    Melody melody2 = new Melody(
        new Rest(DOTTED_QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 5, HALF_NOTE)
    );
    // second melody first rest wrong rhythm value. fail.
    Assert.assertFalse(etalonMelody.isParallelTo(melody2));

    Melody melody3 = new Melody(
        new Rest(QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 4, HALF_NOTE)
    );
    // second melody last note -4 instead of -5. fail.
    Assert.assertFalse(etalonMelody.isParallelTo(melody3));

    Melody melody00 = new Melody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE)
    );
    Melody melody01 = new Melody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 7, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE)
    );
    // both melodies - all rests except one note. pass what ever pitches are
    assertTrue(melody00.isParallelTo(melody01));

    Melody melody10 = new Melody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70, SIXTEENTH_NOTE)
    );
    Melody melody11 = new Melody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 7, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // rests except note in the middle and last note. Those notes are parallel. pass.
    assertTrue(melody10.isParallelTo(melody11));

    Melody melody20 = new Melody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70, SIXTEENTH_NOTE)
    );
    Melody melody21 = new Melody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 6, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // rests except non-parallel notes. fail
    Assert.assertFalse(melody20.isParallelTo(melody21));

    Melody melody30 = new Melody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Rest(QUARTER_NOTE)
    );
    Melody melody31 = new Melody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // first melody is rests - second has one note. fail.
    Assert.assertFalse(melody30.isParallelTo(melody31));
  }
}
