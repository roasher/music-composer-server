package ru.pavelyurkin.musiccomposer.core.model;

import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.pavelyurkin.musiccomposer.core.utils.Utils.isEquals;

/**
 * Class represents music MusicBlock Music Block is the cut from the one's partition with some characteristics - the blocks with which new generated composition
 * will be made from. Created by Pavel Yurkin on 18.07.14.
 */
public class MusicBlock implements Serializable {
	// Origin Self Information
	private List<Melody> melodyList;
	private CompositionInfo compositionInfo;
	private BlockMovement blockMovementFromPreviousToThis;
	// Derivative Self Information
	private List<Integer> startIntervalPattern;
	private List<Integer> endIntervalPattern;
	private double rhythmValue;
	private double startTime;

	public MusicBlock( List<Melody> melodyList, CompositionInfo compositionInfo, BlockMovement blockMovementFromPreviousToThis ) {
		this( melodyList, compositionInfo );
		this.blockMovementFromPreviousToThis = blockMovementFromPreviousToThis;
	}

	public MusicBlock( List<Melody> inputMelodyList, CompositionInfo inputCompositionInfo ) {
		this.melodyList = inputMelodyList;
		this.compositionInfo = inputCompositionInfo;
		// Computing derivative information
		{
			// Computing interval patterns
			this.startIntervalPattern = ModelUtils.retrieveFirstIntervalPattern( inputMelodyList );
			this.endIntervalPattern = ModelUtils.retrieveLastIntervalPattern( inputMelodyList );

			// rhytmValue && start time
			this.rhythmValue = ModelUtils.retrieveRhythmValue( inputMelodyList );
			this.startTime = ModelUtils.retrieveStartTime( inputMelodyList );
		}
	}

	public MusicBlock( CompositionInfo compositionInfo, Melody... melodies ) {
		this( Arrays.asList( melodies ), compositionInfo );
	}

	public MusicBlock( List<MusicBlock> musicBlocks ) {
		List<Melody> melodyList = new ArrayList<>();
		for ( int melodyNubmer = 0; melodyNubmer < musicBlocks.get( 0 ).getMelodyList().size(); melodyNubmer++ ) {
			melodyList.add( new Melody() );
		}
		double rhythmValue = 0;
		for ( MusicBlock currentComposeBlock : musicBlocks ) {
			for ( int melodyNumber = 0; melodyNumber < currentComposeBlock.getMelodyList().size(); melodyNumber++ ) {
				melodyList.get( melodyNumber ).addNoteList( currentComposeBlock.getMelodyList().get( melodyNumber ).getNoteList(), true );
			}
			rhythmValue += currentComposeBlock.getRhythmValue();
		}

		this.melodyList = melodyList;
		this.compositionInfo = null;

		this.blockMovementFromPreviousToThis = musicBlocks.get( 0 ).getBlockMovementFromPreviousToThis();

		this.startIntervalPattern = musicBlocks.get( 0 ).getStartIntervalPattern();
		this.endIntervalPattern = musicBlocks.get( musicBlocks.size() - 1 ).getEndIntervalPattern();

		this.rhythmValue = rhythmValue;
		this.startTime = musicBlocks.get( 0 ).getStartTime();

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
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;

		MusicBlock that = ( MusicBlock ) o;

		if ( !isEquals( that.rhythmValue, rhythmValue ) )
			return false;
		if ( !ModelUtils.isTimeCorrelated( that.startTime, startTime ) )
			return false;
		if ( compositionInfo != null ? !compositionInfo.equals( that.compositionInfo ) : that.compositionInfo != null )
			return false;
		if ( blockMovementFromPreviousToThis != null ?
				!blockMovementFromPreviousToThis.equals( that.blockMovementFromPreviousToThis ) :
				that.blockMovementFromPreviousToThis != null )
			return false;
		if ( !startIntervalPattern.equals( that.startIntervalPattern ) )
			return false;
		if ( this.getMelodyList().size() != that.getMelodyList().size() )
			throw new IllegalArgumentException( "different melody numbers: " + this.getMelodyList().size() + " and " + that.getMelodyList().size() );
		for ( int melodyNumber = 0; melodyNumber < this.getMelodyList().size(); melodyNumber++ ) {
			if ( !this.getMelodyList().get( melodyNumber ).equals( that.getMelodyList().get( melodyNumber ) ) ) return false;
		}
		return endIntervalPattern.equals( that.endIntervalPattern );
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
//		result = melodyList.hashCode();
		result = 1;
		result = 31 * result + ( compositionInfo != null ? compositionInfo.hashCode() : 0 );
		result = 31 * result + ( blockMovementFromPreviousToThis != null ? blockMovementFromPreviousToThis.hashCode() : 0 );
		result = 31 * result + startIntervalPattern.hashCode();
		result = 31 * result + endIntervalPattern.hashCode();
		temp = Double.doubleToLongBits( rhythmValue );
		result = 31 * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		temp = Double.doubleToLongBits( startTime );
		result = 31 * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		return result;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( int melodyNumber = 0; melodyNumber < this.getMelodyList().size(); melodyNumber++ ) {
			Melody melody = this.getMelodyList().get( melodyNumber );
			if ( this.getBlockMovementFromPreviousToThis() != null ) {
				stringBuilder.append( "[" ).append( this.getBlockMovementFromPreviousToThis().getVoiceMovements().get( melodyNumber ).toString() ).append( "]" );
			}
			stringBuilder.append( melody.toString() );
			stringBuilder.append(" & ");
		}
		return stringBuilder.toString();
	}

	public void setBlockMovementFromPreviousToThis( BlockMovement blockMovementFromPreviousToThis ) {
		this.blockMovementFromPreviousToThis = blockMovementFromPreviousToThis;
	}

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

	public BlockMovement getBlockMovementFromPreviousToThis() {
		return blockMovementFromPreviousToThis;
	}

	public void setStartTime( double startTime ) {
		this.startTime = startTime;
	}
}
