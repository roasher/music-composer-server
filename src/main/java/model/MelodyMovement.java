package model;

import jm.music.data.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents note movements regardless off the pitch
 * @author Pavel Yurkin
 */
public class MelodyMovement {
    // list of the pitch intervals
    private List< Integer > pitchIntervals = new ArrayList< Integer >();

    public MelodyMovement( Note[] noteArray ) {
        for ( int noteNumber = 0; noteNumber < noteArray.length - 1; noteNumber ++ ) {
            Note currentNote = noteArray[noteNumber];
            Note nextNote = noteArray[noteNumber + 1];
			int interval = nextNote.getPitch() - currentNote.getPitch();
			// if one of the notes is rest
			if ( interval > 1000 ) {
				interval = Integer.MAX_VALUE;
			} else if ( interval < -1000 ) {
				interval = Integer.MIN_VALUE;
			}
            pitchIntervals.add( interval );
        }
    }

    public MelodyMovement( int[] noteArray ) {
        for ( int noteNumber = 0; noteNumber < noteArray.length - 1; noteNumber ++ ) {
            int currentNote = noteArray[noteNumber];
            int nextNote = noteArray[noteNumber + 1];
            // We are not counting rests
//            if ( currentNote.getPitch() != Note.REST && nextNote.getPitch() != Note.REST ) {
            pitchIntervals.add( nextNote - currentNote );
//            }
        }
    }

    private MelodyMovement() {}

    /**
     * Returns new MelodyMovement represents MelodyMovement with swapped notes that belongs to the numberOfInterval-nth interval
     * For example if intervals were:
     * I( numberOfInterval - 1 )
     * I( numberOfInterval )
     * I( numberOfInterval + 1 ), then in new object they will be:
     * I'( numberOfInterval - 1 ) = I( numberOfInterval - 1 ) + I( numberOfInterval )
     * I'( numberOfInterval ) = -1 * I( numberOfInterval )
     * I'( numberOfInterval + 1 ) = I( numberOfInterval ) + I( numberOfInterval + 1)
     * @param numberOfInterval - number of interval which notes should be swapped
     * @return new MelodyMovement object that is copy of the creator one, but 3 intervals ( numberOfInterval -1; numberOfInterval; numberOfInterval + 1 )
     */
    public MelodyMovement createMelodyMovementWithSwappedNotes( int numberOfInterval ) {
        MelodyMovement melodyMovementWithSwappedNotes = new MelodyMovement();
        for ( int currentInterval = 0; currentInterval < this.getPitchIntervals().size(); currentInterval ++ ) {
            if ( currentInterval == numberOfInterval - 1) {
                melodyMovementWithSwappedNotes.getPitchIntervals().add(
                        this.getPitchIntervals().get( currentInterval ) + this.getPitchIntervals().get( currentInterval + 1 ) );
                continue;
            }
            if ( currentInterval == numberOfInterval ) {
                melodyMovementWithSwappedNotes.getPitchIntervals().add( -this.getPitchIntervals().get( currentInterval ) );
                continue;
            }
            if ( currentInterval == numberOfInterval + 1 ) {
                melodyMovementWithSwappedNotes.getPitchIntervals().add(
                        this.getPitchIntervals().get( currentInterval -1 ) + this.getPitchIntervals().get( currentInterval ) );
                continue;
            }
            melodyMovementWithSwappedNotes.getPitchIntervals().add( this.getPitchIntervals().get( currentInterval ) );
        }
        return melodyMovementWithSwappedNotes;
    }

    /**
     * Two movements considered equals if oll of their intervals equals one by one
     * @param inputObject
     * @return
     */
    @Override
    public boolean equals( Object inputObject ) {
        if ( inputObject instanceof MelodyMovement ) {
            MelodyMovement inputMelodyMovement = ( MelodyMovement ) inputObject;
            if ( inputMelodyMovement.getPitchIntervals().size() == this.getPitchIntervals().size() ) {
                for ( int intervalNumber = 0; intervalNumber < inputMelodyMovement.getPitchIntervals().size(); intervalNumber ++ ) {
                    if ( !inputMelodyMovement.getPitchIntervals().get( intervalNumber ).equals( this.getPitchIntervals().get( intervalNumber ) ) ) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString () {
        StringBuilder builder = new StringBuilder("{");
        for ( int currentIntervel = 0; currentIntervel < this.getPitchIntervals().size(); currentIntervel ++ ) {
            builder.append( this.getPitchIntervals().get( currentIntervel ) + " ");
        }
        builder.append("}");
        return  builder.toString();
    }

    // Getters and Setters
    public List<Integer> getPitchIntervals() {
        return pitchIntervals;
    }

    public void setPitchIntervals(List<Integer> pitchIntervals) {
        this.pitchIntervals = pitchIntervals;
    }
}
