package decomposer.melody.equality.melodymovement;

import static java.lang.Math.abs;

import model.melody.MelodyMovement;

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
    public double getEqualityMetric( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement ) {
        int numberOfShiftedIntervals = 0;
        for ( int currentInterval = 0 ; currentInterval < firstMelodyMovement.getPitchIntervals().size() ; currentInterval ++ ) {
            int intervalDifferece = abs( firstMelodyMovement.getPitchIntervals().get( currentInterval ) - secondMelodyMovement.getPitchIntervals().get( currentInterval ) );
            if ( intervalDifferece != 0) {
                if ( intervalDifferece >= maxShift) {
                    numberOfShiftedIntervals ++;
                }
            }
        }
        return ( firstMelodyMovement.getPitchIntervals().size() - numberOfShiftedIntervals )*1./firstMelodyMovement.getPitchIntervals().size();
    }

    @Override
    /**
     * @returns true if number of shifted intervals ( with value difference fits into maxShift ) <= maxNumberOfShiftedIntervals
     * If there is one interval that has pitch difference > maxShift or there number greater than maxNumberOfShiftedIntervals
     * returns false
     */
    public boolean testEqualityByLogic( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement ) {
        double equalityMetric = getEqualityMetric( firstMelodyMovement, secondMelodyMovement );
        double currentNumberOfShiftedIntervals = ( 1 - equalityMetric ) * firstMelodyMovement.getPitchIntervals().size();
        return currentNumberOfShiftedIntervals <= maxNumberOfShiftedIntervals;
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
