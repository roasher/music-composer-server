package composer.compositionComposer;

import composer.CompositionComposer;
import composer.first.RandomFirstBlockProvider;
import composer.next.SimpleNextBlockProvider;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringComposerTest;
import jm.JMC;
import jm.music.data.Note;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static jm.JMC.*;

/**
 * Created by pyurkin on 15.12.14.
 */
public class CompositionComposerTest extends AbstractSpringComposerTest {

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Autowired
	private CompositionLoader compositionLoader;

	@Autowired
	private CompositionComposer compositionComposer;

	@Autowired
	@Qualifier( "lexiconDAO_database" )
	private LexiconDAO lexiconDAO;

	@Test
	public void getSimplePieceTest1() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( new RandomFirstBlockProvider(), new SimpleNextBlockProvider(), lexicon, "ABCD", 4 * JMC.WHOLE_NOTE );
		assertEquals( 16., composition.getEndTime(), 0 );
	}

	@Test
	public void gatherCompositionTest() {
		List<ComposeBlock> composeBlocks = Arrays.asList(
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( QUARTER_NOTE) ),
						new Melody( new Note( C4, QUARTER_NOTE ) )
				), new BlockMovement( 0 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( EIGHTH_NOTE) ),
						new Melody( new Note( C4, EIGHTH_NOTE ) )
				), new BlockMovement( 2 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Note( D5, EIGHTH_NOTE), new Note( E5, EIGHTH_NOTE), new Note( F5, EIGHTH_NOTE), new Note( E5, EIGHTH_NOTE) ),
						new Melody( new Note( C4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE), new Note( D4, EIGHTH_NOTE), new Note( E4, EIGHTH_NOTE) )
				), new BlockMovement( 0 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( QUARTER_NOTE) ),
						new Melody( new Note( C4, QUARTER_NOTE ) )
				), new BlockMovement( 0 ) )
		);
		Composition composition = compositionComposer.gatherComposition( composeBlocks );
		assertEquals( 2, composition.getPartArray().length );

		assertEquals( 6, composition.getPart( 0 ).getPhrase( 0 ).getNoteArray().length );
		assertEquals( new Rest( QUARTER_NOTE + EIGHTH_NOTE ), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[0] );
		assertEquals( new Note( D5 + 2, EIGHTH_NOTE), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[1] );
		assertEquals( new Note( E5 + 2, EIGHTH_NOTE), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[2] );
		assertEquals( new Note( F5 + 2, EIGHTH_NOTE), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[3] );
		assertEquals( new Note( E5 + 2, EIGHTH_NOTE), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[4] );
		assertEquals( new Rest( QUARTER_NOTE), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[5] );

		assertEquals( 4, composition.getPart( 1 ).getPhrase( 0 ).getNoteArray().length );
		assertEquals( new Note( C4, QUARTER_NOTE + EIGHTH_NOTE ), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[0] );
		assertEquals( new Note( C4 + 2, EIGHTH_NOTE), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[1] );
		assertEquals( new Note( D4 + 2, EIGHTH_NOTE + EIGHTH_NOTE ), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[2] );
		assertEquals( new Note( E4 + 2, EIGHTH_NOTE + QUARTER_NOTE ), composition.getPart( 0 ).getPhrase( 0 ).getNoteArray()[3] );

	}
}
