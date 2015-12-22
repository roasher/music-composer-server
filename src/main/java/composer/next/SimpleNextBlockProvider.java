package composer.next;

import model.ComposeBlock;
import model.Lexicon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wish on 22.12.2015.
 */
public class SimpleNextBlockProvider implements NextBlockProvider {

	/**
	 * Returns one of the possible next currentBlocks randomly
	 */
	@Override
	public ComposeBlock getNextBlock( Lexicon lexicon, ComposeBlock previousComposeBlock, List<ComposeBlock> exclusions ) {
		List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( previousComposeBlock.getPossibleNextComposeBlocks() );
		possibleNextComposeBlocks.removeAll( exclusions );
		if ( !possibleNextComposeBlocks.isEmpty() ) {
			int randomNumber = ( int ) ( Math.random() * ( possibleNextComposeBlocks.size() - 1 ) );
			//int randomNumber = 0;
			ComposeBlock composeBlock = possibleNextComposeBlocks.get( randomNumber );
			return composeBlock;
		} else {
			return null;
		}
	}

}
