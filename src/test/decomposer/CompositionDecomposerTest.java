package decomposer;

import composer.MusicBlockProvider;
import helper.AbstractSpringTest;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jm.JMC;
import jm.music.data.Note;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Form;
import model.melody.Melody;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static jm.JMC.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositionDecomposerTest extends AbstractSpringTest {

	@Autowired
	private CompositionLoader compositionLoader;

	@InjectMocks
	private CompositionDecomposer compositionDecomposer;

	@Mock
	private MusicBlockProvider musicBlockProvider;

	@Before
	public void init() {
		MockitoAnnotations.initMocks( this );
	}

	@Test
	public void getComposeBlocksTest() {

		List<MusicBlock> inputMusicBlock = new ArrayList<MusicBlock>(  );
		MusicBlock musicBlock1 = mock( MusicBlock.class );
		MusicBlock musicBlock2 = mock( MusicBlock.class );
		MusicBlock musicBlock3 = mock( MusicBlock.class );
		MusicBlock musicBlock4 = mock( MusicBlock.class );
		MusicBlock musicBlock5 = mock( MusicBlock.class );

		inputMusicBlock.add( musicBlock1 );
		inputMusicBlock.add( musicBlock2 );
		inputMusicBlock.add( musicBlock3 );
		inputMusicBlock.add( musicBlock4 );
		inputMusicBlock.add( musicBlock5 );

		when( musicBlockProvider.getAllPossibleNextVariants( any( MusicBlock.class ), any( List.class ) ) )
				.thenReturn( Arrays.asList( new MusicBlock[] { musicBlock2, musicBlock5, musicBlock4 } ) )
				.thenReturn( Arrays.asList( new MusicBlock[] { musicBlock1, musicBlock5 } ) )
				.thenReturn( Arrays.asList( new MusicBlock[]{ musicBlock4, musicBlock5, musicBlock1 } ) )
				.thenReturn( Arrays.asList( new MusicBlock[]{ musicBlock1, musicBlock3 } ) )
				.thenReturn( Arrays.asList( new MusicBlock[]{ musicBlock1, musicBlock2, musicBlock3 } ) );
		List<ComposeBlock> composeBlockList = compositionDecomposer.getComposeBlocks( inputMusicBlock );
		assertEquals( inputMusicBlock.size(), composeBlockList.size() );

		ComposeBlock composeBlock1 = new ComposeBlock( musicBlock1 );
		ComposeBlock composeBlock2 = new ComposeBlock( musicBlock1 );
		ComposeBlock composeBlock3 = new ComposeBlock( musicBlock1 );
		ComposeBlock composeBlock4 = new ComposeBlock( musicBlock1 );
		ComposeBlock composeBlock5 = new ComposeBlock( musicBlock1 );

		composeBlock1.getPossibleNextComposeBlocks().add( composeBlock2 );
		composeBlock1.getPossibleNextComposeBlocks().add( composeBlock4 );
		composeBlock1.getPossibleNextComposeBlocks().add( composeBlock5 );

		composeBlock2.getPossibleNextComposeBlocks().add( composeBlock1 );
		composeBlock2.getPossibleNextComposeBlocks().add( composeBlock5 );

		composeBlock3.getPossibleNextComposeBlocks().add( composeBlock1 );
		composeBlock3.getPossibleNextComposeBlocks().add( composeBlock4 );
		composeBlock3.getPossibleNextComposeBlocks().add( composeBlock5 );

		composeBlock4.getPossibleNextComposeBlocks().add( composeBlock1 );
		composeBlock4.getPossibleNextComposeBlocks().add( composeBlock3 );

		composeBlock5.getPossibleNextComposeBlocks().add( composeBlock1 );
		composeBlock5.getPossibleNextComposeBlocks().add( composeBlock2 );
		composeBlock5.getPossibleNextComposeBlocks().add( composeBlock3 );

		assertEquals( composeBlock1.getPossibleNextComposeBlocks().size(), composeBlockList.get( 0 ).getPossibleNextComposeBlocks().size() );
		assertEquals( composeBlock2.getPossibleNextComposeBlocks().size(), composeBlockList.get( 1 ).getPossibleNextComposeBlocks().size() );
		assertEquals( composeBlock3.getPossibleNextComposeBlocks().size(), composeBlockList.get( 2 ).getPossibleNextComposeBlocks().size() );
		assertEquals( composeBlock4.getPossibleNextComposeBlocks().size(), composeBlockList.get( 3 ).getPossibleNextComposeBlocks().size() );
		assertEquals( composeBlock5.getPossibleNextComposeBlocks().size(), composeBlockList.get( 4 ).getPossibleNextComposeBlocks().size() );

		assertEquals( composeBlock3 ,composeBlockList.get( 0 ).getPossibleNextComposeBlocks().get( 1 ).getPossibleNextComposeBlocks().get( 1 ) );
		assertEquals( composeBlock1 ,composeBlockList.get( 0 ).getPossibleNextComposeBlocks().get( 2 ).getPossibleNextComposeBlocks().get( 0 ) );
		assertEquals( composeBlock2 ,composeBlockList.get( 0 ).getPossibleNextComposeBlocks().get( 1 ).getPossibleNextComposeBlocks().get( 4 ).getPossibleNextComposeBlocks().get( 2 )
		.getPossibleNextComposeBlocks().get( 1 ) );
		assertEquals( composeBlock3 ,composeBlockList.get( 4 ).getPossibleNextComposeBlocks().get( 2 ).getPossibleNextComposeBlocks().get( 2 ).getPossibleNextComposeBlocks().get( 2 ) );
		assertEquals( composeBlock4 ,composeBlockList.get( 1 ).getPossibleNextComposeBlocks().get( 1 ).getPossibleNextComposeBlocks().get( 1 ) );
		assertEquals( composeBlock5 ,composeBlockList.get( 3 ).getPossibleNextComposeBlocks().get( 1 ).getPossibleNextComposeBlocks().get( 0 ).getPossibleNextComposeBlocks().get( 2 )
		.getPossibleNextComposeBlocks().get( 2 ) );

	}

	@Test
	@Ignore
	public void getLexiconTest() {
		String fileName = "AABC1_with_base.mid";
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\simpleMelodies\\" + fileName ) );
		Lexicon lexicon = compositionDecomposer.decompose( composition, WHOLE_NOTE );

		int expectedMusicBlocksNumber = 8 + 8 + 3 + 3;
		assertEquals( lexicon.getComposeBlockList().size(), expectedMusicBlocksNumber );
		// length check
		double rhythmValue = 0;
		for ( int musicBlockNumber = 0; musicBlockNumber < expectedMusicBlocksNumber; musicBlockNumber ++ ) {
			// Music Block ckecks
			MusicBlock musicBlock = lexicon.getComposeBlockList().get( musicBlockNumber ).getMusicBlock();
			if ( musicBlockNumber != expectedMusicBlocksNumber - 1 ) {
				assertEquals( musicBlock.getNext(), lexicon.getComposeBlockList().get( musicBlockNumber + 1 ).getMusicBlock() );
			} else {
				assertEquals( musicBlock.getNext(), null );
			}

			if ( musicBlockNumber != 0 ) {
				assertEquals( musicBlock.getPrevious(), lexicon.getComposeBlockList().get( musicBlockNumber - 1 ).getMusicBlock() );
			} else {
				assertEquals( musicBlock.getPrevious(), null );
			}

			assertEquals( musicBlock.getCompositionInfo().getTitle(), fileName );

			// Melody ckecks
			List<Melody> melodyList = musicBlock.getMelodyList();
			assertEquals( melodyList.size(), 2 );

			Melody firstMelody = melodyList.get( 0 );
			Melody secondMelody = melodyList.get( 1 );
			assertEquals( firstMelody.getNoteArray().length, 1 );
			assertEquals( secondMelody.getNoteArray().length, 1 );
			assertEquals( firstMelody.getStartTime(), rhythmValue );
			assertEquals( secondMelody.getStartTime(), rhythmValue );
			rhythmValue += firstMelody.getNote( 0 ).getRhythmValue();

			Note firstNote = firstMelody.getNote( 0 );
			Note secondNote = secondMelody.getNote( 0 );
			// Note checks
			if ( musicBlockNumber < 8 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( musicBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'A' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( musicBlockNumber >= 8 && musicBlockNumber < 8 + 8 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( musicBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'A' ) );
				assertEquals( secondMelody.getForm(), new Form( 'B' ) );
			} else if ( musicBlockNumber == 8 + 8 || musicBlockNumber == 8 + 8 + 1 ) {
				assertEquals( firstNote.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( secondNote.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( musicBlock.getRhythmValue(), DOTTED_QUARTER_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'B' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( musicBlockNumber == 8 + 8 + 2 ) {
				assertEquals( firstNote.getRhythmValue(), QUARTER_NOTE );
				assertEquals( secondNote.getRhythmValue(), QUARTER_NOTE );
				assertEquals( musicBlock.getRhythmValue(), QUARTER_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'B' ) );
				assertEquals( secondMelody.getForm(), new Form( 'A' ) );
			} else if ( musicBlockNumber == 8 + 8 + 3 || musicBlockNumber == 8 + 8 + 4 ) {
				assertEquals( firstNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( secondNote.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( musicBlock.getRhythmValue(), EIGHTH_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'C' ) );
				assertEquals( secondMelody.getForm(), new Form( 'C' ) );
			} else {
				assertEquals( firstNote.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( secondNote.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( musicBlock.getRhythmValue(), DOTTED_HALF_NOTE );
				assertEquals( firstMelody.getForm(), new Form( 'C' ) );
				assertEquals( secondMelody.getForm(), new Form( 'C' ) );
			}
		}
	}

	@Test
	@Ignore
	public void test() {
		// TODO разобраться с импортом. Проблема если части заканчиваются не одномоментно
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Another Phoenix (midi)_2.mid" ) );
		Lexicon lexicon = compositionDecomposer.decompose( composition, JMC.WHOLE_NOTE );
	}

}
