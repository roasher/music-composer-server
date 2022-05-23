package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jm.music.data.Note;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import ru.pavelyurkin.musiccomposer.core.utils.ParallelUtils;

@Data
public class Chord extends NoteGroup {

  // todo: change to set
  private List<Integer> pitches;
  private double rhythmValue;

  public Chord(List<Integer> pitches, double rhythmValue) {
    this.pitches = pitches;
    this.rhythmValue = rhythmValue;
    if (pitches.isEmpty()) {
      throw new IllegalArgumentException("Pitches array can't be empty");
    }
  }

  @Override
  public double getRhythmValue() {
    return rhythmValue;
  }

  @Override
  public Chord clone() {
    return new Chord(new ArrayList<>(pitches), rhythmValue);
  }

  @Override
  public boolean isRest() {
    return pitches.stream().allMatch(integer -> integer.equals(Note.REST));
  }

  @Override
  public int getMaxPitch() {
    return pitches.stream().mapToInt(Integer::intValue).max().getAsInt();
  }

  @Override
  public int getMinNonRestPitch() {
    return pitches.stream().filter(integer -> integer != Note.REST).mapToInt(Integer::intValue).min()
        .getAsInt();
  }

  @Override
  public List<Integer> getFirstVerticalPitches() {
    return pitches;
  }

  @Override
  public List<Integer> getLastVerticalPitches() {
    return pitches;
  }

  @Override
  public NoteGroup transposeClone(int transposePitch) {
    List<Integer> tranposedPitches = pitches.stream()
        .map(integer -> integer != Note.REST ? integer + transposePitch : integer)
        .collect(Collectors.toList());
    return new Chord(tranposedPitches, this.rhythmValue);
  }

  @Override
  public Pair<NoteGroup, NoteGroup> divideByRhythmValue(double rhythmValue) {
    if (rhythmValue >= this.rhythmValue) {
      throw new IllegalArgumentException("Input rhythmValue can't be greater or equal than chords rhythmValue");
    }
    Chord left = this.clone();
    left.setRhythmValue(rhythmValue);

    Chord right = this.clone();
    right.setRhythmValue(this.rhythmValue - rhythmValue);
    return Pair.of(left, right);
  }

  @Override
  public NoteGroup cloneRange(double startTime, double endTime) {
    if (startTime < 0 || endTime > this.getRhythmValue() || startTime >= endTime) {
      throw new IllegalArgumentException("Can't clone range");
    }
    return new Chord(this.getPitches(),
        BigDecimal.valueOf(endTime).subtract(BigDecimal.valueOf(startTime)).doubleValue());
  }

  @Override
  public List<Double> getRhythmEdgeList() {
    return Collections.singletonList(this.rhythmValue);
  }

  @Override
  public Set<Integer> getAllPitches() {
    return new HashSet<>(pitches);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Chord chord = (Chord) o;
    return getPitchDistanceIfParallel(chord).isPresent();
  }

  @Override
  public boolean exactEquals(NoteGroup noteGroup) {
    return equals(noteGroup);
  }

  @Override
  public Optional<Integer> getPitchDistanceIfParallel(NoteGroup noteGroup) {
    if (this == noteGroup) {
      return Optional.of(0);
    }
    if (noteGroup == null || getClass() != noteGroup.getClass()) {
      return Optional.empty();
    }
    Chord thatChord = (Chord) noteGroup;
    if (Double.compare(thatChord.rhythmValue, rhythmValue) != 0) {
      return Optional.empty();
    }
    return ParallelUtils.getSinglePitchDifferenceIfExist(this.pitches, thatChord.pitches);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pitches, rhythmValue);
  }

  public boolean samePitches(List<Integer> pitches) {
    if (this.pitches.size() != pitches.size()) {
      return false;
    }
    for (int noteNumber = 0; noteNumber < this.pitches.size(); noteNumber++) {
      if (!this.pitches.get(noteNumber).equals(pitches.get(noteNumber))) {
        return false;
      }
    }
    return true;
  }
}
