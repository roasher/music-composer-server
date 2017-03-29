package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static jm.constants.Pitches.*;
import static jm.constants.Pitches.C3;
import static jm.constants.Pitches.C4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by wish on 04.02.2016.
 */
public class VoiceRangeFilterTest {

	@Test
	public void test() {
		List<MusicBlock> previousBlocks = Collections.singletonList( new MusicBlock( 0, null, Arrays.asList(
				new Melody( new Note( C3, Note.DEFAULT_RHYTHM_VALUE ) ),
				new Melody( new Note( C4, Note.DEFAULT_RHYTHM_VALUE ) ) ), null ) );

		ComposeStepVoiceRangeFilter composeBlockVoiceRangeFilter = new ComposeStepVoiceRangeFilter( Arrays.asList(
				new ComposeStepVoiceRangeFilter.Range( C3, G3 ),
				new ComposeStepVoiceRangeFilter.Range( A2, D3 )
		) );

		assertTrue( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( C3, REST, E3 ), Arrays.asList( B2, CS3, REST ), C3 - C3, B2 - C4 ), previousBlocks ) );
		assertFalse( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( B2, C3, REST ), Arrays.asList( B3, REST, REST  ), B2 - C3, B3 - C4 ), previousBlocks ) );
		assertFalse( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( C3, B2 ), Arrays.asList( A2, A2 ), C3 - C3, A2 - C4 ), previousBlocks ) );
		assertFalse( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( G3, GS3, REST ), Arrays.asList( REST, REST, REST ), G3 - C3, REST ), previousBlocks ) );
		assertTrue( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( G3, E3 ), Arrays.asList( D3, C3 ), G3 - C3, D3 - C4 ), previousBlocks ) );
		assertTrue( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( REST, REST ), Arrays.asList( REST, REST ), G3 - C3, D3 - C4 ), previousBlocks ) );
	}

	private MusicBlock getTestBlock( List<Integer> firstNotePitches, List<Integer> secondNotePitches, int firstMovement, int secondMovement ) {
		Melody melody0 = new Melody();
		for ( Integer firstNotePitch : firstNotePitches ) {
			melody0.add( new Note( firstNotePitch, Note.DEFAULT_RHYTHM_VALUE) );
		}
		Melody melody1 = new Melody();
		for ( Integer secondNotePitch : secondNotePitches ) {
			melody1.add( new Note( secondNotePitch, Note.DEFAULT_RHYTHM_VALUE) );
		}
		return new MusicBlock( 0, null, Arrays.asList( melody0, melody1 ), new BlockMovement( firstMovement, secondMovement ) );
	}
}
