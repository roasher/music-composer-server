package composer;

import composer.step.FormCompositionStep;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Rest;
import model.BlockMovement;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import model.melody.Form;
import model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import utils.CompositionLoader;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static jm.JMC.*;

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
	@Autowired
	@Qualifier( "simpleComposeBlockProvider" )
	private ComposeBlockProvider composeBlockProvider;

	@Test
	public void formBlockProviderTest() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );

		Optional<FormCompositionStep> formCompositionStep = formBlockProvider
				.getFormElement( composeBlockProvider, new Form( 'A' ), JMC.WHOLE_NOTE, Arrays.asList( new FormCompositionStep() ), lexiconFromFirst );

		List<ComposeBlock> composeBlockList = new ArrayList<>();
		composeBlockList.add( lexiconFromFirst.get( 0 ) );
		composeBlockList.add( lexiconFromFirst.get( 1 ) );

		assertEquals( formCompositionStep.get().getOriginComposeBlocks(), composeBlockList );
	}

	@Test
	public void transposePitchTest() {
		List<ComposeBlock> composeBlocks = Arrays.asList(
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( QUARTER_NOTE ) ),
						new Melody( new Note( C3, QUARTER_NOTE ) ) ),
						new BlockMovement( -256, -256 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( EIGHTH_NOTE ) ),
						new Melody( new Note( C4, EIGHTH_NOTE ) ) ),
						new BlockMovement( 0, 0 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Note( D5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ), new Note( F5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ) ),
						new Melody( new Note( C4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( E4, EIGHTH_NOTE ) ) ),
						new BlockMovement( -256, 2 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Note( B5, QUARTER_NOTE ) ),
						new Melody( new Rest( QUARTER_NOTE ) ) ),
						new BlockMovement( 0, -256 ) ) );

		assertEquals( C3 - C4, formBlockProvider.getTransposePitch( Optional.of( composeBlocks.get( 0 ) ), composeBlocks.get( 1 ) ) );
		assertEquals( C4 - C4 + 2, formBlockProvider.getTransposePitch( Optional.of( composeBlocks.get( 1 ) ), composeBlocks.get( 2 ) ) );
		assertEquals( E5 - B5, formBlockProvider.getTransposePitch( Optional.of( composeBlocks.get( 2 ) ), composeBlocks.get( 3 ) ) );

	}
}
