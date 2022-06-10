package ru.pavelyurkin.musiccomposer.core.service.composer.first;

import java.util.List;
import java.util.Optional;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;

public interface FirstStepProvider {

  Optional<CompositionStep> getFirstBlock(Lexicon lexicon, List<ComposeBlock> exclusions);

}
