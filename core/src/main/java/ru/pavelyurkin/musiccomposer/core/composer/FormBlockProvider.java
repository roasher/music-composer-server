package ru.pavelyurkin.musiccomposer.core.composer;

import javafx.util.Pair;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.equality.form.FormEquality;
import ru.pavelyurkin.musiccomposer.core.equality.form.RelativelyComparable;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getLast;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static ru.pavelyurkin.musiccomposer.core.utils.ModelUtils.getRelativeFormBlocks;

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
	public Optional<FormCompositionStep> getFormElement( double length, Lexicon lexicon, ComposeBlockProvider composeBlockProvider, Form form,
			List<FormCompositionStep> previousSteps ) {
		logger.info( "Composing form element : {}, length : {}", form.getValue(), length );
		List<CompositionStep> compositionSteps = composeSteps( length, lexicon, composeBlockProvider, previousSteps , Optional.of( form ) );
		return Optional.ofNullable( !compositionSteps.isEmpty() ? new FormCompositionStep( compositionSteps, form ) : null );
	}

	/**
	 * Returns composition step list filled with compose blocks that summary covers input length sharp
	 *
	 * @param length
	 * @param lexicon
	 * @param composeBlockProvider
	 * @param previousFormCompositionSteps
	 * @return
	 */
	public List<CompositionStep> composeSteps( double length, Lexicon lexicon, ComposeBlockProvider composeBlockProvider, List<CompositionStep> previousFormCompositionSteps ) {
		logger.info( "Composing element regardless form, length : {}", length );
		List<FormCompositionStep> formCompositionSteps = Arrays.asList( new FormCompositionStep( previousFormCompositionSteps, null ) );
		return composeSteps( length, lexicon, composeBlockProvider, formCompositionSteps, Optional.empty() );
	}

	/**
	 * Returns composition step list filled with compose blocks that summary covers input length sharp regarding similar and different form steps
	 *
	 * @param length
	 * @param lexicon
	 * @param composeBlockProvider
	 * @param previousFormCompositionSteps
	 * @param form
	 * @return
	 */
	private List<CompositionStep> composeSteps( double length, Lexicon lexicon, ComposeBlockProvider composeBlockProvider, List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form ) {

		List<CompositionStep> compositionSteps = new ArrayList<>();
		compositionSteps.add( getLast( getLast( previousFormCompositionSteps ).getCompositionSteps() ) );
		double currentLength = 0;

		for ( int step = 1; step < length / lexicon.getMinRhythmValue() + 1; step++ ) {
			logger.debug( "Current state {}", step );
			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			Optional<ComposeBlock> lastStepOriginComposeBlock = Optional.ofNullable( lastCompositionStep.getOriginComposeBlock() );
			Optional<ComposeBlock> nextComposeBlock = composeBlockProvider.getNextComposeBlock( length, lexicon, compositionSteps, previousFormCompositionSteps, form );

			if ( nextComposeBlock.isPresent() && currentLength + nextComposeBlock.get().getRhythmValue() <= length ) {
				Optional<MusicBlock> lastCompositionStepTransposedBlock = Optional.ofNullable( lastCompositionStep.getTransposedBlock() );
				int transposePitch = ModelUtils.getTransposePitch( lastCompositionStepTransposedBlock, nextComposeBlock.get().getMusicBlock() );
				CompositionStep nextStep = new CompositionStep( nextComposeBlock.get(), nextComposeBlock.get().getMusicBlock().transposeClone( transposePitch ) );
				compositionSteps.add( nextStep );
				currentLength += nextComposeBlock.get().getRhythmValue();
				if ( currentLength == length ) {
					if ( form.isPresent() ) {
						// FORM CHECK
						List<FormCompositionStep> skippedPrevSteps = previousFormCompositionSteps.stream().skip( 1 ).collect( Collectors.toList() );
						Pair<Boolean, Double> formCheckPassed = isFormCheckPassed(
								new MusicBlock( compositionSteps
										.stream()
										.skip( 1 )
										.map( CompositionStep::getTransposedBlock )
										.collect( Collectors.toList() ) ),
								getRelativeFormBlocks( skippedPrevSteps, form.get(), true ) ,
								getRelativeFormBlocks( skippedPrevSteps, form.get(), false )
						);
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
	 * Checks if new block can be used in composition in terms of form. Returning diff measure as well.
	 *
	 * @param block
	 * @param similarFormSteps
	 * @param differentFormSteps
	 * @return
	 */
	private Pair<Boolean, Double> isFormCheckPassed( MusicBlock block, List<MusicBlock> similarFormSteps, List<MusicBlock> differentFormSteps ) {
		double maxDiffMeasure = 0;
		// checking that new block is different to differentFormSteps
		for ( MusicBlock differentStep : differentFormSteps ) {
			Pair<RelativelyComparable.ResultOfComparison, Double> comparison = formEquality.isEqual( block.getMelodyList(), differentStep.getMelodyList() );
			if ( comparison.getKey() != RelativelyComparable.ResultOfComparison.DIFFERENT ) {
				return new Pair<>( false, comparison.getValue() );
			}
			if ( maxDiffMeasure < comparison.getValue() )
				maxDiffMeasure = comparison.getValue();
		}
		// checking that new block is similar to similarFormSteps
		for ( MusicBlock similarStep : similarFormSteps ) {
			Pair<RelativelyComparable.ResultOfComparison, Double> comparison = formEquality.isEqual( block.getMelodyList(), similarStep.getMelodyList() );
			if ( comparison.getKey() != RelativelyComparable.ResultOfComparison.EQUAL ) {
				return new Pair<>( false, comparison.getValue() );
			}
			if ( maxDiffMeasure < comparison.getValue() )
				maxDiffMeasure = comparison.getValue();
		}
		return new Pair<>( true, maxDiffMeasure );
	}
}
