package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handles composition of new piece using lexicon
 * Created by pyurkin on 15.12.14.
 */
@Component
public class CompositionComposer {
	private Logger logger = LoggerFactory.getLogger( getClass() );

	@Autowired
	private FormBlockProvider formBlockProvider;

	/**
	 * Composing piece considering given lexicon, form pattern and composition compositionLength
	 *
	 * @param lexicon
	 * @param form
	 * @param compositionLength
	 * @return
	 */
	public Composition compose( FirstBlockProvider firstStepProvider, NextBlockProvider nextBlockProvider, Lexicon lexicon, String form, double compositionLength ) {
		List<CompositionStep> compositionSteps = composeSteps( firstStepProvider, nextBlockProvider, lexicon, form, compositionLength );
		List<ComposeBlock> composeBlocks = new ArrayList<>();
		for ( CompositionStep compositionStep : compositionSteps ) {
			composeBlocks.add( compositionStep.getComposeBlock() );
		}
		return Utils.gatherComposition( composeBlocks );
	}

	/**
	 * Main composing function.
	 * Assuming we are on k-th step of composing.
	 * Composing k+1 block according given form.
	 * If it is impossible, than recomposing k-th block.
	 * If it is impossible, going back to k-1 and so on.
	 *
	 * @param lexicon
	 * @param form
	 * @param compositionLength
	 * @return
	 */
	public List<CompositionStep> composeSteps( FirstBlockProvider firstStepProvider, NextBlockProvider nextBlockProvider, Lexicon lexicon, String form, double compositionLength ) {
		List<CompositionStep> compositionSteps = new ArrayList<>();
		for ( int formElementNumber = 0; formElementNumber < form.length(); formElementNumber++ ) {

			CompositionStep lastCompositionStep = formElementNumber != 0 ? compositionSteps.get( compositionSteps.size() - 1 ) : null;
			double stepLength = compositionLength / form.length();
			CompositionStep nextStep = composeNext( firstStepProvider, nextBlockProvider, new Form( form.charAt( formElementNumber ) ), stepLength, compositionSteps, lexicon );

			if ( nextStep.getComposeBlock() == null ) {
				if ( formElementNumber != 0 ) {
					// there is no pre last step if we can't create second element
					if ( formElementNumber != 1 ) {
						CompositionStep preLastCompositionStep = compositionSteps.get( formElementNumber - 2 );
						preLastCompositionStep.addNextExclusion( lastCompositionStep.getComposeBlock() );
					}
					// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
					compositionSteps.remove( formElementNumber - 1 );
					formElementNumber = formElementNumber - 2;
					continue;
				} else {
					logger.warn( "There is no possible ways to compose new piece considering such parameters" );
					break;
				}
			} else {
				compositionSteps.add( nextStep );
			}
		}
		return compositionSteps;
	}

	public CompositionStep composeNext( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, Form form, double length, List<CompositionStep> previousSteps,
			Lexicon lexicon ) {
		ComposeBlock musicBlock = formBlockProvider.getFormElement( firstBlockProvider, nextBlockProvider, form, length, previousSteps, lexicon );
		return new CompositionStep( musicBlock, form );
	}
}