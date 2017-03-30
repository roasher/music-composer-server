package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import jm.music.data.Note;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.util.*;

import static jm.constants.Pitches.*;
import static jm.constants.Durations.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ComposeStepRepetitionFilterTest {

	private ComposeStepRepetitionFilter composeBlockRepetitionFilter = new ComposeStepRepetitionFilter();

	@Test
	public void filterOutEighthNoteRepetition() {
		List<MusicBlock> blocks = Arrays.asList(
				new MusicBlock( Collections.singletonList( new Melody( new Note( 24, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( 25, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( 26, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( 27, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( 28, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( 29, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( 30, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( 31, EIGHTH_NOTE ) ) ), null ) );

		List<MusicBlock> previousBlocks = new ArrayList<>();
		previousBlocks.addAll( blocks );
		previousBlocks.addAll( blocks );
		previousBlocks.addAll( blocks );
		for ( int blockNumber = 0; blockNumber < blocks.size() - 1; blockNumber++ ) {
			previousBlocks.add( blocks.get( blockNumber ) );
		}

		assertFalse( composeBlockRepetitionFilter.filterIt( new MusicBlock( Collections.singletonList( new Melody( new Note( 31, EIGHTH_NOTE ) ) ), null ), previousBlocks ) );
	}

	// getRepetitions tests
	@Test
	public void repetitionsInOnePitchBlocks() throws Exception {
		List<MusicBlock> onePitchBlock = Arrays.asList(
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( Collections.singletonList( new Melody( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ) );
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
				new MusicBlock( Collections.singletonList( new Melody( new Note( C3, QUARTER_NOTE ) ) ), null ) );
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
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ) );
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
				new MusicBlock( Collections.singletonList( new Melody( new Note( C2, QUARTER_NOTE ) ) ), null ) );
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( blocks, 2 * WHOLE_NOTE );
		assertThat( 3, is( equalTo( repetitions0.size() ) ) );
		assertThat( 7, is( equalTo( repetitions0.get( HALF_NOTE ) ) ) );
		assertThat( 3, is( equalTo( repetitions0.get( WHOLE_NOTE ) ) ) );
		assertThat( 2, is( equalTo( repetitions0.get( WHOLE_NOTE + HALF_NOTE ) ) ) );
	}

}