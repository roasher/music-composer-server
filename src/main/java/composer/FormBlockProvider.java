package composer;

import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import decomposer.form.analyzer.FormEqualityAnalyser;
import model.ComposeBlock;
import model.Lexicon;
import model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static utils.ModelUtils.getTransposePitch;

/**
 * Class provides form element
 * Created by pyurkin on 16.01.15.
 */
@Component
public class FormBlockProvider {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	@Autowired
	private FormEqualityAnalyser formEqualityAnalyser;

	/**
	 * Generates new form block considering previously generated blocks and it's form.
	 *
	 * @param form          - form, from part of witch new Block is going to be generated
	 * @param length
	 * @param previousSteps
	 * @param lexicon
	 * @return
	 */
	public Optional<FormCompositionStep> getFormElement( ComposeBlockProvider composeBlockProvider, Form form, double length,
			List<FormCompositionStep> previousSteps, Lexicon lexicon ) {
		logger.info( "Composing form element : {}, length : {}", form.getValue(), length );
		List<CompositionStep> compositionSteps = composeSteps( composeBlockProvider, previousSteps, lexicon, length, form );
		return Optional.ofNullable( !compositionSteps.isEmpty() ?
				new FormCompositionStep( compositionSteps.stream().map( CompositionStep::getOriginComposeBlock ).collect( Collectors.toList() ),
						compositionSteps.stream().map( CompositionStep::getTransposeComposeBlock ).collect( Collectors.toList() ), form ) :
				null );
	}

	/**
	 * Returns composition step list filled with compose blocks that summary covers input length sharp
	 *
	 * @param composeBlockProvider
	 * @param previousSteps
	 * @param lexicon
	 * @param length
	 * @param form
	 * @return
	 *
	 * TODO tests
	 */
	public List<CompositionStep> composeSteps( ComposeBlockProvider composeBlockProvider, List<FormCompositionStep> previousSteps, Lexicon lexicon,
			double length, Form form ) {
		List<CompositionStep> compositionSteps = new ArrayList<>();

		CompositionStep compositionStep = fetchLastCompositionStep( previousSteps.get( previousSteps.size() - 1 ) );
		compositionSteps.add( compositionStep );
		double currentLength = 0;
		for ( int step = 1; step < length / lexicon.getMinRhythmValue() + 1; step++ ) {
			logger.debug( "Current state {}", step );
			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			Optional<ComposeBlock> lastStepOriginComposeBlock = Optional.ofNullable( lastCompositionStep.getOriginComposeBlock() );
			Optional<ComposeBlock> nextComposeBlock = composeBlockProvider.getNextComposeBlock( lexicon, compositionSteps );

			if ( nextComposeBlock.isPresent() && currentLength + nextComposeBlock.get().getRhythmValue() <= length ) {
				Optional<ComposeBlock> lastCompositionStepTransposeComposeBlock = Optional.ofNullable( lastCompositionStep.getTransposeComposeBlock() );
				int transposePitch = getTransposePitch( lastCompositionStepTransposeComposeBlock, nextComposeBlock.get() );
				CompositionStep nextStep = new CompositionStep( nextComposeBlock.get(), nextComposeBlock.get().transposeClone( transposePitch ) );
				compositionSteps.add( nextStep );
				currentLength += nextComposeBlock.get().getRhythmValue();
				if ( currentLength == length ) {
					// FORM CHECK
					List<ComposeBlock> transposedComposeBlocks = compositionSteps.stream().skip( 1 ).map( CompositionStep::getTransposeComposeBlock )
							.collect( Collectors.toList() );
					ComposeBlock composeBlock = new ComposeBlock( transposedComposeBlocks );
					boolean formCheckPassed = isFormCheckPassed( previousSteps, composeBlock, form );

					if ( !formCheckPassed ) {
						logger.debug( "ComposeBlock check failed in terms of form" );
						CompositionStep preLastCompositionStep = compositionSteps.get( step - 2 );
						preLastCompositionStep.addNextExclusion( lastStepOriginComposeBlock.get() );
						// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
						if ( compositionSteps.get( step - 1 ).getOriginComposeBlock() != null ) {
							currentLength -= compositionSteps.get( step - 1 ).getOriginComposeBlock().getRhythmValue();
						}
						compositionSteps.remove( step - 1 );
						step = step - 2;
						continue;
					}

					// removing prefirst step
					compositionSteps.remove( 0 );
					return compositionSteps;
				}
			} else {
				if ( step != 1 ) {
					CompositionStep preLastCompositionStep = compositionSteps.get( step - 2 );
					preLastCompositionStep.addNextExclusion( lastStepOriginComposeBlock.get() );
					// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
					if ( compositionSteps.get( step - 1 ).getOriginComposeBlock() != null ) {
						currentLength -= compositionSteps.get( step - 1 ).getOriginComposeBlock().getRhythmValue();
					}
					compositionSteps.remove( step - 1 );
					step = step - 2;
				} else {
					logger.warn( "There is no possible ways to compose new piece considering such parameters" );
					break;
				}
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Checks if new compose block can be used in composition in terms of form
	 * @param previousSteps
	 * @param composeBlock
	 * @param form
	 * @return
	 */
	private boolean isFormCheckPassed( List<FormCompositionStep> previousSteps, ComposeBlock composeBlock, Form form ) {
		// Skipping first step because it is a mock out of form comparison
		List<FormCompositionStep> previousCompositionSteps = previousSteps.stream().skip( 1 ).collect( Collectors.toList());
		List<FormCompositionStep> previouslyComposedStepsOfThisForm = previousCompositionSteps.stream()
				.filter( formCompositionStep -> formCompositionStep.getForm() != null && formCompositionStep.getForm().equals( form ) )
				.collect( Collectors.toList() );
		if ( previouslyComposedStepsOfThisForm.isEmpty() ) {
			// checking that new composed piece is NOT form equals to any of previously composed
			for ( FormCompositionStep previousStep : previousCompositionSteps ) {
				ComposeBlock previousComposeBlock = new ComposeBlock( previousStep.getTrasposedComposeBlocks() );
				if ( formEqualityAnalyser.isEqual( composeBlock.getMelodyList(), previousComposeBlock.getMelodyList() ) ) {
					return false;
				}
			}
		} else {
			// checking that all previouslyComposedStepsOfThisForm IS form equals to new composed block
			for ( FormCompositionStep formCompositionStep : previouslyComposedStepsOfThisForm ) {
				ComposeBlock previousComposeBlock = new ComposeBlock( formCompositionStep.getTrasposedComposeBlocks() );
				if ( !formEqualityAnalyser.isEqual( composeBlock.getMelodyList(), previousComposeBlock.getMelodyList() ) ) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Fetches CompositionStep from FormCompositionStep:
	 * last Compose Block of input = Compose Block for output
	 * List of first Compose Blocks of exclusions of input = output exclusion
	 *
	 * @param formCompositionStep
	 * @return
	 */
	public CompositionStep fetchLastCompositionStep( FormCompositionStep formCompositionStep ) {
		List<ComposeBlock> exclusions = CollectionUtils.getListOfFirsts( formCompositionStep.getNextMusicBlockExclusions() );
		List<ComposeBlock> originComposeBlocks = formCompositionStep.getOriginComposeBlocks();
		List<ComposeBlock> trasposedComposeBlocks = formCompositionStep.getTrasposedComposeBlocks();
		CompositionStep compositionStep;
		if ( originComposeBlocks != null ) {
			compositionStep = new CompositionStep( originComposeBlocks.get( originComposeBlocks.size() - 1 ),
					trasposedComposeBlocks.get( trasposedComposeBlocks.size() - 1 ) );
		} else {
			compositionStep = new CompositionStep( null, null );
		}
		compositionStep.setNextMusicBlockExclusions( exclusions );
		return compositionStep;
	}

}
