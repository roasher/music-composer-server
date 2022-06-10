package ru.pavelyurkin.musiccomposer.core.service.composer.step;

import java.util.Arrays;
import java.util.List;
import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

/**
 * Class represents step that program makes in order to create new composition
 * One step - one added form block to composition
 */
@Data
public class FormCompositionStep {

  private List<CompositionStep> compositionSteps;
  private Form form;

  public FormCompositionStep(List<CompositionStep> compositionSteps, Form form) {
    this.compositionSteps = compositionSteps;
    this.form = form;
  }

  /**
   * Returns empty composition step using just for tracking new block exceptions
   */
  public static FormCompositionStep getEmptyStep() {
    return new FormCompositionStep(Arrays.asList(CompositionStep.getEmptyCompositionStep()), null);
  }

}
