package ru.pavelyurkin.musiccomposer.composer.next.filter;

import org.junit.Test;
import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.model.composition.CompositionInfo;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by wish on 03.02.2016.
 */
public class VarietyFilterTest {

	@Test
	public void test() {
		List<ComposeBlock> composeBlocks = Arrays.asList(
				getMockComposeBlock( new CompositionInfo( "0" ) ),
				getMockComposeBlock( new CompositionInfo( "1" ) ),
				getMockComposeBlock( new CompositionInfo( "0" ) ),
				getMockComposeBlock( new CompositionInfo( "0" ) ),
				getMockComposeBlock( new CompositionInfo( "2" ) )
		);
		List<CompositionStep> mockComposeSteps = getMockComposeSteps();

		ComposeBlockVarietyFilter composeBlockVarietyFilter0 = new ComposeBlockVarietyFilter( 4 );
		List<ComposeBlock> filtered0 = composeBlockVarietyFilter0.filter( composeBlocks, mockComposeSteps );
		assertEquals( 2, filtered0.size() );
		assertTrue( filtered0.get( 0 ).getCompositionInfo().getTitle().equals( "1" ) );
		assertTrue( filtered0.get( 1 ).getCompositionInfo().getTitle().equals( "2" ) );

		ComposeBlockVarietyFilter composeBlockVarietyFilter1 = new ComposeBlockVarietyFilter( 5 );
		List<ComposeBlock> filtered1 = composeBlockVarietyFilter1.filter( composeBlocks, mockComposeSteps );
		assertEquals( 5, filtered1.size() );

		ComposeBlockVarietyFilter composeBlockVarietyFilter2 = new ComposeBlockVarietyFilter( 4 );
		List<ComposeBlock> filtered2 = composeBlockVarietyFilter2.filter( composeBlocks, mockComposeSteps.subList( 5, mockComposeSteps.size() ) );
		assertEquals( 5, filtered2.size() );

	}

	private List<CompositionStep> getMockComposeSteps() {
		CompositionInfo compositionInfo0 = new CompositionInfo( "0" );
		CompositionInfo compositionInfo1 = new CompositionInfo( "1" );
		List<CompositionStep> compositionSteps = Arrays.asList(
				getMockComposeStep( compositionInfo0 ),
				getMockComposeStep( compositionInfo1 ),
				getMockComposeStep( compositionInfo0 ),
				getMockComposeStep( compositionInfo0 ),
				getMockComposeStep( compositionInfo1 ),
				getMockComposeStep( compositionInfo0 ),
				getMockComposeStep( compositionInfo0 ),
				getMockComposeStep( compositionInfo0 ),
				getMockComposeStep( compositionInfo0 )
		);
		return compositionSteps;
	}

	private CompositionStep getMockComposeStep( CompositionInfo compositionInfo ) {
		CompositionStep compositionStep = mock( CompositionStep.class, RETURNS_DEEP_STUBS );
		when( compositionStep.getOriginComposeBlock().getCompositionInfo() ).thenReturn( compositionInfo );
		return compositionStep;
	}

	private ComposeBlock getMockComposeBlock( CompositionInfo compositionInfo ) {
		ComposeBlock composeBlock = mock( ComposeBlock.class );
		when( composeBlock.getCompositionInfo() ).thenReturn( compositionInfo );
		return composeBlock;
	}
}
