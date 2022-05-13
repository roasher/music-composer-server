package ru.pavelyurkin.musiccomposer.core.composer.first;

import java.util.List;
import java.util.Optional;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

/**
 * Created by Wish on 22.12.2015.
 */
public interface FirstStepProvider {

  Optional<CompositionStep> getFirstBlock(Lexicon lexicon, List<ComposeBlock> exclusions);

}
