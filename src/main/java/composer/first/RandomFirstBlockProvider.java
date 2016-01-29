package composer.first;

import composer.next.NextBlockProvider;
import model.ComposeBlock;
import model.Lexicon;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 */
@Component
public class RandomFirstBlockProvider implements FirstBlockProvider {

	/**
	 * Randomly returns one of the compose blocks witch have no possible previous blocks - it means that they are first blocks in the original composition
	 *
	 * @return
	 */
	@Override
	public Optional<ComposeBlock> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions ) {
		List<ComposeBlock> allPossibleFirstBlocks = lexicon.getAllPossibleFirst();
		allPossibleFirstBlocks.removeAll( exclusions );
		if ( !allPossibleFirstBlocks.isEmpty() ) {
			int randomNumber = ( int ) ( Math.random() * ( allPossibleFirstBlocks.size() - 1 ) );
			//int randomNumber = 0;
			return Optional.of( allPossibleFirstBlocks.get( randomNumber ) );
		} else {
			return Optional.empty();
		}
	}

}
