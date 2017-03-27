package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import jm.music.data.Note;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static jm.constants.Pitches.*;
import static jm.constants.Durations.*;
import static org.junit.Assert.*;

public class ComposeBlockRepetitionFilterTest {

	private ComposeBlockRepetitionFilter composeBlockRepetitionFilter = new ComposeBlockRepetitionFilter();

	@Test
	public void getRepetitions() throws Exception {
		List<MusicBlock> onePitchBlock = Arrays.asList(
				new MusicBlock( Arrays.asList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Arrays.asList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Arrays.asList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Arrays.asList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null )
		);
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( onePitchBlock );
		assertEquals( 2, repetitions0.size() );
		assertTrue( repetitions0.get( HALF_NOTE ) == 4 );
		assertTrue( repetitions0.get( WHOLE_NOTE ) == 2 );
	}

}