package ru.pavelyurkin.musiccomposer.core.model;

import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class represents wrapper to Music Block collection
 * Incapsulates all methods of getting proper music block
 * Created by pyurkin on 13.02.15.
 */
@Data
public class Lexicon implements Serializable {

	private List<ComposeBlock> composeBlocks;
	private Map<Integer, List<Integer>> possibleNextMusicBlockNumbers;

	public Lexicon( List<ComposeBlock> composeBlocks, Map<Integer, List<Integer>> possibleNextMusicBlockNumbers ) {
		if (possibleNextMusicBlockNumbers.size() != composeBlocks.size()) {
			throw new IllegalArgumentException("blocks and possibleNexts has different size");
		}
		this.composeBlocks = composeBlocks;
		this.possibleNextMusicBlockNumbers = possibleNextMusicBlockNumbers;
	}

	public Lexicon( Map<Integer, List<Integer>> possibleNextMusicBlockNumbers, List<MusicBlock> musicBlocks ) {
		if (possibleNextMusicBlockNumbers.size() != musicBlocks.size()) {
			throw new IllegalArgumentException("blocks and possibleNexts have different size");
		}

		List<ComposeBlock> composeBlocks = new ArrayList<>();
		for ( int musicBlockNumber = 0; musicBlockNumber < musicBlocks.size(); musicBlockNumber++ ) {
			composeBlocks.add( new ComposeBlock( musicBlocks.get( musicBlockNumber ) ) );
		}

		for ( int composeBlockNumber = 0; composeBlockNumber < composeBlocks.size(); composeBlockNumber++ ) {
			ComposeBlock composeBlock = composeBlocks.get( composeBlockNumber );
			for ( int musicBlockNumber : possibleNextMusicBlockNumbers.get( composeBlockNumber ) ) {
				ComposeBlock possibleNextComposeBlock = composeBlocks.get( musicBlockNumber );
				composeBlock.getPossibleNextComposeBlocks().add( possibleNextComposeBlock );
				// we should check if we need to add previous at first place
				if ( composeBlockNumber + 1 != musicBlockNumber ) {
					possibleNextComposeBlock.getPossiblePreviousComposeBlocks().add( composeBlock );
				} else {
					possibleNextComposeBlock.getPossiblePreviousComposeBlocks().add( 0, composeBlock );
				}
			}
		}

		this.composeBlocks = composeBlocks;
		this.possibleNextMusicBlockNumbers = possibleNextMusicBlockNumbers;
	}

	public Set<CompositionInfo> getCompositionsInLexicon() {
		return composeBlocks.stream()
				.map( ComposeBlock::getCompositionInfo )
				.collect( Collectors.toSet());
	}

	public double getMinRhythmValue() {
		return composeBlocks.stream()
				.mapToDouble( ComposeBlock::getRhythmValue )
				.min()
				.orElse( Double.MAX_VALUE );
	}

	public List<ComposeBlock> getAllPossibleFirsts() {
		List<ComposeBlock> firstBlocks = new ArrayList<>(  );
		for ( ComposeBlock composeBlock : this.composeBlocks ) {
			if ( composeBlock.getPossiblePreviousComposeBlocks().isEmpty() && !composeBlock.isStartsWithRest() ) {
				firstBlocks.add( composeBlock );
			}
		}
		return firstBlocks;
	}

	public static Lexicon getBlankLexicon() {
		return new Lexicon( new ArrayList<>(), Collections.emptyMap() );
	}

	public ComposeBlock get( int number ) {
		return this.composeBlocks.get( number );
	}

}
