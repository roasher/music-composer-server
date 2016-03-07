package decomposer;

import helper.AbstractSpringTest;
import jm.JMC;
import jm.music.data.Note;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Form;
import model.melody.Melody;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static jm.JMC.*;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class CompositionDecomposerTest extends AbstractSpringTest {

	@Autowired private CompositionLoader compositionLoader;
	@Autowired private CompositionDecomposer compositionDecomposer;

	@Test
	public void getLexiconTest() {
		String fileName = "AABC1_with_base.mid";
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\simpleMelodies\\" + fileName ) );
		Lexicon lexicon = compositionDecomposer.decompose( composition, WHOLE_NOTE );

		int expectedMusicBlocksNumber = 8 + 8 + 3 + 3;
		assertEquals( lexicon.getComposeBlockList().size(), expectedMusicBlocksNumber );
		// length check
		double rhythmValue = 0;
		for ( int composeBlockNumber = 0; composeBlockNumber < expectedMusicBlocksNumber; composeBlockNumber ++ ) {
			// Compose Block ckecks
			ComposeBlock composeBlock = lexicon.getComposeBlockList().get( composeBlockNumber );
			if ( composeBlockNumber != expectedMusicBlocksNumber - 1 ) {
				assertEquals( composeBlock.getNext( 0 ), lexicon.getComposeBlockList().get( composeBlockNumber + 1 ) );
			} else {
				assertEquals( composeBlock.getNext( 0 ), null );
			}

			if ( composeBlockNumber != 0 ) {
				assertEquals( composeBlock.getPrevious( 0 ), lexicon.getComposeBlockList().get( composeBlockNumber - 1 ) );
			} else {
				assertEquals( composeBlock.getPrevious( 0 ), null );
			}

			assertEquals( composeBlock.getCompositionInfo().getTitle(), fileName );

			// Melody ckecks
			List<Melody> melodyList = composeBlock.getMelodyList();
			assertEquals( melodyList.size(), 2 );

			Melody firstMelody = melodyList.get( 0 );
			Melody secondMelody = melodyList.get( 1 );
			assertEquals( firstMelody.size(), 1 );
			assertEquals( secondMelody.size(), 1 );
			assertEquals( firstMelody.getStartTime(), rhythmValue );
			assertEquals( secondMelody.getStartTime(), rhythmValue );
			rhythmValue += firstMelody.getNote( 0 ).getRhythmValue();

			Note firstNote = firstMelody.getNote( 0 );
			Note secondNote = secondMelody.getNote( 0 );
			// Note checks
			if ( composeBlockNumber < 8 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( composeBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'A' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( composeBlockNumber >= 8 && composeBlockNumber < 8 + 8 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( composeBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'A' ) );
				assertEquals( secondMelody.getForm(), new Form( 'B' ) );
			} else if ( composeBlockNumber == 8 + 8 || composeBlockNumber == 8 + 8 + 1 ) {
				assertEquals( firstNote.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( secondNote.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( composeBlock.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'B' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( composeBlockNumber == 8 + 8 + 2 ) {
				assertEquals( firstNote.getRhythmValue(), QUARTER_NOTE );
				assertEquals( secondNote.getRhythmValue(), QUARTER_NOTE );
				assertEquals( composeBlock.getRhythmValue(), QUARTER_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'B' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( composeBlockNumber == 8 + 8 + 3 || composeBlockNumber == 8 + 8 + 4 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( composeBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'C' ) );
				assertEquals( secondMelody.getForm(), new Form( 'C' ) );
			} else {
				assertEquals( firstNote.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( secondNote.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( composeBlock.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'C' ) );
				assertEquals( secondMelody.getForm(), new Form( 'C' ) );
			}
		}
	}

	@Test
	public void validBlockPossibleSurroundingTest() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
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
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, WHOLE_NOTE );

		ComposeBlock firstComposeBlock = null;
		ComposeBlock secondPossibleComposeBlock = null;
		for ( ComposeBlock  composeBlock : lexicon.getComposeBlockList() ) {
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

	@Test
	@Ignore
	public void test() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Another Phoenix (midi)_2.mid" ) );
		Lexicon lexicon = compositionDecomposer.decompose( composition, JMC.WHOLE_NOTE );
	}

	/**
	 * Tests if first possible next and previous is exact blocks from the original composition
	 */
	@Test
	public void isFirstPossibleFromOriginalComposition() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		// Decompose all melodies
		Lexicon lexiconFull = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		for ( ComposeBlock composeBlock : lexiconFull.getComposeBlockList() ) {
			if ( composeBlock.getPossiblePreviousComposeBlocks().size() > 0 ) {
				ComposeBlock originPrevious = composeBlock.getPossiblePreviousComposeBlocks().get( 0 );
				assertEquals( composeBlock.getPrevious( 0 ), originPrevious );
			}
			if ( composeBlock.getPossibleNextComposeBlocks().size() > 0 ) {
				ComposeBlock originNext = composeBlock.getPossibleNextComposeBlocks().get( 0 );
				assertEquals( composeBlock.getNext( 0 ), originNext );
			}
		}
	}

}
