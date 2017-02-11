package ru.pavelyurkin.musiccomposer.core.composer;

import ru.pavelyurkin.musiccomposer.core.composer.first.FirstBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextBlockProvider;
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
	@Qualifier( "nextBlockProviderImpl" )
	private NextBlockProvider nextBlockProvider;

	public Optional<ComposeBlock> getNextComposeBlock( double length, List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form ) {
		return nextBlockProvider.getNextBlock( previousCompositionSteps, previousFormCompositionSteps, form, length );
	}

	public Optional<ComposeBlock> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions ) {
		return firstBlockProvider.getFirstBlock( lexicon, exclusions );
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
