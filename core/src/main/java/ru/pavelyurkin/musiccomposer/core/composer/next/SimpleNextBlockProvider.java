package ru.pavelyurkin.musiccomposer.core.composer.next;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

import java.util.List;
import java.util.Optional;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class SimpleNextBlockProvider extends FilteredNextBlockProvider {

	@Override
	public Optional<ComposeBlock> getNextBlockFiltered( List<ComposeBlock> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> formCompositionSteps, Optional<Form> form, double length ) {

		Optional<ComposeBlock> lastOfPossibles = blocksToChooseFrom.stream().reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );
		return lastOfPossibles;
	}

}
