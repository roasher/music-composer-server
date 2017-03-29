package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wish on 04.02.2016.
 */
public abstract class AbstractComposeStepFilter implements ComposeStepFilter {

	private ComposeStepFilter composeStepFilter;

	public AbstractComposeStepFilter( ComposeStepFilter composeStepFilter ) {
		this.composeStepFilter = composeStepFilter;
	}

	public AbstractComposeStepFilter() {
	}

	/**
	 * Returns valid in terms of fitering blocks
	 * @param possibleNextComposeSteps
	 * @param previousCompositionSteps
	 * @return
	 */
	@Override
	public List<CompositionStep> filter( List<CompositionStep> possibleNextComposeSteps, List<CompositionStep> previousCompositionSteps ) {
		List<CompositionStep> filteredPreviously = composeStepFilter != null ?
				composeStepFilter.filter( possibleNextComposeSteps, previousCompositionSteps ) :
				new ArrayList<>( possibleNextComposeSteps );
		List<MusicBlock> previousMusicBlocks = previousCompositionSteps.stream().map( CompositionStep::getTransposedBlock ).collect( Collectors.toList() );
		return filteredPreviously.stream().filter( compositionStep ->  filterIt(compositionStep.getTransposedBlock(), previousMusicBlocks) ).collect( Collectors.toList());
	}

	/**
	 * Returns true if block should stay
	 * @param block
	 * @param previousBlocks
	 * @return
	 */
	public abstract boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks );
}
