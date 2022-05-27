package ru.pavelyurkin.musiccomposer.core.service.composition.loader;

import static jm.constants.Durations.DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.DOTTED_HALF_NOTE;
import static jm.constants.Durations.DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.DOTTED_SIXTEENTH_NOTE;
import static jm.constants.Durations.DOUBLE_DOTTED_EIGHTH_NOTE;
import static jm.constants.Durations.DOUBLE_DOTTED_HALF_NOTE;
import static jm.constants.Durations.DOUBLE_DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE_TRIPLET;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.HALF_NOTE_TRIPLET;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.QUARTER_NOTE_TRIPLET;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE_TRIPLET;
import static jm.constants.Durations.THIRTYSECOND_NOTE;
import static jm.constants.Durations.THIRTYSECOND_NOTE_TRIPLET;
import static jm.constants.Durations.WHOLE_NOTE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Class aggregates logic of rounding rhythm values after loading the composition
 * Created by pyurkin on 08.12.14.
 */
@Component
public class RhythmValueHandler {

  private final double maxRhythmValue;
  public final List<Double> rhythmValues = new ArrayList<>();

  public RhythmValueHandler() {
    rhythmValues.add(WHOLE_NOTE);
    rhythmValues.add(DOTTED_HALF_NOTE);
    rhythmValues.add(DOUBLE_DOTTED_HALF_NOTE);
    rhythmValues.add(2.5);
    rhythmValues.add(HALF_NOTE);
    rhythmValues.add(HALF_NOTE_TRIPLET);
    rhythmValues.add(QUARTER_NOTE);
    rhythmValues.add(QUARTER_NOTE_TRIPLET);
    rhythmValues.add(DOTTED_QUARTER_NOTE);
    rhythmValues.add(DOUBLE_DOTTED_QUARTER_NOTE);
    rhythmValues.add(EIGHTH_NOTE);
    rhythmValues.add(DOTTED_EIGHTH_NOTE);
    rhythmValues.add(EIGHTH_NOTE_TRIPLET);
    rhythmValues.add(DOUBLE_DOTTED_EIGHTH_NOTE);

    rhythmValues.add(SIXTEENTH_NOTE);
    rhythmValues.add(DOTTED_SIXTEENTH_NOTE);
    rhythmValues.add(SIXTEENTH_NOTE_TRIPLET);

    rhythmValues.add(THIRTYSECOND_NOTE);
    rhythmValues.add(THIRTYSECOND_NOTE_TRIPLET);

    rhythmValues.add(0.);

    // FIXME костыль. Таких значений не должно быть
    rhythmValues.add(2.7);
    rhythmValues.add(0.8);
    rhythmValues.add(2.25);

    Collections.sort(rhythmValues);
    maxRhythmValue = Collections.max(rhythmValues);
  }

  ;

  /**
   * Import of midi file can be not so precise as we want to
   * This function rounds import value to the JMC library has
   *
   * @param rhythmValue
   * @return
   */
  public double roundRhythmValue(double rhythmValue) {
    // TODO Might be troubles with comples legato: for example from half_note to eight_triol...
    // How many max rhythm values can fit in input rhythmValue
    int maxRhythmValueNumber = (int) (rhythmValue / maxRhythmValue);
    double roundRhythmValue = getApproximatedValue(rhythmValue - maxRhythmValueNumber * maxRhythmValue)
                              + maxRhythmValueNumber * maxRhythmValue;
    return roundRhythmValue;
  }

  /**
   * Returns list element that is closest to the value
   * List must be ascending sorted
   *
   * @param value
   * @param list
   * @return
   */
  @Deprecated
  public double getClosestListElement(double value, List<Double> list) {
    int place = Collections.binarySearch(list, value);
    if (place >= 0) {
      return list.get(place);
    } else {
      double top = rhythmValues.get(-place - 1);
      double bottom = rhythmValues.get(-place - 2);
      if (top - value < value - bottom) {
        return top;
      }
      if (top - value > value - bottom) {
        return bottom;
      }
      throw new RuntimeException(
          "It is impossible to choose closest list element : there are 2 values having equal distance with " + value);
    }
  }

  public double getApproximatedValue(double value) {
    int quarterNumber = (int) (value / THIRTYSECOND_NOTE);
    double closestQuarterNumber =
        value - quarterNumber * THIRTYSECOND_NOTE > (quarterNumber + 1) * THIRTYSECOND_NOTE - value ?
            quarterNumber + 1 : quarterNumber;

    int tripletNumber = (int) (value / THIRTYSECOND_NOTE_TRIPLET);
    double closestTripletNumber =
        value - tripletNumber * THIRTYSECOND_NOTE_TRIPLET > (tripletNumber + 1) * THIRTYSECOND_NOTE_TRIPLET - value ?
            tripletNumber + 1 : tripletNumber;

    return Math.abs(value - closestQuarterNumber * THIRTYSECOND_NOTE) < Math
        .abs(value - closestTripletNumber * THIRTYSECOND_NOTE_TRIPLET) ? closestQuarterNumber * THIRTYSECOND_NOTE :
        closestTripletNumber * THIRTYSECOND_NOTE_TRIPLET;
  }
}
