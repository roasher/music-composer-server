package ru.pavelyurkin.musiccomposer.core.service.multipleclients;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.service.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

/**
 * Holds parameters of Composing
 */
@Data
public class ComposingParameters {

  private Lexicon lexicon;
  private ComposeStepProvider composeStepProvider;
  private List<CompositionStep> previousCompositionSteps = Collections.emptyList();

}
