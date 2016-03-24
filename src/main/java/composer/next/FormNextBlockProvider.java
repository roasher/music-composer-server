package composer.next;

import composer.next.filter.ComposeBlockFilter;
import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import decomposer.form.analyzer.FormEqualityAnalyser;
import model.ComposeBlock;
import model.melody.Melody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.ModelUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wish on 16.02.2016.
 */
@Component
public class FormNextBlockProvider implements NextBlockProvider {

	@Autowired
	private FormEqualityAnalyser formEqualityAnalyser;

	private ComposeBlockFilter composeBlockFilter;

	@Override
	public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, double length ) {

		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks() );
		possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions() );

		// User filters
		List<ComposeBlock> filteredBlocks =
				composeBlockFilter != null ? composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps ) : possibleNextComposeBlocks;

		double previouslyComposedRhythmValue = previousCompositionSteps.stream().skip( 1 ).mapToDouble( value -> value.getOriginComposeBlock().getRhythmValue() ).sum();

		Optional<ComposeBlock> lastOfPossibles = filteredBlocks.stream().filter( composeBlock -> previouslyComposedRhythmValue + composeBlock.getRhythmValue() <= length ).sorted(
				getComposeBlockComparator( similarFormSteps, differentFormSteps,
						previousCompositionSteps.stream().skip( 1 ).map( CompositionStep::getTransposeComposeBlock ).collect( Collectors.toList() ) ) )
				.reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );

		return lastOfPossibles;
	}

	/**
	 * Comparator that finds out which of compose blocks better suites to add in composition in terms of form
	 *
	 * @param similars                - whole pieces of needed form
	 * @param differents              - whole pieces of NOT desired form
	 * @param previouslyComposedBlocks
	 * @return
	 */
	private Comparator<ComposeBlock> getComposeBlockComparator( List<FormCompositionStep> similars, List<FormCompositionStep> differents, List<ComposeBlock> previouslyComposedBlocks ) {
		return ( firstComposeBlock, secondComposeBlock ) -> {
			List<ComposeBlock> firstComposeBlocks = new ArrayList<>( previouslyComposedBlocks );
			firstComposeBlocks.add( firstComposeBlock );
			double firstEqualEtalons = getEqualityMetrics( previouslyComposedBlocks.isEmpty() ? firstComposeBlock : new ComposeBlock( firstComposeBlocks ), similars );
			double firstEqualDifferents = getEqualityMetrics( previouslyComposedBlocks.isEmpty() ? firstComposeBlock : new ComposeBlock( firstComposeBlocks ), differents );

			List<ComposeBlock> secondComposeBlocks = new ArrayList<>( previouslyComposedBlocks );
			secondComposeBlocks.add( secondComposeBlock );
			double secondEqualEtalons = getEqualityMetrics( previouslyComposedBlocks.isEmpty() ? secondComposeBlock : new ComposeBlock( secondComposeBlocks ), similars );
			double secondEqualDifferents = getEqualityMetrics( previouslyComposedBlocks.isEmpty() ? secondComposeBlock : new ComposeBlock( secondComposeBlocks ), differents );

			double combinedMetric = ( firstEqualEtalons - firstEqualDifferents ) - ( secondEqualEtalons - secondEqualDifferents );
			return combinedMetric != 0 ? ( combinedMetric > 0 ? 1 : -1 ) : 0;
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
		List<List<Melody>> trimmedCollectionOfMelodies = stepsToCompareWith.stream().map( formCompositionStep -> ModelUtils
				.trimToTime( ( new ComposeBlock( formCompositionStep.getTrasposedComposeBlocks() ) ).getMelodyList(), 0, composeBlock.getRhythmValue() ) )
				.collect( Collectors.toList() );
		OptionalDouble average = trimmedCollectionOfMelodies.stream().mapToDouble( value -> formEqualityAnalyser.getAverageEqualityMetric( value, composeBlock.getMelodyList() ) )
				.average();
		return average.orElse( 0 );
	}

	public void setComposeBlockFilter( ComposeBlockFilter composeBlockFilter ) {
		this.composeBlockFilter = composeBlockFilter;
	}
}
