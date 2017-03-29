package ru.pavelyurkin.musiccomposer.core.composer.first;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;

import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 */
public interface FirstBlockProvider {

	Optional<CompositionStep> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions );

}
