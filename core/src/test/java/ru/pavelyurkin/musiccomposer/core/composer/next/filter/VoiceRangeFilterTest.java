package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static jm.constants.Pitches.*;
import static jm.constants.Pitches.C3;
import static jm.constants.Pitches.C4;
import static org.junit.Assert.assertEquals;

/**
 * Created by wish on 04.02.2016.
 */
public class VoiceRangeFilterTest {

	@Test
	public void test() {
		List<ComposeBlock> composeBlocks = Arrays.asList(
				getTestComposeBlock( 0, Arrays.asList( C3, REST, E3 ), Arrays.asList( B2, CS3, REST ), C3 - C3, B2 - C4 ),
				getTestComposeBlock( 1, Arrays.asList( B2, C3, REST ), Arrays.asList( B3, REST, REST  ), B2 - C3, B3 - C4 ),
				getTestComposeBlock( 2, Arrays.asList( C3, B2 ), Arrays.asList( A2, A2 ), C3 - C3, A2 - C4 ),
				getTestComposeBlock( 3, Arrays.asList( G3, GS3, REST ), Arrays.asList( REST, REST, REST ), G3 - C3, REST ),
				getTestComposeBlock( 4, Arrays.asList( G3, E3 ), Arrays.asList( D3, C3 ), G3 - C3, D3 - C4 ),
				getTestComposeBlock( 5, Arrays.asList( REST, REST ), Arrays.asList( REST, REST ), G3 - C3, D3 - C4 ) );
		MusicBlock firstBlock = new MusicBlock( 0, null, Arrays.asList(
				new Melody( new Note( C3, Note.DEFAULT_RHYTHM_VALUE ) ),
				new Melody( new Note( C4, Note.DEFAULT_RHYTHM_VALUE ) ) ), null );

		ComposeBlockVoiceRangeFilter composeBlockVoiceRangeFilter = new ComposeBlockVoiceRangeFilter( Arrays.asList(
				new ComposeBlockVoiceRangeFilter.Range( C3, G3 ),
				new ComposeBlockVoiceRangeFilter.Range( A2, D3 )
		) );

		List<CompositionStep> mockComposeSteps = Arrays.asList( new CompositionStep( new ComposeBlock( firstBlock ), firstBlock ) );
		List<ComposeBlock> filtered0 = composeBlockVoiceRangeFilter.filter( composeBlocks, mockComposeSteps );
		assertEquals( 3, filtered0.size() );
		assertEquals( 0, filtered0.get( 0 ).getStartTime(), 0 );
		assertEquals( 4, filtered0.get( 1 ).getStartTime(), 0 );
		assertEquals( 5, filtered0.get( 2 ).getStartTime(), 0 );
	}

	private ComposeBlock getTestComposeBlock( int id, List<Integer> firstNotePitches, List<Integer> secondNotePitches, int firstMovement, int secondMovement ) {
		Melody melody0 = new Melody();
		for ( Integer firstNotePitch : firstNotePitches ) {
			melody0.add( new Note( firstNotePitch, Note.DEFAULT_RHYTHM_VALUE) );
		}
		Melody melody1 = new Melody();
		for ( Integer secondNotePitch : secondNotePitches ) {
			melody1.add( new Note( secondNotePitch, Note.DEFAULT_RHYTHM_VALUE) );
		}
		return new ComposeBlock( id, null, Arrays.asList( melody0, melody1 ), new BlockMovement( firstMovement, secondMovement ) );
	}
}
