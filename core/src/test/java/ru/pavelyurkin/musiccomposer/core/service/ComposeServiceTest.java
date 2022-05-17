package ru.pavelyurkin.musiccomposer.core.service;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ru.pavelyurkin.musiccomposer.core.utils.ModelUtils.getRhythmValue;
import static ru.pavelyurkin.musiccomposer.core.utils.Utils.epsilon;

import java.io.File;
import java.util.List;
import jm.JMC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

public class ComposeServiceTest extends AbstractSpringTest {

  @Autowired
  private ComposeService composeService;

  @Autowired
  private CompositionComposer compositionComposer;

  @Test
  public void getNextBarsFromComposition() throws Exception {
    String id1 = "id1";
    CompositionFrontDTO composition1 = composeService.getNextBarsFromComposition(id1, 10, null);
    String id2 = "id2";
    CompositionFrontDTO composition11 = composeService.getNextBarsFromComposition(id2, 5, null);
    CompositionFrontDTO composition12 = composeService.getNextBarsFromComposition(id2, 5, null);
    String id3 = "id3";
    CompositionFrontDTO composition21 = composeService.getNextBarsFromComposition(id3, 3, null);
    CompositionFrontDTO composition22 = composeService.getNextBarsFromComposition(id3, 3, null);
    CompositionFrontDTO composition23 = composeService.getNextBarsFromComposition(id3, 4, null);
    // Checking same lexicon
    assertSame(composeService.getComposingParametersById(id1).getLexicon(),
        composeService.getComposingParametersById(id2).getLexicon());
    assertSame(composeService.getComposingParametersById(id1).getLexicon(),
        composeService.getComposingParametersById(id3).getLexicon());
    // Checking same compose results if sum of bars are equal regardless of number of requests
    Composition compositionGather1 =
        compositionComposer.gatherComposition(composition11.getComposition(), composition12.getComposition());
    Composition compositionGather2 = compositionComposer
        .gatherComposition(composition21.getComposition(), composition22.getComposition(),
            composition23.getComposition());
    assertTrue(composition1.hasSameNoteContent(compositionGather1));
    assertTrue(composition1.hasSameNoteContent(compositionGather2));
    assertTrue(compositionGather1.hasSameNoteContent(compositionGather2));
    // checking rhythmValues
    assertThat(composition1.getPreviousSumRhythmValues(), closeTo(0, epsilon));

    assertThat(composition11.getPreviousSumRhythmValues(), closeTo(0, epsilon));
    assertThat(composition12.getPreviousSumRhythmValues(),
        closeTo(getRhythmValue(composition11.getComposition()), epsilon));

    assertThat(composition21.getPreviousSumRhythmValues(), closeTo(0, epsilon));
    double rhythmValueComposition21 = getRhythmValue(composition21.getComposition());
    assertThat(composition22.getPreviousSumRhythmValues(), closeTo(rhythmValueComposition21, epsilon));
    assertThat(composition23.getPreviousSumRhythmValues(),
        closeTo(getRhythmValue(composition22.getComposition()) + rhythmValueComposition21, epsilon));
  }

}