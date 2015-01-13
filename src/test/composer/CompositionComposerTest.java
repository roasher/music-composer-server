package composer;

import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.constants.Durations;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.util.View;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Melody;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;
import utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static jm.constants.Durations.*;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.AS4;
import static jm.constants.Pitches.E5;
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
		Composition composition = compositionComposer.simpleCompose( lexicon, 4 * JMC.WHOLE_NOTE );

		assertNotNull( composition );
		View.show( composition );
		Utils.suspend();
	}

	@Test
	public void followTestCase() {
		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
		List< MusicBlock > lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		List< MusicBlock > lexiconFromSecond = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );
		MusicBlock current = lexiconFromFirst.get( 7 );
		MusicBlock next = lexiconFromSecond.get( 13 );
		assertTrue( compositionComposer.canFollow( current, next ) );
	}
}
