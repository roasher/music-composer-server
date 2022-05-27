package ru.pavelyurkin.musiccomposer.core.service.composition;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Rest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NoteGroup;

/**
 * Slices composition into pieces
 * Created by night wish on 08.11.14.
 */
@Component
@Slf4j
public class CompositionSlicer {

  /**
   * Slices instrumentParts into melodies, having certain timePeriod each.
   *
   * @param instrumentParts
   * @param timePeriod
   * @return
   */
  public List<List<InstrumentPart>> slice(List<InstrumentPart> instrumentParts, double timePeriod) {
    List<List<InstrumentPart>> compositionList = new ArrayList<>();

    adjustToUnifiedEndTime(instrumentParts);
    for (InstrumentPart instrumentPart : instrumentParts) {
      List<InstrumentPart> noteList = slice(instrumentPart, timePeriod);
      compositionList.add(noteList);
    }

    // Composition list should has equal number of slices in each instrument
    int numberOfSlices = compositionList.get(0).size();
    for (List<InstrumentPart> slices : compositionList) {
      if (slices.size() != numberOfSlices) {
        throw new RuntimeException("Sliced instrumentParts has differed number of slices for different instrument.");
      }
    }

    List<List<InstrumentPart>> compositionMelodies = new ArrayList<>();
    for (int melodyBlockNumber = 0; melodyBlockNumber < compositionList.get(0).size(); melodyBlockNumber++) {
      List<InstrumentPart> melodyBlock = new ArrayList<>();
      for (List<InstrumentPart> instrumentPart : compositionList) {
        melodyBlock.add(instrumentPart.get(melodyBlockNumber));
      }
      compositionMelodies.add(melodyBlock);
    }

    return compositionMelodies;
  }

  /**
   * Slice instrumentPart into parts of timePeriod length
   *
   * @param instrumentPart
   * @param timePeriod
   * @return
   */
  public List<InstrumentPart> slice(InstrumentPart instrumentPart, double timePeriod) {

    State state = new State(timePeriod, instrumentPart.getInstrument());
    for (int noteGroupNumber = 0; noteGroupNumber < instrumentPart.getNoteGroups().size(); noteGroupNumber++) {
      NoteGroup noteGroup = instrumentPart.getNoteGroups().get(noteGroupNumber);
      state.add(noteGroup);
    }

    // Filling last slice with rests if it shorter than time period
    InstrumentPart lastSlice = state.slices.get(state.slices.size() - 1);
    double lastSliceRhythmValue = lastSlice.getRythmValue();
    if (lastSliceRhythmValue < timePeriod) {
      lastSlice.glueNoteToTheEnd(new Note(JMC.REST, timePeriod - lastSliceRhythmValue));
    }

    checkSlicesOccupancy(state.slices, timePeriod);
    return state.slices;
  }

  /**
   * Class consumes notes and divide them into slices timePeriod length
   */
  private class State {
    private List<InstrumentPart> slices = new ArrayList<>();
    private int instrument;
    private double timePeriod;

    private State(double timePeriod, int instrument) {
      this.timePeriod = timePeriod;
      this.instrument = instrument;
    }

    public void add(NoteGroup noteGroup) {
      // Finding current state: slice and it's last noteGroup time
      InstrumentPart slice = null;
      double lastNoteEndTime = 0;
      if (slices.size() == 0) {
        slice = new InstrumentPart();
        slice.setInstrument(instrument);
        slices.add(slice);
        lastNoteEndTime = 0;
      } else {
        slice = slices.get(slices.size() - 1);
        for (NoteGroup currentSliceNote : slice.getNoteGroups()) {
          lastNoteEndTime += currentSliceNote.getRhythmValue();
        }
        // fulfill slice check
        if (lastNoteEndTime == timePeriod) {
          lastNoteEndTime = 0;
          slice = new InstrumentPart();
          slice.setInstrument(instrument);
          slices.add(slice);
        }
      }

      // Adding noteGroup
      if (lastNoteEndTime + noteGroup.getRhythmValue() <= timePeriod) {
        slice.getNoteGroups().add(noteGroup.clone());
      } else {
        Pair<NoteGroup, NoteGroup> dividedNoteGroup = noteGroup.divideByRhythmValue(timePeriod - lastNoteEndTime);
        slice.getNoteGroups().add(dividedNoteGroup.getLeft());

        // Recursive call
        add(dividedNoteGroup.getRight());
      }
    }
  }

  /**
   * Checks if all slices has length equal to timePeriod
   *
   * @param slices
   * @param timePeriod
   */
  public void checkSlicesOccupancy(List<InstrumentPart> slices, double timePeriod) {
    for (InstrumentPart slice : slices) {
      BigDecimal rhythmValuesSum = BigDecimal.ZERO;
      for (NoteGroup sliceNote : slice.getNoteGroups()) {
        rhythmValuesSum = rhythmValuesSum.add(BigDecimal.valueOf(sliceNote.getRhythmValue()));
      }
      if (rhythmValuesSum.round(MathContext.DECIMAL32).compareTo(BigDecimal.valueOf(timePeriod)) != 0) {
        throw new RuntimeException("Slice occupancy check failed");
      }
    }
  }

  /**
   * Makes all phrases has equal end time by adding rests if needed
   *
   * @param instrumentParts
   */
  public void adjustToUnifiedEndTime(List<InstrumentPart> instrumentParts) {
    double compositionEndTime = instrumentParts.stream()
        .mapToDouble(InstrumentPart::getRythmValue)
        .max()
        .getAsDouble();
    for (InstrumentPart instrumentPart : instrumentParts) {
      double rythmValue = instrumentPart.getRythmValue();
      if (rythmValue != compositionEndTime) {
        instrumentPart.glueNoteToTheEnd(new Rest(compositionEndTime - rythmValue));
      }
    }
  }
}
