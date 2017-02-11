package ru.pavelyurkin.musiccomposer.core.composer;

import javafx.util.Pair;
import jm.music.data.Phrase;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import jm.music.data.Note;
import jm.music.data.Part;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getLast;

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
	 * Composing piece considering given lexicon and step to start from.
	 * Returns composition and last compose step to being able to start composing process from this point
	 *
	 * @param lexicon
	 * @param compositionLength
	 * @return
	 */
	public Pair<Composition, List<CompositionStep>> compose( ComposeBlockProvider composeBlockProvider, Lexicon lexicon, double compositionLength, List<CompositionStep> previousCompositionSteps ) {
		List<CompositionStep> compositionSteps = formBlockProvider.composeSteps( compositionLength, lexicon, composeBlockProvider, previousCompositionSteps );
		List<List<Melody>> blocks = compositionSteps
				.stream()
				.map( compositionStep -> compositionStep.getTransposedBlock().getMelodyList() )
				.collect( Collectors.toList() );
		Composition composition = gatherComposition( blocks );
		List<CompositionStep> steps = new ArrayList<>();
		steps.addAll( previousCompositionSteps );
		steps.addAll( compositionSteps );
		return new Pair<>( composition, steps );
	}

	/**
	 * Composing piece considering given lexicon and composition compositionLength
	 *
	 * @param lexicon
	 * @param compositionLength
	 * @return
	 */
	public Composition compose( ComposeBlockProvider composeBlockProvider, Lexicon lexicon, double compositionLength ) {
		return compose( composeBlockProvider, lexicon, compositionLength, Arrays.asList( CompositionStep.getEmptyCompositionStep() ) ).getKey();
	}

	/**
	 * Composing piece considering given lexicon, form pattern and composition length
	 *
	 * @param lexicon
	 * @param form
	 * @param compositionLength
	 * @return
	 */
	public Composition compose( ComposeBlockProvider composeBlockProvider, Lexicon lexicon, String form, double compositionLength ) {
		List<FormCompositionStep> formCompositionSteps = composeSteps( composeBlockProvider, lexicon, form, compositionLength );
		List<List<Melody>> blocks = formCompositionSteps
							.stream()
							.flatMap( formCompositionStep -> formCompositionStep.getCompositionSteps()
									.stream()
									.map( compositionStep -> compositionStep.getTransposedBlock().getMelodyList() ) )
							.collect( Collectors.toList() );
		return gatherComposition( blocks );
	}

	/**
	 * Main composing function if form is set.
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

		FormCompositionStep prefirstCompositionStep = FormCompositionStep.getEmptyStep();
		List<FormCompositionStep> compositionSteps = new ArrayList<>();

		double stepLength = compositionLength / form.length();
		for ( int formElementNumber = 0; formElementNumber < form.length(); formElementNumber++ ) {

			FormCompositionStep lastCompositionStep = !compositionSteps.isEmpty() ? getLast( compositionSteps ) : prefirstCompositionStep;
			Form current = new Form( form.charAt( formElementNumber ) );
			Optional<FormCompositionStep> nextStep = formBlockProvider.getFormElement( stepLength, lexicon, composeBlockProvider, current, compositionSteps );

			if ( nextStep.isPresent() ) {
				compositionSteps.add( nextStep.get() );
			} else {
				if ( formElementNumber != 0 ) {
					FormCompositionStep preLastCompositionStep = formElementNumber != 1 ? compositionSteps.get( formElementNumber - 2 ) : prefirstCompositionStep;
					getLast( preLastCompositionStep.getCompositionSteps() ).addNextExclusion( lastCompositionStep.getCompositionSteps().get( 0 ).getOriginComposeBlock() );
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
		return compositionSteps;
	}

	/**
	 * Creates composition, build on input compose blocks without changing the input
	 *
	 * @param blocks
	 * @return
	 */
	public <T extends Phrase> Composition gatherComposition( List<List<T>> blocks ) {
		List<Part> parts = new ArrayList<>();
		// creating parts and add first block
		for ( int partNumber = 0; partNumber < blocks.get( 0 ).size(); partNumber++ ) {
			Part part = new Part();
			part.add( ModelUtils.clone( blocks.get( 0 ).get( partNumber ) ) );
			parts.add( part );
		}
		// gluing
		for ( int blockNumber = 1; blockNumber < blocks.size(); blockNumber++ ) {
			for ( int partNumber = 0; partNumber < parts.size(); partNumber++ ) {
				Phrase phrase = ModelUtils.clone( blocks.get( blockNumber ).get( partNumber ) );
				Phrase previousPhrase = ( Phrase ) parts.get( partNumber ).getPhraseList().get( parts.get( partNumber ).getPhraseList().size() - 1 );

				Note previousNote = ( Note ) previousPhrase.getNoteList().get( previousPhrase.size() - 1 );
				Note firstNote = ( Note ) phrase.getNoteList().get( 0 );

				if ( previousNote.samePitch( firstNote ) ) {
					previousNote.setRhythmValue( previousNote.getRhythmValue() + firstNote.getRhythmValue() );
					previousNote.setDuration( previousNote.getDuration() + firstNote.getDuration() );
					phrase.getNoteList().remove( 0 );
				}

				if ( !phrase.getNoteList().isEmpty() ) {
					parts.get( partNumber ).add( phrase );
				}
			}
		}

		Composition composition = new Composition( parts );
		return composition;
	}

	/**
	 * Creates composition based on sum of the input
	 *
	 * @param compositions
	 * @return
	 */
	public Composition gatherComposition( Composition... compositions ) {
		List<List<Melody>> collect = Arrays.stream( compositions )
				.map( composition -> ( ( List<Part> ) composition.getPartList() )
						.stream()
						.map( part ->  new Melody( ( List<Note> ) part.getPhraseList()
								.stream()
								.flatMap( phrase ->  ( ( Phrase ) phrase ).getNoteList().stream() )
								.collect( Collectors.toList() ) ) )
						.collect( Collectors.toList() ) )
				.collect( Collectors.toList() );
		return gatherComposition( collect );
	}

}