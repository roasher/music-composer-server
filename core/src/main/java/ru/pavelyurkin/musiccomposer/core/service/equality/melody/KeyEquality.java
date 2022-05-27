package ru.pavelyurkin.musiccomposer.core.service.equality.melody;

import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getNumberOfNotesOutOfKey;
import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getPossibleKeys;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import jm.music.data.Note;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Created by night wish on 01.11.14.
 */
@Component
public class KeyEquality implements Equality {

  @Value("${maxNumberOfNotesOutOfKey:0}")
  private int maxNumberOfNotesOutOfKey;

  @Override
  public boolean test(Melody firstMelody, Melody secondMelody) {
    double equalityMetrics = getEqualityMetric(firstMelody, secondMelody);
    double numberOfNotesOutOfKey = (1 - equalityMetrics) * secondMelody.size();
    return numberOfNotesOutOfKey <= maxNumberOfNotesOutOfKey;
  }

  private double getEqualityMetric(Melody firstMelody, Melody secondMelody) {
    List<Key> firstMelodyPossibleKeys = getPossibleKeys(0, getPitchesFromMelody(firstMelody));
    if (firstMelodyPossibleKeys.isEmpty()) {
      return 1;
    }
    OptionalInt minNumberOfOutNotes = firstMelodyPossibleKeys.stream()
        .mapToInt(value -> getNumberOfNotesOutOfKey(value, getPitchesFromMelody(secondMelody))).min();
    return (secondMelody.size() - minNumberOfOutNotes.getAsInt()) * 1. / secondMelody.size();
  }

  @NotNull
  private List<Integer> getPitchesFromMelody(Melody firstMelody) {
    return ((List<Note>) firstMelody.getNoteList()).stream()
        .map(Note::getPitch)
        .collect(Collectors.toList());
  }

  @Override
  public int getMaxNumberOfDiversedNotes() {
    return maxNumberOfNotesOutOfKey;
  }

  public int getMaxNumberOfNotesOutOfKey() {
    return maxNumberOfNotesOutOfKey;
  }

  public void setMaxNumberOfNotesOutOfKey(int maxNumberOfNotesOutOfKey) {
    this.maxNumberOfNotesOutOfKey = maxNumberOfNotesOutOfKey;
  }
}
