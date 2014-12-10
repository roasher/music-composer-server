package decomposer;

import helper.AbstractSpringTest;
import jm.JMC;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.util.List;

public class CompositionDecomposerTest extends AbstractSpringTest {

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Autowired
	private CompositionLoader compositionLoader;

//	@Test
//	public void getLexiconTest() {
//		Composition composition = compositionLoader.getComposition( new File( "" ) );
//		List< MusicBlock > lexicon = compositionDecomposer.decompose( composition, JMC.WHOLE_NOTE );
//	}
}
