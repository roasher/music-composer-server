package ru.pavelyurkin.musiccomposer.core.composer.next;

import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Next Block provider that handles filters
 */
@Data
public abstract class FilteredNextStepProvider extends NextStepProvider {

	private ComposeStepFilter composeStepFilter;

	@Override
	public Optional<CompositionStep> getNext( List<CompositionStep> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form ) {

		List<CompositionStep> allPreviousCompositionSteps = new ArrayList<>( previousCompositionSteps );
		allPreviousCompositionSteps.addAll( previousFormCompositionSteps
				.stream().flatMap( formCompositionStep -> formCompositionStep.getCompositionSteps().stream() ).collect( Collectors.toList() )
		);
		// User filters
		List<CompositionStep> filtered = composeStepFilter != null ? composeStepFilter.filter( blocksToChooseFrom, allPreviousCompositionSteps ) : blocksToChooseFrom;

		return getNextBlockFiltered( filtered, previousCompositionSteps, previousFormCompositionSteps, form );
	}

	/**
	 * Get next block choosing from Filtered Blocks
	 *
	 * @param blocksToChooseFrom
	 * @param previousCompositionSteps
	 * @param formCompositionSteps
	 * @param form
	 * @return
	 */
	public abstract Optional<CompositionStep> getNextBlockFiltered( List<CompositionStep> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> formCompositionSteps, Optional<Form> form );

}
