package ru.pavelyurkin.musiccomposer.core.composer.next;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getLast;

/**
 * Parent class getting Next Block
 */
public abstract class NextBlockProvider {

	public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form,
			double length ) {

		CompositionStep lastCompositionStep = getLast( previousCompositionSteps );
		List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks() );
		possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions() );

		// Leaving all blocks adding whose will keep whole piece in range
		double previouslyComposedRhythmValue = previousCompositionSteps.stream().skip( 1 ).mapToDouble( value -> value.getOriginComposeBlock().getRhythmValue() ).sum();
		List<ComposeBlock> blocksToChooseFrom = possibleNextComposeBlocks.stream().filter( composeBlock -> previouslyComposedRhythmValue + composeBlock.getRhythmValue() <= length )
				.collect( Collectors.toList() );

		return getNextBlock( blocksToChooseFrom, previousCompositionSteps, previousFormCompositionSteps, form, length );

	}

	public abstract Optional<ComposeBlock> getNextBlock( List<ComposeBlock> blocksToChooseFrom, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> formCompositionSteps, Optional<Form> form, double length );
}
