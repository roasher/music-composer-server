package composer;

import static jm.constants.Durations.DOTTED_HALF_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Rest;
import model.BlockMovement;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Melody;
import utils.CompositionLoader;

/**
 * Created by pyurkin on 16.02.15.
 */
public class MusicBlockProviderTest extends AbstractSpringTest {

	@Autowired
	private MusicBlockProvider musicBlockProvider;

	@Autowired
	private CompositionLoader compositionLoader;

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Test
	public void followTestCase() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src/test/composer/simpleMelodies" ) );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		Lexicon lexiconFromSecond = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );
		ComposeBlock current;
		ComposeBlock next;

		assertTrue( musicBlockProvider.isPossibleNext( lexiconFromFirst.get( 7 ).getMusicBlock(), lexiconFromSecond.get( 13 ).getMusicBlock() ) );
	}

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
		assertEquals( Arrays.asList( 1, 2, 3 ), allPossibleNextVariants.get( 0 ) );
		assertEquals( Arrays.asList( 2, 1 ), allPossibleNextVariants.get( 1 ) );
		assertEquals( Arrays.asList( 3, 1 ), allPossibleNextVariants.get( 2 ) );
		assertEquals( Arrays.asList( 4 ), allPossibleNextVariants.get( 3 ) );
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
	public void getPreviousEndIntervalPattern() throws Exception {
		MusicBlock musicBlock = new MusicBlock( null,
				new Melody(
						new Note(4, QUARTER_NOTE),
						new Rest(QUARTER_NOTE),
						new Note(135, QUARTER_NOTE)
				),
				new Melody(
						new Note(3, DOTTED_HALF_NOTE)
				),
				new Melody(
						new Note(0, HALF_NOTE),
						new Rest(QUARTER_NOTE)
				));
		musicBlock.setBlockMovementFromPreviousToThis( new BlockMovement( -1, 1, -1 ) );
		assertEquals( Arrays.asList( 1, 3 ),musicBlockProvider.getPreviousEndIntervalPattern( musicBlock ) );
	}
}
