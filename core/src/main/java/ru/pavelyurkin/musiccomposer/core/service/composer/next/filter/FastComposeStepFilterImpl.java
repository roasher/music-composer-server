package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;

/**
 * That implementation would find first N next convenient blocks.
 * On one hand that would reduce variety and might lead to composition failure
 * On the other hand it drastically reduces composing time that matters if
 * one want to compose on a fly.
 */
public class FastComposeStepFilterImpl extends ComposeStepFilterImpl {

  /**
   * After filter got this number of allowed steps it would cancel further filtering
   * to save time
   */
  private final int maxAllowedNumber;

  public FastComposeStepFilterImpl(List<MusicBlockFilter> composeStepFilters, int maxAllowedNumber) {
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
