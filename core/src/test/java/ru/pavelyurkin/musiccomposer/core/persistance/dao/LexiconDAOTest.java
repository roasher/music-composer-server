package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import jm.JMC;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
	@DatabaseSetup( "/persistance/dao/LexiconDAOTest-blank.xml" )
	public void storeIdentityTest() throws IOException {

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src/test/resources/simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );

		lexiconDAO.persist( lexicon );
		Lexicon fetchedLexicon = lexiconDAO.fetch();

		assertTrue( lexicon.equals( fetchedLexicon ) );
	}

	@Test
	@DatabaseSetup( "/persistance/dao/LexiconDAOTest-blank.xml" )
	@ExpectedDatabase( assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/persistance/dao/LexiconDAOTest-with-data.xml" )
	public void persistTest() throws IOException {
		Lexicon lexicon = getTestLexicon();
		lexiconDAO.persist( lexicon );
	}

	@Test
	@DatabaseSetup( "/persistance/dao/LexiconDAOTest-with-data.xml" )
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

		BlockMovement blockMovement1 = new BlockMovement( 30, 20 );
		BlockMovement blockMovement2 = new BlockMovement( 32, 22 );

		ComposeBlock composeBlock0 = new ComposeBlock( 0, compositionInfo, Arrays.asList( melody1, melody2 ), blockMovement1 );
		ComposeBlock composeBlock1 = new ComposeBlock( 0, compositionInfo, Arrays.asList( melody1, melody3 ), blockMovement1 );
		ComposeBlock composeBlock2 = new ComposeBlock( 0, compositionInfo, Arrays.asList( melody3, melody4 ), blockMovement2 );

		composeBlock0.setPossibleNextComposeBlocks( Arrays.asList( composeBlock2 ) );
		composeBlock2.setPossiblePreviousComposeBlocks( Arrays.asList( composeBlock0 ) );
		composeBlock2.setPossibleNextComposeBlocks( Arrays.asList( composeBlock0 ) );
		composeBlock0.setPossiblePreviousComposeBlocks( Arrays.asList( composeBlock2 ) );

		List<ComposeBlock> composeBlocks = Arrays.asList( composeBlock0, composeBlock1, composeBlock2 );

		List<Integer> possibleNext0 = Arrays.asList( 2 );
		List<Integer> possibleNext2 = Arrays.asList( 0 );
		Map<Integer, List<Integer>> mapOfNexts = new HashMap<>();
		mapOfNexts.put( 0, possibleNext0 );
		mapOfNexts.put( 1, Collections.emptyList() );
		mapOfNexts.put( 2, possibleNext2 );

		Lexicon lexicon = new Lexicon( composeBlocks, mapOfNexts );

		return lexicon;

	}
}
