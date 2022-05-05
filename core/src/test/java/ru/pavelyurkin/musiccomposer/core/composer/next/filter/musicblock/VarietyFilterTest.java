package ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock;

import org.junit.Ignore;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.VarietyFilter;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VarietyFilterTest {

	private VarietyFilter varietyFilter;

	@Test
	public void filtersWithoutMinSequential1() {
		List<MusicBlock> mockComposeSteps = getMockSteps();

		VarietyFilter composeBlockVarietyFilter0 = new VarietyFilter( -1, 4 );
		assertThat( composeBlockVarietyFilter0.filterIt( getMockBlock( "0" ), mockComposeSteps ), is(false) );
		assertThat( composeBlockVarietyFilter0.filterIt( getMockBlock( "1" ), mockComposeSteps ), is(true) );
		assertThat( composeBlockVarietyFilter0.filterIt( getMockBlock( "2" ), mockComposeSteps ), is(true) );

		VarietyFilter composeBlockVarietyFilter1 = new VarietyFilter( -1, 5 );
		assertThat( composeBlockVarietyFilter1.filterIt( getMockBlock( "0" ), mockComposeSteps ), is(true) );
		assertThat( composeBlockVarietyFilter1.filterIt( getMockBlock( "1" ), mockComposeSteps ), is(true) );
		assertThat( composeBlockVarietyFilter1.filterIt( getMockBlock( "2" ), mockComposeSteps ), is(true) );

		VarietyFilter composeBlockVarietyFilter2 = new VarietyFilter( -1, 4 );
		List<MusicBlock> mockSteps1 = mockComposeSteps.subList( 5, mockComposeSteps.size() );
		assertThat( composeBlockVarietyFilter2.filterIt( getMockBlock( "0" ), mockSteps1 ), is(false) );
		assertThat( composeBlockVarietyFilter2.filterIt( getMockBlock( "1" ), mockSteps1 ), is(true) );
		assertThat( composeBlockVarietyFilter2.filterIt( getMockBlock( "2" ), mockSteps1 ), is(true) );
	}

	private List<MusicBlock> getMockSteps() {
		List<MusicBlock> compositionSteps = Arrays.asList(
				getMockBlock( "0" ),
				getMockBlock( "1" ),
				getMockBlock( "0" ),
				getMockBlock( "0" ),
				getMockBlock( "1" ),
				getMockBlock( "0" ),
				getMockBlock( "0" ),
				getMockBlock( "0" ),
				getMockBlock( "0" )
		);
		return compositionSteps;
	}

	@Test
	public void filtersWithoutMinSequential() throws Exception {
		varietyFilter = new VarietyFilter( -1, 3 );

		assertThat( varietyFilter.filterIt( getMockBlock( "1" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "1" ),
						getMockBlock( "1" )
				)), is(false));

		assertThat( varietyFilter.filterIt( getMockBlock( "1" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "2" ),
						getMockBlock( "1" )
				)), is(true));
	}

	@Test
	public void filtersWithoutMaxSequential() throws Exception {
		varietyFilter = new VarietyFilter( 3, -1 );

		assertThat( varietyFilter.filterIt( getMockBlock( "1" ),
				Collections.emptyList()), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "2" ),
				Collections.emptyList()), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "1" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "1" ),
						getMockBlock( "1" )
				)), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "2" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "1" ),
						getMockBlock( "1" )
				)), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "1" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "2" ),
						getMockBlock( "1" )
				)), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "2" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "2" ),
						getMockBlock( "1" )
				)), is(false));
	}

	@Test(expected = RuntimeException.class)
	@Ignore
	public void minCanNotBeGreaterThanMax() throws Exception {
		new VarietyFilter( 5, 4 );
	}

	@Test
	public void filtersIfBothNumbersEnabled() throws Exception {
		varietyFilter = new VarietyFilter( 2, 4 );

		assertThat( varietyFilter.filterIt( getMockBlock( "1" ),
				Collections.emptyList()), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "2" ),
				Arrays.asList(
						getMockBlock( "1" )
				)), is(false));

		assertThat( varietyFilter.filterIt( getMockBlock( "1" ),
				Arrays.asList(
						getMockBlock( "1" )
				)), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "2" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "1" )
				)), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "1" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "1" ),
						getMockBlock( "2" )
				)), is(false));

		assertThat( varietyFilter.filterIt( getMockBlock( "2" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "1" ),
						getMockBlock( "2" ),
						getMockBlock( "2" )
				)), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "2" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "1" ),
						getMockBlock( "2" ),
						getMockBlock( "2" ),
						getMockBlock( "2" )
				)), is(true));

		assertThat( varietyFilter.filterIt( getMockBlock( "2" ),
				Arrays.asList(
						getMockBlock( "1" ),
						getMockBlock( "1" ),
						getMockBlock( "2" ),
						getMockBlock( "2" ),
						getMockBlock( "2" ),
						getMockBlock( "2" )
				)), is(false));
	}

	private MusicBlock getMockBlock( String compositionInfo ) {
		MusicBlock block = mock( MusicBlock.class );
		when( block.getCompositionInfo() ).thenReturn( new CompositionInfo( compositionInfo ) );
		return block;
	}

}