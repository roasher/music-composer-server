package decomposer.melody.equality.melodymovement;

import decomposer.melody.equality.EqualityTest;
import model.melody.Melody;
import model.melody.MelodyMovement;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
public abstract class AbstractMelodyMovementEqualityTest implements EqualityTest {

    public boolean test( Melody firstMelody, Melody secondMelody ) {
        MelodyMovement firstMelodyMovement = new MelodyMovement( firstMelody.getNoteArray() );
        MelodyMovement secondMelodyMovement = new MelodyMovement( secondMelody.getNoteArray() );
        // Test on equality to save some time
        if ( firstMelodyMovement.equals( secondMelodyMovement ) ) {
            return true;
        } else {
            return testEqualityByLogic( firstMelodyMovement, secondMelodyMovement );
        }
    }

    /**
     * Actual test, unique for each of children
     * @param firstMelodyMovement
     * @param secondMelodyMovement
     */
    abstract boolean testEqualityByLogic( MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement );
}
