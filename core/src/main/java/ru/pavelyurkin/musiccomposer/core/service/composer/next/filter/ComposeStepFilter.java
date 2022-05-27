package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter;

import java.util.List;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;

public interface ComposeStepFilter {

  // We assuming that possible next are already transposed
  List<CompositionStep> filter(List<CompositionStep> possibleNextComposeSteps,
                               List<CompositionStep> previousCompositionSteps);

  /**
   * Replaces existing filter with given one
   *
   * @param musicBlockFilter - filter that would replace its predecessor
   */
  void replaceFilter(MusicBlockFilter musicBlockFilter);

}
