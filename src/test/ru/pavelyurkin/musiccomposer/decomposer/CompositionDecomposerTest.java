package ru.pavelyurkin.musiccomposer.decomposer;

import helper.AbstractSpringTest;
import jm.JMC;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.model.Lexicon;
import ru.pavelyurkin.musiccomposer.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.utils.CompositionLoader;
import ru.pavelyurkin.musiccomposer.utils.ModelUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static jm.JMC.WHOLE_NOTE;
import static org.junit.Assert.*;

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
		// TODO particularly validate
		lexicon.get( 0 );
	}

	@Test
	public void singleVoiceMelodyTest() {
		Lexicon lexicon = compositionDecomposer.decompose( compositionLoader.getComposition( new File( "src/test/ru/pavelyurkin/musiccomposer/decomposer/gen_1.mid" ) ), JMC.WHOLE_NOTE );
		if ( ModelUtils.isTimeCorrelated( 1.1, 1.0 ) ) {
			//	if time correlation is disabled
			assertEquals( 6, lexicon.getComposeBlockList().size() );
			lexicon.getComposeBlockList().forEach( composeBlock -> {
				if ( composeBlock.getBlockMovementFromPreviousToThis() != null ) {
					assertEquals( 6, composeBlock.getPossiblePreviousComposeBlocks().size() );
					assertEquals( 5, composeBlock.getPossibleNextComposeBlocks().size() );
				} else {
					assertEquals( 0, composeBlock.getPossiblePreviousComposeBlocks().size() );
					assertEquals( 5, composeBlock.getPossibleNextComposeBlocks().size() );
				}
			} );
		} else {
			//	if time correlation is enabled
			assertEquals( 11, lexicon.getComposeBlockList().size() );
			lexicon.getComposeBlockList().forEach( composeBlock -> {
				if ( composeBlock.getBlockMovementFromPreviousToThis() != null ) {
					if ( composeBlock.getPossiblePreviousComposeBlocks().contains( lexicon.get( 0 ) ) ) {
						assertEquals( 6, composeBlock.getPossiblePreviousComposeBlocks().size() );
					} else {
						assertEquals( 5, composeBlock.getPossiblePreviousComposeBlocks().size() );
					}
					assertEquals( 5, composeBlock.getPossibleNextComposeBlocks().size() );
				} else {
					assertEquals( 0, composeBlock.getPossiblePreviousComposeBlocks().size() );
					assertEquals( 5, composeBlock.getPossibleNextComposeBlocks().size() );
				}
			} );
		}
	}

	@Test
	public void validBlockPossibleSurroundingTest() {
		List<Composition> compositionList = compositionLoader
				.getCompositionsFromFolder( new File( "src/test/resource/simpleMelodies" ), Collections.<String>emptyList() );
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
				.getCompositionsFromFolder( new File( "src/test/resource/simpleMelodies" ), Collections.emptyList() );
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
