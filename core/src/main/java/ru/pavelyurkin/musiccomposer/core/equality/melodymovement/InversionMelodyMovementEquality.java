package ru.pavelyurkin.musiccomposer.core.equality.melodymovement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.melody.MelodyMovement;

/**
 * Class represents inversion test for melodies
 *
 * @author Pavel Yurkin
 * @date 27.06.2014.
 */
@Component
public class InversionMelodyMovementEquality extends AbstractMelodyMovementEquality {
  // Maximum allowable number of inverted intervals
  @Value("${inversionMelodyMovementEquality.maxNumberOfInvertedIntervals:1}")
  private int maxNumberOfInvertedIntervals;

  @Override
  /**
   * true, if number of inverted intervals <= maxNumberOfInvertedIntervals
   */
  public boolean testEqualityByLogic(MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement) {
    int numberOfInvertedIntervals = 0;
    for (int currentInterval = 0; currentInterval < firstMelodyMovement.getPitchIntervals().size(); currentInterval++) {
      if (firstMelodyMovement.getPitchIntervals().get(currentInterval) == -secondMelodyMovement.getPitchIntervals()
          .get(currentInterval)) {
        numberOfInvertedIntervals++;
      }
    }
    if (numberOfInvertedIntervals <= maxNumberOfInvertedIntervals) {
      return true;
    } else {
      return false;
    }
  }

  //Getters and Setters
  public int getMaxNumberOfInvertedIntervals() {
    return maxNumberOfInvertedIntervals;
  }

  public void setMaxNumberOfInvertedIntervals(int maxNumberOfInvertedIntervals) {
    this.maxNumberOfInvertedIntervals = maxNumberOfInvertedIntervals;
  }

  @Override
  public int getMaxNumberOfDiversedNotes() {
    return maxNumberOfInvertedIntervals;
  }
}