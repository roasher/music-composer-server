package ru.pavelyurkin.musiccomposer.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Class represents movement from one musical block to another
 * Created by Pavel Yurkin on 20.07.14.
 */
public class BlockMovement implements Serializable {

  private List<Integer> voiceMovements = new ArrayList<>();
  public static final int MOVEMENT_TO_REST = Integer.MIN_VALUE;
  public static final int MOVEMENT_FROM_REST = Integer.MAX_VALUE;

  public BlockMovement(List<Integer> movements) {
    this.voiceMovements = movements;
  }

  public BlockMovement(Integer... movements) {
    this(Arrays.asList(movements));
  }

  public BlockMovement(List<Melody> firstMelodies, List<Melody> secondMelodies) {

    if (firstMelodies.size() != secondMelodies.size()) {
      throw new RuntimeException("Can't create BlockMovement from different amount of input melody collections");
    }

    for (int melodyNumber = 0; melodyNumber < firstMelodies.size(); melodyNumber++) {
      Vector firstMelodyNoteList = firstMelodies.get(melodyNumber).getNoteList();
      int lastNotePitchFromFirstMelody = ((Note) firstMelodyNoteList.get(firstMelodyNoteList.size() - 1)).getPitch();
      Vector secondMelodyNoteList = secondMelodies.get(melodyNumber).getNoteList();
      int firstNotePitchFromSecondMelody = ((Note) secondMelodyNoteList.get(0)).getPitch();

      if (lastNotePitchFromFirstMelody == Note.REST && firstNotePitchFromSecondMelody != Note.REST) {
        voiceMovements.add(MOVEMENT_FROM_REST);
      } else if (lastNotePitchFromFirstMelody != Note.REST && firstNotePitchFromSecondMelody == Note.REST) {
        voiceMovements.add(MOVEMENT_TO_REST);
      } else {
        voiceMovements.add(firstNotePitchFromSecondMelody - lastNotePitchFromFirstMelody);
      }
    }

  }

  public List<Integer> getVoiceMovements() {
    return voiceMovements;
  }

  public int getVoiceMovement(int melodyNumber) {
    return voiceMovements.get(melodyNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BlockMovement)) {
      return false;
    }

    BlockMovement that = (BlockMovement) o;

    if (!voiceMovements.equals(that.voiceMovements)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return voiceMovements.hashCode();
  }

  @Override
  public String toString() {
    return voiceMovements.toString();
  }
}
