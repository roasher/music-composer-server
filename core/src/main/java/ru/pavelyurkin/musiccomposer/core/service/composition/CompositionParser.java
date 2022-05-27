package ru.pavelyurkin.musiccomposer.core.service.composition;

import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Rest;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;


@Component
public class CompositionParser {

  /**
   * Parses composition to Instrument Part model
   *
   * @param composition
   * @return
   */
  public List<InstrumentPart> parse(Composition composition) {
    List<InstrumentPart> instrumentParts = (List<InstrumentPart>) composition.getPartList().stream()
        .map(part -> {
          InstrumentPart instrumentPart = convert((Part) part);
          // add rest to the end
          if (composition.getEndTime() > instrumentPart.getRythmValue()) {
            instrumentPart.addNoteToTheEnd(new Rest(composition.getEndTime() - instrumentPart.getRythmValue()));
          }
          return instrumentPart;
        })
        .collect(Collectors.toList());

    return instrumentParts;
  }

  @VisibleForTesting
  public InstrumentPart convert(Part part) {
    List<Phrase> phrases = part.getPhraseList();
    List<Double> rhythmEdges = phrases.stream()
        .flatMap(phrase -> getRhythmEdgeList(phrase).stream())
        .distinct()
        .sorted().collect(Collectors.toList());

    double previousEdge = 0;
    InstrumentPart instrumentPart = new InstrumentPart();
    instrumentPart.setInstrument(part.getInstrument());
    for (double edge : rhythmEdges) {
      List<Integer> notePitches = phrases.stream()
          .flatMap(phrase -> getNotesAtTheEdge(phrase, edge).stream())
          .map(Note::getPitch)
          .distinct()
          .collect(Collectors.toList());
      if (notePitches.size() == 1) {
        Note noteToAdd = new Note(notePitches.get(0), edge - previousEdge);
        if (noteToAdd.isRest()) {
          // gluing rest to last note (if it is rest as well they both would be glued)
          instrumentPart.glueNoteToTheEnd(noteToAdd);
        } else {
          instrumentPart.addNoteToTheEnd(noteToAdd);
        }
      } else if (notePitches.size() > 1) {
        instrumentPart.addChordToTheEnd(new Chord(notePitches, edge - previousEdge));
      } else {
        // add rest if delay on start
        instrumentPart.glueNoteToTheEnd(new Rest(edge - previousEdge));
      }
      previousEdge = edge;
    }
    return instrumentPart;
  }

  /**
   * Returns the edge list of phrases notes
   * Sets first edge to rhythm value of the first note, and incrementing this value by
   * rhythm value of all noteGroups one by one
   *
   * @param phrase
   * @return
   */
  public List<Double> getRhythmEdgeList(Phrase phrase) {
    List<Double> edgeList = new ArrayList<>();
    if (phrase.getStartTime() > 0) {
      edgeList.add(phrase.getStartTime());
    }
    double lastEdge = phrase.getStartTime();
    for (int noteNumber = 0; noteNumber < phrase.size(); noteNumber++) {
      Note note = phrase.getNote(noteNumber);
      edgeList.add(lastEdge + note.getRhythmValue());
      lastEdge = edgeList.get(edgeList.size() - 1);
    }
    return edgeList;
  }

  /**
   * Returns note from phrase that sounds in particular time
   * If input time is finish time to one and start time to another, the first one will be returned
   *
   * @param phrase - note array
   * @param time
   * @return TODO refactor using binary search
   */
  public List<Note> getNotesAtTheEdge(Phrase phrase, double time) {
    List<Note> out = new ArrayList<>();
    double startTime = phrase.getStartTime();
    for (int currentNoteNumber = 0; currentNoteNumber < phrase.size(); currentNoteNumber++) {
      double rhythm = phrase.getNote(currentNoteNumber).getRhythmValue();
      if (Double.compare(rhythm, 0.0) == 0) {
        throw new RuntimeException("Note with zero rhythm value");
      }
      if (startTime < time && time <= startTime + rhythm) {
        out.add((Note) phrase.getNoteList().get(currentNoteNumber));
      }
      startTime += rhythm;
    }
    return out;
  }
}
