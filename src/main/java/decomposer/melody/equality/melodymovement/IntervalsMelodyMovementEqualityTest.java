package decomposer.melody.equality.melodymovement;

import model.MelodyMovement;

import static java.lang.Math.abs;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
public class IntervalsMelodyMovementEqualityTest extends AbstractMelodyMovementEqualityTest {

    // Maximum allowable number of intervals that can be different
    private int maxNumberOfShiftedIntervals;
    // Maximum allowable pitch difference
    private int maxShift;

    @Override
    /**
     * @returns true if number of shifted intervals ( with value difference fits into maxShift ) <= maxNumberOfShiftedIntervals
     * If there is one interval that has pitch difference > maxShift or there number greater than maxNumberOfShiftedIntervals
     * returns false
     */
    public boolean testEqualityByLogic( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement ) {
        int numberOfShiftedIntervals = 0;
        for ( int currentInterval = 0 ; currentInterval < firstMelodyMovement.getPitchIntervals().size() ; currentInterval ++ ) {
            int intervalDifferece = abs( firstMelodyMovement.getPitchIntervals().get( currentInterval ) - secondMelodyMovement.getPitchIntervals().get( currentInterval ) );
            if ( intervalDifferece != 0) {
                if ( intervalDifferece <= maxShift) {
                    numberOfShiftedIntervals ++;
                } else {
                    return false;
                }
            }
        }
        if ( numberOfShiftedIntervals <= maxNumberOfShiftedIntervals) {
            return true;
        } else {
            return false;
        }
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
