package ru.pavelyurkin.musiccomposer.core.service;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.service.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.core.service.multipleclients.ComposingParameters;
import ru.pavelyurkin.musiccomposer.core.service.multipleclients.MultipleClientsComposeService;

public class MultipleClientsComposeServiceTest extends AbstractSpringTest {

  @Autowired
  private MultipleClientsComposeService multipleClientsComposeService;

  @Autowired
  private CompositionComposer compositionComposer;

  @AfterEach
  void tearDown() {
    multipleClientsComposeService.resetAllCompositions();
  }

  @Test
  public void sameCompositionOnMultipleCallsToService() {
    String id1 = "id1";
    CompositionFrontDTO composition1 = multipleClientsComposeService.getNextBarsFromComposition(id1, 10, null);
    String id2 = "id2";
    CompositionFrontDTO composition11 = multipleClientsComposeService.getNextBarsFromComposition(id2, 5, null);
    CompositionFrontDTO composition12 = multipleClientsComposeService.getNextBarsFromComposition(id2, 5, null);
    String id3 = "id3";
    CompositionFrontDTO composition21 = multipleClientsComposeService.getNextBarsFromComposition(id3, 3, null);
    CompositionFrontDTO composition22 = multipleClientsComposeService.getNextBarsFromComposition(id3, 3, null);
    CompositionFrontDTO composition23 = multipleClientsComposeService.getNextBarsFromComposition(id3, 4, null);

    // Checking same compose results if sum of bars are equal regardless of number of requests
    Composition compositionGather1 =
        compositionComposer.gatherComposition(composition11.getComposition(), composition12.getComposition());
    Composition compositionGather2 = compositionComposer
        .gatherComposition(composition21.getComposition(), composition22.getComposition(),
            composition23.getComposition());
    assertTrue(composition1.hasSameNoteContent(compositionGather1));
    assertTrue(composition1.hasSameNoteContent(compositionGather2));
    assertTrue(compositionGather1.hasSameNoteContent(compositionGather2));
  }

  @Test
  void sameLexiconBeansInsideButDifferentProviders() {
    String id1 = "id1";
    multipleClientsComposeService.getNextBarsFromComposition(id1, 10, null);
    String id2 = "id2";
    multipleClientsComposeService.getNextBarsFromComposition(id2, 10, null);

    ComposingParameters composingParametersById1 = multipleClientsComposeService.getComposingParametersById(id1);
    ComposingParameters composingParametersById2 = multipleClientsComposeService.getComposingParametersById(id2);

    assertSame(composingParametersById1.getLexicon(), composingParametersById2.getLexicon());
    assertNotSame(composingParametersById1.getComposeStepProvider(), composingParametersById2.getComposeStepProvider());
    assertSame(composingParametersById1.getComposeStepProvider().getFirstStepProvider(),
        composingParametersById2.getComposeStepProvider().getFirstStepProvider());
    assertNotSame(composingParametersById1.getComposeStepProvider().getNextStepProvider(),
        composingParametersById2.getComposeStepProvider().getNextStepProvider());
    assertNotSame(composingParametersById1.getPreviousCompositionSteps(), composingParametersById2.getPreviousCompositionSteps());
  }
}