package decomposer;

import composer.MusicBlockProvider;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.music.data.Note;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositionDecomposerMockTest extends AbstractSpringTest {

	@InjectMocks
	private CompositionDecomposer compositionDecomposer;

	@Mock
	private MusicBlockProvider musicBlockProvider;

	@Before
	public void init() {
		MockitoAnnotations.initMocks( this );
	}

	@Test
	public void isTwoLinked() {

		MusicBlock musicBlock0 = mock( MusicBlock.class );
		MusicBlock musicBlock1 = mock( MusicBlock.class );
		MusicBlock musicBlock2 = mock( MusicBlock.class );
		MusicBlock musicBlock3 = mock( MusicBlock.class );

		List<MusicBlock> inputMusicBlock = new ArrayList<MusicBlock>(  );
		inputMusicBlock.add( musicBlock0 );
		inputMusicBlock.add( musicBlock1 );
		inputMusicBlock.add( musicBlock2 );
		inputMusicBlock.add( musicBlock3 );

		when( musicBlockProvider.getAllPossibleNextVariantNumbers( any( Integer.class ), any( List.class ) ) )
		  .thenReturn( Arrays.asList( 1, 2 ) )
		  .thenReturn( Arrays.asList( 0, 2, 3 ) )
		  .thenReturn( Arrays.asList( 0, 1 ) )
		  .thenReturn( Arrays.asList( 1 ) );

		List<ComposeBlock> composeBlockList = compositionDecomposer.getComposeBlocks( inputMusicBlock );

		ComposeBlock composeBlock0 = composeBlockList.get( 0 );
		ComposeBlock composeBlock1 = composeBlockList.get( 1 );
		ComposeBlock composeBlock2 = composeBlockList.get( 2 );
		ComposeBlock composeBlock3 = composeBlockList.get( 3 );

		assertTrue( composeBlock0.getPossibleNextComposeBlocks().contains( composeBlock1 ) );
		assertTrue( composeBlock0.getPossibleNextComposeBlocks().contains( composeBlock2 ) );
		assertTrue( composeBlock0.getPossiblePreviousComposeBlocks().contains( composeBlock1 ) );
		assertTrue( composeBlock0.getPossiblePreviousComposeBlocks().contains( composeBlock2 ) );

		assertTrue( composeBlock1.getPossibleNextComposeBlocks().contains( composeBlock0 ) );
		assertTrue( composeBlock1.getPossibleNextComposeBlocks().contains( composeBlock2 ) );
		assertTrue( composeBlock1.getPossibleNextComposeBlocks().contains( composeBlock3 ) );
		assertTrue( composeBlock1.getPossiblePreviousComposeBlocks().contains( composeBlock0 ) );
		assertTrue( composeBlock1.getPossiblePreviousComposeBlocks().contains( composeBlock2 ) );
		assertTrue( composeBlock1.getPossiblePreviousComposeBlocks().contains( composeBlock3 ) );

		assertTrue( composeBlock2.getPossibleNextComposeBlocks().contains( composeBlock0 ) );
		assertTrue( composeBlock2.getPossibleNextComposeBlocks().contains( composeBlock1 ) );
		assertTrue( composeBlock2.getPossiblePreviousComposeBlocks().contains( composeBlock0 ) );
		assertTrue( composeBlock2.getPossiblePreviousComposeBlocks().contains( composeBlock1 ) );

		assertTrue( composeBlock3.getPossibleNextComposeBlocks().contains( composeBlock1 ) );
		assertTrue( composeBlock3.getPossiblePreviousComposeBlocks().contains( composeBlock1 ) );
	}

	@Test
	public void getComposeBlocksTest() {

		MusicBlock musicBlock0 = new MusicBlock( Arrays.asList( new Melody[]{ new Melody( new Note( 60, 0 ) )} ), null );
		MusicBlock musicBlock1 = new MusicBlock( Arrays.asList( new Melody[]{ new Melody( new Note( 61, 1 ) )} ), null );
		MusicBlock musicBlock2 = new MusicBlock( Arrays.asList( new Melody[]{ new Melody( new Note( 62, 2 ) )} ), null );
		MusicBlock musicBlock3 = new MusicBlock( Arrays.asList( new Melody[]{ new Melody( new Note( 63, 3 ) )} ), null );
		MusicBlock musicBlock4 = new MusicBlock( Arrays.asList( new Melody[]{ new Melody( new Note( 64, 4 ) )} ), null );

		List<MusicBlock> inputMusicBlock = new ArrayList<MusicBlock>(  );
		inputMusicBlock.add( musicBlock0 );
		inputMusicBlock.add( musicBlock1 );
		inputMusicBlock.add( musicBlock2 );
		inputMusicBlock.add( musicBlock3 );
		inputMusicBlock.add( musicBlock4 );

		when( musicBlockProvider.getAllPossibleNextVariantNumbers( any( Integer.class ), any( List.class ) ) )
				.thenReturn( Arrays.asList( 1, 3, 4 ) )
				.thenReturn( Arrays.asList( 0, 4 ) )
				.thenReturn( Arrays.asList( 0, 3, 4 ) )
				.thenReturn( Arrays.asList( 0, 2 ) )
				.thenReturn( Arrays.asList( 0, 1, 2 ) );
		List<ComposeBlock> composeBlockList = compositionDecomposer.getComposeBlocks( inputMusicBlock );
		assertEquals( inputMusicBlock.size(), composeBlockList.size() );

		assertEquals( 3, composeBlockList.get( 0 ).getPossibleNextComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 1 ).getPossibleNextComposeBlocks().size() );
		assertEquals( 3, composeBlockList.get( 2 ).getPossibleNextComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 3 ).getPossibleNextComposeBlocks().size() );
		assertEquals( 3, composeBlockList.get( 4 ).getPossibleNextComposeBlocks().size() );

		assertEquals( 4, composeBlockList.get( 0 ).getPossiblePreviousComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 1 ).getPossiblePreviousComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 2 ).getPossiblePreviousComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 3 ).getPossiblePreviousComposeBlocks().size() );
		assertEquals( 3, composeBlockList.get( 4 ).getPossiblePreviousComposeBlocks().size() );

		assertEquals( 2, composeBlockList.get( 0 ).getPossibleNextComposeBlocks().get( 1 ).getPossibleNextComposeBlocks().size() );
		assertEquals( 3, composeBlockList.get( 1 ).getPossibleNextComposeBlocks().get( 1 ).getPossibleNextComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 2 ).getPossibleNextComposeBlocks().get( 0 ).getPossibleNextComposeBlocks().get( 0 ).getPossibleNextComposeBlocks().size() );
		assertEquals( 3, composeBlockList.get( 3 ).getPossibleNextComposeBlocks().get( 1 ).getPossibleNextComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 4 ).getPossibleNextComposeBlocks().get( 1 ).getPossibleNextComposeBlocks().size() );

		assertEquals( 2, composeBlockList.get( 0 ).getPossiblePreviousComposeBlocks().get( 1 ).getPossiblePreviousComposeBlocks().size() );
		assertEquals( 3, composeBlockList.get( 1 ).getPossiblePreviousComposeBlocks().get( 1 ).getPossiblePreviousComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 4 ).getPossiblePreviousComposeBlocks().get( 1 ).getPossiblePreviousComposeBlocks().size() );

		assertTrue( isValidForwardRoute( composeBlockList, musicBlock0, musicBlock3, musicBlock0, musicBlock3, musicBlock2, musicBlock4 ) );
		assertTrue( isValidForwardRoute( composeBlockList, musicBlock1, musicBlock0, musicBlock1, musicBlock4, musicBlock2, musicBlock3 ) );
		assertTrue( isValidForwardRoute( composeBlockList, musicBlock2, musicBlock0, musicBlock3 ) );
		assertTrue( isValidForwardRoute( composeBlockList, musicBlock2, musicBlock0, musicBlock3, musicBlock2 ) );
		assertTrue( isValidForwardRoute( composeBlockList, musicBlock4, musicBlock1, musicBlock0, musicBlock3 ) );

		assertFalse( isValidForwardRoute( composeBlockList, musicBlock0, musicBlock2 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock1, musicBlock1 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock1, musicBlock2 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock2, musicBlock1 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock3, musicBlock4 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock4, musicBlock3 ) );

		assertFalse( isValidForwardRoute( composeBlockList, musicBlock0, musicBlock3, musicBlock2, musicBlock1 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock1, musicBlock4, musicBlock3 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock2, musicBlock3, musicBlock1 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock3, musicBlock0, musicBlock3, musicBlock0, musicBlock2, musicBlock4 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock4, musicBlock2, musicBlock1, musicBlock0, musicBlock1 ) );

		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock0, musicBlock3, musicBlock0, musicBlock3, musicBlock2, musicBlock4 ) );
		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock1, musicBlock0, musicBlock1, musicBlock4, musicBlock2, musicBlock3 ) );
		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock3, musicBlock2 ) );
		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock4, musicBlock0, musicBlock2 ) );
		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock4, musicBlock1, musicBlock0, musicBlock3 ) );

		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock1, musicBlock2 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock1, musicBlock1 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock1, musicBlock3 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock1 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock3, musicBlock4 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock4, musicBlock3 ) );

		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock0, musicBlock3, musicBlock2, musicBlock1 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock1, musicBlock4, musicBlock3 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock3, musicBlock1 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock3, musicBlock0, musicBlock3, musicBlock0, musicBlock3, musicBlock4 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock4, musicBlock2, musicBlock1, musicBlock0, musicBlock1 ) );
	}

	private boolean isValidForwardRoute( List<ComposeBlock> composeBlocks, MusicBlock... route ) {
		List<ComposeBlock> currentRoutLexicon = composeBlocks;
		nextRoute: for ( MusicBlock routState : route ) {
			for ( ComposeBlock lexiconBlock : currentRoutLexicon ) {
				if ( lexiconBlock.isSimilar( routState ) ) {
					currentRoutLexicon = lexiconBlock.getPossibleNextComposeBlocks();
					continue nextRoute;
				}
			}
			return false;
		}
		return true;
	}

	private boolean isValidBackwardRoute( List<ComposeBlock> composeBlocks, MusicBlock... route ) {
		List<ComposeBlock> currentRoutLexicon = composeBlocks;
		nextRoute: for ( MusicBlock routState : route ) {
			for ( ComposeBlock lexiconBlock : currentRoutLexicon ) {
				if ( lexiconBlock.isSimilar( routState ) ) {
					currentRoutLexicon = lexiconBlock.getPossiblePreviousComposeBlocks();
					continue nextRoute;
				}
			}
			return false;
		}
		return true;
	}
}
