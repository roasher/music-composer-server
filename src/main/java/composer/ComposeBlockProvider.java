package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.CompositionStep;
import model.ComposeBlock;
import model.Lexicon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by wish on 28.01.2016.
 */
@Component
public class ComposeBlockProvider {

	@Autowired
	private FirstBlockProvider firstBlockProvider;
	@Autowired
	private NextBlockProvider nextBlockProvider;

	public ComposeBlockProvider() {}

	public ComposeBlockProvider( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider ) {
		this.firstBlockProvider = firstBlockProvider;
		this.nextBlockProvider = nextBlockProvider;
	}

	public Optional<ComposeBlock> getNextComposeBlock( Lexicon lexicon, List<CompositionStep> previousCompositionSteps ) {
		CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
		return lastCompositionStep.getComposeBlock() != null ?
				nextBlockProvider.getNextBlock( lexicon, previousCompositionSteps ) :
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
