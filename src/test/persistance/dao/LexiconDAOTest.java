package persistance.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import jm.music.data.Note;
import model.BlockMovement;
import model.composition.CompositionInfo;
import model.melody.Melody;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.Lexicon;
import model.composition.Composition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import model.ComposeBlock;
import utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static jm.JMC.*;

/**
 * Created by pyurkin on 15.04.2015.
 */
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class } )
public class LexiconDAOTest extends AbstractSpringTest {

	@Autowired
	@Qualifier( "lexiconDAO_database" )
	private LexiconDAO lexiconDAO;
	@Autowired
	private CompositionLoader compositionLoader;
	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Test
	@DatabaseSetup( "/LexiconDAOTest-blank.xml" )
	public void storeIdentityTest() throws IOException {

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );

		lexiconDAO.persist( lexicon );
		Lexicon fetchedLexicon = lexiconDAO.fetch();

		// TODO figure out why
		// We are not persisting start time, so for test comparing purposes setting it to zero
		for ( ComposeBlock composeBlock : lexicon.getComposeBlockList() ) {
			for ( Melody melody : composeBlock.getMelodyList() ) {
				melody.setStartTime( 0 );
			}
		}

		assertTrue( lexicon.equals( fetchedLexicon ) );
	}

	@Test
	@DatabaseSetup( "/LexiconDAOTest-blank.xml" )
	@ExpectedDatabase( assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/LexiconDAOTest-with-data.xml" )
	public void persistTest() throws IOException {
		Lexicon lexicon = getTestLexicon();
		lexiconDAO.persist( lexicon );
	}

	@Test
	@DatabaseSetup( "/LexiconDAOTest-with-data.xml" )
	public void fetchTest() {
		Lexicon lexicon = lexiconDAO.fetch();
		assertEquals( lexicon.getComposeBlockList(), getTestLexicon().getComposeBlockList() );
	}

	private Lexicon getTestLexicon() {

		Note note1 = new Note( C4, WHOLE_NOTE, 0, 0 );
		Note note2 = new Note( D4, HALF_NOTE, 0, 0 );
		Note note3 = new Note( E4, EIGHTH_NOTE, 0, 0 );
		Note note4 = new Note( DS4, EIGHTH_NOTE, 0, 0 );
		Note note5 = new Note( B4, WHOLE_NOTE, 0, 0 );

		Melody melody1 = new Melody( 'A', note1, note2 );
		Melody melody2 = new Melody( 'B', note3, note4, note3, note4, note5 );
		Melody melody3 = new Melody( 'C', note2, note2, note2 );
		Melody melody4 = new Melody( 'A', note2, note5 );

		CompositionInfo compositionInfo = new CompositionInfo();
		compositionInfo.setAuthor( "TEST_AUTHOR" );
		compositionInfo.setTitle( "TEST_TITLE" );
		compositionInfo.setTempo( 4.0 );

		BlockMovement blockMovement1 = new BlockMovement( 10, 20 );
		BlockMovement blockMovement2 = new BlockMovement( 11, 22 );

		ComposeBlock composeBlock1 = new ComposeBlock( 0, compositionInfo, Arrays.asList( melody1, melody2 ), blockMovement1 );
		ComposeBlock composeBlock2 = new ComposeBlock( 0, compositionInfo, Arrays.asList( melody1, melody3 ), blockMovement1 );
		ComposeBlock composeBlock3 = new ComposeBlock( 0, compositionInfo, Arrays.asList( melody3, melody4 ), blockMovement2 );

		composeBlock1.setPossibleNextComposeBlocks( Arrays.asList( composeBlock3 ) );
		composeBlock3.setPossiblePreviousComposeBlocks( Arrays.asList( composeBlock1 ) );
		composeBlock3.setPossibleNextComposeBlocks( Arrays.asList( composeBlock1 ) );
		composeBlock1.setPossiblePreviousComposeBlocks( Arrays.asList( composeBlock3 ) );

		List<ComposeBlock> composeBlocks = Arrays.asList( composeBlock1, composeBlock2, composeBlock3 );

		Lexicon lexicon = new Lexicon( composeBlocks );

		return lexicon;

	}
}
