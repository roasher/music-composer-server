package composer.next.filter;

import composer.step.CompositionStep;
import model.ComposeBlock;
import model.composition.CompositionInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by wish on 02.02.2016.
 * Filters next compose block to maintain variety of blocks used in composition.
 * If from previous steps there are already x
 * blocks from one composition, filter will decline blocks from that composition to prevent
 * x + 1 blocks from same composition in the result
 */
@Component
public class ComposeBlockVarietyFilter implements ComposeBlockFilter {

	private ComposeBlockFilter composeBlockFilter;
	private int possibleBlockNumberFromSameCompositionOneByOne;

	public ComposeBlockVarietyFilter() {
	}

	public ComposeBlockVarietyFilter( ComposeBlockFilter composeBlockFilter, int possibleBlockNumberFromSameCompositionOneByOne ) {
		this.composeBlockFilter = composeBlockFilter;
		this.possibleBlockNumberFromSameCompositionOneByOne = possibleBlockNumberFromSameCompositionOneByOne;
	}

	public ComposeBlockVarietyFilter( int possibleBlockNumberFromSameCompositionOneByOne ) {
		this.possibleBlockNumberFromSameCompositionOneByOne = possibleBlockNumberFromSameCompositionOneByOne;
	}

	@Override
	public List<ComposeBlock> filter( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		List<ComposeBlock> filteredPreviously = composeBlockFilter != null ?
				composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps ) :
				new ArrayList<>( possibleNextComposeBlocks );
		List<ComposeBlock> out = new ArrayList<>();
		for ( ComposeBlock possibleNext : filteredPreviously ) {
			// TODO Refactor as for now there is no need in possibleNext inside cycle - we need to take condition out
			if ( previousCompositionSteps.size() > possibleBlockNumberFromSameCompositionOneByOne ) {
				Set<CompositionInfo> compositionInfos = previousCompositionSteps.stream()
						.skip( previousCompositionSteps.size() - possibleBlockNumberFromSameCompositionOneByOne )
						.map( compositionStep -> compositionStep.getOriginComposeBlock().getCompositionInfo() ).collect( Collectors.toSet() );
				if ( compositionInfos.size() != 1 ) {
					out.add( possibleNext );
				}
			} else {
				out.add( possibleNext );
			}
		}
		return out;
	}
}
