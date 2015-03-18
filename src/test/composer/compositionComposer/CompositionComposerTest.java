package composer.compositionComposer;

import composer.CompositionComposer;
import composer.CompositionStep;
import composer.FormBlockProvider;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.util.View;
import junit.framework.Assert;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Form;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pyurkin on 15.12.14.
 */
public class CompositionComposerTest extends AbstractSpringTest {

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Autowired
	private CompositionLoader compositionLoader;

	@Autowired
	private CompositionComposer compositionComposer;

	@Test
//	@Ignore
	public void getSimplePieceTest1() {
		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( lexicon, "ABCD", 4 * JMC.WHOLE_NOTE );
		assertEquals( 16., composition.getEndTime(), 0 );

//		assertNotNull( composition );
//		View.show( composition );
//		Utils.suspend();
	}

	@Test
    @Ignore
	// TODO too slow
	public void getRealPieceTest1() {
		List< Composition > compositionList = compositionLoader.getCompositions(
		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" )
//		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.biosphere(midi).mid" )
//		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Another Phoenix (midi)_2.mid" )
//		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\Метания беспокойного разума.mid" )
		);
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( lexicon, "ABCD", 4 * JMC.WHOLE_NOTE );
		assertEquals( 16., composition.getEndTime(), 0 );

	}
}
