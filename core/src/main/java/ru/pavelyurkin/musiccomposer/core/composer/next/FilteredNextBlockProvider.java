package ru.pavelyurkin.musiccomposer.core.composer.next;

import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeBlockFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

import java.util.List;
import java.util.Optional;

/**
 * Next Block provider that handles filters
 */
public abstract class FilteredNextBlockProvider extends NextBlockProvider {

	private ComposeBlockFilter composeBlockFilter;

	@Override
	public Optional<ComposeBlock> getNextBlock( List<ComposeBlock> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form, double length ) {

		// User filters
		List<ComposeBlock> filteredBlocks = composeBlockFilter != null ? composeBlockFilter.filter( blocksToChooseFrom, previousCompositionSteps ) : blocksToChooseFrom;

		return getNextBlockFiltered( filteredBlocks, previousCompositionSteps, previousFormCompositionSteps, form, length );
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
	public abstract Optional<ComposeBlock> getNextBlockFiltered( List<ComposeBlock> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> formCompositionSteps, Optional<Form> form, double length );

	public void setComposeBlockFilter( ComposeBlockFilter composeBlockFilter ) {
		this.composeBlockFilter = composeBlockFilter;
	}
}
