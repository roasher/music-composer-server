package ru.pavelyurkin.musiccomposer.core.composer.next;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

/**
 * Next Block provider that handles filters
 */
@AllArgsConstructor
@Slf4j
public abstract class FilteredNextStepProvider extends NextStepProvider {

  @Getter
  @Setter
  protected ComposeStepFilter composeStepFilter;

  @Override
  public Optional<CompositionStep> getNext(List<CompositionStep> blocksToChooseFrom,
                                           List<CompositionStep> previousCompositionSteps,
                                           List<FormCompositionStep> previousFormCompositionSteps,
                                           Optional<Form> form) {

    List<CompositionStep> allPreviousCompositionSteps = new ArrayList<>(previousCompositionSteps);
    allPreviousCompositionSteps.addAll(previousFormCompositionSteps
        .stream().flatMap(formCompositionStep -> formCompositionStep.getCompositionSteps().stream())
        .collect(Collectors.toList())
    );
    // User filters
    Instant start = Instant.now();
    List<CompositionStep> filtered =
        composeStepFilter != null ? composeStepFilter.filter(blocksToChooseFrom, allPreviousCompositionSteps) :
            blocksToChooseFrom;
    log.debug("Filtering took {} millis", Duration.between(start, Instant.now()).toMillis());

    return getNextBlockFiltered(filtered, previousCompositionSteps, previousFormCompositionSteps, form);
  }

  /**
   * Get next block choosing from Filtered Blocks
   *
   * @param blocksToChooseFrom
   * @param previousCompositionSteps
   * @param formCompositionSteps
   * @param form
   * @return
   */
  public abstract Optional<CompositionStep> getNextBlockFiltered(List<CompositionStep> blocksToChooseFrom,
                                                                 List<CompositionStep> previousCompositionSteps,
                                                                 List<FormCompositionStep> formCompositionSteps,
                                                                 Optional<Form> form);

}
