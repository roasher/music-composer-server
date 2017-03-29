package ru.pavelyurkin.musiccomposer.core.composer;

import ru.pavelyurkin.musiccomposer.core.composer.first.FirstBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Iterables.getLast;

/**
 * Created by wish on 28.01.2016.
 */
@Component
public class ComposeBlockProvider {

	@Autowired
	@Qualifier( "simpleFirstBlockProvider" )
	private FirstBlockProvider firstBlockProvider;
	@Autowired
	@Qualifier( "nextStepProviderImpl" )
	private NextStepProvider nextStepProvider;

	public Optional<CompositionStep> getNextComposeBlock( double length, List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form ) {
		return nextStepProvider.getNext( previousCompositionSteps, previousFormCompositionSteps, form, length );
	}

	public Optional<CompositionStep> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions ) {
		return firstBlockProvider.getFirstBlock( lexicon, exclusions );
	}

	public FirstBlockProvider getFirstBlockProvider() {
		return firstBlockProvider;
	}

	public void setFirstBlockProvider( FirstBlockProvider firstBlockProvider ) {
		this.firstBlockProvider = firstBlockProvider;
	}

	public NextStepProvider getNextStepProvider() {
		return nextStepProvider;
	}

	public void setNextStepProvider( NextStepProvider nextStepProvider ) {
		this.nextStepProvider = nextStepProvider;
	}

}
