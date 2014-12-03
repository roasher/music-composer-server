package decomposer.melody.equality;

import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Equality test wrapper adding conditions that both comparing melodies must have equal number of notes
 * Pattern: chain of responsibility
 * Created by Pavel Yurkin on 18.08.14.
 */
public class EqualNumberOfNotesRequired implements EqualityTest {

    Logger logger = LoggerFactory.getLogger( getClass() );

    private EqualityTest equalityTest;

    public EqualNumberOfNotesRequired() {}
    public EqualNumberOfNotesRequired( EqualityTest equalityTest ) {
        this.equalityTest = equalityTest;
    }

    @Override
    public boolean test( Melody firstMelody, Melody secondMelody ) {
        if ( firstMelody.getNoteArray().length != secondMelody.getNoteArray().length ) {
            return false;
        } else {
            if ( this.equalityTest != null ) {
                return this.equalityTest.test( firstMelody, secondMelody );
            } else {
                logger.warn( "EqualNumberOfNotesRequired instance has null EqualityTest member. Test will be considered UNsuccessful" );
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
