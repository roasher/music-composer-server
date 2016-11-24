package ru.pavelyurkin.musiccomposer.model;

import ru.pavelyurkin.musiccomposer.model.composition.CompositionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ru.pavelyurkin.musiccomposer.utils.Utils.isEquals;

/**
 * Class represents wrapper to Music Block collection
 * Incapsulates all methods of getting proper music block
 * Created by pyurkin on 13.02.15.
 */
public class Lexicon {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	private List<ComposeBlock> composeBlockList = new ArrayList<>();

	private Map<Integer, List<Integer>> possibleNextMusicBlockNumbers;

	// metadata
	private double minRhythmValue = Double.MAX_VALUE;
	private Set<CompositionInfo> compositionsInLexicon = new HashSet<>(  );

	public Lexicon( List<ComposeBlock> composeBlocks, Map<Integer, List<Integer>> possibleNextMusicBlockNumbers ) {
		this( composeBlocks );
		this.possibleNextMusicBlockNumbers = possibleNextMusicBlockNumbers;
	}

	public Lexicon( List<ComposeBlock> composeBlockList ) {
        this.composeBlockList = composeBlockList;
		for ( ComposeBlock composeBlock : composeBlockList ) {
			if ( minRhythmValue > composeBlock.getRhythmValue() ) {
				minRhythmValue = composeBlock.getRhythmValue();
			}
			compositionsInLexicon.add( composeBlock.getCompositionInfo() );
		}
	}

	public List<ComposeBlock> getAllPossibleFirst() {
		List<ComposeBlock> firstBlocks = new ArrayList<>(  );
		for ( ComposeBlock composeBlock : this.composeBlockList ) {
			if ( composeBlock.getPossiblePreviousComposeBlocks().isEmpty() ) {
				firstBlocks.add( composeBlock );
			}
		}
		return firstBlocks;
	}

	public Map<Integer, List<Integer>> getPossibleNextMusicBlockNumbers() {
		return possibleNextMusicBlockNumbers;
	}

	public static Lexicon getBlankLexicon() {
		return new Lexicon( new ArrayList<ComposeBlock>(  ) );
	}

	public ComposeBlock get( int number ) {
		return this.composeBlockList.get( number );
	}

	public List<ComposeBlock> getComposeBlockList() {
		return composeBlockList;
	}

	public double getMinRhythmValue() {
		return minRhythmValue;
	}

	public Set<CompositionInfo> getCompositionsInLexicon() {
		return compositionsInLexicon;
	}

	public void setCompositionsInLexicon( Set<CompositionInfo> compositionsInLexicon ) {
		this.compositionsInLexicon = compositionsInLexicon;
	}

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof Lexicon ) )
			return false;

		Lexicon lexicon = ( Lexicon ) o;

		if ( !isEquals( lexicon.minRhythmValue, minRhythmValue ) ) {
			return false;
		}
		if ( !composeBlockList.equals( lexicon.composeBlockList ) ) {
			return false;
		}
		if ( !compositionsInLexicon.equals( lexicon.compositionsInLexicon ) ) {
			return false;
		}

		return true;
	}

	@Override public int hashCode() {
		int result;
		long temp;
		result = composeBlockList.hashCode();
		temp = Double.doubleToLongBits( minRhythmValue );
		result = 31 * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		result = 31 * result + compositionsInLexicon.hashCode();
		return result;
	}
}
