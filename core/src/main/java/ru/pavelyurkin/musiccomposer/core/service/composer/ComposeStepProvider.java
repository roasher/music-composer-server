package ru.pavelyurkin.musiccomposer.core.service.composer;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.service.composer.first.FirstStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

/**
 * Created by wish on 28.01.2016.
 */
@AllArgsConstructor
@Data
public class ComposeStepProvider {

  private final FirstStepProvider firstStepProvider;
  private NextStepProvider nextStepProvider;

  public Optional<CompositionStep> getNext(double length, List<CompositionStep> previousCompositionSteps,
                                           List<FormCompositionStep> previousFormCompositionSteps,
                                           Optional<Form> form) {
    return nextStepProvider.getNext(previousCompositionSteps, previousFormCompositionSteps, form, length);
  }

  public Optional<CompositionStep> getFirst(Lexicon lexicon, List<ComposeBlock> exclusions) {
    return firstStepProvider.getFirstBlock(lexicon, exclusions);
  }

}
