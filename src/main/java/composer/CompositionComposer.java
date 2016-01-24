package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.FormCompositionStep;
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
	public List<FormCompositionStep> composeSteps( FirstBlockProvider firstStepProvider, NextBlockProvider nextBlockProvider, Lexicon lexicon, String form, double compositionLength ) {
		List<FormCompositionStep> compositionSteps = new ArrayList<>();
		compositionSteps.add( new FormCompositionStep(  ) );

		double stepLength = compositionLength / form.length();
		for ( int formElementNumber = 1; formElementNumber < form.length() + 1; formElementNumber++ ) {

			FormCompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			FormCompositionStep nextStep = composeNext( firstStepProvider, nextBlockProvider, new Form( form.charAt( formElementNumber - 1 ) ), stepLength, compositionSteps, lexicon );

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

	public FormCompositionStep composeNext( FirstBlockProvider firstBlockProvider, NextBlockProvider nextBlockProvider, Form form, double length, List<FormCompositionStep> previousSteps,
			Lexicon lexicon ) {
		List<ComposeBlock> musicBlock = formBlockProvider.getFormElement( firstBlockProvider, nextBlockProvider, form, length, previousSteps, lexicon );
		return new FormCompositionStep( musicBlock, form );
	}

	/**
	 * Returns composition, build on input compose blocks
	 * @param composeBlockList
	 * @return
	 */
	public Composition gatherComposition( List<ComposeBlock> composeBlockList ) {
		List<Part> parts = new ArrayList<>();
		for ( int partNumber = 0; partNumber < composeBlockList.get( 0 ).getMelodyList().size(); partNumber++ ) {
			parts.add( new Part() );
		}
		for ( ComposeBlock composeBlock : composeBlockList ) {
			logger.info( composeBlock.getCompositionInfo().getTitle() );
			for ( int partNumber = 0; partNumber < parts.size(); partNumber++ ) {
				int melodiesAmount = parts.get( partNumber ).size();
				Melody melody = composeBlock.getMelodyList().get( partNumber );
				Melody newMelody = null;
				//				if ( melodiesAmount == 0 ) {
				// First melody in partNumber part
				newMelody = new Melody( melody.getNoteArray() );
				//				} else {
				// TODO Need to clone all music blocks before gathering composition if we want to change rhythm values of some
				//					// Need to bind first note of melody with previous if it has same pitch
				//					Note newPhraseFirstNote = melody.getNoteArray()[0];
				//					Phrase previousPhrase = parts.get( partNumber ).getPhrase( melodiesAmount - 1 );
				//					Note previousPhraseLastNote = previousPhrase.convertNote( previousPhrase.getNoteArray().length - 1 );
				//					if ( newPhraseFirstNote.getPitch() == previousPhraseLastNote.getPitch() ) {
				//						previousPhraseLastNote.setRhythmValue( previousPhraseLastNote.getRhythmValue() + newPhraseFirstNote.getRhythmValue(), true );
				//						Note[] newNoteArray = Arrays.copyOfRange( melody.getNoteArray(), 1, melody.getNoteArray().length );
				//						if ( newNoteArray.length == 0 ) {
				//							continue;
				//						}
				//						newMelody = new Melody( newNoteArray );
				//					} else {
				//						newMelody = new Melody( melody.getNoteArray() );
				//					}
				//				}
				parts.get( partNumber ).add( newMelody );
			}
		}
		Composition composition = new Composition( parts );
		return composition;
	}
}