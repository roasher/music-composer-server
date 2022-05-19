package ru.pavelyurkin.musiccomposer.core.model.melody;

import java.util.ArrayList;
import java.util.List;
import jm.music.data.Note;
import jm.music.data.Phrase;
import ru.pavelyurkin.musiccomposer.core.model.PlaceInTheComposition;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

/**
 * Class represents Melody entity
 * Melodies considered as simple single-voice note moves
 * Created by Pavel Yurkin on 26.07.14.
 * @Deprecated due to NewMelody
 */
@Deprecated(forRemoval = true)
public class Melody extends Phrase {

  private PlaceInTheComposition placeInTheComposition;
  private Form form = new Form();

  public Melody(List<Note> notes) {
    this((Note[]) notes.toArray(new Note[] {}));
  }

  public Melody(Note... notes) {
    super(notes);
  }

  public Melody(char form, Note... notes) {
    super(notes);
    this.form = new Form(form);
  }

  public Melody() {
    super();
  }

  public PlaceInTheComposition getPlaceInTheComposition() {
    return placeInTheComposition;
  }

  public void setPlaceInTheComposition(PlaceInTheComposition placeInTheComposition) {
    this.placeInTheComposition = placeInTheComposition;
    this.setTitle(getTitle(placeInTheComposition));
  }

  public Form getForm() {
    return form;
  }

  public void setForm(Form form) {
    this.form = form;
  }

  private String getTitle(PlaceInTheComposition placeInTheComposition) {
    StringBuffer out = new StringBuffer();
    out.append(placeInTheComposition.getCompositionInfo().getTitle()).append("\n");
    out.append(" first note start time : ").append(placeInTheComposition.getFirstNoteStartTime()).append("\n");
    out.append(" last note end time : ").append(placeInTheComposition.getLastNoteEndTime()).append("\n");
    return out.toString();
  }

  public double getRythmValue() {
    return this.getNoteList().stream().mapToDouble(value -> ((Note) value).getRhythmValue()).sum();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Melody melody = (Melody) o;

    if (!this.form.equals(melody.form)) {
      return false;
    }

    return isParallelTo(melody);
  }

  public boolean isExactEquals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Melody melody = (Melody) o;

    if (!this.form.equals(melody.form)) {
      return false;
    }

    List<Note> thisNoteArray = this.getNoteList();
    List<Note> melodyNoteArray = melody.getNoteList();

    if (thisNoteArray.size() != melodyNoteArray.size()) {
      return false;
    }

    for (int currentNoteNumber = 0; currentNoteNumber < thisNoteArray.size(); currentNoteNumber++) {
      Note currentNoteFromThis = thisNoteArray.get(currentNoteNumber);
      Note currentNoteFromMelody = melodyNoteArray.get(currentNoteNumber);
      double rhythm1 = currentNoteFromMelody.getRhythmValue();
      double rhythm2 = currentNoteFromThis.getRhythmValue();
      if (currentNoteFromMelody.getPitch() != currentNoteFromThis.getPitch() || rhythm1 != rhythm2) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns transposed clone of melody
   *
   * @param transposePitch
   * @return
   */
  public Melody transposeClone(int transposePitch) {
    List<Note> notes = new ArrayList<>(this.size());
    for (Note note : (List<Note>) this.getNoteList()) {
      notes.add(
          new Note(note.getPitch() == Note.REST ? Note.REST : note.getPitch() + transposePitch, note.getRhythmValue(),
              note.getDynamic(), note.getPan()));
    }
    Melody newMelody = new Melody(notes);
    return newMelody;
  }

  @Override
  public int hashCode() {
    List<Note> notes = this.getNoteList();
    return this.getNote(0).getPitch() - this.getNote(notes.size() - 1).getPitch();
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.getStartTime()).append(" ");
    for (Note note : (List<Note>) this.getNoteList()) {
      stringBuilder.append(String
          .format("{%d %s|%.3f}", note.getPitch(), ModelUtils.getNoteNameByPitch(note.getPitch()),
              note.getRhythmValue()));
    }
    stringBuilder.append(" ").append(this.getEndTime());
    return stringBuilder.toString();
  }

  public boolean isParallelTo(Melody melody) {
    if (this.size() != melody.size()) {
      return false;
    }
    Integer pitchDiff = null;
    for (int noteNumber = 0; noteNumber < this.size(); noteNumber++) {
      Note firstNote = this.getNote(noteNumber);
      Note secondNote = melody.getNote(noteNumber);
      if (firstNote.getRhythmValue() != secondNote.getRhythmValue()) {
        return false;
      }
      if ((firstNote.isRest() && !secondNote.isRest()) || (!firstNote.isRest() && secondNote.isRest())) {
        return false;
      }
      if (firstNote.isRest() && secondNote.isRest()) {
        continue;
      }
      if (pitchDiff == null) {
        pitchDiff = secondNote.getPitch() - firstNote.getPitch();
      } else {
        if (pitchDiff != secondNote.getPitch() - firstNote.getPitch()) {
          return false;
        }
      }
    }
    return true;
  }
}
