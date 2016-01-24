package composer.first;

import model.ComposeBlock;
import model.Lexicon;

import java.util.List;

/**
 * Created by Wish on 22.12.2015.
 */
public interface FirstBlockProvider {

	public ComposeBlock getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions );

}
