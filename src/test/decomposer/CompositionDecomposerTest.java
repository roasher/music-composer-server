package decomposer;

import static jm.JMC.WHOLE_NOTE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helper.AbstractSpringTest;
import jm.JMC;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import utils.CompositionLoader;

public class CompositionDecomposerTest extends AbstractSpringTest {

	@Autowired
	private CompositionLoader compositionLoader;
	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Ignore
	@Test
	public void getLexiconTest() {
		String fileName = "AABC1_with_base.mid";
		Composition composition = compositionLoader.getComposition( new File( "src/test/decomposer/form/formDecomposer/simpleMelodies/" + fileName ) );
		Lexicon lexicon = compositionDecomposer.decompose( composition, WHOLE_NOTE );
		// TODO implementation
	}

	@Ignore
	@Test
	public void singleMelodyTest() {
		Lexicon lexicon = compositionDecomposer
				.decompose( compositionLoader.getComposition( new File( "src/test/decomposer/gen_1" + ".mid" ) ), JMC.WHOLE_NOTE );
		// TODO impl
		lexicon.get( 0 );
	}

	@Test
	public void validBlockPossibleSurroundingTest() {
		List<Composition> compositionList = compositionLoader
				.getCompositionsFromFolder( new File( "src/test/composer/simpleMelodies" ), Collections.<String>emptyList() );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		for ( ComposeBlock composeBlock : lexicon.getComposeBlockList() ) {
			boolean isFirst = composeBlock.getPossiblePreviousComposeBlocks().isEmpty() && composeBlock.getPossibleNextComposeBlocks().size() >= 1;
			boolean isLast = composeBlock.getPossiblePreviousComposeBlocks().size() >= 1 && composeBlock.getPossibleNextComposeBlocks().isEmpty();
			boolean isMiddle = composeBlock.getPossiblePreviousComposeBlocks().size() >= 1 && composeBlock.getPossibleNextComposeBlocks().size() >= 1;
			assertTrue( isFirst || isLast || isMiddle );
		}
	}

	@Test
	public void possibleNextComposeBlocksTest() {
		List<Composition> compositionList = compositionLoader
				.getCompositionsFromFolder( new File( "src/test/composer/simpleMelodies" ), Collections.emptyList() );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, WHOLE_NOTE );

		ComposeBlock firstComposeBlock = null;
		ComposeBlock secondPossibleComposeBlock = null;
		for ( ComposeBlock composeBlock : lexicon.getComposeBlockList() ) {
			if ( composeBlock.getStartTime() == 8.0 && composeBlock.getCompositionInfo().getTitle().contains( "first" ) ) {
				firstComposeBlock = composeBlock;
			}
			if ( composeBlock.getStartTime() == 8.5 && composeBlock.getCompositionInfo().getTitle().contains( "second" ) ) {
				secondPossibleComposeBlock = composeBlock;
			}
		}
		assertNotNull( firstComposeBlock );
		assertNotNull( secondPossibleComposeBlock );

		List<ComposeBlock> listOfPossibleMusicBlocks = firstComposeBlock.getPossibleNextComposeBlocks();
		assertTrue( listOfPossibleMusicBlocks.contains( secondPossibleComposeBlock ) );
	}
}
