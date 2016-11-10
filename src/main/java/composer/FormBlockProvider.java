package composer;

import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import decomposer.form.FormEquality;
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
	private FormEquality formEquality;

	/**
	 * Generates new form block considering previously generated blocks and it's form.
	 *
	 * @param form          - form, from part of witch new Block is going to be generated
	 * @param length
	 * @param previousSteps
	 * @param lexicon
	 * @return
	 */
	public Optional<FormCompositionStep> getFormElement( ComposeBlockProvider composeBlockProvider, Form form, double length, List<FormCompositionStep> previousSteps,
			Lexicon lexicon ) {
		logger.info( "Composing form element : {}, length : {}", form.getValue(), length );

		List<FormCompositionStep> similarFormSteps = previousSteps.stream().skip( 1 )
				.filter( formCompositionStep -> formCompositionStep.getForm() != null && formCompositionStep.getForm().equals( form ) ).collect( Collectors.toList() );
		List<FormCompositionStep> differentFormSteps = previousSteps.stream().skip( 1 )
				.filter( formCompositionStep -> formCompositionStep.getForm() != null && !formCompositionStep.getForm().equals( form ) ).collect( Collectors.toList() );

		List<CompositionStep> compositionSteps = composeSteps( composeBlockProvider, lexicon, length, similarFormSteps, differentFormSteps,
				fetchLastCompositionStep( previousSteps.get( previousSteps.size() - 1 ) ) );

		return Optional.ofNullable( !compositionSteps.isEmpty() ?
				new FormCompositionStep( compositionSteps.stream().map( CompositionStep::getOriginComposeBlock ).collect( Collectors.toList() ),
						compositionSteps.stream().map( CompositionStep::getTransposeComposeBlock ).collect( Collectors.toList() ), form ) :
				null );
	}

	/**
	 * Returns composition step list filled with compose blocks that summary covers input length sharp
	 *
	 * @param composeBlockProvider
	 * @param lexicon
	 * @param length
	 * @param similarFormSteps
	 * @param differentFormSteps
	 * @param preFirstCompositionStep
	 * @return TODO tests
	 */
	public List<CompositionStep> composeSteps( ComposeBlockProvider composeBlockProvider, Lexicon lexicon, double length, List<FormCompositionStep> similarFormSteps,
			List<FormCompositionStep> differentFormSteps, CompositionStep preFirstCompositionStep ) {

		List<CompositionStep> compositionSteps = new ArrayList<>();
		compositionSteps.add( preFirstCompositionStep );
		double currentLength = 0;

		for ( int step = 1; step < length / lexicon.getMinRhythmValue() + 1; step++ ) {
			logger.debug( "Current state {}", step );
			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			Optional<ComposeBlock> lastStepOriginComposeBlock = Optional.ofNullable( lastCompositionStep.getOriginComposeBlock() );
			Optional<ComposeBlock> nextComposeBlock = composeBlockProvider.getNextComposeBlock( lexicon, compositionSteps, similarFormSteps, differentFormSteps, length );

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
					boolean formCheckPassed = isFormCheckPassed( composeBlock, similarFormSteps, differentFormSteps );

					if ( !formCheckPassed ) {
						logger.debug( "ComposeBlock check failed in terms of form" );
						// ( step - 2 ) -> ( step - 1 ) -> ( step )
						CompositionStep preLastCompositionStep = compositionSteps.get( step - 1 );
						preLastCompositionStep.addNextExclusion( compositionSteps.get( step ).getOriginComposeBlock() );
						currentLength -= compositionSteps.get( step ).getOriginComposeBlock().getRhythmValue();
						compositionSteps.remove( step );
						step = step - 1;
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
					continue;
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
	 *
	 * @param composeBlock
	 * @param similarFormSteps
	 * @param differentFormSteps
	 * @return
	 */
	private boolean isFormCheckPassed( ComposeBlock composeBlock, List<FormCompositionStep> similarFormSteps, List<FormCompositionStep> differentFormSteps ) {
		// checking that new composeBlock is different to differentFormSteps
		for ( FormCompositionStep differentStep : differentFormSteps ) {
			ComposeBlock previousComposeBlock = new ComposeBlock( differentStep.getTrasposedComposeBlocks() );
			if ( formEquality.isEqual( composeBlock.getMelodyList(), previousComposeBlock.getMelodyList() ) ) {
				return false;
			}
		}
		// checking that new composeBlock is similar to similarFormSteps
		for ( FormCompositionStep similarStep : similarFormSteps ) {
			ComposeBlock previousComposeBlock = new ComposeBlock( similarStep.getTrasposedComposeBlocks() );
			if ( !formEquality.isEqual( composeBlock.getMelodyList(), previousComposeBlock.getMelodyList() ) ) {
				return false;
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
