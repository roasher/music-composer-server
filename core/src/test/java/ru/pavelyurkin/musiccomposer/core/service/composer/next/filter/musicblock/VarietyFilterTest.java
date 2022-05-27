package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;

public class VarietyFilterTest {

  private VarietyFilter varietyFilter;

  @Test
  public void filtersWithoutMinSequential1() {
    List<MusicBlock> mockComposeSteps = getMockSteps();

    VarietyFilter composeBlockVarietyFilter0 = new VarietyFilter(4, 0);
    assertThat(composeBlockVarietyFilter0.filterIt(getMockBlock("0"), mockComposeSteps), is(false));
    assertThat(composeBlockVarietyFilter0.filterIt(getMockBlock("1"), mockComposeSteps), is(true));
    assertThat(composeBlockVarietyFilter0.filterIt(getMockBlock("2"), mockComposeSteps), is(true));

    VarietyFilter composeBlockVarietyFilter1 = new VarietyFilter(5, 0);
    assertThat(composeBlockVarietyFilter1.filterIt(getMockBlock("0"), mockComposeSteps), is(true));
    assertThat(composeBlockVarietyFilter1.filterIt(getMockBlock("1"), mockComposeSteps), is(true));
    assertThat(composeBlockVarietyFilter1.filterIt(getMockBlock("2"), mockComposeSteps), is(true));

    VarietyFilter composeBlockVarietyFilter2 = new VarietyFilter(4, 0);
    List<MusicBlock> mockSteps1 = mockComposeSteps.subList(5, mockComposeSteps.size());
    assertThat(composeBlockVarietyFilter2.filterIt(getMockBlock("0"), mockSteps1), is(false));
    assertThat(composeBlockVarietyFilter2.filterIt(getMockBlock("1"), mockSteps1), is(true));
    assertThat(composeBlockVarietyFilter2.filterIt(getMockBlock("2"), mockSteps1), is(true));
  }

  private List<MusicBlock> getMockSteps() {
    List<MusicBlock> compositionSteps = Arrays.asList(
        getMockBlock("0"),
        getMockBlock("1"),
        getMockBlock("0"),
        getMockBlock("0"),
        getMockBlock("1"),
        getMockBlock("0"),
        getMockBlock("0"),
        getMockBlock("0"),
        getMockBlock("0")
    );
    return compositionSteps;
  }

  @Test
  public void filtersWithoutMinSequential() throws Exception {
    varietyFilter = new VarietyFilter(3, 0);

    assertThat(varietyFilter.filterIt(getMockBlock("1"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("1"),
            getMockBlock("1")
        )), is(false));

    assertThat(varietyFilter.filterIt(getMockBlock("1"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("2"),
            getMockBlock("1")
        )), is(true));
  }

  @Test
  public void filtersWithoutMaxSequential() throws Exception {
    varietyFilter = new VarietyFilter(Integer.MAX_VALUE, 3);

    assertThat(varietyFilter.filterIt(getMockBlock("1"),
        Collections.emptyList()), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("2"),
        Collections.emptyList()), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("1"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("1"),
            getMockBlock("1")
        )), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("2"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("1"),
            getMockBlock("1")
        )), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("1"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("2"),
            getMockBlock("1")
        )), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("2"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("2"),
            getMockBlock("1")
        )), is(false));
  }

  @Test
  public void minCanNotBeGreaterThanMax() throws Exception {
    assertThrows(IllegalArgumentException.class, () -> new VarietyFilter(4, 5));
  }

  @Test
  public void filtersIfBothNumbersEnabled() throws Exception {
    varietyFilter = new VarietyFilter(4, 2);

    assertThat(varietyFilter.filterIt(getMockBlock("1"),
        Collections.emptyList()), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("2"),
        Arrays.asList(
            getMockBlock("1")
        )), is(false));

    assertThat(varietyFilter.filterIt(getMockBlock("1"),
        Arrays.asList(
            getMockBlock("1")
        )), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("2"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("1")
        )), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("1"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("1"),
            getMockBlock("2")
        )), is(false));

    assertThat(varietyFilter.filterIt(getMockBlock("2"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("1"),
            getMockBlock("2"),
            getMockBlock("2")
        )), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("2"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("1"),
            getMockBlock("2"),
            getMockBlock("2"),
            getMockBlock("2")
        )), is(true));

    assertThat(varietyFilter.filterIt(getMockBlock("2"),
        Arrays.asList(
            getMockBlock("1"),
            getMockBlock("1"),
            getMockBlock("2"),
            getMockBlock("2"),
            getMockBlock("2"),
            getMockBlock("2")
        )), is(false));
  }

  private MusicBlock getMockBlock(String compositionInfo) {
    MusicBlock block = mock(MusicBlock.class);
    when(block.getCompositionInfo()).thenReturn(new CompositionInfo(compositionInfo));
    return block;
  }

}