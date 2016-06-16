package composer;

import static jm.constants.Durations.DOTTED_HALF_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helper.AbstractSpringTest;
import jm.music.data.Note;
import jm.music.data.Rest;
import model.BlockMovement;
import model.MusicBlock;
import model.melody.Melody;

/**
 * Created by pyurkin on 16.02.15.
 */
public class MusicBlockProviderTest extends AbstractSpringTest {

	@Autowired
	private MusicBlockProvider musicBlockProvider;

	@Test
	public void getAllPossibleNextMapTest() {

		List<MusicBlock> musicBlocks = getMusicBlockMocks();

		MusicBlockProvider mockProvider = spy( musicBlockProvider );

		doReturn( false ).when( mockProvider ).isPossibleNext( any( MusicBlock.class ), any( MusicBlock.class ) );
		doReturn( true ).when( mockProvider ).isPossibleNext( musicBlocks.get( 0 ), musicBlocks.get( 2 ) );
		doReturn( true ).when( mockProvider ).isPossibleNext( musicBlocks.get( 1 ), musicBlocks.get( 1 ) );
		doReturn( true ).when( mockProvider ).isPossibleNext( musicBlocks.get( 0 ), musicBlocks.get( 3 ) );
		doReturn( true ).when( mockProvider ).isPossibleNext( musicBlocks.get( 2 ), musicBlocks.get( 1 ) );

		Map<Integer, List<Integer>> allPossibleNextVariants = mockProvider.getAllPossibleNextVariants( musicBlocks );

		assertEquals( musicBlocks.size(), allPossibleNextVariants.size() );
		assertEquals( Arrays.asList( 2, 3 ), allPossibleNextVariants.get( 0 ) );
		assertEquals( Arrays.asList( 1 ), allPossibleNextVariants.get( 1 ) );
		assertEquals( Arrays.asList( 1 ), allPossibleNextVariants.get( 2 ) );
		assertEquals( Arrays.asList(  ), allPossibleNextVariants.get( 3 ) );
		assertEquals( Arrays.asList(  ), allPossibleNextVariants.get( 4 ) );
	}

	private List<MusicBlock> getMusicBlockMocks() {

		MusicBlock musicBlock0 = mock( MusicBlock.class );
		MusicBlock musicBlock1 = mock( MusicBlock.class );
		MusicBlock musicBlock2 = mock( MusicBlock.class );
		MusicBlock musicBlock3 = mock( MusicBlock.class );
		MusicBlock musicBlock4 = mock( MusicBlock.class );

		List<MusicBlock> musicBlockList = new ArrayList<>(  );
		musicBlockList.add( musicBlock0 );
		musicBlockList.add( musicBlock1 );
		musicBlockList.add( musicBlock2 );
		musicBlockList.add( musicBlock3 );
		musicBlockList.add( musicBlock4 );

		return musicBlockList;
	}

	@Test
	public void getPreviousEndIntervalPatternWithoutRests() {
		MusicBlock musicBlock = new MusicBlock( null,
				new Melody(
						new Note(14, QUARTER_NOTE),
						new Rest(QUARTER_NOTE),
						new Note(135, QUARTER_NOTE)
				),
				new Melody(
						new Note(7, DOTTED_HALF_NOTE)
				),
				new Melody(
						new Note(1, HALF_NOTE),
						new Rest(QUARTER_NOTE)
				));
		musicBlock.setBlockMovementFromPreviousToThis( new BlockMovement( -1, 1, -1 ) );
		assertEquals( Arrays.asList( 4, 9 ),musicBlockProvider.getPreviousEndIntervalPattern( musicBlock ) );
	}

	@Test
	public void getPreviousEndIntervalPatternSingleRest() {
		MusicBlock musicBlock = new MusicBlock( null,
				new Melody(
						new Note(4, QUARTER_NOTE),
						new Rest(QUARTER_NOTE),
						new Note(135, QUARTER_NOTE)
				),
				new Melody(
						new Rest(DOTTED_HALF_NOTE)
				),
				new Melody(
						new Note(0, HALF_NOTE),
						new Rest(QUARTER_NOTE)
				));
		musicBlock.setBlockMovementFromPreviousToThis( new BlockMovement( -1, 1, -1 ) );
		assertEquals( Arrays.asList( Note.REST, 4 ),musicBlockProvider.getPreviousEndIntervalPattern( musicBlock ) );
	}

	@Test
	public void getPreviousEndIntervalPatternAllRests() {
		MusicBlock musicBlock = new MusicBlock( null,
				new Melody(
						new Rest(QUARTER_NOTE),
						new Note(135, QUARTER_NOTE)
				),
				new Melody(
						new Rest(HALF_NOTE)
				),
				new Melody(
						new Rest(QUARTER_NOTE),
						new Note(0, QUARTER_NOTE)
				));
		musicBlock.setBlockMovementFromPreviousToThis( new BlockMovement( -10, 100, -100 ) );
		assertEquals( Arrays.asList( Note.REST, Note.REST ),musicBlockProvider.getPreviousEndIntervalPattern( musicBlock ) );
	}
}
