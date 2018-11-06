package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import jm.music.data.Note;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.*;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class ComposeStepRepetitionFilterTest {

	private ComposeStepRepetitionFilter composeBlockRepetitionFilter = new ComposeStepRepetitionFilter();

	@Test
	public void filterOutRepetitionsLiveExample() {
		MusicBlock firstBlock = new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( 69, 0.500 ) ),
				new InstrumentPart( new Note( 64, 0.500 ) ),
				new InstrumentPart( new Note( 60, 0.500 ) ),
				new InstrumentPart( new Note( 45, 0.500 ) ) ),
				null );
		List<MusicBlock> blocks = Arrays.asList( firstBlock,
				new MusicBlock( 0, Arrays.asList( new InstrumentPart( new Note( 69, 0.500 ) ), new InstrumentPart( new Note( 64, 0.500 ) ), new InstrumentPart( new Note( 59, 0.500 ) ), new InstrumentPart( new Note( 45, 0.500 ) ) ), null ),
				new MusicBlock( 0, Arrays.asList( new InstrumentPart( new Note( 69, 0.500 ) ), new InstrumentPart( new Note( 66, 0.500 ) ), new InstrumentPart( new Note( 57, 0.500 ) ), new InstrumentPart( new Note( 50, 0.500 ) ) ), null ),
				new MusicBlock( 0, Arrays.asList( new InstrumentPart( new Note( 69, 0.500 ) ), new InstrumentPart( new Note( 66, 0.500 ) ), new InstrumentPart( new Note( 56, 0.500 ) ), new InstrumentPart( new Note( 50, 0.500 ) ) ), null ),
				new MusicBlock( 0, Arrays.asList( new InstrumentPart( new Note( 71, 0.500 ) ), new InstrumentPart( new Note( 66, 0.500 ) ), new InstrumentPart( new Note( 54, 0.500 ) ), new InstrumentPart( new Note( 51, 0.500 ) ) ), null ),
				new MusicBlock( 0, Arrays.asList( new InstrumentPart( new Note( 71, 0.500 ) ), new InstrumentPart( new Note( 66, 0.500 ) ), new InstrumentPart( new Note( 66, 0.500 ) ), new InstrumentPart( new Note( 51, 0.500 ) ) ), null ),
				new MusicBlock( 0, Arrays.asList( new InstrumentPart( new Note( 71, 0.500 ) ), new InstrumentPart( new Note( 68, 0.500 ) ), new InstrumentPart( new Note( 64, 0.500 ) ), new InstrumentPart( new Note( 52, 0.500 ) ) ), null ),
				new MusicBlock( 0, Arrays.asList( new InstrumentPart( new Note( 71, 0.500 ) ), new InstrumentPart( new Note( 69, 0.500 ) ), new InstrumentPart( new Note( 64, 0.500 ) ), new InstrumentPart( new Note( 54, 0.500 ) ) ), null )
		);

		List<MusicBlock> previousBlocks = new ArrayList<>();
		previousBlocks.addAll( blocks );
		previousBlocks.addAll( blocks );
		previousBlocks.addAll( blocks );

		assertFalse( composeBlockRepetitionFilter.filterIt( firstBlock, previousBlocks ) );
	}

	@Test
	public void filterOutEighthNoteRepetition() {
		MusicBlock firstBlock = new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( 24, EIGHTH_NOTE ) ) ), null );
		List<MusicBlock> blocks = Arrays.asList( firstBlock,
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( 25, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( 26, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( 27, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( 28, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( 29, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( 30, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( 31, EIGHTH_NOTE ) ) ), null ) );

		List<MusicBlock> previousBlocks = new ArrayList<>();
		previousBlocks.addAll( blocks );
		previousBlocks.addAll( blocks );
		previousBlocks.addAll( blocks );

		assertFalse( composeBlockRepetitionFilter.filterIt( firstBlock, previousBlocks ) );
	}

	// getRepetitions tests
	@Test
	public void repetitionsInOnePitchBlocks() throws Exception {
		List<MusicBlock> onePitchBlock = Arrays.asList(
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, EIGHTH_NOTE ), new Note( C1, EIGHTH_NOTE ) ) ), null ) );
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( onePitchBlock, 2 * WHOLE_NOTE );
		assertThat( 3, is( equalTo( repetitions0.size() ) ) );
		assertThat( 6, is( equalTo( repetitions0.get( QUARTER_NOTE ) ) ) );
		assertThat( 3, is( equalTo( repetitions0.get( HALF_NOTE ) ) ) );
		assertThat( 2, is( equalTo( repetitions0.get( DOTTED_HALF_NOTE ) ) ) );
	}

	@Test
	public void repetitionsOfThreeNotes() throws Exception {
		List<MusicBlock> blocks = Arrays.asList(
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C3, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C3, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C3, QUARTER_NOTE ) ) ), null ) );
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( blocks, 2 * WHOLE_NOTE );
		assertThat( 1, is( equalTo( repetitions0.size() ) ) );
		assertThat( 3, is( equalTo( repetitions0.get( DOTTED_HALF_NOTE ) ) ) );
	}

	@Test
	public void zeroRepetitionsOfTwoNotesIfWrongRhythmValue() throws Exception {
		List<MusicBlock> blocks = Arrays.asList(
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, DOTTED_QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ) );
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( blocks, 2 * WHOLE_NOTE );
		assertThat( 0, is( equalTo( repetitions0.size() ) ) );
	}

	@Test
	public void repetitionsOfTwoNotesOutOfBounds() throws Exception {
		List<MusicBlock> blocks = Arrays.asList(
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ),

				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C1, QUARTER_NOTE ) ) ), null ),
				new MusicBlock( 0, Collections.singletonList( new InstrumentPart( new Note( C2, QUARTER_NOTE ) ) ), null ) );
		Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions( blocks, 2 * WHOLE_NOTE );
		assertThat( 3, is( equalTo( repetitions0.size() ) ) );
		assertThat( 7, is( equalTo( repetitions0.get( HALF_NOTE ) ) ) );
		assertThat( 3, is( equalTo( repetitions0.get( WHOLE_NOTE ) ) ) );
		assertThat( 2, is( equalTo( repetitions0.get( WHOLE_NOTE + HALF_NOTE ) ) ) );
	}

}