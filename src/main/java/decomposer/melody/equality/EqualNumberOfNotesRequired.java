package decomposer.melody.equality;

import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Equality test wrapper adding conditions that both comparing melodies must have equal number of notes
 * Pattern: chain of responsibility
 * Created by Pavel Yurkin on 18.08.14.
 */
public class EqualNumberOfNotesRequired implements Equality {

    Logger logger = LoggerFactory.getLogger( getClass() );

    private Equality equality;

    public EqualNumberOfNotesRequired() {}
    public EqualNumberOfNotesRequired( Equality equality ) {
        this.equality = equality;
    }

    @Override
    public double getEqualityMetric( Melody firstMelody, Melody secondMelody ) {
        if ( firstMelody.size() != secondMelody.size() ) {
            return 0;
        } else {
            if ( this.equality != null ) {
                return equality.getEqualityMetric( firstMelody, secondMelody );
            } else {
                return 0;
            }
        }
    }

    @Override
    public boolean test( Melody firstMelody, Melody secondMelody ) {
        if ( firstMelody.size() != secondMelody.size() ) {
            return false;
        } else {
            if ( this.equality != null ) {
                return this.equality.test( firstMelody, secondMelody );
            } else {
                logger.warn( "EqualNumberOfNotesRequired instance has null Equality member. Test will be considered UNsuccessful" );
                return false;
            }
        }
    }

    @Override
    public int getMaxNumberOfDiversedNotes() {
        return equality != null ? equality.getMaxNumberOfDiversedNotes() : 0;
    }

    public Equality getEquality() {
        return equality;
    }

    public void setEquality( Equality equality ) {
        this.equality = equality;
    }
}
