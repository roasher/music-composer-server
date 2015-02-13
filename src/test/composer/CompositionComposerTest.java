package composer;

import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import jm.util.View;
import junit.framework.Assert;
import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Form;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pyurkin on 15.12.14.
 */
public class CompositionComposerTest extends AbstractSpringTest {

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Autowired
	private CompositionLoader compositionLoader;

	@InjectMocks
	private CompositionComposer compositionComposer;

	@Mock
	private FormBlockProvider formBlockProvider;

	@Before
	public void init() {
		MockitoAnnotations.initMocks( this );
	}

	@Test
	@Ignore
	public void getSimplePieceTest() {
		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( lexicon, "AAAB", 4 * JMC.WHOLE_NOTE );

		assertNotNull( composition );
		View.show( composition );
		Utils.suspend();
	}

	@Test
	public void composeStepsTest() {

		List< MusicBlock > musicBlocks = new ArrayList<>(  );
		for ( int musicBlockNumber = 0; musicBlockNumber < 10; musicBlockNumber ++ ) {
			musicBlocks.add( getMockMusicBlock( musicBlockNumber ) );
		}

		when( formBlockProvider.getFormElement( any( Form.class ), any( Double.class ), any( List.class ), any( Lexicon.class ) ) )
		  .thenReturn( musicBlocks.get( 0 ) ).thenReturn( musicBlocks.get( 1 ) ).thenReturn( musicBlocks.get( 2 ) ).thenReturn( null )
		.thenReturn( null ).thenReturn( musicBlocks.get( 3 ) ).thenReturn( null ).thenReturn( null ).thenReturn( musicBlocks.get( 4 ) ).thenReturn( musicBlocks.get( 5 ) )
		.thenReturn( musicBlocks.get( 6 ) ).thenReturn( musicBlocks.get( 7 ) ).thenReturn( musicBlocks.get( 8 ) );

		List<CompositionStep> compositionSteps = compositionComposer.composeSteps( null, "ABCD", JMC.WHOLE_NOTE );
		Assert.assertEquals( 4, compositionSteps.size() );
		Assert.assertEquals( 4., compositionSteps.get( 0 ).getMusicBlock().getStartTime() );
		Assert.assertEquals( 5., compositionSteps.get( 1 ).getMusicBlock().getStartTime() );
		Assert.assertEquals( 6., compositionSteps.get( 2 ).getMusicBlock().getStartTime() );
		Assert.assertEquals( 7., compositionSteps.get( 3 ).getMusicBlock().getStartTime() );

	}

	@Test
	public void composeStepsTest2() {

		List< MusicBlock > musicBlocks = new ArrayList<>(  );
		for ( int musicBlockNumber = 0; musicBlockNumber < 10; musicBlockNumber ++ ) {
			musicBlocks.add( getMockMusicBlock( musicBlockNumber ) );
		}

		when( formBlockProvider.getFormElement( any( Form.class ), any( Double.class ), any( List.class ), any( Lexicon.class ) ) )
		  .thenReturn( musicBlocks.get( 0 ) ).thenReturn( musicBlocks.get( 1 ) ).thenReturn( musicBlocks.get( 2 ) ).thenReturn( null )
		  .thenReturn( musicBlocks.get( 3 ) ).thenReturn( null ).thenReturn( musicBlocks.get( 4 ) ).thenReturn( musicBlocks.get( 5 ) )
		  .thenReturn( musicBlocks.get( 6 ) ).thenReturn( musicBlocks.get( 7 ) ).thenReturn( musicBlocks.get( 8 ) );

		List<CompositionStep> compositionSteps = compositionComposer.composeSteps( null, "ABCD", 2*JMC.WHOLE_NOTE );
		Assert.assertEquals( 4, compositionSteps.size() );
		Assert.assertEquals( 0., compositionSteps.get( 0 ).getMusicBlock().getStartTime() );
		Assert.assertEquals( 1., compositionSteps.get( 1 ).getMusicBlock().getStartTime() );
		Assert.assertEquals( 4., compositionSteps.get( 2 ).getMusicBlock().getStartTime() );
		Assert.assertEquals( 5., compositionSteps.get( 3 ).getMusicBlock().getStartTime() );

	}

	@Test
	public void composeStepsFailing() {

		List< MusicBlock > musicBlocks = new ArrayList<>(  );
		for ( int musicBlockNumber = 0; musicBlockNumber < 10; musicBlockNumber ++ ) {
			musicBlocks.add( getMockMusicBlock( musicBlockNumber ) );
		}

		when( formBlockProvider.getFormElement( any( Form.class ), any( Double.class ), any( List.class ), any( Lexicon.class ) ) )
		  .thenReturn( musicBlocks.get( 0 ) ).thenReturn( musicBlocks.get( 1 ) ).thenReturn( musicBlocks.get( 2 ) ).thenReturn( null )
		  .thenReturn( null ).thenReturn( null ).thenReturn( null ).thenReturn( musicBlocks.get( 3 ) ).thenReturn( null ).thenReturn( null ).thenReturn( musicBlocks.get( 4 ) ).thenReturn( musicBlocks.get( 5 ) )
		  .thenReturn( musicBlocks.get( 6 ) ).thenReturn( musicBlocks.get( 7 ) ).thenReturn( musicBlocks.get( 8 ) );

		List<CompositionStep> compositionSteps = compositionComposer.composeSteps( null, "ABCD", JMC.WHOLE_NOTE );
		Assert.assertEquals( 0, compositionSteps.size() );
	}

	private MusicBlock getMockMusicBlock( double mockStartTime ) {
		MusicBlock musicBlock = mock( MusicBlock.class );
		when( musicBlock.getStartTime() ).thenReturn( mockStartTime );
		return musicBlock;
	}

//	@Test
//	public void followTestCase() {
//		List< Composition > compositionList = compositionLoader.getCompositions( new File( "src\\test\\composer\\simpleMelodies" ), Collections.<String>emptyList() );
//		List< MusicBlock > lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );
//		List< MusicBlock > lexiconFromSecond = compositionDecomposer.decompose( compositionList.get( 1 ), JMC.WHOLE_NOTE );
//		MusicBlock current = lexiconFromFirst.get( 8 );
//		MusicBlock next = lexiconFromSecond.get( 13 );
//		assertTrue( compositionComposer.canSubstitute( current, next ) );
//	}

//	@Test
//	public void timeCorrelationTest() {
//
//		class MockFactory {
//			private List<Integer> startIntervalPattern = Collections.EMPTY_LIST;
//			private BlockMovement blockMovement = new BlockMovement();
//
//			private MusicBlock getOrigin( double preRhythmValue, double startTime ) {
//				MusicBlock preFirst = mock( MusicBlock.class );
//				when( preFirst.getRhythmValue()).thenReturn( preRhythmValue );
//				MusicBlock first = mock( MusicBlock.class );
//				when( first.getPrevious() ).thenReturn( preFirst );
//				when( first.getStartTime() ).thenReturn( startTime );
//				when( first.getStartIntervalPattern() ).thenReturn( startIntervalPattern );
//				when( first.getBlockMovementFromPreviousToThis() ).thenReturn( blockMovement );
//				return first;
//			}
//
//			private MusicBlock getSubstitutor( double mockStartTime, double startTime ) {
//				MusicBlock first = mock( MusicBlock.class );
//				when( first.getRhythmValue() ).thenReturn( mockStartTime );
//				when( first.getStartTime() ).thenReturn( startTime );
//				when( first.getStartIntervalPattern() ).thenReturn( startIntervalPattern );
//				when( first.getBlockMovementFromPreviousToThis() ).thenReturn( blockMovement );
//				return first;
//			}
//		}
//
//		MockFactory mockFactory = new MockFactory();
//
////		MusicBlock musicBlock01 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 );
////		MusicBlock musicBlock02 = mockFactory.getSubstitutor( JMC.DOUBLE_DOTTED_HALF_NOTE, 0.11 );
////		assertTrue( compositionComposer.canSubstitute( musicBlock01, musicBlock02 ) );
//
//		MusicBlock musicBlock11 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 );
//		MusicBlock musicBlock12 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 0.11 );
//		assertFalse( compositionComposer.canSubstitute( musicBlock11, musicBlock12 ) );
//
//		MusicBlock musicBlock21 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 );
//		MusicBlock musicBlock22 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 0 );
//		assertTrue( compositionComposer.canSubstitute( musicBlock21, musicBlock22 ) );
//
//		MusicBlock musicBlock31 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 + 0.5 );
//		MusicBlock musicBlock32 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 11 + 0.5 );
//		assertTrue( compositionComposer.canSubstitute( musicBlock31, musicBlock32 ) );
//
//		MusicBlock musicBlock41 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 20 + 0.75 );
//		MusicBlock musicBlock42 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 45 + 0.25 );
//		assertTrue( compositionComposer.canSubstitute( musicBlock41, musicBlock42 ) );
//
//		MusicBlock musicBlock51 = mockFactory.getOrigin( JMC.WHOLE_NOTE, 0.25 );
//		MusicBlock musicBlock52 = mockFactory.getSubstitutor( JMC.WHOLE_NOTE + JMC.QUARTER_NOTE_TRIPLET, 0.5 );
//		assertFalse( compositionComposer.canSubstitute( musicBlock51, musicBlock52 ) );
//
//		MusicBlock musicBlock61 = mockFactory.getOrigin( 2.0, 2 );
//		MusicBlock musicBlock62 = mockFactory.getSubstitutor( 0.5, 14.5 );
//		assertFalse( compositionComposer.canSubstitute( musicBlock61, musicBlock62 ) );
//
//	}
}
