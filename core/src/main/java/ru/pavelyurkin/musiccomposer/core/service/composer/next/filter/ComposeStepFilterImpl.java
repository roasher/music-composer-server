package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

@Slf4j
@Data
public class ComposeStepFilterImpl implements ComposeStepFilter {

  private List<MusicBlockFilter> composeStepFilters;

  public ComposeStepFilterImpl(List<MusicBlockFilter> composeStepFilters) {
    this.composeStepFilters = composeStepFilters;
  }

  /**
   * Returns valid in terms of fitering blocks
   *
   * @param possibleNextComposeSteps
   * @param previousCompositionSteps
   * @return
   */
  @Override
  public List<CompositionStep> filter(List<CompositionStep> possibleNextComposeSteps,
                                      List<CompositionStep> previousCompositionSteps) {
    List<MusicBlock> previousMusicBlocks = previousCompositionSteps.stream()
        .map(CompositionStep::getTransposedBlock)
        .collect(Collectors.toList());
    return possibleNextComposeSteps.stream()
        .filter(compositionStep -> this.composeStepFilters.stream()
            .allMatch(filter -> {
              boolean passed = filter.filterIt(compositionStep.getTransposedBlock(), previousMusicBlocks);
              if (!passed) {
                log.debug("Block was filtered out due to filter: {}", filter.getClass().getSimpleName());
              }
              return passed;
            }))
        .collect(Collectors.toList());
  }

  @Override
  public void replaceFilter(MusicBlockFilter musicBlockFilter) {
    List<MusicBlockFilter> filtered = this.composeStepFilters.stream()
        .filter(filter -> !musicBlockFilter.getClass().equals(filter.getClass()))
        .collect(Collectors.toList());
    if (filtered.size() == this.composeStepFilters.size()) {
      return;
    }
    filtered.add(musicBlockFilter);
    this.composeStepFilters = filtered;
  }
}
