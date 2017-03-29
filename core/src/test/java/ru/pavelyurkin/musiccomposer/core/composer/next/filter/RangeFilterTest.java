package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import jm.music.data.Note;
import jm.music.data.Rest;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static jm.JMC.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by wish on 03.02.2016.
 */
public class RangeFilterTest {

	@Test
	public void test() {
		ComposeStepRangeFilter composeBlockRangeFilter = new ComposeStepRangeFilter( C3, C4 );
		assertTrue( composeBlockRangeFilter.filterIt( getBlock( C3, C4, 0, 0 ), null ) );
		assertFalse( composeBlockRangeFilter.filterIt( getBlock( B2, B3, B2 - C3, B3 - C4 ), null ) );
		assertFalse( composeBlockRangeFilter.filterIt( getBlock( D3, D4, D3 - C3, D4 - C4 ), null ) );
		assertFalse( composeBlockRangeFilter.filterIt( getBlock( E3, B4, E3 - C3, B4 - C4 ), null ) );
		assertTrue( composeBlockRangeFilter.filterIt( getBlock( E3, B3, E3 - C3, B3 - C4 ), null ) );
		Melody restMelody = new Melody( new Note( REST, Note.DEFAULT_RHYTHM_VALUE ) );
		assertTrue( composeBlockRangeFilter.filterIt( new MusicBlock( 5, null, Arrays.asList( restMelody, restMelody ), new BlockMovement( Note.REST, Note.REST ) ), null ) );
	}

	private MusicBlock getBlock( int lowPitch, int highPitch, int lowMovement, int highMovement ) {
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
		return new MusicBlock( 0, null, Arrays.asList( melody0, melody1 ), new BlockMovement( highMovement, lowMovement ) );
	}

	private Note getRandomNote( int lowPitch, int highPitch ) {
		return new Note( ( int ) ( highPitch - ( highPitch - lowPitch ) * Math.random() ), Note.DEFAULT_RHYTHM_VALUE );
	}
}
