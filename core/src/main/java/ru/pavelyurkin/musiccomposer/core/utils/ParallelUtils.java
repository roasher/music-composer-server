package ru.pavelyurkin.musiccomposer.core.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jm.music.data.Note;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NoteGroup;

@UtilityClass
@Slf4j
public class ParallelUtils {

  /**
   * Return pitch that could be added to pitches asIs to make it toBe if able
   *
   * @param pitchesAsIs
   * @param pitchesToBe
   * @return
   */
  public static Optional<Integer> getSinglePitchDifferenceIfExist(List<Integer> pitchesAsIs, List<Integer> pitchesToBe) {
    if (pitchesToBe.size() != pitchesAsIs.size()) {
      log.debug("Can't calculate transpose pitch. Desired end pitches and fact previous end pitches has "
                + "different amount of notes");
      return Optional.empty();
    }

    List<Integer> subtractions = IntStream.range(0, pitchesToBe.size())
        .filter(operand -> {
          boolean isRest1 = pitchesAsIs.get(operand) == Note.REST;
          boolean isRest2 = pitchesToBe.get(operand) == Note.REST;
          // exclude rests
          return !(isRest1 && isRest2);
        })
        .map(operand -> pitchesToBe.get(operand) - pitchesAsIs.get(operand))
        .distinct()
        .boxed()
        .collect(Collectors.toList());

    // all are rests
    if (subtractions.isEmpty()) {
      return Optional.of(0);
    }

    if (subtractions.size() > 1) {
      log.debug(
          "Can't calculate transpose pitch. Desired end pitches and fact previous end pitches are not compatible.");
      return Optional.empty();
    }

    Integer transposePitch = subtractions.get(0);
    if (Math.abs(transposePitch) > Note.MAX_PITCH) {
      log.debug("Calculated pitch is out of range");
      return Optional.empty();
    }
    return Optional.of(transposePitch);
  }

  public static Optional<Integer> getPitchDistanceIfParallel(List<NoteGroup> noteGroups1, List<NoteGroup> noteGroups2) {
    if (noteGroups1.size() != noteGroups2.size()) {
      return Optional.empty();
    }

    Integer transposePitch = null;
    for (int noteGroupNumber = 0; noteGroupNumber < noteGroups1.size(); noteGroupNumber++) {
      NoteGroup noteGroup1 = noteGroups1.get(noteGroupNumber);
      NoteGroup noteGroup2 = noteGroups2.get(noteGroupNumber);
      Optional<Integer> noteGroupTransposePitch = noteGroup1.getPitchDistanceIfParallel(noteGroup2);
      if (noteGroupTransposePitch.isEmpty()
          || (transposePitch != null && !transposePitch.equals(noteGroupTransposePitch.get()))) {
        return Optional.empty();
      }
      if (transposePitch == null) {
        transposePitch = noteGroupTransposePitch.get();
      }
    }
    return Optional.ofNullable(transposePitch);
  }


}
