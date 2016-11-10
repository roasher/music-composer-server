package ru.pavelyurkin.musiccomposer.composer.first;

import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.model.Lexicon;

import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 */
public interface FirstBlockProvider {

	Optional<ComposeBlock> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions );

}
