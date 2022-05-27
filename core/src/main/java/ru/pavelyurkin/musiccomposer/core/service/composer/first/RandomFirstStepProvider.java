package ru.pavelyurkin.musiccomposer.core.service.composer.first;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;

/**
 * Randomly returns one of the compose blocks witch have no possible previous blocks - it means that they are
 * first blocks in the original composition
 */
@Component
@Slf4j
public class RandomFirstStepProvider implements FirstStepProvider {

  @Override
  public Optional<CompositionStep> getFirstBlock(Lexicon lexicon, List<ComposeBlock> exclusions) {
    List<ComposeBlock> allPossibleFirstBlocks = lexicon.getAllPossibleFirsts();
    allPossibleFirstBlocks.removeAll(exclusions);
    if (!allPossibleFirstBlocks.isEmpty()) {
      int randomNumber = (int) (Math.random() * (allPossibleFirstBlocks.size() - 1));
      log.info("First block number {}", randomNumber);
      ComposeBlock composeBlock = allPossibleFirstBlocks.get(randomNumber);
      return Optional.of(new CompositionStep(composeBlock, composeBlock.getMusicBlock().clone()));
    } else {
      return Optional.empty();
    }
  }

}
