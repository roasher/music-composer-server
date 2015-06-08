package persistance.dao;

import model.melody.Melody;
import persistance.dao.LexiconDAO;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.Lexicon;
import model.composition.Composition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import model.ComposeBlock;
import utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by pyurkin on 15.04.2015.
 */
public class LexiconDAOTest extends AbstractSpringTest {

	@Autowired
	@Qualifier( "lexiconDAO_database" )
	private LexiconDAO lexiconDAO;
	@Autowired
	private CompositionLoader compositionLoader;
	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Test
	public void storeIdentityTest() throws IOException {

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder(
				new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );

		lexiconDAO.store( lexicon );
		Lexicon fetchedLexicon = lexiconDAO.fetch();

		// We are not persisting start time, so for test comparing purposes setting it to zero
		for ( ComposeBlock composeBlock : lexicon.getComposeBlockList() ) {
			for ( Melody melody : composeBlock.getMelodyList() ) {
				melody.setStartTime( 0 );
			}
		}
		assertTrue( lexicon.equals( fetchedLexicon ) );
	}
}
