package ru.pavelyurkin.musiccomposer.core.composer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jm.JMC;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

/**
 * Created by pyurkin on 17.02.15.
 */
public class FormBlockProviderTest extends AbstractSpringTest {
  @Autowired
  private CompositionLoader compositionLoader;
  @Autowired
  private CompositionDecomposer compositionDecomposer;
  @Autowired
  private FormBlockProvider formBlockProvider;
  @Autowired
  private ComposeStepProvider composeStepProvider;

  @Test
  @Ignore
  // form related. todo: fix
  public void formBlockProviderTest() {
    List<Composition> compositionList =
        compositionLoader.getCompositionsFromFolder(new File("src/test/resources/simpleMelodies"));
    Lexicon lexiconFromFirst = compositionDecomposer.decompose(compositionList.get(0), JMC.WHOLE_NOTE);

    double lenght = JMC.WHOLE_NOTE;
    Optional<FormCompositionStep> optFormCompositionStep = formBlockProvider
        .getFormElement(lenght, lexiconFromFirst, composeStepProvider, new Form('A'), new ArrayList<>(),
            new ArrayList<>());

    assertTrue(optFormCompositionStep.isPresent());
    optFormCompositionStep.ifPresent(formCompositionStep -> {
      // checking length
      Assert.assertEquals(lenght, ModelUtils.sumAllRhythmValues(
          formCompositionStep.getCompositionSteps()
              .stream()
              .map(CompositionStep::getTransposedBlock)
              .collect(Collectors.toList())), 0);
      // checking quality of composing
      for (int compositionStepNumber = 1; compositionStepNumber < formCompositionStep.getCompositionSteps().size();
           compositionStepNumber++) {
        ComposeBlock currentOrigin =
            formCompositionStep.getCompositionSteps().get(compositionStepNumber).getOriginComposeBlock();
        ComposeBlock previousOrigin =
            formCompositionStep.getCompositionSteps().get(compositionStepNumber - 1).getOriginComposeBlock();
        assertTrue(previousOrigin.getPossibleNextComposeBlocks().contains(currentOrigin));

        // todo check that transposed right???
      }
    });
  }

}
