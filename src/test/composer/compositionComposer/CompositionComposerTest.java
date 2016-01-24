package composer.compositionComposer;

import composer.CompositionComposer;
import composer.first.FirstBlockProvider;
import composer.first.RandomFirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.next.SimpleNextBlockProvider;
import composer.step.CompositionStep;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringComposerTest;
import jm.JMC;
import jm.util.Write;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import model.composition.CompositionInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import persistance.dao.LexiconDAO;
import utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

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
	//	@Ignore
	public void getSimplePieceTest1() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( new RandomFirstBlockProvider(), new SimpleNextBlockProvider(), lexicon, "ABCD", 4 * JMC.WHOLE_NOTE );
		assertEquals( 16., composition.getEndTime(), 0 );

		//		assertNotNull( composition );
		//		View.show( composition );
		//		Utils.suspend();
	}
}
