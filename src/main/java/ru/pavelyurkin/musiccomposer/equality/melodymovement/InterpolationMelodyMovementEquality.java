package ru.pavelyurkin.musiccomposer.equality.melodymovement;

import ru.pavelyurkin.musiccomposer.model.melody.MelodyMovement;

import static java.lang.Math.abs;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
public class InterpolationMelodyMovementEquality extends AbstractMelodyMovementEquality {

    // Maximum allowable number of intervals that can be different
    private int maxNumberOfAddedIntervals;

    @Override
    /**
     * @returns true if number of interpolated notes <= maxNumberOfAddedIntervals false otherwise
     * Interpolation may occurs only if global intervals of the melodies are equal
     */
    public boolean testEqualityByLogic( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement ) {
        // 1-st melody
        int firstMelodyGlobalMovement = 0;
        for ( int currentInterval : firstMelodyMovement.getPitchIntervals() ) {
            firstMelodyGlobalMovement += currentInterval;
        }

        // 2-st melody
        int secondMelodyGlobalMovement = 0;
        for ( int currentInterval : secondMelodyMovement.getPitchIntervals() ) {
            secondMelodyGlobalMovement += currentInterval;
        }

        if ( firstMelodyGlobalMovement != secondMelodyGlobalMovement ) {
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
