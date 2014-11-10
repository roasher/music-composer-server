package decomposer.analyzer.melody.equality;

import model.MelodyMovement;

import static java.lang.Math.abs;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
public class InterpolationMelodyMovementEqualityTest extends AbstractMelodyMovementEqualityTest {

    // Maximum allowable number of intervals that can be different
    private int maxNumberOfAddedIntervals;

    @Override
    /**
     * @returns true if number of interpolated notes <= maxNumberOfAddedIntervals false otherwise
     * Interpolation may occurs only if global intervals of the signatures are equal
     */
    public boolean testEqualityByLogic( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement ) {
        // 1-st signature
        int firstSignatureGlobalMovement = 0;
        for ( int currentInterval : firstMelodyMovement.getPitchIntervals() ) {
            firstSignatureGlobalMovement += currentInterval;
        }

        // 2-st signature
        int secondSignatureGlobalMovement = 0;
        for ( int currentInterval : secondMelodyMovement.getPitchIntervals() ) {
            secondSignatureGlobalMovement += currentInterval;
        }

        if ( firstSignatureGlobalMovement != secondSignatureGlobalMovement ) {
            return false;
        } else {
            if ( abs( firstMelodyMovement.getPitchIntervals().size() - secondMelodyMovement.getPitchIntervals().size() ) <= maxNumberOfAddedIntervals ) {
                return true;
            } else {
                return false;
            }
        }
    }

    public int getMaxNumberOfAddedIntervals() {
        return maxNumberOfAddedIntervals;
    }

    public void setMaxNumberOfAddedIntervals(int maxNumberOfAddedIntervals) {
        this.maxNumberOfAddedIntervals = maxNumberOfAddedIntervals;
    }

    @Override
    public int getMaxNumberOfDiversedNotes() {
        return maxNumberOfAddedIntervals;
    }
}
