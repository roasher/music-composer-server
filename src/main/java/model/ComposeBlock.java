package model;

import model.composition.CompositionInfo;
import model.melody.Melody;
import sun.plugin.com.event.COMEventHandler;

import java.util.ArrayList;
import java.util.List;

import static utils.ModelUtils.*;

/**
 * Class represents blocks which can be used in composing process
 * Created by pyurkin on 05.03.2015.
 */
public class ComposeBlock {

	// The only information we need to compose
	private double startTime;
	private CompositionInfo compositionInfo;
	private List<Melody> melodyList;
	private BlockMovement blockMovementFromPreviousToThis;
	// Derivative information
	private double rhythmValue;
	private List<Integer> startIntervalPattern;

    // Lists of possible next and previous Compose Blocks that has convenient voice leading in the original composition
	// We are assuming that first member of list is the original composition member - so all blocks except firsts and lasts will have at least one member in the list
    private List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );
    private List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	public ComposeBlock( MusicBlock musicBlock ) {
		init( musicBlock.getStartTime(), musicBlock.getCompositionInfo(), musicBlock.getMelodyList(), musicBlock.getBlockMovementFromPreviousToThis() );
		this.rhythmValue = musicBlock.getRhythmValue();
		this.startIntervalPattern = musicBlock.getStartIntervalPattern();
	}

	public ComposeBlock( double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, BlockMovement blockMovementFromPreviousToThis ) {
		init( startTime, compositionInfo, melodyList, blockMovementFromPreviousToThis );
		// Setting up derivative information
		this.rhythmValue = retrieveRhythmValue( melodyList );
		this.startIntervalPattern = retrieveFirstIntervalPattern( melodyList );
	}

	// Constructor helper
	private void init( double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, BlockMovement blockMovementFromPreviousToThis ) {
		this.startTime = startTime;
		this.compositionInfo = compositionInfo;
		this.melodyList = melodyList;
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
		boolean isEqualRhythmValues = Double.compare( this.rhythmValue, rhythmValue ) == 0;
		boolean isEqualStartTimes = Double.compare( this.startTime, startTime ) == 0;

		boolean isEqualCompositionInfos;
		if ( this.compositionInfo != null ) {
			isEqualCompositionInfos = this.compositionInfo.equals( compositionInfo );
		} else {
			isEqualCompositionInfos = compositionInfo == null;
		}

		boolean isEqualMelodyList = this.melodyList.equals( melodyList );
		boolean isEqualStartIntervalPattern = this.startIntervalPattern.equals( startIntervalPattern );

		boolean isEqualsBlockMovements;
		if ( this.blockMovementFromPreviousToThis != null ) {
			isEqualsBlockMovements = this.blockMovementFromPreviousToThis.equals( blockMovementFromPreviousToThis );
		} else {
			isEqualsBlockMovements = blockMovementFromPreviousToThis == null;
		}

		return isEqualRhythmValues && isEqualStartTimes && isEqualCompositionInfos && isEqualMelodyList && isEqualStartIntervalPattern && isEqualsBlockMovements;
	}

	public double getRhythmValue() {
		return rhythmValue;
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

	public List<Melody> getMelodyList() {
		return melodyList;
	}

	public List<Integer> getStartIntervalPattern() {
		return startIntervalPattern;
	}

	public BlockMovement getBlockMovementFromPreviousToThis() {
		return blockMovementFromPreviousToThis;
	}

	public List<ComposeBlock> getPossibleNextComposeBlocks() {
        return possibleNextComposeBlocks;
    }

    public List<ComposeBlock> getPossiblePreviousComposeBlocks() {
        return possiblePreviousComposeBlocks;
    }

	public ComposeBlock getNext( int number ) {
		if ( this.possibleNextComposeBlocks.size() > number ) {
			return this.possibleNextComposeBlocks.get( number );
		}
		return null;
	}

	public ComposeBlock getPrevious( int number ) {
		if ( this.possiblePreviousComposeBlocks.size() > number ) {
			return this.possiblePreviousComposeBlocks.get( number );
		}
		return null;
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

		if ( !isEquals( this.possibleNextComposeBlocks, that.possibleNextComposeBlocks ) ) {
			return false;
		}
		if ( !isEquals( this.possiblePreviousComposeBlocks, that.possiblePreviousComposeBlocks ) ) {
			return false;
		}
		return true;
	}

	/**
	 * Two lists considered equals if they have equal size and every entry from one has similar entry from another
	 * @param firstComposeBlockList
	 * @param secondComposeBlockList
	 * @return
	 */
	private boolean isEquals( List<ComposeBlock> firstComposeBlockList, List<ComposeBlock> secondComposeBlockList ) {
		if ( firstComposeBlockList.size() != secondComposeBlockList.size() ) return false;

		for( ComposeBlock firstComposeBlock : firstComposeBlockList ) {
			boolean isInList = false;
			for ( ComposeBlock secondComposeBlock : secondComposeBlockList ) {
				if ( ( firstComposeBlock == null && secondComposeBlock != null ) ||
						( firstComposeBlock != null && secondComposeBlock == null ) ) {
					continue;
				} else if ( firstComposeBlock == null && secondComposeBlock == null ||
						firstComposeBlock.isSimilar( secondComposeBlock ) ) {
					isInList = true;
					break;
				}
			}
			if ( !isInList ) {
				return false;
			}
		}
		return true;
	}

}
