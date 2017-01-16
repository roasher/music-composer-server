package ru.pavelyurkin.musiccomposer.core.composer.next.form;

import ru.pavelyurkin.musiccomposer.core.composer.next.form.FilteredNextFormBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class SimpleNextFormBlockProvider extends FilteredNextFormBlockProvider {

	@Override
	public Optional<ComposeBlock> getNextBlockFiltered( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, double length, List<ComposeBlock> blocksToChooseFrom ) {

		Optional<ComposeBlock> lastOfPossibles = blocksToChooseFrom.stream().reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );
		return lastOfPossibles;
	}

}
