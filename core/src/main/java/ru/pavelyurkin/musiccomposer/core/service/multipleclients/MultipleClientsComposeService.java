package ru.pavelyurkin.musiccomposer.core.service.multipleclients;

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
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.core.service.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.FilteredNextStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;

/**
 * Class handles composing multiple compositions in a same time
 */
@Slf4j
@RequiredArgsConstructor
@Data
@Component
public class MultipleClientsComposeService {

  private final ApplicationContext applicationContext;
  private final CompositionComposer compositionComposer;
  /**
   * Map, holding parameters to compose per id (session for example)
   */
  private Map<String, ComposingParameters> composingParametersMap = new HashMap<>();

  public CompositionFrontDTO getNextBarsFromComposition(String compositionId, int numberOfBars,
                                                        List<MusicBlockFilter> filtersToReplace) {
    log.info("Getting next {} bars of composition for id = {}", numberOfBars, compositionId);
    ComposingParameters composingParameters = getComposingParameters(compositionId);
    ComposeStepProvider composeStepProvider = composingParameters.getComposeStepProvider();
    replaceFilters(composeStepProvider, filtersToReplace);
    Pair<Composition, List<CompositionStep>> compose = compositionComposer
        .compose(composeStepProvider, composingParameters.getLexicon(), numberOfBars * JMC.WHOLE_NOTE,
            composingParameters.getPreviousCompositionSteps());
    CompositionFrontDTO compositionFrontDTO = new CompositionFrontDTO(compose.getKey(),
        composingParameters.getRhythmValue());
    // add composed steps to params only before constructing compositionFrontDTO cause of rhythm value
    composingParameters.addCompositionSteps(compose.getValue());
    return compositionFrontDTO;
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

  public ComposingParameters getComposingParametersById(String id) {
    return composingParametersMap.get(id);
  }

  public void resetAllCompositions() {
    this.composingParametersMap = new HashMap<>();
  }
}
