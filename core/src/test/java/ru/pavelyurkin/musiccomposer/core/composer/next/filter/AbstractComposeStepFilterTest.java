package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.*;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AbstractComposeStepFilterTest {

	@Test
	public void nonePassIfFilterItFalse() throws Exception {
		AbstractComposeStepFilter falseFilter = new AbstractComposeStepFilter(List.of(
				(MusicBlockFilter) (block, previousBlocks) -> false)){};
		assertThat( 0, is( falseFilter.filter( getListOfCompositionStepMocks( 10 ), getListOfCompositionStepMocks( 2 ) ).size() ) );
	}

	@Test
	public void allPassIfFilterItTrue() throws Exception {
		AbstractComposeStepFilter passFilter = new AbstractComposeStepFilter(List.of(
				(MusicBlockFilter) (block, previousBlocks) -> true)){};
		List<CompositionStep> possibleNexts = getListOfCompositionStepMocks( 10 );
		assertThat( possibleNexts.size(), is( passFilter.filter( possibleNexts, getListOfCompositionStepMocks( 2 ) ).size() ) );
	}

	private List<CompositionStep> getListOfCompositionStepMocks( int number ) {
		List<CompositionStep> compositionSteps = new ArrayList<>(  );
		for ( int count = 0; count < number; count++ ) {
			compositionSteps.add( mock(CompositionStep.class) );
		}
		return compositionSteps;
	}

	@Test
	public void correctFilterReplacement() {
		AbstractComposeStepFilter composeStepFilter = new AbstractComposeStepFilter(List.of(
				new VoiceRangeFilter( Arrays.asList(
						new VoiceRangeFilter.Range( C4, C6 ),
						new VoiceRangeFilter.Range( F3, F5 ),
						new VoiceRangeFilter.Range( A2, A4 ),
						new VoiceRangeFilter.Range( F2, F4 )
				) ),
				new RestFilter( QUARTER_NOTE),
				new VarietyFilter( -1, 6),
				new RepetitionFilter()
		)){};

		composeStepFilter.replaceFilter(new VarietyFilter(100, 1));
		MusicBlockFilter newFilter = composeStepFilter.getComposeStepFilters().stream()
				.filter(musicBlockFilter -> musicBlockFilter instanceof VarietyFilter)
				.findFirst()
				.get();
		assertThat(((VarietyFilter) newFilter).getMaxSequentialBlocksFromSameComposition(), is (100));
		assertThat(((VarietyFilter) newFilter).getMinSequentialBlocksFromSameComposition(), is (1));

		composeStepFilter.replaceFilter(new VoiceRangeFilter(Collections.emptyList()));
		MusicBlockFilter newFilter1 = composeStepFilter.getComposeStepFilters().stream()
				.filter(musicBlockFilter -> musicBlockFilter instanceof VoiceRangeFilter)
				.findFirst()
				.get();
		assertTrue((((VoiceRangeFilter) newFilter1).getMelodyRange().isEmpty()));

		assertThat(composeStepFilter.getComposeStepFilters().size(), is(4));
	}

	@Test
	public void nothingChangedIfReplaceOfNonExistingClassFilter() {
		List<MusicBlockFilter> composeStepFilters = List.of(
				new VoiceRangeFilter(Arrays.asList(
						new VoiceRangeFilter.Range(C4, C6),
						new VoiceRangeFilter.Range(F3, F5),
						new VoiceRangeFilter.Range(A2, A4),
						new VoiceRangeFilter.Range(F2, F4)
				)),
				new RestFilter(QUARTER_NOTE),
				new VarietyFilter(-1, 6),
				new RepetitionFilter()
		);
		AbstractComposeStepFilter composeStepFilter = new AbstractComposeStepFilter(composeStepFilters){};

		composeStepFilter.replaceFilter(new RangeFilter(-1, 10));
		assertThat(composeStepFilters.size(), is(4));
	}
}