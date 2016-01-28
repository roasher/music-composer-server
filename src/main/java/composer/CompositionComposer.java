package composer;

import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.FormCompositionStep;
import jm.music.data.Note;
import jm.music.data.Part;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import model.composition.CompositionInfo;
import model.melody.Form;
import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

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
			Part part = new Part();
			part.add( composeBlocks.get( 0 ).getMelodyList().get( partNumber ) );
			parts.add( part );
		}
		// transposing
		for ( int composeBlockNumber = 1; composeBlockNumber < composeBlocks.size(); composeBlockNumber++ ) {

			ComposeBlock previousComposeBlock = composeBlocks.get( composeBlockNumber - 1 );
			ComposeBlock composeBlock = composeBlocks.get( composeBlockNumber );

			Optional.ofNullable( composeBlock.getCompositionInfo() ).ifPresent( compositionInfo -> logger.info( compositionInfo.getTitle() ) );

			int transposePitch = getTransposePitch( previousComposeBlock, composeBlock );
			composeBlocks.set( composeBlockNumber, composeBlock.transposeClone( transposePitch ) );
		}
		// gluing
		for ( int composeBlockNumber = 1; composeBlockNumber < composeBlocks.size(); composeBlockNumber++ ) {
			for ( int partNumber = 0; partNumber < parts.size(); partNumber++ ) {
				Melody melody = composeBlocks.get( composeBlockNumber ).getMelodyList().get( partNumber );
				Melody previousMelody = ( Melody ) parts.get( partNumber ).getPhraseList().get( parts.get( partNumber ).getPhraseList().size() - 1 );

				Note previousNote = ( Note ) previousMelody.getNoteList().get( previousMelody.getNoteList().size() - 1 );
				Note firstNote = ( Note ) melody.getNoteList().get( 0 );

				if ( previousNote.samePitch( firstNote ) ) {
					previousNote.setRhythmValue( previousNote.getRhythmValue() + firstNote.getRhythmValue() );
					previousNote.setDuration( previousNote.getDuration() + firstNote.getDuration() );
					melody.getNoteList().remove( 0 );
				}

				if ( !melody.getNoteList().isEmpty() ) {
					parts.get( partNumber ).add( melody );
				}
			}
		}

		Composition composition = new Composition( parts );
		return composition;
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