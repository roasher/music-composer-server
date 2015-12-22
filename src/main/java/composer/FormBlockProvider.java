package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import decomposer.form.analyzer.MusicBlockFormEqualityAnalyser;
import model.ComposeBlock;
import model.Lexicon;
import model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	 * @param form - form, from part of witch new Block is going to be generated
	 * @param length
	 * @param previousSteps
	 * @param lexicon
	 * @return
	 */
	public ComposeBlock getFormElement( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, Form form, double length, List<CompositionStep> previousSteps, Lexicon lexicon ) {
		logger.info( "Composing new form element : {}, length : {}", form.getValue(), length );
		CompositionStep last = previousSteps.isEmpty() ? null : previousSteps.get( previousSteps.size() - 1 );
		ComposeBlock composeBlock;
		if ( last != null ) {
			composeBlock = composeBlock( firstBlockProvider, nextBlockProvider, last.getComposeBlock(), lexicon, length );
			//			List<ComposeBlock> previouslyComposedBlocksHavingCertainForm = getComposeBlocksHavingCertainForm( previousSteps, form );
			//			if ( !previouslyComposedBlocksHavingCertainForm.isEmpty() ) {
			//				for ( ComposeBlock previouslyComposedBlock : previouslyComposedBlocksHavingCertainForm ) {
			//					logger.info( "Taking {} as for the base", previouslyComposedBlock );
			//				}
			//			} else {
			//				logger.info( "Composing first music block on the composition of certain form type: {}", form );
			//			}
		} else {
			logger.info( "Creating first Compose block according to form: {}", form );
			composeBlock = composeBlock( firstBlockProvider, nextBlockProvider, null, lexicon, length );
			return composeBlock;
		}
		return composeBlock;
	}

	public ComposeBlock composeBlock( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, ComposeBlock previousComposeBlock, Lexicon lexicon, double length ) {
		List<CompositionStep> compositionSteps = composeSteps( firstBlockProvider, nextBlockProvider, previousComposeBlock, lexicon, length );
		List<ComposeBlock> composeBlocks = new ArrayList<>();
		for ( CompositionStep compositionStep : compositionSteps ) {
			composeBlocks.add( compositionStep.getComposeBlock() );
		}
		return new ComposeBlock( composeBlocks );
	}

	/**
	 * Returns composition step list that consists with compose blocks that summary covers input length sharp
	 *
	 * @param previousComposeBlock - compose block prior to that are going to be composed
	 * @param lexicon
	 * @param length
	 * @return
	 */
	public List<CompositionStep> composeSteps( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, ComposeBlock previousComposeBlock, Lexicon lexicon,
			double length ) {
		List<CompositionStep> compositionSteps = new ArrayList<>();
		// adding pre first step
		compositionSteps.add( new CompositionStep( previousComposeBlock ) );
		double currentLength = 0;

		for ( int step = 0; step < length / lexicon.getMinRhythmValue(); step++ ) {
			logger.debug( "Current state {}", step );
			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );

			CompositionStep nextStep = lastCompositionStep.getComposeBlock() != null ?
					new CompositionStep( nextBlockProvider.getNextBlock( lexicon, lastCompositionStep.getComposeBlock(), lastCompositionStep.getNextMusicBlockExclusion() ) ) :
					new CompositionStep( firstBlockProvider.getNextBlock( lexicon, lastCompositionStep.getNextMusicBlockExclusion() ) );

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
		throw new RuntimeException( "Can't create new block" );
	}

	/**
	 * Retrieves composed music blocks having particular input form
	 *
	 * @param compositionSteps
	 * @param form
	 * @return
	 */
	private List<ComposeBlock> getComposeBlocksHavingCertainForm( List<CompositionStep> compositionSteps, Form form ) {
		List<ComposeBlock> composeBlocksHavingCertainForm = new ArrayList<>();
		for ( CompositionStep compositionStep : compositionSteps ) {
			if ( compositionStep.getForm().equals( form ) ) {
				composeBlocksHavingCertainForm.add( compositionStep.getComposeBlock() );
			}
		}
		return composeBlocksHavingCertainForm;
	}
}
