package ru.pavelyurkin.musiccomposer.core.service.composer.first;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

@Component
public class SimpleFirstStepProvider implements FirstStepProvider {
  @Override
  public Optional<CompositionStep> getFirstBlock(Lexicon lexicon, List<ComposeBlock> exclusions) {
    Optional<ComposeBlock> firstBlock = lexicon.getComposeBlocks().stream()
        .filter(composeBlock -> !exclusions.contains(composeBlock) && !composeBlock.isStartsWithRest())
        .findFirst();
    return firstBlock.map(composeBlock -> new CompositionStep(composeBlock, composeBlock.getMusicBlock().clone()));
  }
}
