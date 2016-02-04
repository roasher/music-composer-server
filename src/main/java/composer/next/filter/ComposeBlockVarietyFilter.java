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
public class ComposeBlockVarietyFilter extends AbstractComposeBlockFilter {

	private int possibleBlockNumberFromSameCompositionOneByOne;

	public ComposeBlockVarietyFilter() {
	}

	public ComposeBlockVarietyFilter( int possibleBlockNumberFromSameCompositionOneByOne, ComposeBlockFilter composeBlockFilter ) {
		super( composeBlockFilter );
		this.possibleBlockNumberFromSameCompositionOneByOne = possibleBlockNumberFromSameCompositionOneByOne;
	}

	public ComposeBlockVarietyFilter( int possibleBlockNumberFromSameCompositionOneByOne ) {
		this.possibleBlockNumberFromSameCompositionOneByOne = possibleBlockNumberFromSameCompositionOneByOne;
	}

	@Override
	public List<ComposeBlock> filterIt( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		List<ComposeBlock> out = new ArrayList<>();
		for ( ComposeBlock possibleNext : possibleNextComposeBlocks ) {
			if ( previousCompositionSteps.size() > possibleBlockNumberFromSameCompositionOneByOne ) {
				Set<CompositionInfo> compositionInfos = previousCompositionSteps.stream()
						.skip( previousCompositionSteps.size() - possibleBlockNumberFromSameCompositionOneByOne )
						.map( compositionStep -> compositionStep.getOriginComposeBlock().getCompositionInfo() ).collect( Collectors.toSet() );
				if ( compositionInfos.size() != 1 || !compositionInfos.contains( possibleNext.getCompositionInfo() ) ) {
					out.add( possibleNext );
				}
			} else {
				out.add( possibleNext );
			}
		}
		return out;
	}
}
