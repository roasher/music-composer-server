package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.A2;
import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.C6;
import static jm.constants.Pitches.F2;
import static jm.constants.Pitches.F3;
import static jm.constants.Pitches.F4;
import static jm.constants.Pitches.F5;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.RangeFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.RepetitionFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.RestFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.VarietyFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.VoiceRangeFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;

public class ComposeStepFilterImplTest {

  @Test
  public void nonePassIfFilterItFalse() throws Exception {
    ComposeStepFilterImpl falseFilter = new ComposeStepFilterImpl(List.of(
        (MusicBlockFilter) (block, previousBlocks) -> false)) {
    };
    assertThat(0, is(falseFilter.filter(getListOfCompositionStepMocks(10), getListOfCompositionStepMocks(2)).size()));
  }

  @Test
  public void allPassIfFilterItTrue() throws Exception {
    ComposeStepFilterImpl passFilter = new ComposeStepFilterImpl(List.of(
        (MusicBlockFilter) (block, previousBlocks) -> true)) {
    };
    List<CompositionStep> possibleNexts = getListOfCompositionStepMocks(10);
    assertThat(possibleNexts.size(), is(passFilter.filter(possibleNexts, getListOfCompositionStepMocks(2)).size()));
  }

  private List<CompositionStep> getListOfCompositionStepMocks(int number) {
    List<CompositionStep> compositionSteps = new ArrayList<>();
    for (int count = 0; count < number; count++) {
      compositionSteps.add(mock(CompositionStep.class));
    }
    return compositionSteps;
  }

  @Test
  public void correctFilterReplacement() {
    ComposeStepFilterImpl composeStepFilter = new ComposeStepFilterImpl(List.of(
        new VoiceRangeFilter(Arrays.asList(
            new VoiceRangeFilter.Range(C4, C6),
            new VoiceRangeFilter.Range(F3, F5),
            new VoiceRangeFilter.Range(A2, A4),
            new VoiceRangeFilter.Range(F2, F4)
        )),
        new RestFilter(QUARTER_NOTE),
        new VarietyFilter(-1, 6),
        new RepetitionFilter()
    )) {
    };

    composeStepFilter.replaceFilter(new VarietyFilter(100, 1));
    MusicBlockFilter newFilter = composeStepFilter.getComposeStepFilters().stream()
        .filter(musicBlockFilter -> musicBlockFilter instanceof VarietyFilter)
        .findFirst()
        .get();
    assertThat(((VarietyFilter) newFilter).getMaxSequentialBlocksFromSameComposition(), is(100));
    assertThat(((VarietyFilter) newFilter).getMinSequentialBlocksFromSameComposition(), is(1));

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
    ComposeStepFilterImpl composeStepFilter = new ComposeStepFilterImpl(composeStepFilters) {
    };

    composeStepFilter.replaceFilter(new RangeFilter(-1, 10));
    assertThat(composeStepFilters.size(), is(4));
  }
}