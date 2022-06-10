package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.C3;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.C5;
import static jm.constants.Pitches.C6;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ChordTest {

  @Test
  void notParallelIfDifferentRhythmValue() {
    Chord chord1 = new Chord(List.of(C3, C4, C5), WHOLE_NOTE);
    Chord chord2 = new Chord(List.of(C3, C4, C5), QUARTER_NOTE);
    assertFalse(chord1.getPitchDistanceIfParallel(chord2).isPresent());
  }

  @Test
  void notParallelIfDifferentNumberOfNotes() {
    Chord chord1 = new Chord(List.of(C3, C4, C5), WHOLE_NOTE);
    Chord chord2 = new Chord(List.of(C3, C4), WHOLE_NOTE);
    assertFalse(chord1.getPitchDistanceIfParallel(chord2).isPresent());
  }

  @Test
  @Disabled("todo")
  void parallelIfNotesAreInDifferentOrder() {
    Chord chord1 = new Chord(List.of(C3, C4, C5), WHOLE_NOTE);
    Chord chord2 = new Chord(List.of(C5, C4, C3), WHOLE_NOTE);
    assertThat(chord1.getPitchDistanceIfParallel(chord2), is(Optional.of(0)));
  }

  @Test
  void parallelIfWithCorrectPitchDiff() {
    Chord chord1 = new Chord(List.of(C3, C4, C5), WHOLE_NOTE);
    Chord chord2 = new Chord(List.of(C4, C5, C6), WHOLE_NOTE);
    assertThat(chord1.getPitchDistanceIfParallel(chord2), is(Optional.of(12)));

    Chord chord3 = new Chord(List.of(C3, C4, C5), WHOLE_NOTE);
    Chord chord4 = new Chord(List.of(C3, C4, C5), WHOLE_NOTE);
    assertThat(chord3.getPitchDistanceIfParallel(chord4), is(Optional.of(0)));
  }

  @Test
  @Disabled("todo")
  void samePitchesEvenIfNotSorted() {
    assertTrue(new Chord(List.of(C3, C4, C5), WHOLE_NOTE).samePitches(List.of(C5, C4, C3)));
  }
}
