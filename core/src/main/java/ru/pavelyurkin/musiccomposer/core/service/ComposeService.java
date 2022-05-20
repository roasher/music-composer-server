package ru.pavelyurkin.musiccomposer.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jm.JMC;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.composer.next.FilteredNextStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;

/**
 * Class provides different composing services
 */
@Slf4j
@RequiredArgsConstructor
@Data
@Component
public class ComposeService {

  /**
   * Map, holding parameters to compose per id (session for example)
   */
  private Map<String, ComposingParameters> composingParametersMap = new HashMap<>();

  private final ApplicationContext applicationContext;
  private final CompositionComposer compositionComposer;

  public CompositionFrontDTO getNextBarsFromComposition(String compositionId, int numberOfBars,
                                                        List<MusicBlockFilter> filtersToReplace) {
    log.info("Getting next {} bars of composition for id = {}", numberOfBars, compositionId);
    ComposingParameters composingParameters = getComposingParameters(compositionId);
    ComposeStepProvider composeStepProvider = composingParameters.getComposeStepProvider();
    replaceFilters(composeStepProvider, filtersToReplace);
    double previousSumRhythmValue = getPreviousSumRhythmValue(composingParameters.getPreviousCompositionSteps());
    Pair<Composition, List<CompositionStep>> compose = compositionComposer
        .compose(composeStepProvider, composingParameters.getLexicon(), numberOfBars * JMC.WHOLE_NOTE,
            composingParameters.getPreviousCompositionSteps());
    if (compose.getValue() != null) {
      composingParameters.setPreviousCompositionSteps(compose.getValue());
    }
    return new CompositionFrontDTO(compose.getKey(), previousSumRhythmValue);
  }

  private ComposingParameters getComposingParameters(String compositionId) {
    ComposingParameters composingParameters;
    if (composingParametersMap.containsKey(compositionId)) {
      composingParameters = composingParametersMap.get(compositionId);
    } else {
      composingParameters = applicationContext.getBean("composingParameters", ComposingParameters.class);
      composingParametersMap.put(compositionId, composingParameters);
    }
    return composingParameters;
  }

  private void replaceFilters(ComposeStepProvider composeStepProvider,
                              List<MusicBlockFilter> filtersToReplace) {
    if (filtersToReplace == null || filtersToReplace.isEmpty()) {
      return;
    }
    NextStepProvider nextStepProvider = composeStepProvider.getNextStepProvider();
    if (nextStepProvider instanceof FilteredNextStepProvider) {
      filtersToReplace.forEach(filter -> {
        ((FilteredNextStepProvider) nextStepProvider).getComposeStepFilter().replaceFilter(filter);
      });
    } else {
      throw new IllegalStateException("Can't replace filter because provider isn't filtered type");
    }
  }

  private double getPreviousSumRhythmValue(List<CompositionStep> previousCompositionSteps) {
    return previousCompositionSteps.stream().mapToDouble(value -> value.getTransposedBlock().getRhythmValue()).sum();
  }

  public ComposingParameters getComposingParametersById(String id) {
    return composingParametersMap.get(id);
  }

  public void resetAllCompositions() {
    this.composingParametersMap = new HashMap<>();
  }
}
