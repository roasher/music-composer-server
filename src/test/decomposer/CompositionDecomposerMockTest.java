package decomposer;

import composer.MusicBlockProvider;
import helper.AbstractSpringTest;
import model.ComposeBlock;
import model.MusicBlock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositionDecomposerMockTest extends AbstractSpringTest {

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
				.thenReturn( Arrays.asList( new MusicBlock[] { musicBlock2, musicBlock4, musicBlock5 } ) )
				.thenReturn( Arrays.asList( new MusicBlock[] { musicBlock1, musicBlock5 } ) )
				.thenReturn( Arrays.asList( new MusicBlock[]{ musicBlock1, musicBlock4, musicBlock5 } ) )
				.thenReturn( Arrays.asList( new MusicBlock[]{ musicBlock1, musicBlock3 } ) )
				.thenReturn( Arrays.asList( new MusicBlock[]{ musicBlock1, musicBlock2, musicBlock3 } ) );
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
		assertEquals( 4, composeBlockList.get( 2 ).getPossiblePreviousComposeBlocks().get( 0 ).getPossiblePreviousComposeBlocks().get( 0 ).getPossiblePreviousComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 3 ).getPossiblePreviousComposeBlocks().get( 1 ).getPossiblePreviousComposeBlocks().size() );
		assertEquals( 2, composeBlockList.get( 4 ).getPossiblePreviousComposeBlocks().get( 1 ).getPossiblePreviousComposeBlocks().size() );

		assertTrue( isValidForwardRoute( composeBlockList, musicBlock1, musicBlock4, musicBlock1, musicBlock4, musicBlock3, musicBlock5 ) );
		assertTrue( isValidForwardRoute( composeBlockList, musicBlock2, musicBlock1, musicBlock2, musicBlock5, musicBlock3, musicBlock4 ) );
		assertTrue( isValidForwardRoute( composeBlockList, musicBlock3, musicBlock1, musicBlock4 ) );
		assertTrue( isValidForwardRoute( composeBlockList, musicBlock3, musicBlock1, musicBlock4, musicBlock3 ) );
		assertTrue( isValidForwardRoute( composeBlockList, musicBlock5, musicBlock2, musicBlock1, musicBlock4 ) );

		assertFalse( isValidForwardRoute( composeBlockList, musicBlock1, musicBlock3 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock2, musicBlock2 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock2, musicBlock3 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock3, musicBlock2 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock4, musicBlock5 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock5, musicBlock4 ) );

		assertFalse( isValidForwardRoute( composeBlockList, musicBlock1, musicBlock4, musicBlock3, musicBlock2 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock2, musicBlock5, musicBlock4 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock3, musicBlock4, musicBlock2 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock4, musicBlock1, musicBlock4, musicBlock1, musicBlock3, musicBlock5 ) );
		assertFalse( isValidForwardRoute( composeBlockList, musicBlock5, musicBlock3, musicBlock2, musicBlock1, musicBlock2 ) );

		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock1, musicBlock4, musicBlock1, musicBlock4, musicBlock3, musicBlock5 ) );
		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock1, musicBlock2, musicBlock5, musicBlock3, musicBlock4 ) );
		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock3, musicBlock4, musicBlock3 ) );
		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock3, musicBlock5, musicBlock1, musicBlock3 ) );
		assertTrue( isValidBackwardRoute( composeBlockList, musicBlock5, musicBlock2, musicBlock1, musicBlock4 ) );

		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock3 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock2 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock4 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock3, musicBlock2 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock4, musicBlock5 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock5, musicBlock4 ) );

		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock1, musicBlock4, musicBlock3, musicBlock2 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock2, musicBlock5, musicBlock4 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock3, musicBlock4, musicBlock2 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock4, musicBlock1, musicBlock4, musicBlock1, musicBlock4, musicBlock5 ) );
		assertFalse( isValidBackwardRoute( composeBlockList, musicBlock5, musicBlock3, musicBlock2, musicBlock1, musicBlock2 ) );
	}

	private boolean isValidForwardRoute( List<ComposeBlock> composeBlocks, MusicBlock... route ) {
		List<ComposeBlock> currentRoutLexicon = composeBlocks;
		nextRoute: for ( MusicBlock routState : route ) {
			for ( ComposeBlock lexiconBlock : currentRoutLexicon ) {
				if ( routState == lexiconBlock.getMusicBlock() ) {
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
				if ( routState == lexiconBlock.getMusicBlock() ) {
					currentRoutLexicon = lexiconBlock.getPossiblePreviousComposeBlocks();
					continue nextRoute;
				}
			}
			return false;
		}
		return true;
	}
}
