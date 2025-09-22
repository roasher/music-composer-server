package ru.pavelyurkin.musiccomposer.core.service.multipleclients;

import com.google.common.collect.EvictingQueue;
import java.time.Instant;
import java.util.List;
import java.util.Queue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.service.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;

/**
 * Holds parameters of Composing
 */
@Slf4j
public class ComposingParameters {

  @Getter
  @Setter
  private Lexicon lexicon;
  @Getter
  @Setter
  private ComposeStepProvider composeStepProvider;
  private Queue<CompositionStep> previousCompositionSteps = EvictingQueue.create(1);
  @Getter
  private double rhythmValue = 0;
  @Getter
  private Instant lastCompositionTime = Instant.now();

  public List<CompositionStep> getPreviousCompositionSteps() {
    return previousCompositionSteps.stream().toList();
  }

  public void addCompositionSteps(List<CompositionStep> compositionSteps) {
    log.debug("Adding {} new compositions steps", compositionSteps.size());
    previousCompositionSteps.addAll(compositionSteps);
    rhythmValue += compositionSteps.stream().mapToDouble(value -> value.getTransposedBlock().getRhythmValue()).sum();
    updateLastCompositionTime();
    log.debug("Composition steps queue size: {}", previousCompositionSteps.size());
    log.debug("New rhythm value: {}", rhythmValue);
    log.debug("Last composition time updated to: {}", lastCompositionTime);
  }

  /**
   * Updates the last composition time to current time
   */
  public void updateLastCompositionTime() {
    this.lastCompositionTime = Instant.now();
  }
}
