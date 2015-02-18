package composer;

import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.BlockMovement;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
	public void getFirstConvenientMusicBlock1() {
		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		Lexicon lexiconFromSecond = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );

		List<MusicBlock> sum = new ArrayList<>( lexiconFromFirst.getMusicBlockList() );
		sum.addAll( lexiconFromSecond.getMusicBlockList() );
		Lexicon lexicon = new Lexicon( sum );

		MusicBlock firstConvenientMusicBlock = musicBlockProvider.getFirstConvenientMusicBlock( lexiconFromFirst.get( 8 ), lexicon.getMusicBlockList(  ) );
		assertEquals( lexiconFromSecond.get( 14 ), firstConvenientMusicBlock );
	}

	@Test
	public void followTestCase() {
		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
		Lexicon lexiconFromSecond = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );
		MusicBlock current = lexiconFromFirst.getMusicBlockList().get( 8 );
		MusicBlock next = lexiconFromSecond.getMusicBlockList().get( 13 );
		assertTrue( musicBlockProvider.canSubstitute( current, next ) );
	}

	@Test
	public void timeCorrelationTest() {

		class MockFactory {
			private List<Integer> startIntervalPattern = Collections.EMPTY_LIST;
			private BlockMovement blockMovement = new BlockMovement();

			private MusicBlock getOrigin( double preRhythmValue, double startTime ) {
				MusicBlock preFirst = mock( MusicBlock.class );
				when( preFirst.getRhythmValue()).thenReturn( preRhythmValue );
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

		//		MusicBlock musicBlock01 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 );
		//		MusicBlock musicBlock02 = mockFactory.getSubstitutor( JMC.DOUBLE_DOTTED_HALF_NOTE, 0.11 );
		//		assertTrue( compositionComposer.canSubstitute( musicBlock01, musicBlock02 ) );

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
}
