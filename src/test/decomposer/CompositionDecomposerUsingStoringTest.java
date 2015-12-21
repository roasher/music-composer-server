package decomposer;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import persistance.dao.LexiconDAO;
import helper.AbstractSpringTest;
import jm.JMC;
import model.Lexicon;
import model.composition.Composition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by pyurkin on 17.04.2015.
 */
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class } )
@DatabaseSetup( "/persistance/dao/LexiconDAOTest-blank.xml" )
public class CompositionDecomposerUsingStoringTest extends AbstractSpringTest {
	@Autowired
	private CompositionDecomposer compositionDecomposer;
	@Autowired
	private CompositionLoader compositionLoader;
	@Autowired
	@Qualifier( "lexiconDAO_database" )
	private LexiconDAO lexiconDAO;

	@Test
	public void decomposeWithOrWithoutStoringEqual() throws IOException {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		// first decompose with no persistance
		Lexicon lexiconWithoutDB = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		lexiconDAO.persist( lexiconWithoutDB );

		// second decompose with file
		Lexicon lexiconWithDB = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		assertEquals( lexiconWithDB, lexiconWithoutDB );
	}

	@Test
	public void decomposeWithOrWithoutStoring_intersect() throws IOException {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		// Decompose all melodies
		Lexicon lexiconFull = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );

		// Decompose first melody and storing it into DB
		Lexicon lexiconWithoutDB = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		lexiconDAO.persist( lexiconWithoutDB );

		// Decompose all melodies using DB with only one melody
		Lexicon lexiconWithDB = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		assertEquals( lexiconWithDB, lexiconFull );
	}

	@Test
	public void decomposeWithOrWithoutStoring_nonoverlapping() throws IOException {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		// Decompose second
		Lexicon lexiconSecond = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );

		// Decompose first melody and storing it into DB
		Lexicon lexiconFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		lexiconDAO.persist( lexiconFirst );

		// Decompose second melody using DB with only first melody
		Lexicon lexiconSecondWithDB = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );
		assertEquals( lexiconSecondWithDB, lexiconSecond );
	}

	@Test
	public void failDecompose() throws IOException {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		// Decompose second
		Lexicon lexiconSecond = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );

		// Decompose first melody and storing it into DB
		Lexicon lexiconFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		lexiconDAO.persist( lexiconFirst );

		// Decompose second melody using DB with only first melody
		Lexicon lexiconSecondWithDB = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		assertNotEquals( lexiconSecondWithDB, lexiconSecond );

		Lexicon lexiconAllWithDB = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		assertNotEquals( lexiconAllWithDB, lexiconSecond );
	}

}
