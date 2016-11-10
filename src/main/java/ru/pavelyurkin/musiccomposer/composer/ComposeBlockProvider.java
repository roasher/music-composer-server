package ru.pavelyurkin.musiccomposer.composer;

import ru.pavelyurkin.musiccomposer.composer.first.FirstBlockProvider;
import ru.pavelyurkin.musiccomposer.composer.next.NextBlockProvider;
import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.model.Lexicon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by wish on 28.01.2016.
 */
@Component
public class ComposeBlockProvider {

	@Autowired
	@Qualifier( "simpleFirstBlockProvider" )
	private FirstBlockProvider firstBlockProvider;
	@Autowired
	@Qualifier( "formNextBlockProvider" )
	private NextBlockProvider nextBlockProvider;

	public ComposeBlockProvider() {}

	public ComposeBlockProvider( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider ) {
		this.firstBlockProvider = firstBlockProvider;
		this.nextBlockProvider = nextBlockProvider;
	}

	public Optional<ComposeBlock> getNextComposeBlock( Lexicon lexicon, List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, double length ) {
		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		return lastCompositionStep.getOriginComposeBlock() != null ?
				nextBlockProvider.getNextBlock( previousCompositionSteps, similarFormSteps, differentFormSteps, length ) :
				firstBlockProvider.getFirstBlock( lexicon, lastCompositionStep.getNextMusicBlockExclusions() );
	}

	public FirstBlockProvider getFirstBlockProvider() {
		return firstBlockProvider;
	}

	public void setFirstBlockProvider( FirstBlockProvider firstBlockProvider ) {
		this.firstBlockProvider = firstBlockProvider;
	}

	public NextBlockProvider getNextBlockProvider() {
		return nextBlockProvider;
	}

	public void setNextBlockProvider( NextBlockProvider nextBlockProvider ) {
		this.nextBlockProvider = nextBlockProvider;
	}
}
