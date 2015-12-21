package model;

import model.composition.Composition;
import model.composition.CompositionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.ComponentUI;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Class represents wrapper to Music Block collection
 * Incapsulates all methods of getting proper music block
 * Created by pyurkin on 13.02.15.
 */
public class Lexicon {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	private List<ComposeBlock> composeBlockList = new ArrayList<>();

	// metadata
	private double minRhythmValue = Double.MAX_VALUE;
	private Set<CompositionInfo> compositionsInLexicon = new HashSet<>(  );

	public Lexicon( List<ComposeBlock> composeBlockList ) {
        this.composeBlockList = composeBlockList;
		for ( ComposeBlock composeBlock : composeBlockList ) {
			if ( minRhythmValue > composeBlock.getRhythmValue() ) {
				minRhythmValue = composeBlock.getRhythmValue();
			}
			compositionsInLexicon.add( composeBlock.getCompositionInfo() );
		}
	}

	/**
	 * Returns one of the possible next currentBlocks randomly
	 * @param currentBlock
	 * @param exclusions
	 * @return
	 */
	public ComposeBlock getRandomNext( ComposeBlock currentBlock, List<ComposeBlock> exclusions ) {
		List<ComposeBlock> possibleNextComposeBlocks = currentBlock.getPossibleNextComposeBlocks();
		possibleNextComposeBlocks.removeAll( exclusions );
		if ( !possibleNextComposeBlocks.isEmpty() ) {
//			int randomNumber = ( int ) ( Math.random() * ( possibleNextComposeBlocks.size() - 1 ) );
			int randomNumber = 0;
			ComposeBlock composeBlock = possibleNextComposeBlocks.get( randomNumber );
			logger.info( "Returning one of the next possible blocks randomly: {}", composeBlock );
			return composeBlock;
		} else {
			return null;
		}
	}

	/**
	 * Randomly returns one of the compose blocks witch have no possible previous blocks - it means that they are first blocks in the original composition
	 * @return
	 */
	public ComposeBlock getRandomFirst( List<ComposeBlock> exclusions ) {
		List<ComposeBlock> allPossibleFirstBlocks = getAllPossibleFirst();
		allPossibleFirstBlocks.removeAll( exclusions );
		if ( !allPossibleFirstBlocks.isEmpty() ) {
//			int randomNumber = ( int ) ( Math.random() * ( allPossibleFirstBlocks.size() - 1 ) );
			int randomNumber = 0;
			logger.info( "Returning one of the blocks which are first in the original composition randomly" );
			return allPossibleFirstBlocks.get( randomNumber );
		} else {
			return null;
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

		if ( Double.compare( lexicon.minRhythmValue, minRhythmValue ) != 0 ) {
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
