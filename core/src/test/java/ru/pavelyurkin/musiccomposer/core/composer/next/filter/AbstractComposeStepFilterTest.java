package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.F4;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class AbstractComposeStepFilterTest {

	@Test
	public void nonePassIfFilterItFalse() throws Exception {
		AbstractComposeStepFilter falseFilter = new AbstractComposeStepFilter() {
			@Override
			public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
				return false;
			}
		};
		assertThat( 0, is( falseFilter.filter( getListOfCompositionStepMocks( 10 ), getListOfCompositionStepMocks( 2 ) ).size() ) );
	}

	@Test
	public void allPassIfFilterItTrue() throws Exception {
		AbstractComposeStepFilter passFilter = new AbstractComposeStepFilter() {
			@Override
			public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
				return true;
			}
		};
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
		ComposeStepFilter composeStepFilter = new ComposeStepRepetitionFilter(
				new ComposeStepVarietyFilter( -1, 6,
						new ComposeStepRestFilter( QUARTER_NOTE,
								new ComposeStepVoiceRangeFilter( Arrays.asList(
										new ComposeStepVoiceRangeFilter.Range( C4, C6 ),
										new ComposeStepVoiceRangeFilter.Range( F3, F5 ),
										new ComposeStepVoiceRangeFilter.Range( A2, A4 ),
										new ComposeStepVoiceRangeFilter.Range( F2, F4 )
								) ) ) ) );

		composeStepFilter.replaceFilter(new ComposeStepVarietyFilter(-1, 1));
		AbstractComposeStepFilter abstractComposeStepFilter = (AbstractComposeStepFilter) composeStepFilter;
		assertThat(((ComposeStepVarietyFilter) abstractComposeStepFilter.getComposeStepFilter())
				.getMaxSequentialBlocksFromSameComposition(), is (1));

		composeStepFilter.replaceFilter(new ComposeStepVoiceRangeFilter(Collections.emptyList()));
		assertTrue(((ComposeStepVoiceRangeFilter) abstractComposeStepFilter.getComposeStepFilter().getComposeStepFilter().getComposeStepFilter())
				.getMelodyRange().isEmpty());
	}

	@Test
	public void nothingChangedIfReplaceOfNonExistingClassFilter() {
		ComposeStepVoiceRangeFilter composeStepFilter1 = new ComposeStepVoiceRangeFilter(Arrays.asList(
				new ComposeStepVoiceRangeFilter.Range(C4, C6),
				new ComposeStepVoiceRangeFilter.Range(F3, F5),
				new ComposeStepVoiceRangeFilter.Range(A2, A4),
				new ComposeStepVoiceRangeFilter.Range(F2, F4)
		));
		ComposeStepRestFilter composeStepFilter2 = new ComposeStepRestFilter(QUARTER_NOTE, composeStepFilter1);
		ComposeStepVarietyFilter composeStepFilter3 = new ComposeStepVarietyFilter(-1, 6,
				composeStepFilter2);
		ComposeStepFilter composeStepFilter = new ComposeStepRepetitionFilter(composeStepFilter3);

		composeStepFilter.replaceFilter(new ComposeStepRangeFilter());
		assertSame(((AbstractComposeStepFilter) composeStepFilter).getComposeStepFilter(), composeStepFilter3);
		assertSame(((AbstractComposeStepFilter) composeStepFilter).getComposeStepFilter().getComposeStepFilter(), composeStepFilter2);
		assertSame(((AbstractComposeStepFilter) composeStepFilter).getComposeStepFilter().getComposeStepFilter().getComposeStepFilter(), composeStepFilter1);
		assertNull(((AbstractComposeStepFilter) composeStepFilter).getComposeStepFilter().getComposeStepFilter().getComposeStepFilter().getComposeStepFilter());
	}
}