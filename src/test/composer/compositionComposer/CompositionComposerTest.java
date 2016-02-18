package composer.compositionComposer;

import composer.ComposeBlockProvider;
import composer.CompositionComposer;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Rest;
import model.BlockMovement;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import persistance.dao.LexiconDAO;
import utils.CompositionLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static jm.JMC.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
	@Qualifier( "simpleComposeBlockProvider" )
	private ComposeBlockProvider composeBlockProvider;

	@Test
	public void getSimplePieceTest1() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( composeBlockProvider, lexicon, "ABCD", 4 * JMC.WHOLE_NOTE );
		assertEquals( 16., composition.getEndTime(), 0 );
	}

	@Test
	public void gatherCompositionTest() {
		List<ComposeBlock> composeBlocks = Arrays.asList(
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( QUARTER_NOTE ) ),
						new Melody( new Note( C3, QUARTER_NOTE ) ) ),
						new BlockMovement( -256, -256 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( EIGHTH_NOTE ) ),
						new Melody( new Note( C4, EIGHTH_NOTE ) ) ),
						new BlockMovement( 0, 0 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Note( D5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ), new Note( F5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ) ),
						new Melody( new Note( C4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( E4, EIGHTH_NOTE ) ) ),
						new BlockMovement( -256, 2 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Note( B5, QUARTER_NOTE ) ),
						new Melody( new Rest( QUARTER_NOTE ) ) ),
						new BlockMovement( 0, -256 ) ) );
		Composition composition = compositionComposer.gatherComposition( composeBlocks );
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

	private List<Note> getListOfNotes( Part part ) {
		List<Note> notes = new ArrayList<>();
		for ( Object o : part.getPhraseList() ) {
			Phrase phrase = ( Phrase ) o;
			phrase.getNoteList().forEach( o1 -> notes.add( ( Note ) o1 ) );
		}
		return notes;
	}
}
