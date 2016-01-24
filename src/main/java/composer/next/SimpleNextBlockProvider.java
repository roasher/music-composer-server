package composer.next;

import composer.step.CompositionStep;
import model.ComposeBlock;
import model.Lexicon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wish on 22.12.2015.
 */
public class SimpleNextBlockProvider implements NextBlockProvider {

	/**
	 * Returns one of the possible next currentBlocks randomly
	 */
	@Override
	public ComposeBlock getNextBlock( Lexicon lexicon, List<CompositionStep> previousCompositionSteps ) {
		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getComposeBlock().getPossibleNextComposeBlocks() );
		possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions());
		if ( !possibleNextComposeBlocks.isEmpty() ) {
			int randomNumber = ( int ) ( Math.random() * ( possibleNextComposeBlocks.size() - 1 ) );
			//int randomNumber = 0;
			ComposeBlock composeBlock = possibleNextComposeBlocks.get( randomNumber );
			return composeBlock;
		} else {
			return null;
		}
	}

}
