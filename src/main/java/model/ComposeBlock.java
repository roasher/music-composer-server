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

	public ComposeBlock( MusicBlock musicBlock, List<MusicBlock> possibleNextMusicBlocks ) {
		this.musicBlock = musicBlock;
		for ( MusicBlock possibleNextMusicBlock : possibleNextMusicBlocks ) {
			this.possibleNextComposeBlocks.add( new ComposeBlock( possibleNextMusicBlock ) );
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
}
