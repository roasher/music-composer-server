package ru.pavelyurkin.musiccomposer.core.composer;

import javafx.util.Pair;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.equality.form.FormEquality;
import ru.pavelyurkin.musiccomposer.core.equality.form.RelativelyComparable;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.utils.CollectionUtils;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.util.AssertionErrors.assertTrue;

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
	 * @param length
	 * @param lexicon
	 * @param form          - form, from part of witch new Block is going to be generated
	 * @param previousSteps
	 * @return
	 */
	public Optional<FormCompositionStep> getFormElement( double length, Lexicon lexicon, ComposeBlockProvider composeBlockProvider, Form form, List<FormCompositionStep> previousSteps ) {
		logger.info( "Composing form element : {}, length : {}", form.getValue(), length );

		List<FormCompositionStep> similarFormSteps = previousSteps.stream().skip( 1 )
				.filter( formCompositionStep -> formCompositionStep.getForm() != null && formCompositionStep.getForm().equals( form ) ).collect( Collectors.toList() );
		List<FormCompositionStep> differentFormSteps = previousSteps.stream().skip( 1 )
				.filter( formCompositionStep -> formCompositionStep.getForm() != null && !formCompositionStep.getForm().equals( form ) ).collect( Collectors.toList() );

		List<CompositionStep> compositionSteps = composeSteps( length, lexicon, composeBlockProvider, fetchLastCompositionStep( previousSteps.get( previousSteps.size() - 1 ) ),
				similarFormSteps, differentFormSteps, true );

		return Optional.ofNullable( !compositionSteps.isEmpty() ?
				new FormCompositionStep( compositionSteps.stream().map( CompositionStep::getOriginComposeBlock ).collect( Collectors.toList() ),
						compositionSteps.stream().map( CompositionStep::getTransposeComposeBlock ).collect( Collectors.toList() ), form ) :
				null );
	}

	/**
	 * Returns composition step list filled with compose blocks that summary covers input length sharp
	 *
	 * @param length
	 * @param lexicon
	 * @param composeBlockProvider
	 * @param preFirstCompositionStep
	 * @return
	 */
	public List<CompositionStep> composeSteps( double length, Lexicon lexicon, ComposeBlockProvider composeBlockProvider, CompositionStep preFirstCompositionStep ) {
		return composeSteps( length, lexicon, composeBlockProvider, preFirstCompositionStep, null, null, false );
	}

	/**
	 * Returns composition step list filled with compose blocks that summary covers input length sharp regarding similar and different form steps
	 *
	 * @param length
	 * @param lexicon
	 * @param composeBlockProvider
	 * @param preFirstCompositionStep
	 * @param similarFormSteps
	 * @param differentFormSteps
	 * @param composingRegardingForm
	 * @return TODO tests
	 */
	private List<CompositionStep> composeSteps( double length, Lexicon lexicon, ComposeBlockProvider composeBlockProvider, CompositionStep preFirstCompositionStep,
			List<FormCompositionStep> similarFormSteps, List<FormCompositionStep> differentFormSteps, boolean composingRegardingForm ) {

		List<CompositionStep> compositionSteps = new ArrayList<>();
		compositionSteps.add( preFirstCompositionStep );
		double currentLength = 0;

		for ( int step = 1; step < length / lexicon.getMinRhythmValue() + 1; step++ ) {
			logger.debug( "Current state {}", step );
			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			Optional<ComposeBlock> lastStepOriginComposeBlock = Optional.ofNullable( lastCompositionStep.getOriginComposeBlock() );
			Optional<ComposeBlock> nextComposeBlock = composingRegardingForm ?
					composeBlockProvider.getNextComposeBlock( lexicon, compositionSteps, similarFormSteps, differentFormSteps, length ) :
					composeBlockProvider.getNextComposeBlock( lexicon, compositionSteps, length );

			if ( nextComposeBlock.isPresent() && currentLength + nextComposeBlock.get().getRhythmValue() <= length ) {
				Optional<ComposeBlock> lastCompositionStepTransposeComposeBlock = Optional.ofNullable( lastCompositionStep.getTransposeComposeBlock() );
				int transposePitch = ModelUtils.getTransposePitch( lastCompositionStepTransposeComposeBlock, nextComposeBlock.get() );
				CompositionStep nextStep = new CompositionStep( nextComposeBlock.get(), nextComposeBlock.get().transposeClone( transposePitch ) );
				compositionSteps.add( nextStep );
				currentLength += nextComposeBlock.get().getRhythmValue();
				if ( currentLength == length ) {
					if ( composingRegardingForm ) {
						// FORM CHECK
						List<ComposeBlock> transposedComposeBlocks = compositionSteps.stream().skip( 1 ).map( CompositionStep::getTransposeComposeBlock ).collect( Collectors.toList() );
						ComposeBlock composeBlock = new ComposeBlock( transposedComposeBlocks );
						Pair<Boolean, Double> formCheckPassed = isFormCheckPassed( composeBlock, similarFormSteps, differentFormSteps );
						if ( !formCheckPassed.getKey() ) {
							int stepToRevert = getStepToRevert( step, formCheckPassed.getValue() );
							logger.debug( "ComposeBlock check failed in terms of form. Reverting to step {}", stepToRevert );
							// ( stepToRevert ) -> ... -> ( step - 1 ) -> ( step )
							compositionSteps.get( stepToRevert ).addNextExclusion( compositionSteps.get( stepToRevert + 1 ).getOriginComposeBlock() );
							ListIterator<CompositionStep> iterator = compositionSteps.listIterator( compositionSteps.size() );
							while ( iterator.previousIndex() != stepToRevert ) {
								CompositionStep previousStep = iterator.previous();
								currentLength -= previousStep.getOriginComposeBlock().getRhythmValue();
								iterator.remove();
							}
							step = stepToRevert;
							continue;
						}
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
	 * Should return step to revert - so new composition will be handled to next to this step.
	 * Step calculated according to step count and measure that shows difference with what form comparison was failed.
	 * Step should be no greater then step-2. There is no point returning to step-1 cause starting to compose next = last block (last step) block provider will give best
	 * variant in terms of form.
	 *
	 * @param step
	 * @param diffMeasure
	 * @return
	 */
	private int getStepToRevert( int step, Double diffMeasure ) {
		assertTrue( "Diff measure " + diffMeasure + " < 0", diffMeasure >= 0 );
		int calculatedStepToReturn = ( int ) ( step * diffMeasure );
		if ( calculatedStepToReturn == 0 )
			return 1;
		if ( calculatedStepToReturn > step - 2 )
			return step - 2;
		return calculatedStepToReturn;
	}

	/**
	 * Checks if new compose block can be used in composition in terms of form. Returning diff measure as well.
	 *
	 * @param composeBlock
	 * @param similarFormSteps
	 * @param differentFormSteps
	 * @return
	 */
	private Pair<Boolean, Double> isFormCheckPassed( ComposeBlock composeBlock, List<FormCompositionStep> similarFormSteps, List<FormCompositionStep> differentFormSteps ) {
		double maxDiffMeasure = 0;
		// checking that new composeBlock is different to differentFormSteps
		for ( FormCompositionStep differentStep : differentFormSteps ) {
			ComposeBlock previousComposeBlock = new ComposeBlock( differentStep.getTransposedComposeBlocks() );
			Pair<RelativelyComparable.ResultOfComparison, Double> comparison = formEquality.isEqual( composeBlock.getMelodyList(), previousComposeBlock.getMelodyList() );
			if ( comparison.getKey() != RelativelyComparable.ResultOfComparison.DIFFERENT ) {
				return new Pair<>( false, comparison.getValue() );
			}
			if ( maxDiffMeasure < comparison.getValue() )
				maxDiffMeasure = comparison.getValue();
		}
		// checking that new composeBlock is similar to similarFormSteps
		for ( FormCompositionStep similarStep : similarFormSteps ) {
			ComposeBlock previousComposeBlock = new ComposeBlock( similarStep.getTransposedComposeBlocks() );
			Pair<RelativelyComparable.ResultOfComparison, Double> comparison = formEquality.isEqual( composeBlock.getMelodyList(), previousComposeBlock.getMelodyList() );
			if ( comparison.getKey() != RelativelyComparable.ResultOfComparison.EQUAL ) {
				return new Pair<>( false, comparison.getValue() );
			}
			if ( maxDiffMeasure < comparison.getValue() )
				maxDiffMeasure = comparison.getValue();
		}
		return new Pair<>( true, maxDiffMeasure );
	}

	/**
	 * Fetches CompositionStep from FormCompositionStep:
	 * last Compose Block of input = Compose Block for output
	 * List of first Compose Blocks of exclusions of input = output exclusion
	 *
	 * @param formCompositionStep
	 * @return
	 */
	private CompositionStep fetchLastCompositionStep( FormCompositionStep formCompositionStep ) {
		List<ComposeBlock> exclusions = CollectionUtils.getListOfFirsts( formCompositionStep.getNextMusicBlockExclusions() );
		List<ComposeBlock> originComposeBlocks = formCompositionStep.getOriginComposeBlocks();
		List<ComposeBlock> trasposedComposeBlocks = formCompositionStep.getTransposedComposeBlocks();
		CompositionStep compositionStep;
		if ( originComposeBlocks != null ) {
			compositionStep = new CompositionStep( originComposeBlocks.get( originComposeBlocks.size() - 1 ), trasposedComposeBlocks.get( trasposedComposeBlocks.size() - 1 ) );
		} else {
			compositionStep = new CompositionStep( null, null );
		}
		compositionStep.setNextMusicBlockExclusions( exclusions );
		return compositionStep;
	}

}
