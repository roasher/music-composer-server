package ru.pavelyurkin.musiccomposer.core.composer.first;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 */
@Component
public class RandomFirstStepProvider implements FirstStepProvider {

	/**
	 * Randomly returns one of the compose blocks witch have no possible previous blocks - it means that they are first blocks in the original composition
	 *
	 * @return
	 */
	@Override
	public Optional<CompositionStep> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions ) {
		List<ComposeBlock> allPossibleFirstBlocks = lexicon.getAllPossibleFirst();
		allPossibleFirstBlocks.removeAll( exclusions );
		if ( !allPossibleFirstBlocks.isEmpty() ) {
//			int randomNumber = ( int ) ( Math.random() * ( allPossibleFirstBlocks.size() - 1 ) );
			int randomNumber = 0;
			ComposeBlock composeBlock = allPossibleFirstBlocks.get( randomNumber );
			return Optional.of( new CompositionStep( composeBlock, composeBlock.getMusicBlock().transposeClone( 0 ) ) );
		} else {
			return Optional.empty();
		}
	}

}
