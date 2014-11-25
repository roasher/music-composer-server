package decomposer.melody.equality.melodymovement;

import model.MelodyMovement;

import static java.lang.Math.abs;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
public class FragmentationMelodyMovementEqualityTest extends AbstractMelodyMovementEqualityTest {

    // Maximum allowable number of intervals that can be different
    private int maxNumberOfDeletedIntervals;

    @Override
    /**
     * @returns true if number of deleted notes <= maxNumberOfDeletedIntervals false otherwise
     * Fragmentation may occurs only if some of the intervals was removed from right of left side of the Melody
     */
    public boolean testEqualityByLogic( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement ) {
        // Fragmentation occurs only if one list is part of another
        if ( !firstMelodyMovement.getPitchIntervals().containsAll( secondMelodyMovement.getPitchIntervals() ) &&
                !secondMelodyMovement.getPitchIntervals().containsAll( firstMelodyMovement.getPitchIntervals() ) ) {
            return false;
        }

        if ( abs( firstMelodyMovement.getPitchIntervals().size() - secondMelodyMovement.getPitchIntervals().size() ) <= maxNumberOfDeletedIntervals) {
            return true;
        } else{
            return false;
        }
    }

    public int getMaxNumberOfDeletedIntervals() {
        return maxNumberOfDeletedIntervals;
    }

    public void setMaxNumberOfDeletedIntervals(int maxNumberOfDeletedIntervals) {
        this.maxNumberOfDeletedIntervals = maxNumberOfDeletedIntervals;
    }

    @Override
    public int getMaxNumberOfDiversedNotes() {
        return maxNumberOfDeletedIntervals;
    }
}
