package ru.pavelyurkin.musiccomposer.core.equality.melodymovement;

import static java.lang.Math.signum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.melody.MelodyMovement;

/**
 * Class represents Contour test for melodies
 *
 * @author Pavel Yurkin
 * @date 27.06.2014.
 */
@Component
public class ContourMelodyMovementEquality extends AbstractMelodyMovementEquality {
  // Maximum allowable number of intervals in different direction

  @Value("${ContourMelodyMovementEquality.maxNumberOfDiffDirectionIntervals}")
  private int maxNumberOfDiffDirectionIntervals;

  @Override
  /**
   * true, if number of inverted intervals <= maxNumberOfDiffDirectionIntervals
   */
  public boolean testEqualityByLogic(MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement) {
    int numberNumberOfDiffDirectionIntervals = 0;
    for (int currentInterval = 0; currentInterval < firstMelodyMovement.getPitchIntervals().size(); currentInterval++) {
      int firstInterval = firstMelodyMovement.getPitchIntervals().get(currentInterval);
      int secondInterval = secondMelodyMovement.getPitchIntervals().get(currentInterval);
      boolean sameDirection = signum(firstInterval) == signum(secondInterval);
      if (!sameDirection) {
        numberNumberOfDiffDirectionIntervals++;
      }
    }
    return numberNumberOfDiffDirectionIntervals <= maxNumberOfDiffDirectionIntervals;
  }

  //Getters and Setters
  public int getMaxNumberOfDiffDirectionIntervals() {
    return maxNumberOfDiffDirectionIntervals;
  }

  public void setMaxNumberOfDiffDirectionIntervals(int maxNumberOfDiffDirectionIntervals) {
    this.maxNumberOfDiffDirectionIntervals = maxNumberOfDiffDirectionIntervals;
  }

  @Override
  public int getMaxNumberOfDiversedNotes() {
    return maxNumberOfDiffDirectionIntervals;
  }
}