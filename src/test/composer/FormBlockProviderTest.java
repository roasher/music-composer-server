package composer;

import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Form;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;
import utils.ModelUtils;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );

		MusicBlock formElement = formBlockProvider.getFormElement( new Form( 'A' ), JMC.WHOLE_NOTE, Collections.<CompositionStep>emptyList(), lexiconFromFirst );
		List<MusicBlock> musicBlockList = new ArrayList<>(  );
		musicBlockList.add( lexiconFromFirst.get( 0 ) );
		musicBlockList.add( lexiconFromFirst.get( 1 ) );
		assertEquals( formElement, new MusicBlock( null, musicBlockList ) );
	}

	@Test
	public void variantHandleTest() {
		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );

		MusicBlock firstMusicBlock = formBlockProvider.handleVariant( Arrays.asList( new Double[] { 2., 2. } ), null, lexiconFromFirst );
		assertNotNull( firstMusicBlock );
		MusicBlock secondMusicBlock = formBlockProvider.handleVariant( Arrays.asList( new Double[] { 1., 1., 1., .5, .5 } ), firstMusicBlock, lexiconFromFirst );
		assertNotNull( secondMusicBlock );
		MusicBlock thirdMusicBlock = formBlockProvider.handleVariant( Arrays.asList( new Double[] { .5, 1.5, 1., .5, .5 } ), secondMusicBlock, lexiconFromFirst );
		assertNotNull( thirdMusicBlock );
		MusicBlock forthMusicBlock = formBlockProvider.handleVariant( Arrays.asList( new Double[] { .5, .5, .5, .5, .5, .5, 1. } ), thirdMusicBlock, lexiconFromFirst );
		assertNotNull( forthMusicBlock );
		MusicBlock fifthMusicBlock = formBlockProvider.handleVariant( Arrays.asList( new Double[] { .5, .5, .5, .5, .5, .5, .5, .5 } ), forthMusicBlock, lexiconFromFirst );
		assertNotNull( fifthMusicBlock );
		MusicBlock sixthMusicBlock = formBlockProvider.handleVariant( Arrays.asList( new Double[] { .5, .5, .5, .5, 2. } ), fifthMusicBlock, lexiconFromFirst );
		assertNotNull( sixthMusicBlock );
	}
}
