package composer.next.filter;

import composer.step.CompositionStep;
import model.ComposeBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wish on 04.02.2016.
 */
public abstract class AbstractComposeBlockFilter implements ComposeBlockFilter {

	private ComposeBlockFilter composeBlockFilter;

	public AbstractComposeBlockFilter( ComposeBlockFilter composeBlockFilter ) {
		this.composeBlockFilter = composeBlockFilter;
	}

	public AbstractComposeBlockFilter() {
	}

	public List<ComposeBlock> filter( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		List<ComposeBlock> filteredPrevously = composeBlockFilter != null ?
				composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps ) :
				new ArrayList<>( possibleNextComposeBlocks );
		return filterIt( filteredPrevously, previousCompositionSteps );
	}

	public abstract List<ComposeBlock> filterIt( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps );
}
