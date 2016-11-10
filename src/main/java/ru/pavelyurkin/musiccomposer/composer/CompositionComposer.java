package ru.pavelyurkin.musiccomposer.composer;

import ru.pavelyurkin.musiccomposer.composer.step.FormCompositionStep;
import jm.music.data.Note;
import jm.music.data.Part;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.model.Lexicon;
import ru.pavelyurkin.musiccomposer.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.model.melody.Form;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
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
	public Composition compose( ComposeBlockProvider composeBlockProvider, Lexicon lexicon, String form, double compositionLength ) {
		List<FormCompositionStep> compositionSteps = composeSteps( composeBlockProvider, lexicon, form, compositionLength );
		List<ComposeBlock> composeBlocks = new ArrayList<>();
		for ( FormCompositionStep compositionStep : compositionSteps ) {
			for ( ComposeBlock composeBlock : compositionStep.getTransposedComposeBlocks() ) {
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
	public List<FormCompositionStep> composeSteps( ComposeBlockProvider composeBlockProvider, Lexicon lexicon, String form, double compositionLength ) {
		List<FormCompositionStep> compositionSteps = new ArrayList<>();
		compositionSteps.add( new FormCompositionStep() );

		double stepLength = compositionLength / form.length();
		for ( int formElementNumber = 1; formElementNumber < form.length() + 1; formElementNumber++ ) {

			FormCompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );
			Form form1 = new Form( form.charAt( formElementNumber - 1 ) );
			Optional<FormCompositionStep> nextStep = formBlockProvider.getFormElement( composeBlockProvider, form1, stepLength, compositionSteps, lexicon );

			if ( nextStep.isPresent() ) {
				compositionSteps.add( nextStep.get() );
			} else {
				if ( formElementNumber != 1 ) {
					FormCompositionStep preLastCompositionStep = compositionSteps.get( formElementNumber - 2 );
					preLastCompositionStep.addNextExclusion( lastCompositionStep.getOriginComposeBlocks() );
					// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
					compositionSteps.remove( formElementNumber - 1 );
					formElementNumber = formElementNumber - 2;
					continue;
				} else {
					logger.warn( "There is no possible ways to compose new piece considering such parameters" );
					break;
				}
			}
		}
		compositionSteps.remove( 0 );
		return compositionSteps;
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
		// gluing
		for ( int composeBlockNumber = 1; composeBlockNumber < composeBlocks.size(); composeBlockNumber++ ) {
			Optional.ofNullable( composeBlocks.get( composeBlockNumber ).getCompositionInfo() )
					.ifPresent( compositionInfo -> logger.info( compositionInfo.getTitle() ) );
			for ( int partNumber = 0; partNumber < parts.size(); partNumber++ ) {
				Melody melody = composeBlocks.get( composeBlockNumber ).getMelodyList().get( partNumber );
				Melody previousMelody = ( Melody ) parts.get( partNumber ).getPhraseList().get( parts.get( partNumber ).getPhraseList().size() - 1 );

				Note previousNote = ( Note ) previousMelody.getNoteList().get( previousMelody.size() - 1 );
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

}