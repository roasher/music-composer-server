package ru.pavelyurkin.musiccomposer.core.utils;

import static com.google.common.collect.Iterables.getLast;

import com.google.common.annotations.VisibleForTesting;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jm.constants.Pitches;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NoteGroup;

/**
 * Class aggregates useful utilities upon Model objects
 * Created by Pavel Yurkin on 20.07.14.
 */
public class ModelUtils {

  /**
   * Checking that all melodies has same rhythm value and returns this value
   *
   * @param instrumentParts
   * @return
   */
  public static double retrieveRhythmValue(List<InstrumentPart> instrumentParts) {
    double currentRhytmValue = instrumentParts.get(0).getRythmValue();
    for (int currentInstrument = 1; currentInstrument < instrumentParts.size(); currentInstrument++) {
      if (currentRhytmValue != instrumentParts.get(currentInstrument).getRythmValue()) {
        throw new IllegalArgumentException(
            String.format("Several instruments has different rhytmValues, for example: 0 and %s ", currentInstrument));
      }
    }
    return currentRhytmValue;
  }

  /**
   * Retrieves interval pattern between first notes of melodies.
   *
   * @param instrumentParts
   * @return
   */
  public static List<Integer> retrieveFirstIntervalPattern(List<InstrumentPart> instrumentParts) {
    List<Integer> firstVertical = new ArrayList<Integer>();
    for (int currentInstrument = 0; currentInstrument < instrumentParts.size(); currentInstrument++) {
      firstVertical.addAll(instrumentParts.get(currentInstrument).getFirstVerticalPitches());
    }
    return retrieveIntervalPattern(firstVertical);
  }

  /**
   * Retrieves interval pattern between last notes of melodies.
   *
   * @param instrumentParts
   * @return
   */
  public static List<Integer> retrieveLastIntervalPattern(List<InstrumentPart> instrumentParts) {
    List<Integer> lastVertical = new ArrayList<Integer>();
    for (int currentInstrument = 0; currentInstrument < instrumentParts.size(); currentInstrument++) {
      lastVertical.addAll(instrumentParts.get(currentInstrument).getLastVerticalPitches());
    }
    return retrieveIntervalPattern(lastVertical);
  }

  /**
   * Retrieves interval pattern from note list, represents music vertical
   *
   * @param notePitches
   * @return
   */
  public static List<Integer> retrieveIntervalPattern(List<Integer> notePitches) {
    // To prevent input List changing we will create a copy
    List<Integer> copyInputNotePitchesWithoutRests = notePitches.stream()
        .filter(pitch -> pitch != Note.REST)
        .collect(Collectors.toList());

    Collections.sort(copyInputNotePitchesWithoutRests);

    List<Integer> intervalPattern = new ArrayList<>();
    for (int currentPitchNumber = 0; currentPitchNumber < copyInputNotePitchesWithoutRests.size() - 1;
         currentPitchNumber++) {
      Integer currentPitch = copyInputNotePitchesWithoutRests.get(currentPitchNumber);
      Integer nextPitch = copyInputNotePitchesWithoutRests.get(currentPitchNumber + 1);
      if ((currentPitch != Note.REST && nextPitch == Note.REST) || (currentPitch == Note.REST
                                                                    && nextPitch != Note.REST)) {
        intervalPattern.add(Note.REST);
      } else {
        intervalPattern.add(nextPitch - currentPitch);
      }
    }
    return intervalPattern;
  }

  /**
   * Summarises all rhythm values in the array
   *
   * @param notes
   * @return
   */
  public static double sumAllRhytmValues(List<Note> notes) {
    double rhytmSum = 0;
    for (Note currentNote : notes) {
      rhytmSum += currentNote.getRhythmValue();
    }
    return rhytmSum;
  }

  /**
   * Summarises all rhythm values in the array
   *
   * @param melody
   * @return
   */
  public static double sumAllRhytmValues(Melody melody) {
    return sumAllRhytmValues(melody.getNoteList());
  }

  public static double sumAllRhythmValues(List<MusicBlock> musicBlocks) {
    return musicBlocks.stream().mapToDouble(MusicBlock::getRhythmValue).sum();
  }

  public static double getRhythmValue(Composition composition) {
    return composition.getPartList().stream()
        .mapToDouble(part -> sumAllRhytmValues((List<Note>)
            ((Part) part).getPhraseList().stream()
                .flatMap(phrase -> ((Phrase) phrase).getNoteList().stream())
                .collect(Collectors.toList())))
        .max()
        .orElse(0);
  }

  public static String getNoteNameByPitch(int pitch) {
    return Arrays.stream(Pitches.class.getDeclaredFields())
        .filter(field -> {
          try {
            return field.getInt(null) == pitch;
          } catch (IllegalAccessException e) {
            return false;
          }
        })
        .map(Field::getName)
        .sorted(Comparator.comparingInt(String::length))
        .findFirst()
        .map(name -> name.charAt(0) == 'F' ?
            'F' + replace(name.substring(1)) :
            replace(name))
        .orElse("");
  }

  private static String replace(String note) {
    return note.replace("F", "b").replace("S", "#");
  }

  /**
   * Return pitch that could be added to pitches asIs to make it toBe
   *
   * @param pitchesAsIs
   * @param pitchesToBe
   * @return
   */
  public static int getTransposePitch(List<Integer> pitchesAsIs, List<Integer> pitchesToBe) {
    if (pitchesToBe.size() != pitchesAsIs.size()) {
      throw new RuntimeException(
          "Can't capreviousEndPitcheslculate transpose pitch. Desired end pitches and fact previous end pitches has "
          + "different amount of notes");
    }
//		Collections.sort( pitchesToBe );
//		Collections.sort( pitchesAsIs );

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
      return 0;
    }

    if (subtractions.size() > 1) {
      throw new RuntimeException(
          "Can't calculate transpose pitch. Desired end pitches and fact previous end pitches are not compatible.");
    }

    Integer transposePitch = subtractions.get(0);
    if (Math.abs(transposePitch) > Note.MAX_PITCH) {
      throw new RuntimeException("Calculated pitch is out of range");
    }
    return transposePitch;
  }

  public static List<InstrumentPart> trimToTime(List<InstrumentPart> instrumentParts, double startTime,
                                                double endTime) {
    return instrumentParts.stream()
        .map(melody -> trimToTime(melody, startTime, endTime))
        .collect(Collectors.toList());
  }

  /**
   * @param instrumentPart
   * @param startTime
   * @param endTime
   * @return
   */
  public static InstrumentPart trimToTime(InstrumentPart instrumentPart, double startTime, double endTime) {
    if (startTime < 0 || endTime > instrumentPart.getRythmValue() || startTime > endTime) {
      throw new IllegalArgumentException(
          "Cant trim with this parameters: startTime = " + startTime + " ,endTime = " + endTime);
    }
    BigDecimal noteGroupStartTime = BigDecimal.ZERO;
    BigDecimal startTimeBD = BigDecimal.valueOf(startTime);
    BigDecimal endTimeBD = BigDecimal.valueOf(endTime);

    List<NoteGroup> noteGroups = new ArrayList<>();
    InstrumentPart out = new InstrumentPart(noteGroups, instrumentPart.getInstrument());
    for (int noteGroupNumber = 0; noteGroupNumber < instrumentPart.getNoteGroups().size(); noteGroupNumber++) {
      NoteGroup noteGroup = instrumentPart.getNoteGroups().get(noteGroupNumber);
      BigDecimal rhythmValue = BigDecimal.valueOf(noteGroup.getRhythmValue());
      BigDecimal noteGroupEndTime = noteGroupStartTime.add(rhythmValue);
      //   noteStTime             noteEndTime					noteEndTime may be after endTime
      // ------<>--------|------------<>------------------|----------<>-------
      //	 			startTime		  				endTime
      if (noteGroupStartTime.doubleValue() <= startTime && noteGroupEndTime.doubleValue() > startTime) {
        noteGroups.add(noteGroup.cloneRange(startTimeBD.subtract(noteGroupStartTime).doubleValue(),
            (endTimeBD.min(noteGroupEndTime)).subtract(noteGroupStartTime).doubleValue()));
      } else
        //             noteStTime     noteEndTime
        // ------|--------<>------------<>------------------|-------------------
        //	 startTime		  				              endTime
        if (noteGroupStartTime.doubleValue() >= startTime && noteGroupEndTime.doubleValue() <= endTime) {
          noteGroups.add(noteGroup.clone());
        } else
          //             noteStTime     								noteEndTime
          // ------|--------<>---------------------------------|-----------<>-------
          //	 startTime		  				              endTime
          if (noteGroupStartTime.doubleValue() < endTime && noteGroupEndTime.doubleValue() > endTime) {
            noteGroups.add(noteGroup.cloneRange(0, endTimeBD.subtract(noteGroupStartTime).doubleValue()));
            return out;
          }
      noteGroupStartTime = noteGroupStartTime.add(rhythmValue);
    }
    return out;
  }

  /**
   * Check if two times have equal "strength"
   *
   * @param firstStartTime
   * @param secondStartTime
   * @return
   */
  public static boolean isTimeCorrelated(double firstStartTime, double secondStartTime) {
//		return true;
    //TODO Disabled for testing purposes (Don't know if needed decide later)
    int originStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces(firstStartTime);
    int substitutorStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces(secondStartTime);
    if (originStartTimeDecimalPlacesNumber == substitutorStartTimeDecimalPlacesNumber) {
      return true;
    }
    return false;
  }

  /**
   * Needle blocks from first to the last transposing due to block movement. Solid Block at the end.
   *
   * @param blocks
   * @return
   */
  public static MusicBlock gatherBlocksWithTransposition(List<MusicBlock> blocks) {
    List<MusicBlock> transposedBlocks = new ArrayList<>();
    transposedBlocks.add(blocks.get(0));
    for (int blockNumber = 1; blockNumber < blocks.size(); blockNumber++) {
      MusicBlock previousBlock = blocks.get(blockNumber - 1);
      MusicBlock currentBlock = blocks.get(blockNumber);
      transposedBlocks.add(currentBlock.transposeClone(previousBlock));
    }
    return new MusicBlock(transposedBlocks);
  }

  public static Note clone(Note note) {
    return new Note(note.getPitch(), note.getRhythmValue(), note.getDynamic(), note.getPan());
  }

  public static Phrase clone(Phrase phrase) {
    Note[] clonedNotes = (Note[]) phrase.getNoteList()
        .stream()
        .map(note -> clone((Note) note))
        .toArray(Note[]::new);
    return new Phrase(clonedNotes);
  }

  /**
   * Returns blocks that are similar or different in terms of form
   *
   * @param formCompositionSteps
   * @param form
   * @param similar              - if true returns similar, different otherwise
   * @return
   */
  public static List<MusicBlock> getRelativeFormBlocks(List<FormCompositionStep> formCompositionSteps, Form form,
                                                       boolean similar) {
    return formCompositionSteps
        .stream()
        .filter(formCompositionStep -> similar == form.equals(formCompositionStep.getForm()))
        .map(formCompositionStep -> new MusicBlock(formCompositionStep.getCompositionSteps()
            .stream()
            .map(CompositionStep::getTransposedBlock)
            .collect(Collectors.toList())))
        .collect(Collectors.toList());
  }

  public static boolean isExactEquals(List<InstrumentPart> firstInstrumentParts,
                                      List<InstrumentPart> secondIntrumentParts) {
    if (firstInstrumentParts.size() != secondIntrumentParts.size()) {
      return false;
    }
    for (int melodyNumber = 0; melodyNumber < firstInstrumentParts.size(); melodyNumber++) {
      InstrumentPart normalizedFirst = normalizeInstrumentPart(firstInstrumentParts.get(melodyNumber));
      InstrumentPart normalizedSecond = normalizeInstrumentPart(secondIntrumentParts.get(melodyNumber));
      if (!normalizedFirst.equals(normalizedSecond)) {
        return false;
      }
    }
    return true;
  }

  @VisibleForTesting
  static InstrumentPart normalizeInstrumentPart(InstrumentPart instrumentPartNoNormalize) {
    InstrumentPart instrumentPart = new InstrumentPart();
    instrumentPart.setInstrument(instrumentPartNoNormalize.getInstrument());

    instrumentPart.getNoteGroups().add(instrumentPartNoNormalize.getNoteGroups().get(0).clone());

    for (int noteGroupNumber = 1; noteGroupNumber < instrumentPartNoNormalize.getNoteGroups().size();
         noteGroupNumber++) {
      NoteGroup noteGroupToAdd = instrumentPartNoNormalize.getNoteGroups().get(noteGroupNumber).clone();
      NoteGroup lastGroupNote = getLast(instrumentPart.getNoteGroups());

      if (lastGroupNote instanceof NewMelody && noteGroupToAdd instanceof NewMelody) {
        ((NewMelody) lastGroupNote).addNotesToTheEnd(((NewMelody) noteGroupToAdd).getNotes());
      } else {
        instrumentPart.getNoteGroups().add(noteGroupToAdd);
      }
    }

    return instrumentPart;
  }

}
