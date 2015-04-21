package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class wraps Music Block providing information of possible next and previous music blocks due to voice leading
 * Created by pyurkin on 05.03.2015.
 */
public class ComposeBlock {

    private MusicBlock musicBlock;

    // Lists of possible next and previous Compose Blocks that has convenient voice leading in the original composition
	// We are assuming that first member of list is the original composition member - so all blocks except firsts and lasts will have at least one member in the list
    private List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );
    private List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	public ComposeBlock( MusicBlock musicBlock ) {
		this.musicBlock = musicBlock;
	}

	public ComposeBlock( List<ComposeBlock> composeBlockList ) {
		if ( composeBlockList != null && composeBlockList.size() != 0 ) {
			List<MusicBlock> musicBlockList = new ArrayList<>();
			for ( ComposeBlock composeBlock : composeBlockList ) {
				musicBlockList.add( composeBlock.getMusicBlock() );
			}
			this.musicBlock = new MusicBlock( null, musicBlockList );
			this.possiblePreviousComposeBlocks = composeBlockList.get( 0 ).getPossiblePreviousComposeBlocks();
			this.possibleNextComposeBlocks = composeBlockList.get( composeBlockList.size() - 1 ).getPossibleNextComposeBlocks();

		} else {
			throw new RuntimeException( "Input compose block is malformed" );
		}
	}

    public double getRhythmValue() {
        return this.musicBlock.getRhythmValue();
    }

	public MusicBlock getMusicBlock() {
		return musicBlock;
	}

	public List<ComposeBlock> getPossibleNextComposeBlocks() {
        return possibleNextComposeBlocks;
    }

    public List<ComposeBlock> getPossiblePreviousComposeBlocks() {
        return possiblePreviousComposeBlocks;
    }

	public double getStartTime() {
		return this.musicBlock.getStartTime();
	}

	@Override
	public String toString() {
		return this.musicBlock.toString();
	}

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof ComposeBlock ) )
			return false;

		ComposeBlock that = ( ComposeBlock ) o;

		if ( !musicBlock.equals( that.musicBlock ) )
			return false;

		// this will lead to stack overflow:
//		if ( !possibleNextComposeBlocks.equals( that.possibleNextComposeBlocks ) )
//			return false;
//		if ( !possiblePreviousComposeBlocks.equals( that.possiblePreviousComposeBlocks ) )
//			return false;

		if ( possiblePreviousComposeBlocks.size() != that.possiblePreviousComposeBlocks.size() )
			return false;
		for ( int possiblePreviousComposeBlockNumber = 0; possiblePreviousComposeBlockNumber < possiblePreviousComposeBlocks.size(); possiblePreviousComposeBlockNumber++ ) {
			if ( !possiblePreviousComposeBlocks.get( possiblePreviousComposeBlockNumber ).getMusicBlock().equals( that.possiblePreviousComposeBlocks.get( possiblePreviousComposeBlockNumber ).getMusicBlock() ) )
				return false;
		}

		if ( possibleNextComposeBlocks.size() != that.possibleNextComposeBlocks.size() )
			return false;
		for ( int possibleNextComposeBlockNumber = 0; possibleNextComposeBlockNumber < possibleNextComposeBlocks.size(); possibleNextComposeBlockNumber++ ) {
			if ( !possibleNextComposeBlocks.get( possibleNextComposeBlockNumber ).getMusicBlock().equals( that.possibleNextComposeBlocks.get( possibleNextComposeBlockNumber ).getMusicBlock() ) )
				return false;
		}

		return true;
	}

	@Override public int hashCode() {
		int result = musicBlock.hashCode();
		result = 31 * result + possibleNextComposeBlocks.size();
		result = 31 * result + possiblePreviousComposeBlocks.size();
		return result;
	}
}
