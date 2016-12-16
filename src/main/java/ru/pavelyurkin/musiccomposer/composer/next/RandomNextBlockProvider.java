package ru.pavelyurkin.musiccomposer.composer.next;

import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 */
@Component
public class RandomNextBlockProvider extends NextBlockProvider {

	/**
	 * Returns one of the possible next currentBlocks randomly
	 */
	@Override
	public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, double length, List<ComposeBlock> blocksToChooseFrom ) {
		if ( !blocksToChooseFrom.isEmpty() ) {
			//			int randomNumber = ( int ) ( Math.random() * ( possibleNextComposeBlocks.size() - 1 ) );
			int randomNumber = 0;
			ComposeBlock composeBlock = blocksToChooseFrom.get( randomNumber );
			return Optional.of( composeBlock );
		} else {
			return Optional.empty();
		}
	}

}
