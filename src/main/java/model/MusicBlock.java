package model;

import jm.music.data.Note;
import model.composition.CompositionInfo;
import model.tension.Tension;
import utils.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static model.utils.ModelUtils.*;

/**
 * Class represents music MusicBlock
 * Music Block is the cut from the one's partition with some characteristics - the blocks with which new generated composition will be made from.
 * Created by Pavel Yurkin on 18.07.14.
 */
public class MusicBlock implements Serializable {
    // Origin Self Information
    private List< List < Note > > notes;
    private CompositionInfo compositionInfo;
    // Derivative Self Information
    private List< Integer > startIntervalPattern;
    private List< Integer > endIntervalPattern;
    private double rhythmValue;
    private Tension tension;

    // Origin surrounding information
    private MusicBlock previous;
    private MusicBlock next;
	private Form form;

    // Derivative information
    private BlockMovement blockMovementFromPreviousMusicBlockToThisMusicBlock;
    private BlockMovement blockMovementFromThisMusicBlockToNextMusicBlock;

    public MusicBlock( List< List< Note > > inputNotes, CompositionInfo inputCompositionInfo ) {
        this.notes = inputNotes;
        this.compositionInfo = inputCompositionInfo;
        // Computing derivative information
        {
            // Computing interval patterns
            // first
            {
                List< Integer > firstVertical = new ArrayList<Integer>();
                for ( int currentInstrument = 0; currentInstrument < inputNotes.size() ; currentInstrument ++ ) {
                    firstVertical.add( inputNotes.get( currentInstrument ).get( 0 ).getPitch() );
                }
                this.startIntervalPattern = getIntervalPattern( firstVertical );
            }
            // last
            {
                List< Integer > lastVertical = new ArrayList<Integer>();
                for ( int currentInstrument = 0; currentInstrument < inputNotes.size() ; currentInstrument ++ ) {
                    int lastNoteNumber = inputNotes.get( currentInstrument ).size() - 1;
                    lastVertical.add( inputNotes.get( currentInstrument ).get( lastNoteNumber ).getPitch() );
                }
                this.endIntervalPattern = getIntervalPattern( lastVertical );
            }
            // rhytmValue
            {
                int currentRhytmValue = sumAllRhytmValues( inputNotes.get( 0 ) );
                for ( int currentInstrument = 1; currentInstrument < inputNotes.size() ; currentInstrument ++ ) {
                    if ( currentRhytmValue != sumAllRhytmValues( inputNotes.get( currentInstrument ) ) ) {
                        throw new IllegalArgumentException( String.format( "Several instruments has different rhytmValues, for example: 0 and %s " , currentInstrument ) );
                    }
                }
                this.rhythmValue = currentRhytmValue;
            }
            // Tension and form stuff
            // TODO implementation
        }
    }

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof MusicBlock ) ) {
			return false;
		}

		MusicBlock that = ( MusicBlock ) o;

		if ( compositionInfo != null ? !compositionInfo.equals( that.compositionInfo ) : that.compositionInfo != null ) {
			return false;
		}

		if ( this.notes.size() != that.notes.size() ) {
			return false;
		}

		if ( !Utils.ListOfListsIsEquals( this.notes, that.notes ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = notes.hashCode();
		result = 31 * result + ( compositionInfo != null ? compositionInfo.hashCode() : 0 );
		return result;
	}

	// Getters & Setters
    public List<List<Note>> getNotes() {
        return notes;
    }

    public void setNotes(List<List<Note>> notes) {
        this.notes = notes;
    }

    public Tension getTension() {
        return tension;
    }

    public void setTension(Tension tension) {
        this.tension = tension;
    }

    public CompositionInfo getCompositionInfo() {
        return compositionInfo;
    }

    public void setCompositionInfo(CompositionInfo compositionInfo) {
        this.compositionInfo = compositionInfo;
    }

    public List<Integer> getStartIntervalPattern() {
        return startIntervalPattern;
    }

    public List<Integer> getEndIntervalPattern() {
        return endIntervalPattern;
    }

    public double getRhythmValue() {
        return rhythmValue;
    }

	public Form getForm() {
		return form;
	}

	public void setForm( Form form ) {
		this.form = form;
	}

	public MusicBlock getPrevious() {
        return previous;
    }

    public void setPrevious(MusicBlock previous) {
        this.previous = previous;
        // Setting movement from previous MusicBlock to this MusicBlock
        this.blockMovementFromPreviousMusicBlockToThisMusicBlock = new BlockMovement( previous, this );
    }

    public MusicBlock getNext() {
        return next;
    }

    public void setNext(MusicBlock next) {
        this.next = next;
        // Setting movement from this MusicBlock to the next MusicBlock
        this.blockMovementFromThisMusicBlockToNextMusicBlock = new BlockMovement( this, next );
    }

    public BlockMovement getBlockMovementFromPreviousMusicBlockToThisMusicBlock() {
        return blockMovementFromPreviousMusicBlockToThisMusicBlock;
    }

    public void setBlockMovementFromPreviousMusicBlockToThisMusicBlock( BlockMovement blockMovementFromPreviousMusicBlockToThisMusicBlock ) {
        this.blockMovementFromPreviousMusicBlockToThisMusicBlock = blockMovementFromPreviousMusicBlockToThisMusicBlock;
    }

    public BlockMovement getBlockMovementFromThisMusicBlockToNextMusicBlock() {
        return blockMovementFromThisMusicBlockToNextMusicBlock;
    }

    public void setBlockMovementFromThisMusicBlockToNextMusicBlock( BlockMovement blockMovementFromThisMusicBlockToNextMusicBlock ) {
        this.blockMovementFromThisMusicBlockToNextMusicBlock = blockMovementFromThisMusicBlockToNextMusicBlock;
    }
}
