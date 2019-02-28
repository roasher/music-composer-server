package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import jm.music.data.Note;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jm.constants.Pitches.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeyVarietyFilterTest {

	private KeyVarietyFilter keyVarietyFilter;

	@Test
	public void returnTrueIfPreviousBlocksRhythmValueIsLessThenRequiredFieldValue() {
		keyVarietyFilter = new KeyVarietyFilter( 0, 8 );
		assertTrue( keyVarietyFilter.filterIt( null,
				Arrays.asList( getTestMusicBlock( Arrays.asList( C1, C1, C1 ) ),
						getTestMusicBlock( Arrays.asList( C1, C1, C1, C1 ) ) ) ) );
	}

	@Test
	public void returnTrueIfNumberOfNotesOutOfKeyIsLessOrEqualThanMaxNumber() {
		keyVarietyFilter = new KeyVarietyFilter( 1, 7 );

		assertTrue( keyVarietyFilter.filterIt( getTestMusicBlock( Arrays.asList( C1, C1, CS1 ) ),
				Arrays.asList( getTestMusicBlock( Arrays.asList( C1, D1, E1, F1, G1, A1, B1 ) ) ) ) );

		assertTrue( keyVarietyFilter.filterIt( getTestMusicBlock( Arrays.asList( C1, C1, BF1, EF1, GF1 ) ),
				// filter should not count frist two cause they are out of 7
				Arrays.asList( getTestMusicBlock( Arrays.asList( B1, E1, C1, D1, EF1, F1, G1, A1, BF1 ) ) ) ) );

		assertTrue( new KeyVarietyFilter( 0, 7 )
				.filterIt( getTestMusicBlock( Arrays.asList( C1, C1, D1 ) ),
				Arrays.asList( getTestMusicBlock( Arrays.asList( C1, D1, E1, F1, G1, A1, B1 ) ) ) ) );

		assertTrue( new KeyVarietyFilter( 0, 4 )
				.filterIt( getTestMusicBlock( Arrays.asList( BF1 ) ),
						Arrays.asList( getTestMusicBlock( Arrays.asList( B1, C1, D1, E1, F1, G1, A1 ) ) ) ) );
	}

	@Test
	public void returnFalseIfNumberOfNotesOutOfKeyIsGreaterThanMax() {
		keyVarietyFilter = new KeyVarietyFilter( 1, 7 );

		assertFalse( keyVarietyFilter.filterIt( getTestMusicBlock( Arrays.asList( C1, C1, EF1, BF1 ) ),
				Arrays.asList( getTestMusicBlock( Arrays.asList( C1, D1, E1, F1, G1, A1, B1 ) ) ) ) );

		assertFalse( keyVarietyFilter.filterIt( getTestMusicBlock( Arrays.asList( C1, C1, BF1, GF1, AF1 ) ),
				// filter should not count first two cause they are out of 7
				Arrays.asList( getTestMusicBlock( Arrays.asList( B1, E1, C1, D1, EF1, F1, G1, A1, BF1 ) ) ) ) );

		assertFalse( new KeyVarietyFilter( 0, 7 )
				.filterIt( getTestMusicBlock( Arrays.asList( C1, C1, CS1 ) ),
						Arrays.asList( getTestMusicBlock( Arrays.asList( C1, D1, E1, F1, G1, A1, B1 ) ) ) ) );
	}

	@Test
	public void returnTrueIfNoKeyCanBeCalculatedFromPreviousMusicBlocks() {
		assertTrue( new KeyVarietyFilter( 0, 7 )
				.filterIt( getTestMusicBlock( Arrays.asList( C1, C1, CS1 ) ),
						Arrays.asList( getTestMusicBlock( Arrays.asList( C1, CS1, D1, DS1, E1, F1, G1 ) ) ) ) );
	}

	private MusicBlock getTestMusicBlock( List<Integer> pitches ) {
		return new MusicBlock( 0.0, Arrays.asList( new InstrumentPart(
				pitches.stream().map( pitch -> new Note( pitch, 1.0 ) ).collect( Collectors.toList() )
						.toArray( new Note[] {} ) ) ), null );
	}
}