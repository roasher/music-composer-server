package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.FormCompositionStep;
import jm.music.data.Note;
import jm.music.data.Part;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import model.melody.Form;
import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	public Composition compose( FirstBlockProvider firstStepProvider, NextBlockProvider nextBlockProvider, Lexicon lexicon, String form,
			double compositionLength ) {
		List<FormCompositionStep> compositionSteps = composeSteps( firstStepProvider, nextBlockProvider, lexicon, form, compositionLength );
		List<ComposeBlock> composeBlocks = new ArrayList<>();
		for ( FormCompositionStep compositionStep : compositionSteps ) {
			for ( ComposeBlock composeBlock : compositionStep.getComposeBlocks() ) {
				composeBlocks.add( composeBlock );
			}
		}
		return gatherComposition( composeBlocks );
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
	public List<FormCompositionStep> composeSteps( FirstBlockProvider firstStepProvider, NextBlockProvider nextBlockProvider, Lexicon lexicon, String form,
			double compositionLength ) {
		List<FormCompositionStep> compositionSteps = new ArrayList<>();
		compositionSteps.add( new FormCompositionStep() );

		double stepLength = compositionLength / form.length();
		for ( int formElementNumber = 1; formElementNumber < form.length() + 1; formElementNumber++ ) {

			FormCompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			FormCompositionStep nextStep = composeNext( firstStepProvider, nextBlockProvider, new Form( form.charAt( formElementNumber - 1 ) ), stepLength,
					compositionSteps, lexicon );

			if ( nextStep.getComposeBlocks() == null ) {
				if ( formElementNumber != 1 ) {
					FormCompositionStep preLastCompositionStep = compositionSteps.get( formElementNumber - 2 );
					preLastCompositionStep.addNextExclusion( lastCompositionStep.getComposeBlocks() );
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
		compositionSteps.remove( 0 );
		return compositionSteps;
	}

	public FormCompositionStep composeNext( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, Form form, double length,
			List<FormCompositionStep> previousSteps, Lexicon lexicon ) {
		List<ComposeBlock> musicBlock = formBlockProvider.getFormElement( firstBlockProvider, nextBlockProvider, form, length, previousSteps, lexicon );
		return new FormCompositionStep( musicBlock, form );
	}

	/**
	 * Returns composition, build on input compose blocks
	 *
	 * @param composeBlocks
	 * @return
	 */
	public Composition gatherComposition( List<ComposeBlock> composeBlocks ) {
		List<Part> parts = new ArrayList<>();
		for ( int partNumber = 0; partNumber < composeBlocks.get( 0 ).getMelodyList().size(); partNumber++ ) {
			parts.add( new Part() );
		}
		for ( int composeBlockNumber = 0; composeBlockNumber < composeBlocks.size(); composeBlockNumber++ ) {
			ComposeBlock composeBlock = composeBlocks.get( composeBlockNumber );
			ComposeBlock previousComposeBlock =	composeBlockNumber != 0 ? composeBlocks.get( composeBlockNumber - 1 ) : null;

			if ( composeBlock.getCompositionInfo() != null ) {
				logger.info( composeBlock.getCompositionInfo().getTitle() );
			}

			for ( int partNumber = 0; partNumber < parts.size(); partNumber++ ) {
				Melody melodyClone = composeBlock.getMelodyList().get( partNumber ).clone();
				Melody previousMelody = previousComposeBlock != null ? previousComposeBlock.getMelodyList().get( partNumber ) : null;
				if ( previousMelody != null  ) {
					Note previousNote = ( Note ) previousMelody.getNoteList().get( previousMelody.getNoteList().size() - 1 );
					Note firstNote = ( Note ) melodyClone.getNoteList().get( 0 );
					if ( previousNote.samePitch( firstNote ) ) {
						previousNote.setRhythmValue( previousNote.getRhythmValue() + firstNote.getRhythmValue() );
						melodyClone.getNoteList().remove( 0 );
					}
				}
				parts.get( partNumber ).add( melodyClone );
			}
		}

		Composition composition = new Composition( parts );
		return composition;
	}
}