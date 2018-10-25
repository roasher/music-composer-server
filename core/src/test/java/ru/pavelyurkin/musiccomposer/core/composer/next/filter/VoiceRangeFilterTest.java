package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import jm.music.data.Note;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static jm.constants.Pitches.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by wish on 04.02.2016.
 */
public class VoiceRangeFilterTest {

	@Test
	public void test() {
		List<MusicBlock> previousBlocks = Collections.singletonList( new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( C3, Note.DEFAULT_RHYTHM_VALUE ) ),
				new InstrumentPart( new Note( C4, Note.DEFAULT_RHYTHM_VALUE ) ) ), null ) );

		ComposeStepVoiceRangeFilter composeBlockVoiceRangeFilter = new ComposeStepVoiceRangeFilter( Arrays.asList(
				new ComposeStepVoiceRangeFilter.Range( C3, G3 ),
				new ComposeStepVoiceRangeFilter.Range( A2, D3 )
		) );

		assertTrue( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( C3, REST, E3 ), Arrays.asList( B2, CS3, REST ) ), previousBlocks ) );
		assertFalse( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( B2, C3, REST ), Arrays.asList( B3, REST, REST ) ), previousBlocks ) );
		assertFalse( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( C3, B2 ), Arrays.asList( A2, A2 ) ), previousBlocks ) );
		assertFalse( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( G3, GS3, REST ), Arrays.asList( REST, REST, REST ) ), previousBlocks ) );
		assertTrue( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( G3, E3 ), Arrays.asList( D3, C3 ) ), previousBlocks ) );
		assertTrue( composeBlockVoiceRangeFilter.filterIt( getTestBlock( Arrays.asList( REST, REST ), Arrays.asList( REST, REST ) ), previousBlocks ) );
	}

	private MusicBlock getTestBlock( List<Integer> firstNotePitches, List<Integer> secondNotePitches ) {
		InstrumentPart melody0 = new InstrumentPart(
				new NewMelody( firstNotePitches.stream()
				.map( pitch -> new Note( pitch , Note.DEFAULT_RHYTHM_VALUE ) )
				.collect( Collectors.toList() ) )
		);
		InstrumentPart melody1 = new InstrumentPart(
				new NewMelody( secondNotePitches.stream()
						.map( pitch -> new Note( pitch , Note.DEFAULT_RHYTHM_VALUE ) )
						.collect( Collectors.toList() ) )
		);
		return new MusicBlock( 0, Arrays.asList( melody0, melody1 ), null );
	}
}
