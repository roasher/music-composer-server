package ru.pavelyurkin.musiccomposer.core.composer.next;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static ru.pavelyurkin.musiccomposer.core.utils.Utils.isEquals;

@Component
public class FormNextBlockProvider extends FilteredNextBlockProvider {

	@Autowired
	private EqualityMetricAnalyzer<List<Melody>> equalityMetricAnalyzer;

	@Override
	public Optional<ComposeBlock> getNextBlockFiltered( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, double length, List<ComposeBlock> blocksToChooseFrom ) {

		Optional<ComposeBlock> lastOfPossibles = blocksToChooseFrom.stream()
				.sorted(
				getComposeBlockComparator( similarFormSteps, differentFormSteps,
						previousCompositionSteps.stream().skip( 1 ).map( CompositionStep::getTransposeComposeBlock ).collect( Collectors.toList() ) ) )
				.reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );

		return lastOfPossibles;
	}

	/**
	 * Comparator that finds out which of compose blocks better suites to add in composition in terms of form
	 *
	 * @param similars                 - whole pieces of needed form
	 * @param differents               - whole pieces of NOT desired form
	 * @param previouslyComposedBlocks
	 * @return
	 */
	private Comparator<ComposeBlock> getComposeBlockComparator( List<FormCompositionStep> similars, List<FormCompositionStep> differents,
			List<ComposeBlock> previouslyComposedBlocks ) {
		return ( firstComposeBlock, secondComposeBlock ) -> {
			List<ComposeBlock> firstBlocks = new ArrayList<>( previouslyComposedBlocks );
			firstBlocks.add( firstComposeBlock );
			ComposeBlock firstBlockToCompare = ModelUtils.gatherBlocksWithTransposition( firstBlocks );
			double firstEqualEtalons = getEqualityMetrics( firstBlockToCompare, similars );
			double firstEqualDifferents = getEqualityMetrics( firstBlockToCompare, differents );

			List<ComposeBlock> secondBlocks = new ArrayList<>( previouslyComposedBlocks );
			secondBlocks.add( secondComposeBlock );
			ComposeBlock secondBlockToCompare = ModelUtils.gatherBlocksWithTransposition( secondBlocks );
			double secondEqualEtalons = getEqualityMetrics( secondBlockToCompare, similars );
			double secondEqualDifferents = getEqualityMetrics( secondBlockToCompare, differents );

			double combinedMetric = ( firstEqualEtalons - firstEqualDifferents ) - ( secondEqualEtalons - secondEqualDifferents );
			return !isEquals( combinedMetric, 0 ) ? ( combinedMetric > 0 ? 1 : -1 ) : 0;
		};
	}

	/**
	 * Returns average of equality metrics between composeBlock and list of stepsToCompareWith
	 * Actual comparison take place between composeBlock and trimmed blocks inside composition step.
	 * Blocks from composition steps trimmed according to composeBlock rhythm value
	 *
	 * @param composeBlock
	 * @param stepsToCompareWith
	 * @return
	 */
	public double getEqualityMetrics( ComposeBlock composeBlock, List<FormCompositionStep> stepsToCompareWith ) {
		List<List<Melody>> trimmedCollectionOfMelodies = stepsToCompareWith.stream()
				.map( formCompositionStep -> ModelUtils.trimToTime( ( new ComposeBlock( formCompositionStep.getTransposedComposeBlocks() ) ).getMelodyList(), 0, composeBlock.getRhythmValue() ) )
				.collect( Collectors.toList() );
		OptionalDouble average = trimmedCollectionOfMelodies.stream().mapToDouble( value -> equalityMetricAnalyzer.getEqualityMetric( value, composeBlock.getMelodyList() ) )
				.average();
		return average.orElse( 0 );
	}
}
