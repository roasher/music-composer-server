package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by wish on 04.02.2016.
 */
@Slf4j
@Data
public abstract class AbstractComposeStepFilter implements ComposeStepFilter {

	private AbstractComposeStepFilter composeStepFilter;

	public AbstractComposeStepFilter( AbstractComposeStepFilter composeStepFilter ) {
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

	@Override
	public void replaceFilter(AbstractComposeStepFilter composeStepFilterToReplaceWith) {
		AbstractComposeStepFilter filterHoldingFilterToReplace = this;
		AbstractComposeStepFilter filterToBeReplaced = composeStepFilter;
		while (filterToBeReplaced != null &&
				!composeStepFilterToReplaceWith.getClass().equals(filterToBeReplaced.getClass())) {
			// step down
			filterHoldingFilterToReplace = filterHoldingFilterToReplace.getComposeStepFilter();
			filterToBeReplaced = filterHoldingFilterToReplace.getComposeStepFilter();
		};
		if (filterToBeReplaced != null) {
			filterHoldingFilterToReplace.setComposeStepFilter(composeStepFilterToReplaceWith);
			composeStepFilterToReplaceWith.setComposeStepFilter(filterToBeReplaced.getComposeStepFilter());
		} else {
			log.warn("Didn't find inner filter with such type to replace {}", composeStepFilterToReplaceWith.getClass());
		}
	}
}
