package composer.next;

import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import decomposer.form.analyzer.FormEqualityAnalyser;
import helper.AbstractSpringTest;
import jm.music.data.Rest;
import model.ComposeBlock;
import model.melody.Melody;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static jm.JMC.*;

/**
 * Created by night wish on 05.03.2016.
 */
public class FormNextBlockProviderTest extends AbstractSpringTest {

	@InjectMocks
	private FormNextBlockProvider formNextBlockProvider;

	@Mock
	private FormEqualityAnalyser formEqualityAnalyser;

	@Before
	public void init() {
		initMocks( this );
	}

	@Test
	public void testGetNextBlock() throws Exception {
		// already composed blocks
		ComposeBlock composeBlock0 = getMockComposeBlock( 0 );
		ComposeBlock composeBlock1 = getMockComposeBlock( 1 );
		ComposeBlock composeBlock2 = getMockComposeBlock( 2 );
		// Possible next blocks
		ComposeBlock composeBlock20 = getMockComposeBlock( 20 );
		ComposeBlock composeBlock21 = getMockComposeBlock( 21 );
		ComposeBlock composeBlock22 = getMockComposeBlock( 22 );
		when( composeBlock2.getPossibleNextComposeBlocks() ).thenReturn( Arrays.asList(
				composeBlock20,
				composeBlock21,
				composeBlock22
		) );

		List<CompositionStep> previousCompositionSteps = Arrays.asList(
				new CompositionStep( composeBlock0, null ),
				new CompositionStep( composeBlock1, null ),
				new CompositionStep( composeBlock2, null )
		);

		List<Melody> melodyList20 = composeBlock20.getMelodyList();
		when( formEqualityAnalyser.getAverageEqualityMetric( any( List.class ), eq( melodyList20 ) ) ).thenReturn( 0.5 );
		List<Melody> melodyList21 = composeBlock21.getMelodyList();
		when( formEqualityAnalyser.getAverageEqualityMetric( any( List.class ), eq( melodyList21 ) ) ).thenReturn( 0.51 );
		List<Melody> melodyList22 = composeBlock22.getMelodyList();
		when( formEqualityAnalyser.getAverageEqualityMetric( any( List.class ), eq( melodyList22 ) ) ).thenReturn( 0.4 );

		// Actually we don't care about similarFromSteps as long we mocked formEqualityAnalyser
		List<ComposeBlock> originComposeBlocks = Arrays.asList( new ComposeBlock( 0, null, Arrays.asList( new Melody( new Rest( WHOLE_NOTE ) ) ), null ) );
		List<FormCompositionStep> similarFormSteps = Arrays.asList(
			new FormCompositionStep( originComposeBlocks, originComposeBlocks, null	)
		);

		Optional<ComposeBlock> nextBlock = formNextBlockProvider.getNextBlock( previousCompositionSteps, similarFormSteps, Collections.emptyList(), WHOLE_NOTE );

		assertEquals( composeBlock21, nextBlock.get() );
	}

	private ComposeBlock getMockComposeBlock( int id ) {
		ComposeBlock composeBlock = mock( ComposeBlock.class, RETURNS_DEEP_STUBS );
		when( composeBlock.getStartTime() ).thenReturn( (double) id );
		when( composeBlock.toString() ).thenReturn( String.valueOf( id ) );
		return composeBlock;
	}

}