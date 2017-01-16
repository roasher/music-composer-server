package ru.pavelyurkin.musiccomposer.core.composer.next;

import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeBlockFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;

import java.util.List;
import java.util.Optional;

/**
 * Created by night_wish on 16.01.17.
 */
public abstract class FilteredNextBlockProvider extends NextBlockProvider {

	private ComposeBlockFilter composeBlockFilter;

	@Override
	public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, double length, List<ComposeBlock> blocksToChooseFrom ) {

		// User filters
		List<ComposeBlock> filteredBlocks =
				composeBlockFilter != null ? composeBlockFilter.filter( blocksToChooseFrom, previousCompositionSteps ) : blocksToChooseFrom;

		return getNextBlockFiltered( previousCompositionSteps, length, filteredBlocks );
	}

	/**
	 * Get next block choosing from Filtered Blocks
	 * @param previousCompositionSteps
	 * @param length
	 * @param filteredBlocks
	 * @return
	 */
	public abstract Optional<ComposeBlock> getNextBlockFiltered( List<CompositionStep> previousCompositionSteps, double length, List<ComposeBlock> filteredBlocks );

	public void setComposeBlockFilter( ComposeBlockFilter composeBlockFilter ) {
		this.composeBlockFilter = composeBlockFilter;
	}

}
