package model;

import model.composition.CompositionInfo;
import model.melody.Melody;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents blocks which can be used in composing process
 * Created by pyurkin on 05.03.2015.
 */
public class ComposeBlock {

	// The only information we need to compose
	private double rhythmValue;
	private double startTime;
	private CompositionInfo compositionInfo;
	private List<Melody> melodyList;
	private List<Integer> startIntervalPattern;
	private BlockMovement blockMovementFromPreviousToThis;

    // Lists of possible next and previous Compose Blocks that has convenient voice leading in the original composition
	// We are assuming that first member of list is the original composition member - so all blocks except firsts and lasts will have at least one member in the list
    private List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );
    private List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	public ComposeBlock( MusicBlock musicBlock ) {
		this( musicBlock.getRhythmValue(), musicBlock.getStartTime(), musicBlock.getCompositionInfo(), musicBlock.getMelodyList(), musicBlock.getStartIntervalPattern(), musicBlock.getBlockMovementFromPreviousToThis() );
	}

	public ComposeBlock( double rhythmValue, double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, List<Integer> startIntervalPattern, BlockMovement blockMovementFromPreviousToThis ) {
		this.rhythmValue = rhythmValue;
		this.startTime = startTime;
		this.compositionInfo = compositionInfo;
		this.melodyList = melodyList;
		this.startIntervalPattern = startIntervalPattern;
		this.blockMovementFromPreviousToThis = blockMovementFromPreviousToThis;
	}

	/**
	 * Building compose block using list of blocks. Loosing compositionInfo information.
	 * @param composeBlockList
	 */
	public ComposeBlock( List<ComposeBlock> composeBlockList ) {
		if ( composeBlockList != null && composeBlockList.size() != 0 ) {

			List<Melody> melodyList = new ArrayList<>(  );
			for ( int melodyNubmer = 0; melodyNubmer < composeBlockList.get( 0 ).getMelodyList().size(); melodyNubmer ++ ) {
				melodyList.add( new Melody(  ) );
			}
			double rhythmValue = 0;
			for ( ComposeBlock currentComposeBlock : composeBlockList ) {
				for ( int melodyNumber = 0; melodyNumber < currentComposeBlock.getMelodyList().size(); melodyNumber ++ ) {
					melodyList.get( melodyNumber ).addNoteList( currentComposeBlock.getMelodyList().get( melodyNumber ).getNoteArray() );
				}
				rhythmValue += currentComposeBlock.getRhythmValue();
			}

			this.melodyList = melodyList;
			this.compositionInfo = null;
			this.startIntervalPattern = composeBlockList.get( 0 ).getStartIntervalPattern();
//			this.endIntervalPattern = composeBlockList.get( composeBlockList.size() -1 ).getEndIntervalPattern();
			this.rhythmValue = rhythmValue;
			this.startTime = composeBlockList.get( 0 ).getStartTime();

			this.possiblePreviousComposeBlocks = composeBlockList.get( 0 ).getPossiblePreviousComposeBlocks();
			this.possibleNextComposeBlocks = composeBlockList.get( composeBlockList.size() - 1 ).getPossibleNextComposeBlocks();

		} else {
			throw new RuntimeException( "Input compose block is malformed" );
		}
	}

	/**
	 * Checks if input music block has the same information that compose block has.
	 * @param musicBlock
	 * @return
	 */
	public boolean isSimilar( MusicBlock musicBlock ) {
		return isSimilar( musicBlock.getRhythmValue(), musicBlock.getStartTime(), musicBlock.getCompositionInfo(), musicBlock.getMelodyList(), musicBlock.getStartIntervalPattern(), musicBlock.getBlockMovementFromPreviousToThis() );
	}

	public boolean isSimilar( ComposeBlock composeBlock ) {
		return isSimilar( composeBlock.getRhythmValue(), composeBlock.getStartTime(), composeBlock.getCompositionInfo(), composeBlock.getMelodyList(), composeBlock.getStartIntervalPattern(), composeBlock.getBlockMovementFromPreviousToThis() );
	}

	public boolean isSimilar( double rhythmValue, double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, List<Integer> startIntervalPattern, BlockMovement blockMovementFromPreviousToThis ) {
		return ( Double.compare( this.rhythmValue, rhythmValue ) == 0 ) && ( Double.compare( this.startTime, startTime ) == 0 ) &&
				this.compositionInfo.equals( compositionInfo ) && this.melodyList.equals( melodyList ) &&
				this.startIntervalPattern.equals( startIntervalPattern ) &&	this.blockMovementFromPreviousToThis.equals( blockMovementFromPreviousToThis );
	}

	public double getRhythmValue() {
		return rhythmValue;
	}

	public void setRhythmValue( double rhythmValue ) {
		this.rhythmValue = rhythmValue;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime( double startTime ) {
		this.startTime = startTime;
	}

	public CompositionInfo getCompositionInfo() {
		return compositionInfo;
	}

	public void setCompositionInfo( CompositionInfo compositionInfo ) {
		this.compositionInfo = compositionInfo;
	}

	public List<Melody> getMelodyList() {
		return melodyList;
	}

	public void setMelodyList( List<Melody> melodyList ) {
		this.melodyList = melodyList;
	}

	public List<Integer> getStartIntervalPattern() {
		return startIntervalPattern;
	}

	public void setStartIntervalPattern( List<Integer> startIntervalPattern ) {
		this.startIntervalPattern = startIntervalPattern;
	}

	public BlockMovement getBlockMovementFromPreviousToThis() {
		return blockMovementFromPreviousToThis;
	}

	public void setBlockMovementFromPreviousToThis( BlockMovement blockMovementFromPreviousToThis ) {
		this.blockMovementFromPreviousToThis = blockMovementFromPreviousToThis;
	}

	public List<ComposeBlock> getPossibleNextComposeBlocks() {
        return possibleNextComposeBlocks;
    }

    public List<ComposeBlock> getPossiblePreviousComposeBlocks() {
        return possiblePreviousComposeBlocks;
    }

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( Melody melody : this.getMelodyList() ) {
			stringBuilder.append('|').append( melody.toString() );
		}
		return stringBuilder.toString();
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof ComposeBlock ) )
			return false;

		ComposeBlock that = ( ComposeBlock ) o;

		if ( !this.isSimilar( that ) )
			return false;

		if ( possiblePreviousComposeBlocks.size() != that.possiblePreviousComposeBlocks.size() )
			return false;

		for ( ComposeBlock thisPossiblePreviousComposeBlock : this.possiblePreviousComposeBlocks ) {
			boolean isInList = false;
			for ( ComposeBlock thatPossiblePreviousComposeBlock : that.possiblePreviousComposeBlocks ) {
				if ( thisPossiblePreviousComposeBlock.isSimilar( thatPossiblePreviousComposeBlock ) ) {
					isInList = true;
				}
			}
			if ( !isInList )
				return false;
		}

		if ( possibleNextComposeBlocks.size() != that.possibleNextComposeBlocks.size() )
			return false;

		for ( ComposeBlock thisPossibleNextComposeBlock : this.possibleNextComposeBlocks ) {
			boolean isInList = false;
			for ( ComposeBlock thatPossibleNextComposeBlock : that.possibleNextComposeBlocks ) {
				if ( thisPossibleNextComposeBlock.isSimilar( thatPossibleNextComposeBlock )  ) {
					isInList = true;
				}
			}
			if ( !isInList )
				return false;
		}

		return true;
	}

}
