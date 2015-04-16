package database;

import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.Lexicon;
import model.composition.Composition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by pyurkin on 15.04.2015.
 */
public class LexiconDAOTest extends AbstractSpringTest {

	@Autowired private LexiconDAO lexiconDAO;
	@Autowired private CompositionLoader compositionLoader;
	@Autowired private CompositionDecomposer compositionDecomposer;

	public static final Path storeFile = Paths.get( "src\\main\\java\\database\\test\\Lexicon.xml" );

	@Before
	public void before() throws IOException {
		storeFile.toFile().delete();
		lexiconDAO.setStoreFile( storeFile.toFile() );
	}

	@After
	public void after() {
		// may delete the file

	}

	@Test
	public void daoTest() throws IOException {

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );

		storeFile.getParent().toFile().mkdirs();
		storeFile.toFile().createNewFile();
		lexiconDAO.store( lexicon );
		assertTrue( storeFile.toFile().exists() );

		Lexicon fetchedLexicon = lexiconDAO.fetch();
		assertTrue( lexicon.equals( fetchedLexicon ) );
	}
}
