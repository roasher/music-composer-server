package composer;

import decomposer.form.analyzer.MusicBlockFormEqualityAnalyser;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
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
	 * @param form - form, from part of witch are going to be generated new Music Block
	 * @param lexicon - Music Block's database
	 * @return
	 */
	public ComposeBlock getFormElement( Form form, double length, List<CompositionStep> previousSteps, Lexicon lexicon ) {
		logger.info( "Composing new form element : {}, length : {}", form.getValue(), length );
		CompositionStep last = previousSteps.isEmpty() ? null : previousSteps.get( previousSteps.size() - 1 );
		if ( last != null ) {
			logger.info( "Creating first Compose block according to form: {}", form );
			ComposeBlock composeBlock = lexicon.getRandomFirst();
			return composeBlock;
		} else {
			List<ComposeBlock> previouslyComposedBlocksHavingCertainForm = getComposeBlocksHavingCertainForm( previousSteps, form );
			if ( !previouslyComposedBlocksHavingCertainForm.isEmpty() ) {
				for ( ComposeBlock previouslyComposedBlock : previouslyComposedBlocksHavingCertainForm ) {
					logger.info( "Taking {} as for the base", previouslyComposedBlock );
				}
			} else {
				logger.info( "Composing first music block on the composition of certain form type: {}", form );
			}
		}

	}

	/**
	 * Composes first form block of the composition
	 * @param length
	 * @param lexicon
	 * @return
	 */
	public ComposeBlock composeFirstBlock( double length, Lexicon lexicon ) {
		List<CompositionStep> compositionSteps = composeSteps( length, lexicon );
		List<ComposeBlock> composeBlocks = new ArrayList<>(  );
		for ( CompositionStep compositionStep : compositionSteps ) {
			composeBlocks.add( compositionStep.getComposeBlock() );
		}
		return Utils.gatherComposition( composeBlocks );
	}

	public List<CompositionStep> composeSteps( double length, Lexicon lexicon ) {
		List<CompositionStep> compositionSteps = new ArrayList<>(  );
		compositionSteps.add( new CompositionStep(  ) );
		double currentLength = 0;

		for ( int step = 0; step < length / lexicon.getMinRhythmValue(); step++ ) {

			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			CompositionStep nextStep = lastCompositionStep.getComposeBlock() != null ?
					new CompositionStep( lexicon.getRandomNext( lastCompositionStep.getComposeBlock(), lastCompositionStep.getNextMusicBlockExclusion() ) ) :
					new CompositionStep( lexicon.getRandomFirst( lastCompositionStep.getNextMusicBlockExclusion() ) );
			currentLength += nextStep.getComposeBlock().getRhythmValue();

			if ( nextStep.getComposeBlock() != null || currentLength <= length ) {
				compositionSteps.add( nextStep );
			} else {
				if ( step != 0 ) {
					// there is no pre last step if we can't create second element
					if ( step != 1 ) {
						CompositionStep preLastCompositionStep = compositionSteps.get( step - 2 );
						preLastCompositionStep.addNextExclusion( lastCompositionStep.getComposeBlock() );
					}
					// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
					compositionSteps.remove( step - 1 );
					step = step - 2;
					continue;
				} else {
					logger.warn( "There is no possible ways to compose new piece considering such parameters" );
					break;
				}
			}
		}
		return compositionSteps;
	}

	/**
	 * Retrieves composed music blocks having particular input form
	 * @param compositionSteps
	 * @param form
	 * @return
	 */
	private List<ComposeBlock> getComposeBlocksHavingCertainForm( List<CompositionStep> compositionSteps, Form form ) {
		List<ComposeBlock> composeBlocksHavingCertainForm = new ArrayList<>(  );
		for ( CompositionStep compositionStep : compositionSteps ) {
			if ( compositionStep.getForm().equals( form ) ) {
				composeBlocksHavingCertainForm.add( compositionStep.getComposeBlock() );
			}
		}
		return composeBlocksHavingCertainForm;
	}
}
