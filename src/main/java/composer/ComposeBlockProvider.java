package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import model.ComposeBlock;
import model.Lexicon;
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

	public Optional<ComposeBlock> getNextComposeBlock( Lexicon lexicon, List<CompositionStep> previousCompositionSteps,
			List<FormCompositionStep> similarFormSteps, List<FormCompositionStep> differentFormSteps ) {
		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		return lastCompositionStep.getOriginComposeBlock() != null ?
				nextBlockProvider.getNextBlock( previousCompositionSteps, similarFormSteps, differentFormSteps ) :
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
