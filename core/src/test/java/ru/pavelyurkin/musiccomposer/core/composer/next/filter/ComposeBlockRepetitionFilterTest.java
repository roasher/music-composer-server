package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import jm.music.data.Note;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static jm.constants.Pitches.*;
import static jm.constants.Durations.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ComposeBlockRepetitionFilterTest {

	private ComposeStepRepetitionFilter composeBlockRepetitionFilter = new ComposeStepRepetitionFilter();

	// TODO finish after filter refactoring
//	public void filterOutEighthNoteRepetition() {
//		List<MusicBlock> blocks = Arrays.asList(
//				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ) ) ), null ),
//				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, EIGHTH_NOTE ) ) ), null ),
//				new MusicBlock( Collections.singletonList( new Melody( new Note( C3, EIGHTH_NOTE ) ) ), null ),
//				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ) ) ), null ),
//				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, EIGHTH_NOTE ) ) ), null ),
//				new MusicBlock( Collections.singletonList( new Melody( new Note( C3, EIGHTH_NOTE ) ) ), null )
//		);
//		composeBlockRepetitionFilter.filterIt(
//				Collections.singletonList( new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ) ) ), null ) ),
//				blocks
//		)
//	}

	// getRepetitions tests
	@Test
	public void repetitionsInOnePitchBlocks() throws Exception {
		List<MusicBlock> onePitchBlock = Arrays.asList(
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null )
		);
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( onePitchBlock, 2 * WHOLE_NOTE );
		assertThat( 3, is( equalTo( repetitions0.size() ) ) );
		assertThat( 6, is( equalTo( repetitions0.get( QUARTER_NOTE ) ) ) );
		assertThat( 3, is( equalTo( repetitions0.get( HALF_NOTE ) ) ) );
		assertThat( 2, is( equalTo( repetitions0.get( DOTTED_HALF_NOTE ) ) ) );
	}

	@Test
	public void repetitionsOfThreeNotes() throws Exception {
		List<MusicBlock> blocks = Arrays.asList(
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C3, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C3, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C3, QUARTER_NOTE ) ) ), null )
		);
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( blocks, 2 * WHOLE_NOTE );
		assertThat( 1, is( equalTo( repetitions0.size() ) ) );
		assertThat( 3, is( equalTo( repetitions0.get( DOTTED_HALF_NOTE ) ) ) );
	}

	@Test
	public void zeroRepetitionsOfTwoNotesIfWrongRhythmValue() throws Exception {
		List<MusicBlock> blocks = Arrays.asList(
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, DOTTED_QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null )
		);
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( blocks, 2 * WHOLE_NOTE );
		assertThat( 0, is( equalTo( repetitions0.size() ) ) );
	}

	@Test
	public void repetitionsOfTwoNotesOutOfBounds() throws Exception {
		List<MusicBlock> blocks = Arrays.asList(
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null )
		);
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( blocks, 2 * WHOLE_NOTE );
		assertThat( 3, is( equalTo( repetitions0.size() ) ) );
		assertThat( 7, is( equalTo( repetitions0.get( HALF_NOTE ) ) ) );
		assertThat( 3, is( equalTo( repetitions0.get( WHOLE_NOTE ) ) ) );
		assertThat( 2, is( equalTo( repetitions0.get( WHOLE_NOTE + HALF_NOTE ) ) ) );
	}

}