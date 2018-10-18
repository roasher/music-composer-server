package ru.pavelyurkin.musiccomposer.core.decomposer;

import jm.music.data.Note;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.pavelyurkin.musiccomposer.core.composer.MusicBlockProvider;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class CompositionDecomposerMockTest {

	@InjectMocks
	private CompositionDecomposer compositionDecomposer;

	@Mock
	private MusicBlockProvider musicBlockProvider;

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

		Map<Integer, List<Integer>> map = new HashMap<>();
		map.put( 0, Arrays.asList( 1, 2 ) );
		map.put( 1, Arrays.asList( 0, 2, 3 ) );
		map.put( 2, Arrays.asList( 0, 1 ) );
		map.put( 3, Arrays.asList( 1 ) );

		when( musicBlockProvider.getAllPossibleNextVariants( any( List.class ) ) ).thenReturn( map );

		List<ComposeBlock> composeBlockList = compositionDecomposer.getComposeBlocks( inputMusicBlock ).getComposeBlockList();

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

		Map<Integer, List<Integer>> map = new HashMap<>();
		map.put( 0, Arrays.asList( 1, 3, 4 ) );
		map.put( 1, Arrays.asList( 0, 4 ) );
		map.put( 2, Arrays.asList( 0, 3, 4 ) );
		map.put( 3, Arrays.asList( 0, 2 ) );
		map.put( 4, Arrays.asList( 0, 1, 2 ) );

		when( musicBlockProvider.getAllPossibleNextVariants( any() ) ).thenReturn( map );

		List<ComposeBlock> composeBlockList = compositionDecomposer.getComposeBlocks( inputMusicBlock ).getComposeBlockList();
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
		List<ComposeBlock> routeComposeBlocks = Arrays.asList( route ).stream().map( o -> new ComposeBlock( o ) ).collect( Collectors.toList() );
		nextRoute: for ( ComposeBlock routState : routeComposeBlocks ) {
			for ( ComposeBlock lexiconBlock : currentRoutLexicon ) {
				if ( lexiconBlock.hasEqualsMusicBlock( routState ) ) {
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
		List<ComposeBlock> routeComposeBlocks = Arrays.asList( route ).stream().map( o -> new ComposeBlock( o ) ).collect( Collectors.toList() );
		nextRoute: for ( ComposeBlock routState : routeComposeBlocks ) {
			for ( ComposeBlock lexiconBlock : currentRoutLexicon ) {
				if ( lexiconBlock.hasEqualsMusicBlock( routState ) ) {
					currentRoutLexicon = lexiconBlock.getPossiblePreviousComposeBlocks();
					continue nextRoute;
				}
			}
			return false;
		}
		return true;
	}
}
