package ru.pavelyurkin.musiccomposer.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jm.constants.Pitches;
import lombok.experimental.UtilityClass;
import ru.pavelyurkin.musiccomposer.core.model.Key;

@UtilityClass
public class KeyUtils {

  public static int getNumberOfNotesOutOfKey(Key key, Collection<Integer> pitches) {
    return pitches.stream()
        .filter(pitch -> pitch != Pitches.REST)
        .mapToInt(pitch -> pitch % 12)
        .filter(notePitch -> !key.getKeyPitches().contains(notePitch))
        .toArray().length;
  }

  public static List<Key> getPossibleKeys(int maxNumberOfNotesOutOfKey, Collection<Integer> pitches) {
    List<Key> possibleKeys = new ArrayList<>();
    for (Key key : Key.values()) {
      int numberOfNotesOutOfKey = 0;
      for (Integer pitch : pitches) {
        if (pitch != Pitches.REST && !key.getKeyPitches().contains(pitch % 12)) {
          numberOfNotesOutOfKey++;
        }
      }
      if (numberOfNotesOutOfKey <= maxNumberOfNotesOutOfKey) {
        possibleKeys.add(key);
      }
    }

    return possibleKeys;
  }


}
