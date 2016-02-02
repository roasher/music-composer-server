package composer.next;

import composer.step.CompositionStep;
import model.ComposeBlock;
import model.Lexicon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 */
@Component
public class SimpleNextBlockProvider implements NextBlockProvider {

	/**
	 * Returns one of the possible next currentBlocks randomly
	 */
	@Override
	public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps ) {
		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks() );
		possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions());
		if ( !possibleNextComposeBlocks.isEmpty() ) {
			int randomNumber = ( int ) ( Math.random() * ( possibleNextComposeBlocks.size() - 1 ) );
			//int randomNumber = 0;
			ComposeBlock composeBlock = possibleNextComposeBlocks.get( randomNumber );
			return Optional.of( composeBlock );
		} else {
			return Optional.empty();
		}
	}

}
