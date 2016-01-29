package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import decomposer.form.analyzer.MusicBlockFormEqualityAnalyser;
import jm.music.data.Note;
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
	 *
	 * @param form          - form, from part of witch new Block is going to be generated
	 * @param length
	 * @param previousSteps
	 * @param lexicon
	 * @return
	 */
	public List<ComposeBlock> getFormElement( ComposeBlockProvider composeBlockProvider, Form form, double length, List<FormCompositionStep> previousSteps,
			Lexicon lexicon ) {
		logger.info( "Composing new form element : {}, length : {}", form.getValue(), length );
		List<ComposeBlock> composeBlock = composeBlock( composeBlockProvider, previousSteps.get( previousSteps.size() - 1 ), lexicon, length );
		return composeBlock;
	}

	public List<ComposeBlock> composeBlock( ComposeBlockProvider composeBlockProvider, FormCompositionStep previousCompositionStep, Lexicon lexicon,
			double length ) {
		List<CompositionStep> compositionSteps = composeSteps( composeBlockProvider, previousCompositionStep, lexicon, length );
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
	 * @return TODO refactor
	 */
	public List<CompositionStep> composeSteps( ComposeBlockProvider composeBlockProvider, FormCompositionStep previousCompositionStep, Lexicon lexicon,
			double length ) {
		List<CompositionStep> compositionSteps = new ArrayList<>();

		CompositionStep compositionStep = fetchLastCompositionStep( previousCompositionStep );
		compositionSteps.add( compositionStep );
		double currentLength = 0;

		for ( int step = 1; step < length / lexicon.getMinRhythmValue() + 1; step++ ) {
			logger.debug( "Current state {}", step );
			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			Optional<ComposeBlock> lastStepComposeBlock = Optional.ofNullable( lastCompositionStep.getComposeBlock() );

			Optional<ComposeBlock> nextComposeBlock = composeBlockProvider.getNextComposeBlock( lexicon, compositionSteps );
			CompositionStep nextStep = new CompositionStep(
//					transpose( nextComposeBlock, lastStepComposeBlock )
					nextComposeBlock.orElse( null )
			);

			if ( nextStep.getComposeBlock() != null && currentLength + nextStep.getComposeBlock().getRhythmValue() <= length ) {
				currentLength += nextStep.getComposeBlock().getRhythmValue();
				compositionSteps.add( nextStep );
				if ( currentLength == length ) {
					// removing prefirst step
					compositionSteps.remove( 0 );
					return compositionSteps;
				}
			} else {
				if ( step != 1 ) {
					CompositionStep preLastCompositionStep = compositionSteps.get( step - 2 );
					preLastCompositionStep.addNextExclusion( lastStepComposeBlock.get() );
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
	 * List of first Compose Blocks of exclusions of input = output exclusion
	 *
	 * @param formCompositionStep
	 * @return
	 */
	public CompositionStep fetchLastCompositionStep( FormCompositionStep formCompositionStep ) {
		List<ComposeBlock> exclusions = CollectionUtils.getListOfFirsts( formCompositionStep.getNextMusicBlockExclusions() );
		List<ComposeBlock> composeBlocks = formCompositionStep.getComposeBlocks();
		CompositionStep compositionStep = new CompositionStep( composeBlocks != null ? composeBlocks.get( composeBlocks.size() - 1 ) : null );
		compositionStep.setNextMusicBlockExclusions( exclusions );
		return compositionStep;
	}

	/**
	 * Transposes input compose block and
	 *
	 * @param composeBlock
	 * @param previousComposeBlock
	 * @return
	 */
	public ComposeBlock transpose( Optional<ComposeBlock> composeBlock, Optional<ComposeBlock> previousComposeBlock ) {
		if ( !composeBlock.isPresent() ) return null;
		return composeBlock.get().transposeClone( previousComposeBlock.isPresent() ? getTransposePitch( previousComposeBlock.get(), composeBlock.get() ) : 0 );
	}

	private int getTransposePitch( ComposeBlock firstComposeBlock, ComposeBlock secondComposeBlock ) {
		for ( int melodyNumber = 0; melodyNumber < firstComposeBlock.getMelodyList().size(); melodyNumber++ ) {
			Note lastNoteOfFirst = firstComposeBlock.getMelodyList().get( melodyNumber )
					.getNote( firstComposeBlock.getMelodyList().get( melodyNumber ).size() - 1 );
			Note firstNoteOfSecond = secondComposeBlock.getMelodyList().get( melodyNumber ).getNote( 0 );
			if ( lastNoteOfFirst.getPitch() != Note.REST && firstNoteOfSecond.getPitch() != Note.REST ) {
				return lastNoteOfFirst.getPitch() + secondComposeBlock.getBlockMovementFromPreviousToThis().getVoiceMovements().get( melodyNumber )
						- firstNoteOfSecond.getPitch();
			}
		}
		return 0;
	}
}
