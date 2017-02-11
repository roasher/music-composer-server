package ru.pavelyurkin.musiccomposer.core.composer.next;

import ru.pavelyurkin.musiccomposer.core.composer.next.form.NextFormBlockProviderImpl;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import jm.music.data.Note;
import jm.music.data.Rest;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static jm.JMC.*;

/**
 * Created by night wish on 05.03.2016.
 */
public class FormNextBlockProviderTest extends AbstractSpringTest {

	@InjectMocks
	private NextFormBlockProviderImpl formNextBlockProvider;

	@Mock
	private EqualityMetricAnalyzer<List<Melody>> equalityMetricAnalyzer;

	@Before
	public void init() {
		initMocks( this );
	}

	@Test
	public void testGetNextBlock() throws Exception {

		// Already composed blocks
        BlockMovement blockMovementFromPreviousToThis = new BlockMovement( 0, 0 );
        ComposeBlock composeBlock0 = new ComposeBlock( 0, null, Arrays.asList(
				new Melody( new Note( C3, QUARTER_NOTE ) ),
				new Melody( new Note( F3, QUARTER_NOTE ) ) ),
                blockMovementFromPreviousToThis );
		ComposeBlock composeBlock1 = new ComposeBlock( 1, null, Arrays.asList(
                new Melody( new Note( D3, EIGHTH_NOTE ) ),
				new Melody( new Note( G3, EIGHTH_NOTE ) ) ),
                blockMovementFromPreviousToThis );
		ComposeBlock composeBlock2 = new ComposeBlock( 2, null, Arrays.asList(
				new Melody( new Note( E3, EIGHTH_NOTE ), new Note( F3, EIGHTH_NOTE ) ),
				new Melody( new Note( A3, EIGHTH_NOTE ), new Note( B3, EIGHTH_NOTE ) ) ),
                blockMovementFromPreviousToThis );
        ComposeBlock allreadyComposedBlock = new ComposeBlock( Arrays.asList( composeBlock0, composeBlock1, composeBlock2 ) );

		// Possible next blocks
		ComposeBlock composeBlock20 = new ComposeBlock( 20, null, Arrays.asList(
				new Melody( new Note( BF4, SIXTEENTH_NOTE ) ),
				new Melody( new Rest( SIXTEENTH_NOTE ) ) ),
                blockMovementFromPreviousToThis );
		ComposeBlock composeBlock21 = new ComposeBlock( 21, null, Arrays.asList(
                new Melody( new Note( BF5, SIXTEENTH_NOTE ) ),
                new Melody( new Rest( SIXTEENTH_NOTE ) ) ),
                blockMovementFromPreviousToThis );
		ComposeBlock composeBlock22 = new ComposeBlock( 22, null, Arrays.asList(
                new Melody( new Note( BF6, SIXTEENTH_NOTE ) ),
                new Melody( new Rest( SIXTEENTH_NOTE ) ) ),
                blockMovementFromPreviousToThis );

        composeBlock2.setPossibleNextComposeBlocks( Arrays.asList(
                composeBlock20,
                composeBlock21,
                composeBlock22
        ) );

		List<Melody> melodyList20 = sumMelodies( allreadyComposedBlock.getMelodyList(), composeBlock20.getMelodyList() );
		when( equalityMetricAnalyzer.getEqualityMetric( any( List.class ), eq( melodyList20 ) ) ).thenReturn( 0.41 );
		List<Melody> melodyList21 = sumMelodies( allreadyComposedBlock.getMelodyList(), composeBlock21.getMelodyList() );
		when( equalityMetricAnalyzer.getEqualityMetric( any( List.class ), eq( melodyList21 ) ) ).thenReturn( 0.51 );
		List<Melody> melodyList22 = sumMelodies( allreadyComposedBlock.getMelodyList(), composeBlock22.getMelodyList() );
		when( equalityMetricAnalyzer.getEqualityMetric( any( List.class ), eq(melodyList22) ) ).thenReturn( 0.4 );

		// Actually we don't care about similarFromSteps as long we mocked equalityMetricAnalyzer
		List<ComposeBlock> originComposeBlocks = Arrays.asList( new ComposeBlock( 0, null, Arrays.asList( new Melody( new Rest( WHOLE_NOTE ) ) ), null ) );
		List<FormCompositionStep> similarFormSteps = Arrays.asList(
			new FormCompositionStep( originComposeBlocks.stream().map( composeBlock -> new CompositionStep( composeBlock, composeBlock.getMusicBlock() ) ).collect( Collectors.toList()), null )
		);

        List<CompositionStep> previousCompositionSteps = Arrays.asList(
				new CompositionStep( null, null ),
                new CompositionStep( composeBlock0, composeBlock0.getMusicBlock() ),
                new CompositionStep( composeBlock1, composeBlock1.getMusicBlock() ),
                new CompositionStep( composeBlock2, composeBlock2.getMusicBlock() )
        );

		Optional<ComposeBlock> nextBlock = formNextBlockProvider.getNextBlock( previousCompositionSteps, similarFormSteps, Collections.emptyList(), WHOLE_NOTE );

		assertEquals( composeBlock21, nextBlock.get() );
	}

	private List<Melody> sumMelodies(List<Melody> melodies, List<Melody> melodiesToAdd ) {
		List<Melody> out = new ArrayList<>();
		for (int melodyNumber = 0; melodyNumber < melodies.size(); melodyNumber++) {
			Melody melody = new Melody();
			melody.addNoteList( melodies.get( melodyNumber ).getNoteList(), true );
			melody.addNoteList( melodiesToAdd.get( melodyNumber ).getNoteList(), true );
			out.add( melody );
		}
		return out;
	}

}