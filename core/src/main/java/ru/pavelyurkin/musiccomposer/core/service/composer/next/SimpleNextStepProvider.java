package ru.pavelyurkin.musiccomposer.core.service.composer.next;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class SimpleNextStepProvider extends FilteredNextStepProvider {

  public SimpleNextStepProvider(ComposeStepFilter composeStepFilter) {
    super(composeStepFilter);
  }

  @Override
  public Optional<CompositionStep> getNextBlockFiltered(List<CompositionStep> blocksToChooseFrom,
                                                        List<CompositionStep> previousCompositionSteps,
                                                        List<FormCompositionStep> formCompositionSteps,
                                                        Optional<Form> form) {

    Optional<CompositionStep> lastOfPossibles =
        blocksToChooseFrom.stream().reduce((composeBlock1, composeBlock2) -> composeBlock2);
    return lastOfPossibles;
  }

}
