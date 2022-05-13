package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

public abstract class FastComposeStepFilter extends AbstractComposeStepFilter {

  /**
   * After filter got this number of allowed steps it would cancel further filtering
   * to save time
   */
  private final int maxAllowedNumber;

  public FastComposeStepFilter(List<MusicBlockFilter> composeStepFilters, int maxAllowedNumber) {
    super(composeStepFilters);
    this.maxAllowedNumber = maxAllowedNumber;
  }

  @Override
  public List<CompositionStep> filter(List<CompositionStep> possibleNextComposeSteps,
                                      List<CompositionStep> previousCompositionSteps) {
    List<MusicBlock> previousMusicBlocks = previousCompositionSteps.stream()
        .map(CompositionStep::getTransposedBlock)
        .collect(Collectors.toList());
    List<CompositionStep> out = new ArrayList<>();
    for (CompositionStep compositionStep : possibleNextComposeSteps) {
      boolean allowedStep = this.getComposeStepFilters().stream()
          .allMatch(filter -> filter.filterIt(compositionStep.getTransposedBlock(), previousMusicBlocks));
      if (allowedStep) {
        out.add(compositionStep);
        if (out.size() >= maxAllowedNumber) {
          return out;
        }
      }
    }
    return out;
  }
}
