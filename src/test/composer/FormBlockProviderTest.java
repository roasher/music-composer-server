package composer;

import composer.first.RandomFirstBlockProvider;
import composer.next.SimpleNextBlockProvider;
import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import model.melody.Form;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by pyurkin on 17.02.15.
 */
public class FormBlockProviderTest extends AbstractSpringTest {
	@Autowired
	private CompositionLoader compositionLoader;
	@Autowired
	private CompositionDecomposer compositionDecomposer;
	@Autowired
	private FormBlockProvider formBlockProvider;

	@Test
	public void formBlockProviderTest() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );

		List<ComposeBlock> formElement = formBlockProvider.getFormElement( new RandomFirstBlockProvider(), new SimpleNextBlockProvider(), new Form( 'A' ), JMC.WHOLE_NOTE,
				Collections.<FormCompositionStep>emptyList(), lexiconFromFirst );

		List<ComposeBlock> composeBlockList = new ArrayList<>();
		composeBlockList.add( lexiconFromFirst.get( 0 ) );
		composeBlockList.add( lexiconFromFirst.get( 1 ) );
		assertEquals( formElement, composeBlockList );
	}
}
