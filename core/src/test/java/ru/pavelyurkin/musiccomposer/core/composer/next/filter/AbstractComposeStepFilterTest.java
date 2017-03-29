package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.ArrayList;
import java.util.List;

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
}