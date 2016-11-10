package ru.pavelyurkin.musiccomposer.composer.next;

import ru.pavelyurkin.musiccomposer.composer.next.filter.ComposeBlockFilter;
import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class SimpleNextBlockProvider implements NextBlockProvider {

	private ComposeBlockFilter composeBlockFilter;

	@Override
	public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, double length ) {

		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks() );
		possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions() );

		List<ComposeBlock> filteredBlocks =
				composeBlockFilter != null ? composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps ) : possibleNextComposeBlocks;
		Optional<ComposeBlock> lastOfPossibles = filteredBlocks.stream().reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );
		return lastOfPossibles;
	}

	public void setComposeBlockFilter( ComposeBlockFilter composeBlockFilter ) {
		this.composeBlockFilter = composeBlockFilter;
	}
}
