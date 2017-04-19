package ru.pavelyurkin.musiccomposer.core.composer.next;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

import java.util.List;
import java.util.Optional;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class SimpleNextStepProvider extends FilteredNextStepProvider {

	@Override
	public Optional<CompositionStep> getNextBlockFiltered( List<CompositionStep> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> formCompositionSteps, Optional<Form> form ) {

		Optional<CompositionStep> lastOfPossibles = blocksToChooseFrom.stream().reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );
		return lastOfPossibles;
	}

}