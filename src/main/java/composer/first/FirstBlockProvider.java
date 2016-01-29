package composer.first;

import model.ComposeBlock;
import model.Lexicon;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 */
public interface FirstBlockProvider {

	Optional<ComposeBlock> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions );

}
