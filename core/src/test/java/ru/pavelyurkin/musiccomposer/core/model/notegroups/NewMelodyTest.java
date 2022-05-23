package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import static jm.constants.Durations.DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.jupiter.api.Test;

class NewMelodyTest {

  @Test
  public void melodiesBuildedFromSameNotesAreEqual() {
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
  public void calculatePitchDifferenceIfParallel() throws Exception {
    NewMelody etalonMelody = new NewMelody(
        new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Note(70, EIGHTH_NOTE), new Note(80, HALF_NOTE)
    );
    assertThat(etalonMelody.getPitchDistanceIfParallel(etalonMelody), is(Optional.of(0)));

    NewMelody melody1 = new NewMelody(
        new Rest(QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 5, HALF_NOTE)
    );
    // second melody all pitches minus 5. pass.
    assertThat(etalonMelody.getPitchDistanceIfParallel(melody1), is(Optional.of(-5)));

    NewMelody melody2 = new NewMelody(
        new Rest(DOTTED_QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 5, HALF_NOTE)
    );
    // second melody first rest wrong rhythm value. fail.
    assertThat(etalonMelody.getPitchDistanceIfParallel(melody2), is(Optional.empty()));

    NewMelody melody3 = new NewMelody(
        new Rest(QUARTER_NOTE), new Note(60 - 5, SIXTEENTH_NOTE), new Note(70 - 5, EIGHTH_NOTE),
        new Note(80 - 4, HALF_NOTE)
    );
    // second melody last note -4 instead of -5. fail.
    assertThat(etalonMelody.getPitchDistanceIfParallel(melody3), is(Optional.empty()));

    NewMelody melody00 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE)
    );
    NewMelody melody01 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 7, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE)
    );
    // both melodies - all rests except one note. pass what ever pitches are
    assertThat(melody00.getPitchDistanceIfParallel(melody01), is(Optional.of(-7)));

    NewMelody melody10 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70, SIXTEENTH_NOTE)
    );
    NewMelody melody11 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 7, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // rests except note in the middle and last note. Those notes are parallel. pass.
    assertThat(melody10.getPitchDistanceIfParallel(melody11), is(Optional.of(-7)));

    NewMelody melody20 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70, SIXTEENTH_NOTE)
    );
    NewMelody melody21 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60 - 6, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // rests except non-parallel notes. fail
    assertThat(melody20.getPitchDistanceIfParallel(melody21), is(Optional.empty()));

    NewMelody melody30 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Rest(QUARTER_NOTE)
    );
    NewMelody melody31 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Note(70 - 7, SIXTEENTH_NOTE)
    );
    // first melody is rests - second has one note. fail.
    assertThat(melody30.getPitchDistanceIfParallel(melody31), is(Optional.empty()));
  }

  @Test
  void restsAreParallel() {
    NewMelody melody1 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Rest(QUARTER_NOTE)
    );
    NewMelody melody2 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Rest(QUARTER_NOTE)
    );
    // all rests - parallel with 0
    assertThat(melody1.getPitchDistanceIfParallel(melody2), is(Optional.of(0)));

    NewMelody melody3 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Rest(QUARTER_NOTE)
    );
    NewMelody melody4 = new NewMelody(
        new Rest(QUARTER_NOTE), new Rest(QUARTER_NOTE), new Note(60, SIXTEENTH_NOTE), new Rest(QUARTER_NOTE),
        new Rest(QUARTER_NOTE)
    );
    // all rests except one same note - parallel with 0
    assertThat(melody4.getPitchDistanceIfParallel(melody3), is(Optional.of(0)));
  }
}