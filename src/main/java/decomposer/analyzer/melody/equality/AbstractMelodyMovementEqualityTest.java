package decomposer.analyzer.melody.equality;

import model.MelodyMovement;
import model.Signature;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
public abstract class AbstractMelodyMovementEqualityTest implements EqualityTest {

    public boolean test( Signature firstSignature, Signature secondSignature ) {
        MelodyMovement firstMelodyMovement = new MelodyMovement( firstSignature.getNoteArray() );
        MelodyMovement secondMelodyMovement = new MelodyMovement( secondSignature.getNoteArray() );
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
