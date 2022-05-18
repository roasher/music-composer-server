package ru.pavelyurkin.musiccomposer.core.equality.melodymovement;

import static java.lang.Math.abs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.melody.MelodyMovement;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
@Component
public class IntervalsMelodyMovementEquality extends AbstractMelodyMovementEquality {

  // Maximum allowable number of intervals that can be different
  @Value("${intervalsMelodyMovementEquality.maxNumberOfShiftedIntervals:2}")
  private int maxNumberOfShiftedIntervals;
  // Maximum allowable pitch difference
  @Value("${intervalsMelodyMovementEquality.maxShift:2}")
  private int maxShift;

  public double getEqualityMetric(MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement) {
    double difference = 0;
    for (int currentInterval = 0; currentInterval < firstMelodyMovement.getPitchIntervals().size(); currentInterval++) {
      difference += getWeight(firstMelodyMovement.getPitchIntervals().get(currentInterval),
          secondMelodyMovement.getPitchIntervals().get(currentInterval));
    }
    return (firstMelodyMovement.getPitchIntervals().size() - difference) * 1. / firstMelodyMovement.getPitchIntervals()
        .size();
  }

  private double getWeight(int firstInterval, int secondInterval) {
    if (firstInterval == secondInterval) {
      return 0;
    }
    float firstSignum = Math.signum(firstInterval);
    float secondSignum = Math.signum(secondInterval);
    if (firstSignum != 0 && secondSignum != 0 && firstSignum != secondSignum) {
      return 1.0;
    }
    int intervalDifferece = abs(firstInterval - secondInterval);
    if (intervalDifferece >= 3) {
      return 1.0;
    } else if (intervalDifferece >= 2) {
      return 0.7;
    } else {
      return 0.5;
    }
  }

  @Override
  /**
   * @returns true if number of shifted intervals ( with value difference fits into maxShift ) <=
   * maxNumberOfShiftedIntervals
   * If there is one interval that has pitch difference > maxShift or there number greater than
   * maxNumberOfShiftedIntervals
   * returns false
   */ public boolean testEqualityByLogic(MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement) {
    int numberOfShiftedIntervals = 0;
    for (int currentInterval = 0; currentInterval < firstMelodyMovement.getPitchIntervals().size(); currentInterval++) {
      int intervalDifferece =
          abs(firstMelodyMovement.getPitchIntervals().get(currentInterval) - secondMelodyMovement.getPitchIntervals()
              .get(currentInterval));
      if (intervalDifferece != 0) {
        if (intervalDifferece <= maxShift) {
          numberOfShiftedIntervals++;
        } else {
          return false;
        }
      }
    }
    return numberOfShiftedIntervals <= maxNumberOfShiftedIntervals;
  }

  public int getMaxShift() {
    return maxShift;
  }

  public void setMaxShift(int maxShift) {
    this.maxShift = maxShift;
  }

  public int getMaxNumberOfShiftedIntervals() {
    return maxNumberOfShiftedIntervals;
  }

  public void setMaxNumberOfShiftedIntervals(int maxNumberOfShiftedIntervals) {
    this.maxNumberOfShiftedIntervals = maxNumberOfShiftedIntervals;
  }

  @Override
  public int getMaxNumberOfDiversedNotes() {
    return maxNumberOfShiftedIntervals;
  }
}
