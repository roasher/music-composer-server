package ru.pavelyurkin.musiccomposer.core.model.composition;

import java.util.List;
import java.util.stream.Collectors;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

/**
 * Class extends Score class from jMusic adding new information about the Composition
 */
public class Composition extends jm.music.data.Score {

  private CompositionInfo compositionInfo;

  public Composition() {
  }

  public Composition(Score score) {
    super(score.getPartArray());
  }

  public Composition(Part[] parts) {
    super(parts);
  }

  public Composition(List<Part> parts) {
    super(parts.toArray(new Part[] {}));
  }

  public CompositionInfo getCompositionInfo() {
    return compositionInfo;
  }

  public void setCompositionInfo(CompositionInfo compositionInfo) {
    this.compositionInfo = compositionInfo;
  }

  public boolean hasSameNoteContent(Composition inputComposition) {
    Composition that = inputComposition;
    if (this.getPartList().size() != that.getPartList().size()) {
      return false;
    }
    for (int partNumber = 0; partNumber < this.getPartList().size(); partNumber++) {
      Part firstPart = this.getPart(partNumber);
      Part secondPart = that.getPart(partNumber);
      List<Note> notesFromFirstPart = (List<Note>) firstPart.getPhraseList()
          .stream()
          .flatMap(phrase -> ((Phrase) phrase).getNoteList().stream())
          .collect(Collectors.toList());
      List<Note> notesFromSecondPart = (List<Note>) secondPart.getPhraseList()
          .stream()
          .flatMap(phrase -> ((Phrase) phrase).getNoteList().stream())
          .collect(Collectors.toList());
      if (notesFromFirstPart.size() != notesFromSecondPart.size()) {
        return false;
      }
      for (int noteNumber = 0; noteNumber < notesFromFirstPart.size(); noteNumber++) {
        if (!notesFromFirstPart.get(noteNumber).equals(notesFromSecondPart.get(noteNumber))) {
          return false;
        }
      }
    }
    return true;
  }

}
