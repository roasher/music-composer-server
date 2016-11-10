package decomposer.melody.equality.melodymovement;

import decomposer.melody.equality.Equality;
import model.melody.Melody;
import model.melody.MelodyMovement;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
public abstract class AbstractMelodyMovementEquality implements Equality {

    @Override
    public boolean test( Melody firstMelody, Melody secondMelody ) {
        MelodyMovement firstMelodyMovement = new MelodyMovement( firstMelody.getNoteList() );
        MelodyMovement secondMelodyMovement = new MelodyMovement( secondMelody.getNoteList() );
        // Test on equality to save some time
        if ( firstMelodyMovement.equals( secondMelodyMovement ) ) {
            return true;
        } else {
            return testEqualityByLogic( firstMelodyMovement, secondMelodyMovement );
        }
    }

    @Override
    public double getEqualityMetric( Melody firstMelody, Melody secondMelody ) {
        MelodyMovement firstMelodyMovement = new MelodyMovement( firstMelody.getNoteList() );
        MelodyMovement secondMelodyMovement = new MelodyMovement( secondMelody.getNoteList() );
        // Test on equality to save some time
        if ( firstMelodyMovement.equals( secondMelodyMovement ) ) {
            return 1;
        } else {
            return getEqualityMetric( firstMelodyMovement, secondMelodyMovement );
        }
    }

    public double getEqualityMetric( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement ) {
        throw new RuntimeException( "we don't support that ;)" );
    }

    /**
     * Actual test, unique for each of children
     * @param firstMelodyMovement
     * @param secondMelodyMovement
     */
    abstract boolean testEqualityByLogic( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement );
}
