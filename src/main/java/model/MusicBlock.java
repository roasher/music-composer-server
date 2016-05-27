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

/**
 * Class represents music MusicBlock
 * Music Block is the cut from the one's partition with some characteristics - the blocks with which new generated composition will be made from.
 * Created by Pavel Yurkin on 18.07.14.
 */
public class MusicBlock implements Serializable {
	// Origin Self Information
	private List<Melody> melodyList;
	private CompositionInfo compositionInfo;
	private BlockMovement blockMovementFromPreviousToThis;
	private BlockMovement blockMovementFromThisToNext;
	// Derivative Self Information
	private List<Integer> startIntervalPattern;
	private List<Integer> endIntervalPattern;
	private double rhythmValue;
	private double startTime;

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
		}
	}

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MusicBlock that = (MusicBlock) o;

		if (Double.compare(that.rhythmValue, rhythmValue) != 0) return false;
		if (Double.compare(that.startTime, startTime) != 0) return false;
		if (!melodyList.equals(that.melodyList)) return false;
		if (compositionInfo != null ? !compositionInfo.equals(that.compositionInfo) : that.compositionInfo != null) return false;
		if (
			blockMovementFromPreviousToThis != null ? !blockMovementFromPreviousToThis.equals(that.blockMovementFromPreviousToThis) :
				that.blockMovementFromPreviousToThis != null) return false;
		if (
			blockMovementFromThisToNext != null ? !blockMovementFromThisToNext.equals(that.blockMovementFromThisToNext) :
				that.blockMovementFromThisToNext != null) return false;
		if (!startIntervalPattern.equals(that.startIntervalPattern)) return false;
		return endIntervalPattern.equals(that.endIntervalPattern);

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = melodyList.hashCode();
		result = 31 * result + (compositionInfo != null ? compositionInfo.hashCode() : 0);
		result = 31 * result + (blockMovementFromPreviousToThis != null ? blockMovementFromPreviousToThis.hashCode() : 0);
		result = 31 * result + (blockMovementFromThisToNext != null ? blockMovementFromThisToNext.hashCode() : 0);
		result = 31 * result + startIntervalPattern.hashCode();
		result = 31 * result + endIntervalPattern.hashCode();
		temp = Double.doubleToLongBits(rhythmValue);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(startTime);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
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


  public void setBlockMovementFromPreviousToThis(BlockMovement blockMovementFromPreviousToThis) {
    this.blockMovementFromPreviousToThis = blockMovementFromPreviousToThis;
  }

  public void setBlockMovementFromThisToNext(BlockMovement blockMovementFromThisToNext) {
    this.blockMovementFromThisToNext = blockMovementFromThisToNext;
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

	public BlockMovement getBlockMovementFromThisToNext() {
		return blockMovementFromThisToNext;
	}
}
