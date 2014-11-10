package decomposer.analyzer.melody.equality;

import model.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Equality test wrapper adding conditions that both comparing signatures must have different number of notes
 * Pattern: chain of responsibility
 * Created by Pavel Yurkin on 18.08.14.
 */
public class DifferentNumberOfNotesRequired implements EqualityTest {

    Logger logger = LoggerFactory.getLogger( getClass() );

    private EqualityTest equalityTest;

    public DifferentNumberOfNotesRequired() {}
    public DifferentNumberOfNotesRequired( EqualityTest equalityTest ) {
        this.equalityTest = equalityTest;
    }

    @Override
    public boolean test( Signature firstSignature, Signature secondSignature ) {
        if ( firstSignature.getNoteArray().length == secondSignature.getNoteArray().length ) {
            return false;
        } else {
            if ( this.equalityTest != null ) {
                return this.equalityTest.test( firstSignature, secondSignature );
            } else {
                logger.warn( "DifferentNumberOfNotesRequired instance has null EqualityTest member. Test will be considered UNsuccessful" );
                return false;
            }
        }
    }

    @Override
    public int getMaxNumberOfDiversedNotes() {
        return equalityTest != null ? equalityTest.getMaxNumberOfDiversedNotes() : 0;
    }

    public EqualityTest getEqualityTest() {
        return equalityTest;
    }

    public void setEqualityTest( EqualityTest equalityTest ) {
        this.equalityTest = equalityTest;
    }
}
