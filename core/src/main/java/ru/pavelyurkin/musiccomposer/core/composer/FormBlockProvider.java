package ru.pavelyurkin.musiccomposer.core.composer;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.equality.form.FormEquality;
import ru.pavelyurkin.musiccomposer.core.equality.form.RelativelyComparable;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

import java.util.*;
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
	public Optional<FormCompositionStep> getFormElement( double length, Lexicon lexicon, ComposeStepProvider composeStepProvider, Form form,
			List<FormCompositionStep> previousSteps ) {
		logger.info( "Composing form element : {}, length : {}", form.getValue(), length );
		List<CompositionStep> compositionSteps = composeSteps( length, lexicon, composeStepProvider, previousSteps , Optional.of( form ) );
		return Optional.ofNullable( !compositionSteps.isEmpty() ? new FormCompositionStep( compositionSteps, form ) : null );
	}

	/**
	 * Returns composition step list filled with compose blocks that summary covers input length sharp
	 *
	 * @param length
	 * @param lexicon
	 * @param composeStepProvider
	 * @param previousFormCompositionSteps
	 * @return
	 */
	public List<CompositionStep> composeSteps( double length, Lexicon lexicon, ComposeStepProvider composeStepProvider, List<CompositionStep> previousFormCompositionSteps ) {
		logger.info( "Composing element regardless form, length : {}", length );
		List<FormCompositionStep> formCompositionSteps = !previousFormCompositionSteps.isEmpty() ?
				Collections.singletonList( new FormCompositionStep( previousFormCompositionSteps, null ) ) :
				Collections.emptyList();
		return composeSteps( length, lexicon, composeStepProvider, formCompositionSteps, Optional.empty() );
	}

	/**
	 * Returns composition step list filled with compose blocks that summary covers input length sharp regarding previous form steps
	 *
	 * @param length
	 * @param lexicon
	 * @param composeStepProvider
	 * @param previousFormCompositionSteps
	 * @param form
	 * @return
	 */
	private List<CompositionStep> composeSteps( double length, Lexicon lexicon, ComposeStepProvider composeStepProvider
			, List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form ) {

		CompositionStep prefirstCompositionStep = previousFormCompositionSteps.isEmpty() ?
				CompositionStep.getEmptyCompositionStep() :
				getLast( getLast( previousFormCompositionSteps ).getCompositionSteps() );
		List<CompositionStep> compositionSteps = new ArrayList<>();
		double currentLength = 0;

		for ( int step = 0; step < length / lexicon.getMinRhythmValue(); step++ ) {
			logger.debug( "Current state {}", step );
			CompositionStep lastCompositionStep = !compositionSteps.isEmpty() ? getLast( compositionSteps ) : prefirstCompositionStep;

			Optional<CompositionStep> nextCompositionStep = step != 0 || !previousFormCompositionSteps.isEmpty() ?
					composeStepProvider.getNext( length, compositionSteps, previousFormCompositionSteps, form ) :
					composeStepProvider.getFirst( lexicon, prefirstCompositionStep.getNextMusicBlockExclusions() );

			if ( nextCompositionStep.isPresent() && currentLength + nextCompositionStep.get().getTransposedBlock().getRhythmValue() <= length ) {
				compositionSteps.add( nextCompositionStep.get() );
				logger.debug( "Composed {}", nextCompositionStep.get().getTransposedBlock().toString() );
				currentLength += nextCompositionStep.get().getTransposedBlock().getRhythmValue();
				if ( currentLength == length ) {
					if ( form.isPresent() ) {
						// FORM CHECK
						Pair<Boolean, Double> formCheckPassed = isFormCheckPassed(
								new MusicBlock( compositionSteps
										.stream()
										.map( CompositionStep::getTransposedBlock )
										.collect( Collectors.toList() ) ),
								getRelativeFormBlocks( previousFormCompositionSteps, form.get(), true ) ,
								getRelativeFormBlocks( previousFormCompositionSteps, form.get(), false )
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
					return compositionSteps;
				}
			} else {
				if ( step != 0 ) {
					CompositionStep preLastCompositionStep = step != 1 ? compositionSteps.get( step - 2 ) : prefirstCompositionStep ;
					preLastCompositionStep.addNextExclusion( lastCompositionStep.getOriginComposeBlock() );
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
			return 0;
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
