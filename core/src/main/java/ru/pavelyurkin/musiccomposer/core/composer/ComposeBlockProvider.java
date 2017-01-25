package ru.pavelyurkin.musiccomposer.core.composer;

import ru.pavelyurkin.musiccomposer.core.composer.first.FirstBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.form.NextFormBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
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
	@Qualifier( "nextFormBlockProviderImpl" )
	private NextFormBlockProvider nextFormBlockProvider;
	@Autowired
	@Qualifier( "simpleNextBlockProvider" )
	private NextBlockProvider nextBlockProvider;

	public Optional<ComposeBlock> getNextComposeBlock( Lexicon lexicon, List<CompositionStep> previousCompositionSteps, double length ) {
		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		return lastCompositionStep.getOriginComposeBlock() != null ?
				nextBlockProvider.getNextBlock( previousCompositionSteps, length ) :
				firstBlockProvider.getFirstBlock( lexicon, lastCompositionStep.getNextMusicBlockExclusions() );
	}

	public Optional<ComposeBlock> getNextComposeBlock( Lexicon lexicon, List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, double length ) {
		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		return lastCompositionStep.getOriginComposeBlock() != null ?
				nextFormBlockProvider.getNextBlock( previousCompositionSteps, similarFormSteps, differentFormSteps, length ) :
				firstBlockProvider.getFirstBlock( lexicon, lastCompositionStep.getNextMusicBlockExclusions() );
	}

	public FirstBlockProvider getFirstBlockProvider() {
		return firstBlockProvider;
	}

	public void setFirstBlockProvider( FirstBlockProvider firstBlockProvider ) {
		this.firstBlockProvider = firstBlockProvider;
	}

	public NextFormBlockProvider getNextFormBlockProvider() {
		return nextFormBlockProvider;
	}

	public void setNextFormBlockProvider( NextFormBlockProvider nextFormBlockProvider ) {
		this.nextFormBlockProvider = nextFormBlockProvider;
	}

	public void setNextBlockProvider( NextBlockProvider nextBlockProvider ) {
		this.nextBlockProvider = nextBlockProvider;
	}
}
