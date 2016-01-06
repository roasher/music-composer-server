package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import decomposer.form.analyzer.MusicBlockFormEqualityAnalyser;
import model.ComposeBlock;
import model.Lexicon;
import model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class provides form element
 * Created by pyurkin on 16.01.15.
 */
@Component
public class FormBlockProvider {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	@Autowired
	private MusicBlockProvider musicBlockProvider;

	@Autowired
	private MusicBlockFormEqualityAnalyser formEqualityAnalyser;

	/**
	 * Generates new form block considering previously generated blocks and it's form.
	 * @param form - form, from part of witch new Block is going to be generated
	 * @param length
	 * @param previousSteps
	 * @param lexicon
	 * @return
	 */
	public List<ComposeBlock> getFormElement( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, Form form, double length, List<FormCompositionStep> previousSteps, Lexicon lexicon ) {
		logger.info( "Composing new form element : {}, length : {}", form.getValue(), length );
		Optional<FormCompositionStep> last = Optional.ofNullable( previousSteps.isEmpty() ? null : previousSteps.get( previousSteps.size() - 1 ) );
		List<ComposeBlock> composeBlock = composeBlock( firstBlockProvider, nextBlockProvider, last, lexicon, length );
		return composeBlock;
	}

	public List<ComposeBlock> composeBlock( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, Optional<FormCompositionStep> previousCompositionStep, Lexicon lexicon, double length ) {
		List<CompositionStep> compositionSteps = composeSteps( firstBlockProvider, nextBlockProvider, previousCompositionStep, lexicon, length );
		if ( compositionSteps != null ) {
			List<ComposeBlock> composeBlocks = new ArrayList<>();
			for ( CompositionStep compositionStep : compositionSteps ) {
				composeBlocks.add( compositionStep.getComposeBlock() );
			}
			return composeBlocks;
		} else {
			return null;
		}
	}

	/**
	 * Returns composition step list that consists with compose blocks that summary covers input length sharp
	 *
	 * @param previousCompositionStep - compose block prior to that are going to be composed
	 * @param lexicon
	 * @param length
	 * @return
	 * TODO refactor
	 */
	public List<CompositionStep> composeSteps( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, Optional<FormCompositionStep> previousCompositionStep, Lexicon lexicon,
			double length ) {
		List<CompositionStep> compositionSteps = new ArrayList<>();

		Optional<CompositionStep> compositionStep = fetchLastCompositionStep( previousCompositionStep );
		compositionSteps.add( compositionStep.orElse( new CompositionStep( null ) ) );
		double currentLength = 0;

		for ( int step = 1; step < length / lexicon.getMinRhythmValue() + 1; step++ ) {
			logger.debug( "Current state {}", step );
			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );

			CompositionStep nextStep = lastCompositionStep.getComposeBlock() != null ?
					new CompositionStep( nextBlockProvider.getNextBlock( lexicon, lastCompositionStep.getComposeBlock(), lastCompositionStep.getNextMusicBlockExclusions() ) ) :
					new CompositionStep( firstBlockProvider.getNextBlock( lexicon, lastCompositionStep.getNextMusicBlockExclusions() ) );

			if ( nextStep.getComposeBlock() != null && currentLength + nextStep.getComposeBlock().getRhythmValue() <= length ) {
				currentLength += nextStep.getComposeBlock().getRhythmValue();
				compositionSteps.add( nextStep );
				if ( currentLength == length ) {
					// removing prefirst step
					compositionSteps.remove( 0 );
					return compositionSteps;
				}
			} else {
				if ( step != 0 ) {
					// there is no pre last step if we can't create second element
					if ( step != 1 ) {
						CompositionStep preLastCompositionStep = compositionSteps.get( step - 2 );
						preLastCompositionStep.addNextExclusion( lastCompositionStep.getComposeBlock() );
					}
					// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
					if ( compositionSteps.get( step - 1 ).getComposeBlock() != null ) {
						currentLength -= compositionSteps.get( step - 1 ).getComposeBlock().getRhythmValue();
					}
					compositionSteps.remove( step - 1 );
					step = step - 2;
					continue;
				} else {
					logger.warn( "There is no possible ways to compose new piece considering such parameters" );
					break;
				}
			}
		}
//		throw new RuntimeException( "Can't create new block" );
		return null;
	}

	/**
	 * Fetches CompositionStep from FormCompositionStep:
	 * last Compose Block of input = Compose Block for output
	 * List of first Compose Blocks of exclusions of input = output exclustion
	 * @param formCompositionStep
	 * @return
	 */
	public Optional<CompositionStep> fetchLastCompositionStep( Optional<FormCompositionStep> formCompositionStep ) {
		CompositionStep compositionStep = null;
		if ( formCompositionStep.isPresent() ) {
			List<ComposeBlock> exclusions = CollectionUtils.getListOfFirsts( formCompositionStep.get().getNextMusicBlockExclusions() );
			List<ComposeBlock> composeBlocks = formCompositionStep.get().getComposeBlocks();
			compositionStep = new CompositionStep( composeBlocks.get( composeBlocks.size() - 1 ) );
			compositionStep.setNextMusicBlockExclusions( exclusions );
		}
		return Optional.ofNullable( compositionStep );
	}
}
