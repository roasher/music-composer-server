package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by wish on 03.02.2016.
 */
public class VarietyFilterTest {

	@Test
	public void test() {
		List<MusicBlock> mockComposeSteps = getMockSteps();

		ComposeStepVarietyFilter composeBlockVarietyFilter0 = new ComposeStepVarietyFilter( 4 );
		assertFalse( composeBlockVarietyFilter0.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockComposeSteps ) );
		assertTrue( composeBlockVarietyFilter0.filterIt( getMockBlock( new CompositionInfo( "1" ) ), mockComposeSteps ) );
		assertFalse( composeBlockVarietyFilter0.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockComposeSteps ) );
		assertFalse( composeBlockVarietyFilter0.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockComposeSteps ) );
		assertTrue( composeBlockVarietyFilter0.filterIt( getMockBlock( new CompositionInfo( "2" ) ), mockComposeSteps ) );

		ComposeStepVarietyFilter composeBlockVarietyFilter1 = new ComposeStepVarietyFilter( 5 );
		assertTrue( composeBlockVarietyFilter1.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockComposeSteps ) );
		assertTrue( composeBlockVarietyFilter1.filterIt( getMockBlock( new CompositionInfo( "1" ) ), mockComposeSteps ) );
		assertTrue( composeBlockVarietyFilter1.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockComposeSteps ) );
		assertTrue( composeBlockVarietyFilter1.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockComposeSteps ) );
		assertTrue( composeBlockVarietyFilter1.filterIt( getMockBlock( new CompositionInfo( "2" ) ), mockComposeSteps ) );

		ComposeStepVarietyFilter composeBlockVarietyFilter2 = new ComposeStepVarietyFilter( 4 );
		List<MusicBlock> mockSteps1 = mockComposeSteps.subList( 5, mockComposeSteps.size() );
		assertTrue( composeBlockVarietyFilter2.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockSteps1 ) );
		assertTrue( composeBlockVarietyFilter2.filterIt( getMockBlock( new CompositionInfo( "1" ) ), mockSteps1 ) );
		assertTrue( composeBlockVarietyFilter2.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockSteps1 ) );
		assertTrue( composeBlockVarietyFilter2.filterIt( getMockBlock( new CompositionInfo( "0" ) ), mockSteps1 ) );
		assertTrue( composeBlockVarietyFilter2.filterIt( getMockBlock( new CompositionInfo( "2" ) ), mockSteps1 ) );
	}

	private List<MusicBlock> getMockSteps() {
		CompositionInfo compositionInfo0 = new CompositionInfo( "0" );
		CompositionInfo compositionInfo1 = new CompositionInfo( "1" );
		List<MusicBlock> compositionSteps = Arrays.asList(
				getMockBlock( compositionInfo0 ),
				getMockBlock( compositionInfo1 ),
				getMockBlock( compositionInfo0 ),
				getMockBlock( compositionInfo0 ),
				getMockBlock( compositionInfo1 ),
				getMockBlock( compositionInfo0 ),
				getMockBlock( compositionInfo0 ),
				getMockBlock( compositionInfo0 ),
				getMockBlock( compositionInfo0 )
		);
		return compositionSteps;
	}

	private MusicBlock getMockBlock( CompositionInfo compositionInfo ) {
		MusicBlock block = mock( MusicBlock.class );
		when( block.getCompositionInfo() ).thenReturn( compositionInfo );
		return block;
	}
}
