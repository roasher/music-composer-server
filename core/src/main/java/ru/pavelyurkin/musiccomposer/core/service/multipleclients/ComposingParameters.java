package ru.pavelyurkin.musiccomposer.core.service.multipleclients;

import java.util.Collections;
import java.util.List;
import ru.pavelyurkin.musiccomposer.core.service.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

/**
 * Holds parameters of Composing
 */
public class ComposingParameters {

  private Lexicon lexicon;
  private ComposeStepProvider composeStepProvider;
  private List<CompositionStep> previousCompositionSteps = Collections.emptyList();

  public List<CompositionStep> getPreviousCompositionSteps() {
    return previousCompositionSteps;
  }

  public void setPreviousCompositionSteps(List<CompositionStep> previousCompositionSteps) {
    this.previousCompositionSteps = previousCompositionSteps;
  }

  public Lexicon getLexicon() {
    return lexicon;
  }

  public void setLexicon(Lexicon lexicon) {
    this.lexicon = lexicon;
  }

  public ComposeStepProvider getComposeStepProvider() {
    return composeStepProvider;
  }

  public void setComposeStepProvider(ComposeStepProvider composeStepProvider) {
    this.composeStepProvider = composeStepProvider;
  }

}
