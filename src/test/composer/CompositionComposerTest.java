package composer;

import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.util.View;
import model.MusicBlock;
import model.composition.Composition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;
import utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pyurkin on 15.12.14.
 */
public class CompositionComposerTest extends AbstractSpringTest {
	@Autowired
	private CompositionComposer compositionComposer;

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Autowired
	private CompositionLoader compositionLoader;

	@Test
	public void getSimplePieceTest() {
		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
		List< MusicBlock > lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.simpleCompose( lexicon );

		assertNotNull( composition );
		View.notate( composition );
		Utils.suspend();
	}
}
