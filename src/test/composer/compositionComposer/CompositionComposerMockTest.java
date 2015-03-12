package composer.compositionComposer;

import composer.CompositionComposer;
import composer.CompositionStep;
import composer.FormBlockProvider;
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
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;
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
public class CompositionComposerMockTest extends AbstractSpringTest {

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
//	@Ignore
	public void getSimplePieceTest() {
		List< Composition > compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( lexicon, "ABCD", 4 * JMC.WHOLE_NOTE );

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
		Assert.assertEquals( 4., compositionSteps.get( 0 ).getComposeBlock().getStartTime() );
		Assert.assertEquals( 5., compositionSteps.get( 1 ).getComposeBlock().getStartTime() );
		Assert.assertEquals( 6., compositionSteps.get( 2 ).getComposeBlock().getStartTime() );
		Assert.assertEquals( 7., compositionSteps.get( 3 ).getComposeBlock().getStartTime() );

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
		Assert.assertEquals( 0., compositionSteps.get( 0 ).getComposeBlock().getStartTime() );
		Assert.assertEquals( 1., compositionSteps.get( 1 ).getComposeBlock().getStartTime() );
		Assert.assertEquals( 4., compositionSteps.get( 2 ).getComposeBlock().getStartTime() );
		Assert.assertEquals( 5., compositionSteps.get( 3 ).getComposeBlock().getStartTime() );

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
}
