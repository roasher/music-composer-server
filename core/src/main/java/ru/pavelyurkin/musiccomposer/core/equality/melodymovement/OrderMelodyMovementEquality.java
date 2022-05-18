package ru.pavelyurkin.musiccomposer.core.equality.melodymovement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.melody.MelodyMovement;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
@Component
public class OrderMelodyMovementEquality extends AbstractMelodyMovementEquality {

  // Maximum allowable numbers of intervals which notes has been swapped
  @Value("${orderMelodyMovementEquality.maxNumberOfIntervalsHavingSwappedNotes}")
  private int maxNumberOfIntervalsHavingSwappedNotes;

  @Override
  /**
   * @returns true if one melody can be received from another swapping notes, and number of that swaps <=
   * maxNumberOfIntervalsHavingSwappedNotes
   * Swapping may move one note to the next ( +1 ) or previous ( -1 ) position but no further
   */
  public boolean testEqualityByLogic(MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement) {
    if (firstMelodyMovement.equals(secondMelodyMovement)) {
      return true;
    }
    return testIfEqualsToSwappedInterval(firstMelodyMovement, secondMelodyMovement, 0, 0);
  }

  // TODO this function may need refactoring, because: it uses brood force - may be too long
  private boolean testIfEqualsToSwappedInterval(final MelodyMovement etalonMelodyMovement,
                                                MelodyMovement melodyMovementToSwap, int numberOfIntervalToSwap,
                                                int numberOfSwappedIntervals) {
    // end of the recursion - Checking if we are out of range
    if (numberOfIntervalToSwap >= melodyMovementToSwap.getPitchIntervals().size()) {
      return false;
    }

    //  check case if Movements are equals if Interval( numberOfIntervalToSwap ) will stay unchanged
    boolean equalsIfRemainInterval =
        testIfEqualsToSwappedInterval(etalonMelodyMovement, melodyMovementToSwap, numberOfIntervalToSwap + 1,
            numberOfSwappedIntervals);

    // check case if Interval( numberOfIntervalToSwap ) should be swapped to make movements equal
    boolean equalsIfChangeInterval = false;
    numberOfSwappedIntervals++; // we will swap notes in the interval
    if (numberOfSwappedIntervals <= maxNumberOfIntervalsHavingSwappedNotes) {
      // Swapping intervals to see if that action will make movement equals to first movement
      MelodyMovement melodyMovementWithSwappedIntervals =
          melodyMovementToSwap.createMelodyMovementWithSwappedNotes(numberOfIntervalToSwap);
      if (etalonMelodyMovement.equals(melodyMovementWithSwappedIntervals)) {
        return true;
      } else {
        equalsIfChangeInterval = testIfEqualsToSwappedInterval(etalonMelodyMovement, melodyMovementWithSwappedIntervals,
            numberOfIntervalToSwap + 1, numberOfSwappedIntervals);
      }
    } else {
      return false;
    }
    return equalsIfChangeInterval || equalsIfRemainInterval;
  }

  public int getMaxNumberOfIntervalsHavingSwappedNotes() {
    return maxNumberOfIntervalsHavingSwappedNotes;
  }

  public void setMaxNumberOfIntervalsHavingSwappedNotes(int maxNumberOfIntervalsHavingSwappedNotes) {
    this.maxNumberOfIntervalsHavingSwappedNotes = maxNumberOfIntervalsHavingSwappedNotes;
  }

  @Override
  public int getMaxNumberOfDiversedNotes() {
    return maxNumberOfIntervalsHavingSwappedNotes;
  }
}
