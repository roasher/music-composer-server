package ru.pavelyurkin.musiccomposer.core.composer.compositionComposer;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Rest;
import jm.util.View;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposerTest;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.persistance.dao.LexiconDAO;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jm.JMC.*;
import static jm.constants.Durations.WHOLE_NOTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by pyurkin on 15.12.14.
 */
public class CompositionComposerTest extends AbstractSpringTest {

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Autowired
	private CompositionLoader compositionLoader;

	@Autowired
	private CompositionComposer compositionComposer;

	@Autowired
	@Qualifier( "lexiconDAO_database" )
	private LexiconDAO lexiconDAO;

	@Autowired
	private ComposeStepProvider composeStepProvider;

	@Test
	public void getSimplePieceTest1() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src/test/resources/simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( composeStepProvider, lexicon, "ABCD", 4 * JMC.WHOLE_NOTE );
		assertEquals( 16., composition.getEndTime(), 0 );
	}

	@Test
	public void gatherCompositionTest() {
		List<List<InstrumentPart>> blocks = getBlocks();

		Composition composition = compositionComposer.gatherComposition( blocks );
		compositionCheck( composition );

		List<List<InstrumentPart>> blocks0 = Arrays.asList(
				Arrays.asList( new InstrumentPart( new Rest( QUARTER_NOTE ) ), new InstrumentPart( new Note( C3, QUARTER_NOTE ) ) ) );
		List<List<InstrumentPart>> blocks1 = Arrays.asList(
				Arrays.asList( new InstrumentPart( new Rest( EIGHTH_NOTE ) ), new InstrumentPart( new Note( C4, EIGHTH_NOTE ) ) ),
				Arrays.asList(
						new InstrumentPart( new Note( D5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ), new Note( F5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ) ),
						new InstrumentPart( new Note( C4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( E4, EIGHTH_NOTE ) ) ) );
		List<List<InstrumentPart>> blocks2 = Arrays.asList(
				Arrays.asList( new InstrumentPart( new Note( B5, QUARTER_NOTE ) ), new InstrumentPart( new Rest( QUARTER_NOTE ) ) ) );

		Composition composition0 = compositionComposer.gatherComposition( blocks0 );
		Composition composition1 = compositionComposer.gatherComposition( blocks1 );
		Composition composition2 = compositionComposer.gatherComposition( blocks2 );

		compositionCheck( compositionComposer.gatherComposition( composition0, composition1, composition2 ) );

	}

	@Test
	public void gatherCompositionNotChangingInputTest() {
		List<List<InstrumentPart>> blocksBeforeGathering = getBlocks();
		compositionComposer.gatherComposition( blocksBeforeGathering );
		assertEquals( blocksBeforeGathering, getBlocks() );
	}

	private void compositionCheck( Composition composition ) {
		assertEquals( 2, composition.getPartList().size() );

		List<Note> firstListOfNotes = getListOfNotes( composition.getPart( 0 ) );
		assertEquals( 6, firstListOfNotes.size() );
		assertTrue( new Rest( QUARTER_NOTE + EIGHTH_NOTE ).equals( firstListOfNotes.get( 0 ) ) );
		assertTrue( new Note( D5, EIGHTH_NOTE ).equals( firstListOfNotes.get( 1 ) ) );
		assertTrue( new Note( E5, EIGHTH_NOTE ).equals( firstListOfNotes.get( 2 ) ) );
		assertTrue( new Note( F5, EIGHTH_NOTE ).equals( firstListOfNotes.get( 3 ) ) );
		assertTrue( new Note( E5, EIGHTH_NOTE ).equals( firstListOfNotes.get( 4 ) ) );
		assertTrue( new Note( B5, QUARTER_NOTE ).equals( firstListOfNotes.get( 5 ) ) );

		List<Note> secondListOfNotes = getListOfNotes( composition.getPart( 1 ) );
		assertEquals( 6, secondListOfNotes.size() );
		assertTrue( new Note( C3, QUARTER_NOTE ).equals( secondListOfNotes.get( 0 ) ) );
		assertTrue( new Note( C4, EIGHTH_NOTE + EIGHTH_NOTE ).equals( secondListOfNotes.get( 1 ) ) );
		assertTrue( new Note( D4, EIGHTH_NOTE ).equals( secondListOfNotes.get( 2 ) ) );
		assertTrue( new Note( D4, EIGHTH_NOTE ).equals( secondListOfNotes.get( 3 ) ) );
		assertTrue( new Note( E4, EIGHTH_NOTE ).equals( secondListOfNotes.get( 4 ) ) );
		assertTrue( new Rest( QUARTER_NOTE ).equals( secondListOfNotes.get( 5 ) ) );
	}

	private List<List<InstrumentPart>> getBlocks() {
		return Arrays.asList(
				Arrays.asList( new InstrumentPart( new Rest( QUARTER_NOTE ) ), new InstrumentPart( new Note( C3, QUARTER_NOTE ) ) ),
				Arrays.asList( new InstrumentPart( new Rest( EIGHTH_NOTE ) ), new InstrumentPart( new Note( C4, EIGHTH_NOTE ) ) ),
				Arrays.asList(
						new InstrumentPart( new Note( D5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ), new Note( F5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ) ),
						new InstrumentPart( new Note( C4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( E4, EIGHTH_NOTE ) ) ),
				Arrays.asList( new InstrumentPart( new Note( B5, QUARTER_NOTE ) ), new InstrumentPart( new Rest( QUARTER_NOTE ) ) ) );
	}

	private List<Note> getListOfNotes( Part part ) {
		List<Note> notes = new ArrayList<>();
		for ( Object o : part.getPhraseList() ) {
			Phrase phrase = ( Phrase ) o;
			phrase.getNoteList().forEach( o1 -> notes.add( ( Note ) o1 ) );
		}
		return notes;
	}

	@Ignore
	@Test
	public void singleVoiceComposingTest() {
		Composition composition = compositionLoader.getComposition( new File( CompositionDecomposerTest.class.getResource( "gen_1.mid" ).getFile() ) );
		Lexicon lexicon = compositionDecomposer.decompose( composition, WHOLE_NOTE );
		Composition composedComposition = compositionComposer.compose( composeStepProvider, lexicon, "ABCB", WHOLE_NOTE * 4 );
		View.show( composedComposition );
		Utils.suspend();
	}
}
