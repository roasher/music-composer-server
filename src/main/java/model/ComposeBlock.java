package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class wraps Music Block providing information of possible next and previous music blocks due to voice leading
 * Created by pyurkin on 05.03.2015.
 */
public class ComposeBlock {

    private MusicBlock musicBlock;

	// Links to the wrapper blocks in the original composition
	private ComposeBlock previousComposeBlock;
	private ComposeBlock nextComposeBlock;

    // Lists of possible next and previous Compose Blocks that has convenient voice leading in the original composition
    private List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );
    private List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	public ComposeBlock( MusicBlock musicBlock ) {
		this.musicBlock = musicBlock;
	}

    public ComposeBlock( MusicBlock musicBlock, List<ComposeBlock> possibleNextComposeBlocks, List<ComposeBlock> possiblePreviousComposeBlocks, ComposeBlock previousComposeBlock, ComposeBlock nextComposeBlock ) {
        this.musicBlock = musicBlock;
        this.possibleNextComposeBlocks = possibleNextComposeBlocks;
        this.possiblePreviousComposeBlocks = possiblePreviousComposeBlocks;

		this.previousComposeBlock = previousComposeBlock;
		this.nextComposeBlock = nextComposeBlock;
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

			this.previousComposeBlock =  composeBlockList.get( 0 ).previousComposeBlock;
			this.nextComposeBlock = composeBlockList.get( composeBlockList.size() - 1 ).nextComposeBlock;
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
		if ( !possibleNextComposeBlocks.equals( that.possibleNextComposeBlocks ) )
			return false;
		if ( !possiblePreviousComposeBlocks.equals( that.possiblePreviousComposeBlocks ) )
			return false;

		return true;
	}

	@Override public int hashCode() {
		int result = musicBlock.hashCode();
		result = 31 * result + possibleNextComposeBlocks.hashCode();
		result = 31 * result + possiblePreviousComposeBlocks.hashCode();
		return result;
	}
}
