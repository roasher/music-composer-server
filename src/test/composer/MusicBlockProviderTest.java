package composer;

import com.sun.java.swing.plaf.motif.resources.motif_ko;
import com.sun.org.apache.bcel.internal.generic.MULTIANEWARRAY;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import junit.framework.Assert;
import model.BlockMovement;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.util.*;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		Lexicon lexiconFromSecond = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );
		ComposeBlock current;
		ComposeBlock next;

		current = lexiconFromFirst.get( 8 );
		next = lexiconFromSecond.get( 13 );
		assertTrue( musicBlockProvider.canSubstitute( current, next ) );

		current = lexiconFromFirst.get( 1 );
		next = lexiconFromSecond.get( 1 );
		assertFalse( musicBlockProvider.canSubstitute( current, next ) );
	}

	@Test
	public void timeCorrelationTest() {

		class MockFactory {
			private List<Integer> startIntervalPattern = Collections.EMPTY_LIST;
			private BlockMovement blockMovement = new BlockMovement( 0, 0 );

			private MusicBlock getOrigin( double preRhythmValue, double startTime ) {
				MusicBlock preFirst = mock( MusicBlock.class );
				when( preFirst.getRhythmValue() ).thenReturn( preRhythmValue );
				MusicBlock first = mock( MusicBlock.class );
				when( first.getPrevious() ).thenReturn( preFirst );
				when( first.getStartTime() ).thenReturn( startTime );
				when( first.getStartIntervalPattern() ).thenReturn( startIntervalPattern );
				when( first.getBlockMovementFromPreviousToThis() ).thenReturn( blockMovement );
				return first;
			}

			private MusicBlock getSubstitutor( double mockStartTime, double startTime ) {
				MusicBlock first = mock( MusicBlock.class );
				when( first.getRhythmValue() ).thenReturn( mockStartTime );
				when( first.getStartTime() ).thenReturn( startTime );
				when( first.getStartIntervalPattern() ).thenReturn( startIntervalPattern );
				when( first.getBlockMovementFromPreviousToThis() ).thenReturn( blockMovement );
				return first;
			}
		}

		MockFactory mockFactory = new MockFactory();

		MusicBlock musicBlock11 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 );
		MusicBlock musicBlock12 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 0.11 );
		assertFalse( musicBlockProvider.canSubstitute( musicBlock11, musicBlock12 ) );

		MusicBlock musicBlock21 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 );
		MusicBlock musicBlock22 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 0 );
		assertTrue( musicBlockProvider.canSubstitute( musicBlock21, musicBlock22 ) );

		MusicBlock musicBlock31 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 + 0.5 );
		MusicBlock musicBlock32 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 11 + 0.5 );
		assertTrue( musicBlockProvider.canSubstitute( musicBlock31, musicBlock32 ) );

		MusicBlock musicBlock41 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 + 0.75 );
		MusicBlock musicBlock42 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 45 + 0.25 );
		assertTrue( musicBlockProvider.canSubstitute( musicBlock41, musicBlock42 ) );

		MusicBlock musicBlock51 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 0.25 );
		MusicBlock musicBlock52 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 0.5 );
		assertFalse( musicBlockProvider.canSubstitute( musicBlock51, musicBlock52 ) );

		MusicBlock musicBlock61 = mockFactory.getOrigin( 2.0, 2 );
		MusicBlock musicBlock62 = mockFactory.getSubstitutor( 0.5, 14.5 );
		assertFalse( musicBlockProvider.canSubstitute( musicBlock61, musicBlock62 ) );

	}

	@Test
	public void getAllPossibleNextMapTest() {

		List<MusicBlock> musicBlocks = getMusicBlockMocks();

		MusicBlockProvider mockProvider = spy( musicBlockProvider );

		doReturn( false ).when( mockProvider ).canSubstitute( any( MusicBlock.class ), any( MusicBlock.class ) );
		doReturn( true ).when( mockProvider ).canSubstitute( musicBlocks.get( 1 ), musicBlocks.get( 2 ) );
		doReturn( true ).when( mockProvider ).canSubstitute( musicBlocks.get( 3 ), musicBlocks.get( 1 ) );

		Map<Integer, List<Integer>> allPossibleNextVariants = mockProvider.getAllPossibleNextVariants( musicBlocks );

		assertEquals( musicBlocks.size(), allPossibleNextVariants.size() );
		assertEquals( Arrays.asList( 1, 2 ), allPossibleNextVariants.get( 0 ) );
		assertEquals( Arrays.asList( 2 ), allPossibleNextVariants.get( 1 ) );
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

		when( musicBlock0.getNext() ).thenReturn( musicBlock1 );
		when( musicBlock1.getNext() ).thenReturn( musicBlock2 );
		when( musicBlock2.getNext() ).thenReturn( musicBlock3 );
		when( musicBlock3.getNext() ).thenReturn( musicBlock4 );
		when( musicBlock4.getNext() ).thenReturn( null );

		List<MusicBlock> musicBlockList = new ArrayList<>(  );
		musicBlockList.add( musicBlock0 );
		musicBlockList.add( musicBlock1 );
		musicBlockList.add( musicBlock2 );
		musicBlockList.add( musicBlock3 );
		musicBlockList.add( musicBlock4 );

		return musicBlockList;
	}

}
