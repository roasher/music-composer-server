package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.ComponentUI;
import java.util.*;

/**
 * Class represents wrapper to Music Block collection
 * Incapsulates all methods of getting proper music block
 * Created by pyurkin on 13.02.15.
 */
public class Lexicon {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	private List<ComposeBlock> composeBlockList = new ArrayList<>();
	private double minRhythmValue = Double.MAX_VALUE;

	public Lexicon( List<ComposeBlock> composeBlockList ) {
        this.composeBlockList = composeBlockList;
		for ( ComposeBlock composeBlock : composeBlockList ) {
			if ( minRhythmValue > composeBlock.getRhythmValue() ) {
				minRhythmValue = composeBlock.getRhythmValue();
			}
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
			int randomNumber = ( int ) ( Math.random() * ( possibleNextComposeBlocks.size() - 1 ) );
			logger.info( "Returning one of the next possible blocks randomly" );
			return possibleNextComposeBlocks.get( randomNumber );
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
			int randomNumber = ( int ) ( Math.random() * ( allPossibleFirstBlocks.size() - 1 ) );
			logger.info( "Returning one of the first blocks witch are first in the original composition randomly" );
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

	public ComposeBlock get( int number ) {
		return this.composeBlockList.get( number );
	}

	public List<ComposeBlock> getComposeBlockList() {
		return composeBlockList;
	}

	public double getMinRhythmValue() {
		return minRhythmValue;
	}
}
