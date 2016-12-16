package ru.pavelyurkin.musiccomposer.composer.next;

import ru.pavelyurkin.musiccomposer.composer.next.filter.ComposeBlockFilter;
import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class SimpleNextBlockProvider extends FilteredNextBlockProvider {

	@Override
	public Optional<ComposeBlock> getNextBlockFiltered( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, double length, List<ComposeBlock> blocksToChooseFrom ) {

		Optional<ComposeBlock> lastOfPossibles = blocksToChooseFrom.stream().reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );
		return lastOfPossibles;
	}

}
