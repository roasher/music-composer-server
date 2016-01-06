package composer.compositionComposer;

import composer.CompositionComposer;
import composer.step.CompositionStep;
import composer.FormBlockProvider;
import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.FormCompositionStep;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringTest;
import jm.JMC;
import model.ComposeBlock;
import model.Lexicon;
import model.melody.Form;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import utils.CompositionLoader;

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
	public void composeStepsTest() {

		List<List<ComposeBlock>> composeBlocks = new ArrayList<>();
		for ( int musicBlockNumber = 0; musicBlockNumber < 10; musicBlockNumber++ ) {
			composeBlocks.add( getMockComposeBlock( musicBlockNumber ) );
		}

		when( formBlockProvider.getFormElement( any( FirstBlockProvider.class ), any( NextBlockProvider.class ), any( Form.class ), any( Double.class ), any( List.class ),
				any( Lexicon.class ) ) ).thenReturn( composeBlocks.get( 0 ) ).thenReturn( composeBlocks.get( 1 ) ).thenReturn( composeBlocks.get( 2 ) ).thenReturn( null )
				.thenReturn( null ).thenReturn( composeBlocks.get( 3 ) ).thenReturn( null ).thenReturn( null ).thenReturn( composeBlocks.get( 4 ) )
				.thenReturn( composeBlocks.get( 5 ) ).thenReturn( composeBlocks.get( 6 ) ).thenReturn( composeBlocks.get( 7 ) ).thenReturn( composeBlocks.get( 8 ) );

		List<FormCompositionStep> compositionSteps = compositionComposer.composeSteps( null, null, null, "ABCD", JMC.WHOLE_NOTE );
		assertEquals( 4, compositionSteps.size() );
		assertEquals( 4., compositionSteps.get( 0 ).getComposeBlocks().get(0).getStartTime(), 0 );
		assertEquals( 5., compositionSteps.get( 1 ).getComposeBlocks().get(0).getStartTime(), 0 );
		assertEquals( 6., compositionSteps.get( 2 ).getComposeBlocks().get(0).getStartTime(), 0 );
		assertEquals( 7., compositionSteps.get( 3 ).getComposeBlocks().get(0).getStartTime(), 0 );

	}

	@Test
	public void composeStepsTest2() {

		List<List<ComposeBlock>> composeBlocks = new ArrayList<>();
		for ( int musicBlockNumber = 0; musicBlockNumber < 10; musicBlockNumber++ ) {
			composeBlocks.add( getMockComposeBlock( musicBlockNumber ) );
		}

		when( formBlockProvider.getFormElement( any( FirstBlockProvider.class ), any( NextBlockProvider.class ), any( Form.class ), any( Double.class ), any( List.class ),
				any( Lexicon.class ) ) ).thenReturn( composeBlocks.get( 0 ) ).thenReturn( composeBlocks.get( 1 ) ).thenReturn( composeBlocks.get( 2 ) ).thenReturn( null )
				.thenReturn( composeBlocks.get( 3 ) ).thenReturn( null ).thenReturn( composeBlocks.get( 4 ) ).thenReturn( composeBlocks.get( 5 ) )
				.thenReturn( composeBlocks.get( 6 ) ).thenReturn( composeBlocks.get( 7 ) ).thenReturn( composeBlocks.get( 8 ) );

		List<FormCompositionStep> compositionSteps = compositionComposer.composeSteps( null, null, null, "ABCD", 2 * JMC.WHOLE_NOTE );
		assertEquals( 4, compositionSteps.size() );
		assertEquals( 0., compositionSteps.get( 0 ).getComposeBlocks().get(0).getStartTime(), 0 );
		assertEquals( 1., compositionSteps.get( 1 ).getComposeBlocks().get(0).getStartTime(), 0 );
		assertEquals( 4., compositionSteps.get( 2 ).getComposeBlocks().get(0).getStartTime(), 0 );
		assertEquals( 5., compositionSteps.get( 3 ).getComposeBlocks().get(0).getStartTime(), 0 );

	}

	@Test
	public void composeStepsFailing() {

		List<List<ComposeBlock>> composeBlocks = new ArrayList<>();
		for ( int musicBlockNumber = 0; musicBlockNumber < 10; musicBlockNumber++ ) {
			composeBlocks.add( getMockComposeBlock( musicBlockNumber ) );
		}

		when( formBlockProvider.getFormElement( any( FirstBlockProvider.class ), any( NextBlockProvider.class ), any( Form.class ), any( Double.class ), any( List.class ),
				any( Lexicon.class ) ) ).thenReturn( composeBlocks.get( 0 ) ).thenReturn( composeBlocks.get( 1 ) ).thenReturn( composeBlocks.get( 2 ) ).thenReturn( null )
				.thenReturn( null ).thenReturn( null ).thenReturn( null ).thenReturn( composeBlocks.get( 3 ) ).thenReturn( null ).thenReturn( null )
				.thenReturn( composeBlocks.get( 4 ) ).thenReturn( composeBlocks.get( 5 ) ).thenReturn( composeBlocks.get( 6 ) ).thenReturn( composeBlocks.get( 7 ) )
				.thenReturn( composeBlocks.get( 8 ) );

		List<FormCompositionStep> compositionSteps = compositionComposer.composeSteps( null, null, null, "ABCD", JMC.WHOLE_NOTE );
		assertEquals( 0, compositionSteps.size() );
	}

	private List<ComposeBlock> getMockComposeBlock( double mockStartTime ) {
		List<ComposeBlock> composeBlocks = mock( List.class );
		ComposeBlock composeBlock = mock( ComposeBlock.class );
		when( composeBlock.getStartTime() ).thenReturn( mockStartTime );
		when( composeBlocks.get(0) ).thenReturn( composeBlock );
		return composeBlocks;
	}
}
