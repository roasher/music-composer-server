package ru.pavelyurkin.musiccomposer.core.composer.next;

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
public abstract class FilteredNextStepProvider extends NextStepProvider {

	private ComposeStepFilter composeStepFilter;

	@Override
	public Optional<CompositionStep> getNext( List<CompositionStep> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form, double length ) {

		List<CompositionStep> allPreviousCompositionSteps = new ArrayList<>( previousCompositionSteps );
		allPreviousCompositionSteps.addAll( previousFormCompositionSteps
				.stream().flatMap( formCompositionStep -> formCompositionStep.getCompositionSteps().stream() ).collect( Collectors.toList() )
		);
		// User filters
		List<CompositionStep> filtered = composeStepFilter != null ? composeStepFilter.filter( blocksToChooseFrom, allPreviousCompositionSteps ) : blocksToChooseFrom;

		return getNextBlockFiltered( filtered, previousCompositionSteps, previousFormCompositionSteps, form, length );
	}

	/**
	 * Get next block choosing from Filtered Blocks
	 *
	 * @param blocksToChooseFrom
	 * @param previousCompositionSteps
	 * @param formCompositionSteps
	 * @param form
	 * @param length
	 * @return
	 */
	public abstract Optional<CompositionStep> getNextBlockFiltered( List<CompositionStep> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> formCompositionSteps, Optional<Form> form, double length );

	public void setComposeStepFilter( ComposeStepFilter composeStepFilter ) {
		this.composeStepFilter = composeStepFilter;
	}
}
