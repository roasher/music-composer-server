package ru.pavelyurkin.musiccomposer.core.model;

import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * Class represents blocks which can be used in composing process
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

	public ComposeBlock( double startTime, CompositionInfo compositionInfo, List<Melody> melodyList, BlockMovement blockMovementFromPreviousToThis ) {
		this.musicBlock = new MusicBlock( startTime, compositionInfo, melodyList, blockMovementFromPreviousToThis );
	}

	public ComposeBlock( List<ComposeBlock> composeBlockList ) {
		this.musicBlock = new MusicBlock( composeBlockList
				.stream()
				.map( ComposeBlock::getMusicBlock )
				.collect( Collectors.toList())
		);
		this.possiblePreviousComposeBlocks = composeBlockList.get( 0 ).getPossiblePreviousComposeBlocks();
		this.possibleNextComposeBlocks = composeBlockList.get( composeBlockList.size() - 1 ).getPossibleNextComposeBlocks();
	}

	public boolean hasEqualsMusicBlock( ComposeBlock composeBlock ) {
		return this.musicBlock.equals( composeBlock.getMusicBlock() );
	}

	public boolean isStartsWithRest() {
		OptionalInt firstNonRestPitch = this.musicBlock.getMelodyList().stream().mapToInt( melody -> melody.getNote( 0 ).getPitch() ).filter( value -> value != Note.REST ).findFirst();
		return !firstNonRestPitch.isPresent();
	}

	@Override
	public String toString() {
		return this.musicBlock.toString();
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof ComposeBlock ) )
			return false;

		ComposeBlock that = ( ComposeBlock ) o;

		if ( !this.hasEqualsMusicBlock( that ) )
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
						firstComposeBlock.hasEqualsMusicBlock( secondComposeBlock ) ) {
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

	public void setPossibleNextComposeBlocks( List<ComposeBlock> possibleNextComposeBlocks ) {
		this.possibleNextComposeBlocks = possibleNextComposeBlocks;
	}

	public void setPossiblePreviousComposeBlocks( List<ComposeBlock> possiblePreviousComposeBlocks ) {
		this.possiblePreviousComposeBlocks = possiblePreviousComposeBlocks;
	}

	public double getRhythmValue() {
		return this.musicBlock.getRhythmValue();
	}

	public double getStartTime() {
		return this.musicBlock.getStartTime();
	}

	public CompositionInfo getCompositionInfo() {
		return this.musicBlock.getCompositionInfo();
	}

	public List<Melody> getMelodyList() {
		return this.musicBlock.getMelodyList();
	}

	public List<Integer> getStartIntervalPattern() {
		return this.musicBlock.getStartIntervalPattern();
	}

	public List<Integer> getEndIntervalPattern() {
		return this.musicBlock.getEndIntervalPattern();
	}

	public BlockMovement getBlockMovementFromPreviousToThis() {
		return this.musicBlock.getBlockMovementFromPreviousToThis();
	}

	public List<ComposeBlock> getPossibleNextComposeBlocks() {
		return possibleNextComposeBlocks;
	}

	public List<ComposeBlock> getPossiblePreviousComposeBlocks() {
		return possiblePreviousComposeBlocks;
	}

	public MusicBlock getMusicBlock() {
		return musicBlock;
	}

	public void setMusicBlock( MusicBlock musicBlock ) {
		this.musicBlock = musicBlock;
	}
}
