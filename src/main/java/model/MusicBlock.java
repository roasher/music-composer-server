package model;

import static utils.ModelUtils.retrieveFirstIntervalPattern;
import static utils.ModelUtils.retrieveLastIntervalPattern;
import static utils.ModelUtils.retrieveRhythmValue;
import static utils.ModelUtils.retrieveStartTime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import model.composition.CompositionInfo;
import model.melody.Melody;
import utils.Utils;

/**
 * Class represents music MusicBlock
 * Music Block is the cut from the one's partition with some characteristics - the blocks with which new generated composition will be made from.
 * Created by Pavel Yurkin on 18.07.14.
 */
public class MusicBlock implements Serializable {
	// Origin Self Information
	private List<Melody> melodyList;
	private CompositionInfo compositionInfo;
	// Derivative Self Information
	private List<Integer> startIntervalPattern;
	private List<Integer> endIntervalPattern;
	private double rhythmValue;
	private double startTime;

	// Origin surrounding information
	private MusicBlock previous;
	private MusicBlock next;
	//	private Form form;

	// Derivative information
	private BlockMovement blockMovementFromPreviousToThis;
	private BlockMovement blockMovementFromThisToNext;

	public MusicBlock( List<Melody> inputMelodyList, CompositionInfo inputCompositionInfo ) {
		this.melodyList = inputMelodyList;
		this.compositionInfo = inputCompositionInfo;
		// Computing derivative information
		{
			// Computing interval patterns
			this.startIntervalPattern = retrieveFirstIntervalPattern( inputMelodyList );
			this.endIntervalPattern = retrieveLastIntervalPattern( inputMelodyList );

			// rhytmValue && start time
			this.rhythmValue = retrieveRhythmValue( inputMelodyList );
			this.startTime = retrieveStartTime( inputMelodyList );

			// Tension and form stuff
			// TODO implementation
		}
	}

//	public MusicBlock( CompositionInfo inputCompositionInfo, List<MusicBlock> musicBlockList ) {
//		if ( musicBlockList != null && musicBlockList.size() > 0 ) {
//			List<Melody> melodies = new ArrayList<>(  );
//			for ( int melodyNubmer = 0; melodyNubmer < musicBlockList.get( 0 ).getMelodyList().size(); melodyNubmer ++ ) {
//				melodies.add( new Melody(  ) );
//			}
//			double rhythmValue = 0;
//			for ( MusicBlock currentMusicBlock : musicBlockList ) {
//				for ( int melodyNumber = 0; melodyNumber < currentMusicBlock.getMelodyList().size(); melodyNumber ++ ) {
//					melodies.get( melodyNumber ).addNoteList( currentMusicBlock.getMelodyList().get( melodyNumber ).getNoteArray() );
//				}
//				rhythmValue += currentMusicBlock.getRhythmValue();
//			}
//
//			this.melodies = melodies;
//			this.compositionInfo = inputCompositionInfo;
//			this.startIntervalPattern = musicBlockList.get( 0 ).getStartIntervalPattern();
//			this.endIntervalPattern = musicBlockList.get( musicBlockList.size() -1 ).getEndIntervalPattern();
//			this.rhythmValue = rhythmValue;
//			this.startTime = musicBlockList.get( 0 ).getStartTime();
//			setNext( musicBlockList.get( musicBlockList.size() - 1 ).getNext() );
//			setPrevious( musicBlockList.get( 0 ).getPrevious() );
//
//		} else {
//			throw new IllegalArgumentException( "Input music block in malformed ( null or zero-length )" );
//		}
//	}

	public MusicBlock( CompositionInfo compositionInfo, Melody... melodies ) {
		this( Arrays.asList( melodies ), compositionInfo );
	}

	public String getForm() {
		StringBuilder stringBuilder = new StringBuilder();
		for ( Melody melody : this.getMelodyList() ) {
			stringBuilder.append( melody.getForm().getValue() );
		}
		return stringBuilder.toString();
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

		if ( this.melodyList.size() != that.melodyList.size() ) {
			return false;
		}

		if ( !Utils.listOfMelodiesAreEqual( this.melodyList, that.melodyList ) ) {
			return false;
		}

		if ( ( this.previous == null && that.previous != null ) || ( this.previous != null && that.previous == null ) || ( this.next == null && that.next != null ) || ( this.next != null && that.next == null ) ) {
			return false;
		} else {
			if ( ( this.previous != null && that.previous != null && !that.previous.melodyList.equals( that.previous.melodyList ) ) ||
					( this.next != null && that.next != null && !that.next.melodyList.equals( that.next.melodyList ) ) ) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = this.melodyList.hashCode();
		result = 31 * result + ( compositionInfo != null ? compositionInfo.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( Melody melody : this.getMelodyList() ) {
			stringBuilder.append('|').append( melody.toString() );
		}
		return stringBuilder.toString();
	}

	// Getters & Setters
	public List<Melody> getMelodyList() {
		return melodyList;
	}

	public double getStartTime() {
		return startTime;
	}

	public CompositionInfo getCompositionInfo() {
		return compositionInfo;
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

	public MusicBlock getPrevious() {
		return previous;
	}

	public void setPrevious( MusicBlock previous ) {
		this.previous = previous;
		if ( previous != null ) {
			// Setting movement from previous MusicBlock to this MusicBlock
			this.blockMovementFromPreviousToThis = new BlockMovement( previous.getMelodyList(), this.getMelodyList() );
		}
	}

	public MusicBlock getNext() {
		return next;
	}

	public void setNext( MusicBlock next ) {
		this.next = next;
		if ( next != null ) {
		// Setting movement from this MusicBlock to the next MusicBlock
		this.blockMovementFromThisToNext = new BlockMovement( this.getMelodyList(), next.getMelodyList() );
		}
	}

	public BlockMovement getBlockMovementFromPreviousToThis() {
		return blockMovementFromPreviousToThis;
	}

	public BlockMovement getBlockMovementFromThisToNext() {
		return blockMovementFromThisToNext;
	}
}
