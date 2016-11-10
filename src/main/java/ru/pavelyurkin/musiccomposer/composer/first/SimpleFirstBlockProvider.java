package ru.pavelyurkin.musiccomposer.composer.first;

import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.model.Lexicon;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class SimpleFirstBlockProvider implements FirstBlockProvider {
	@Override
	public Optional<ComposeBlock> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions ) {
		return lexicon.getComposeBlockList().stream().filter( composeBlock -> !exclusions.contains( composeBlock ) && !composeBlock.isStartsWithRest() ).findFirst();
	}
}
