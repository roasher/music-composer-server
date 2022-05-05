package ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock;

import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RangeFilter;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.Arrays;

import static jm.JMC.*;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by wish on 03.02.2016.
 */
public class RangeFilterTest {

	@Test
	public void test() {
		RangeFilter composeBlockRangeFilter = new RangeFilter( C3, C4 );
		assertTrue( composeBlockRangeFilter.filterIt( getBlock( C3, C4, 0, 0 ), null ) );
		assertFalse( composeBlockRangeFilter.filterIt( getBlock( B2, B3, B2 - C3, B3 - C4 ), null ) );
		assertFalse( composeBlockRangeFilter.filterIt( getBlock( D3, D4, D3 - C3, D4 - C4 ), null ) );
		assertFalse( composeBlockRangeFilter.filterIt( getBlock( E3, B4, E3 - C3, B4 - C4 ), null ) );
		assertTrue( composeBlockRangeFilter.filterIt( getBlock( E3, B3, E3 - C3, B3 - C4 ), null ) );
		InstrumentPart restMelody = new InstrumentPart( new Note( REST, Note.DEFAULT_RHYTHM_VALUE ) );
		assertTrue( composeBlockRangeFilter.filterIt( new MusicBlock( 5, Arrays.asList( restMelody, restMelody ), null ), null ) );
	}

	private MusicBlock getBlock( int lowPitch, int highPitch, int lowMovement, int highMovement ) {
		InstrumentPart melody0 = new InstrumentPart(
				new Note( highPitch, Note.DEFAULT_RHYTHM_VALUE ),
				getRandomNote( lowPitch, highPitch ),
				new Rest( Note.DEFAULT_RHYTHM_VALUE),
				getRandomNote( lowPitch, highPitch ) );
		InstrumentPart melody1 = new InstrumentPart(
				new Note( lowPitch, Note.DEFAULT_RHYTHM_VALUE ),
				getRandomNote( lowPitch, highPitch ),
				new Rest( Note.DEFAULT_RHYTHM_VALUE),
				getRandomNote( lowPitch, highPitch ) );
		return new MusicBlock( 0, Arrays.asList( melody0, melody1 ), null );
	}

	private Note getRandomNote( int lowPitch, int highPitch ) {
		return new Note( ( int ) ( highPitch - ( highPitch - lowPitch ) * Math.random() ), Note.DEFAULT_RHYTHM_VALUE );
	}
}
