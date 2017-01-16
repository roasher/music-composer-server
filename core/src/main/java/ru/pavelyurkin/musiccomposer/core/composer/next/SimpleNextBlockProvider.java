package ru.pavelyurkin.musiccomposer.core.composer.next;

import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;

import java.util.List;
import java.util.Optional;

/**
 * Created by night_wish on 16.01.17.
 */
@Component
public class SimpleNextBlockProvider extends FilteredNextBlockProvider {

	@Override
	public Optional<ComposeBlock> getNextBlockFiltered( List<CompositionStep> previousCompositionSteps, double length, List<ComposeBlock> blocksToChooseFrom ) {

		Optional<ComposeBlock> lastOfPossibles = blocksToChooseFrom.stream().reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );
		return lastOfPossibles;

	}

}
