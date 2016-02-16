package composer.next;

import composer.next.filter.ComposeBlockFilter;
import composer.step.CompositionStep;
import decomposer.form.analyzer.FormEqualityAnalyser;
import model.ComposeBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by wish on 16.02.2016.
 */
@Component
public class FormNextBlockProvider implements NextBlockProvider {

	@Autowired
	private FormEqualityAnalyser formEqualityAnalyser;

	private ComposeBlockFilter composeBlockFilter;

	@Override
	public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps ) {
		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks() );
		possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions() );
		// TODO etalons diffs and previouslyComposedRhythmValue
		Optional<List<ComposeBlock>> etalons = null;
		Optional<List<ComposeBlock>> differents = null;
		double previouslyComposedRhythmValue = 0;

		Optional<ComposeBlock> lastOfPossibles = composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps )
				.stream()
				.sorted( getComposeBlockComparator( etalons, differents, 0 ) )
				.reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );

		return lastOfPossibles;
	}

	/**
	 * Comparator that finds out which of compose blocks better suites to add in composition in terms of form
	 * @param etalons - whole pieces of needed form
	 * @param differents - whole pieces of NOT desired form
	 * @param previouslyComposedRhythmValue - amount of time that was already
	 * @return
	 */
	private Comparator<ComposeBlock> getComposeBlockComparator( Optional<List<ComposeBlock>> etalons, Optional<List<ComposeBlock>> differents, double previouslyComposedRhythmValue ) {
		// TODO implementation
		return ( firstComposeBlock, secondComposeBlock ) -> {
			double firstEqualEtalons = getEqualityMetrics( firstComposeBlock, etalons );
			double firstEqualDifferents = getEqualityMetrics( firstComposeBlock, differents );
			double secondEqualEtalons = getEqualityMetrics( secondComposeBlock, etalons );
			double secondEqualDifferents = getEqualityMetrics( secondComposeBlock, differents );
			return (int) ( ( firstEqualEtalons - firstEqualDifferents ) - ( secondEqualEtalons - secondEqualDifferents ) );
		};
	}

	/**
	 * Returns average of equality metrics between composeBlock and list of composeBlocksToCompareWith
	 * @param composeBlock
	 * @param composeBlocksToCompareWith
	 * @return
	 */
	private double getEqualityMetrics( ComposeBlock composeBlock, Optional<List<ComposeBlock>> composeBlocksToCompareWith ) {
		double firstMetric = 0;
		if ( composeBlocksToCompareWith.isPresent() ) {
			OptionalDouble average = composeBlocksToCompareWith.get().stream()
					.mapToDouble( value -> formEqualityAnalyser.getEqualityMetric( value.getMelodyList(), composeBlock.getMelodyList() ) ).average();
			firstMetric += average.orElse( 0 );
		}
		return firstMetric;
	}

}
