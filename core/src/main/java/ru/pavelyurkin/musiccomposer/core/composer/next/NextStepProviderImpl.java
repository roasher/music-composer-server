package ru.pavelyurkin.musiccomposer.core.composer.next;

import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static ru.pavelyurkin.musiccomposer.core.utils.ModelUtils.getRelativeFormBlocks;
import static ru.pavelyurkin.musiccomposer.core.utils.Utils.isEquals;

public class NextStepProviderImpl extends FilteredNextStepProvider {

	private final EqualityMetricAnalyzer<List<Melody>> equalityMetricAnalyzer;

	public NextStepProviderImpl( EqualityMetricAnalyzer<List<Melody>> equalityMetricAnalyzer,
			ComposeStepFilter composeStepFilter ) {
		super( composeStepFilter );
		this.equalityMetricAnalyzer = equalityMetricAnalyzer;
	}

	@Override
	public Optional<CompositionStep> getNextBlockFiltered( List<CompositionStep> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> formCompositionSteps, Optional<Form> form ) {

		if ( form.isPresent() ) {

			List<MusicBlock> similarFormSteps = getRelativeFormBlocks( formCompositionSteps, form.get(), true );
			List<MusicBlock> differentFormSteps = getRelativeFormBlocks( formCompositionSteps, form.get(), false );

			Optional<CompositionStep> lastOfPossibles = blocksToChooseFrom.stream()
					.sorted(
							getComposeBlockComparator( similarFormSteps, differentFormSteps,
									previousCompositionSteps
											.stream()
											.map( CompositionStep::getTransposedBlock )
											.collect( Collectors.toList() ) ) )
					.reduce( ( composeStep1, composeStep2 ) -> composeStep2 );

			return lastOfPossibles;
		} else {
			return blocksToChooseFrom.stream().reduce( ( composeStep1, composeStep2 ) -> composeStep2 );
		}
	}

	/**
	 * Comparator that finds out which of compose blocks better suites to add in composition in terms of form
	 *
	 * @param similars                 - whole pieces of needed form
	 * @param differents               - whole pieces of NOT desired form
	 * @param previouslyComposedBlocks
	 * @return
	 */
	private Comparator<CompositionStep> getComposeBlockComparator( List<MusicBlock> similars, List<MusicBlock> differents,
			List<MusicBlock> previouslyComposedBlocks ) {
		return ( firstComposeBlock, secondComposeBlock ) -> {
			List<MusicBlock> firstBlocks = new ArrayList<>( previouslyComposedBlocks );
			firstBlocks.add( firstComposeBlock.getTransposedBlock() );
			MusicBlock firstBlockToCompare = ModelUtils.gatherBlocksWithTransposition( firstBlocks );
			double firstEqualEtalons = getEqualityMetrics( firstBlockToCompare, similars );
			double firstEqualDifferents = getEqualityMetrics( firstBlockToCompare, differents );

			List<MusicBlock> secondBlocks = new ArrayList<>( previouslyComposedBlocks );
			secondBlocks.add( secondComposeBlock.getTransposedBlock() );
			MusicBlock secondBlockToCompare = ModelUtils.gatherBlocksWithTransposition( secondBlocks );
			double secondEqualEtalons = getEqualityMetrics( secondBlockToCompare, similars );
			double secondEqualDifferents = getEqualityMetrics( secondBlockToCompare, differents );

			double combinedMetric = ( firstEqualEtalons - firstEqualDifferents ) - ( secondEqualEtalons - secondEqualDifferents );
			return !isEquals( combinedMetric, 0 ) ? ( combinedMetric > 0 ? 1 : -1 ) : 0;
		};
	}

	/**
	 * Returns average of equality metrics between block and list of blocksToCompareWith
	 * Actual comparison take place between block and trimmed blocksToCompareWith inside composition step.
	 * Blocks from composition steps trimmed according to block rhythm value
	 *
	 * @param block
	 * @param blocksToCompareWith
	 * @return
	 */
	public double getEqualityMetrics( MusicBlock block, List<MusicBlock> blocksToCompareWith ) {
		List<List<Melody>> trimmedCollectionOfMelodies = blocksToCompareWith.stream()
				.map( blockToCompareWith -> ModelUtils.trimToTime( blockToCompareWith.getMelodyList(), 0, block.getRhythmValue() ) )
				.collect( Collectors.toList() );
		OptionalDouble average = trimmedCollectionOfMelodies.stream().mapToDouble( value -> equalityMetricAnalyzer.getEqualityMetric( value, block.getMelodyList() ) )
				.average();
		return average.orElse( 0 );
	}

}
