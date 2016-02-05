package composer.next.filter;

import composer.step.CompositionStep;
import jm.music.data.Note;
import jm.music.data.Rest;
import model.BlockMovement;
import model.ComposeBlock;
import model.melody.Melody;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static jm.JMC.*;

/**
 * Created by wish on 03.02.2016.
 */
public class RangeFilterTest {

	@Test
	public void test() {
		Melody restMelody = new Melody( new Note( REST, Note.DEFAULT_RHYTHM_VALUE ) );
		List<ComposeBlock> composeBlocks = Arrays.asList(
				getMockComposeBlock( 0, C3, C4, 0, 0 ),
				getMockComposeBlock( 1, B2, B3, B2 - C3, B3 - C4 ),
				getMockComposeBlock( 2, D3, D4, D3 - C3, D4 - C4 ),
				getMockComposeBlock( 3, E3, B4, E3 - C3, B4 - C4 ),
				getMockComposeBlock( 4, E3, B3, E3 - C3, B3 - C4 ),
				new ComposeBlock( 5, null, Arrays.asList( restMelody, restMelody ), new BlockMovement( Note.REST, Note.REST ) ) );
		ComposeBlock firstComposeBlock = new ComposeBlock( 0, null, Arrays.asList(
				new Melody( new Note( C4, Note.DEFAULT_RHYTHM_VALUE ) ),
				new Melody( new Note( C3, Note.DEFAULT_RHYTHM_VALUE ) ) ), null );
		List<CompositionStep> mockComposeSteps = Arrays.asList( new CompositionStep( firstComposeBlock, firstComposeBlock ) );

		ComposeBlockRangeFilter ComposeBlockRangeFilter0 = new ComposeBlockRangeFilter( C3, C4 );
		List<ComposeBlock> filtered0 = ComposeBlockRangeFilter0.filter( composeBlocks, mockComposeSteps );
		assertEquals( 3, filtered0.size() );
		assertEquals( 0, filtered0.get( 0 ).getStartTime(), 0 );
		assertEquals( 4, filtered0.get( 1 ).getStartTime(), 0 );
		assertEquals( 5, filtered0.get( 2 ).getStartTime(), 0 );
	}

	private ComposeBlock getMockComposeBlock( int id, int lowPitch, int highPitch, int lowMovement, int highMovement ) {
		Melody melody0 = new Melody(
				new Note( highPitch, Note.DEFAULT_RHYTHM_VALUE ),
				getRandomNote( lowPitch, highPitch ),
				new Rest( Note.DEFAULT_RHYTHM_VALUE),
				getRandomNote( lowPitch, highPitch ) );
		Melody melody1 = new Melody(
				new Note( lowPitch, Note.DEFAULT_RHYTHM_VALUE ),
				getRandomNote( lowPitch, highPitch ),
				new Rest( Note.DEFAULT_RHYTHM_VALUE),
				getRandomNote( lowPitch, highPitch ) );
		return new ComposeBlock( id, null, Arrays.asList( melody0, melody1 ), new BlockMovement( highMovement, lowMovement ) );
	}

	private Note getRandomNote( int lowPitch, int highPitch ) {
		return new Note( ( int ) ( highPitch - ( highPitch - lowPitch ) * Math.random() ), Note.DEFAULT_RHYTHM_VALUE );
	}
}
