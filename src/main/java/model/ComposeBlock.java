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
    private List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>(  );
    private List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>(  );

	public ComposeBlock( MusicBlock musicBlock ) {
		this.musicBlock = musicBlock;
	}

    public ComposeBlock( MusicBlock musicBlock, List<ComposeBlock> possibleNextComposeBlocks, List<ComposeBlock> possiblePreviousComposeBlocks ) {
        this.musicBlock = musicBlock;
        this.possibleNextComposeBlocks = possibleNextComposeBlocks;
        this.possiblePreviousComposeBlocks = possiblePreviousComposeBlocks;
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
}
