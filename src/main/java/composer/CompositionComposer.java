package composer;

import model.MusicBlock;
import model.composition.Composition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class handles composition of new piece using lexicon
 * Created by pyurkin on 15.12.14.
 */
@Component
public class CompositionComposer {
	private Logger logger = LoggerFactory.getLogger( getClass() );
	/**
	 * First try to implement compose logic:
	 * @param musicBlockList
	 * @return
	 */
	public Composition simpleCompose( List<MusicBlock > musicBlockList ) {
		return null;
	}

	/**
	 * Composes form element
	 * @param musicBlockList
	 * @return
	 */
	public List<MusicBlock> composeFormElement( List< MusicBlock > musicBlockList, String form, double rhythmValue ) {

	}

	public MusicBlock chooseNextMusicBlock( MusicBlock currentMusicBlock, List< MusicBlock > musicBlockList ) {
		if ( currentMusicBlock == null ) {
			MusicBlock firstMusicBlock =  musicBlockList.get( ( int ) Math.random() );
			logger.debug( "First music block has been chosen: \n {}", firstMusicBlock );
			return firstMusicBlock;
		} else {
			for ( MusicBlock musicBlock : musicBlockList ) {
				if ( !currentMusicBlock.getNext().equals( musicBlock ) &&
				  currentMusicBlock.getNext().getStartIntervalPattern().equals( musicBlock.getStartIntervalPattern() ) &&
				  currentMusicBlock.getBlockMovementFromThisMusicBlockToNextMusicBlock() == musicBlock.getBlockMovementFromPreviousMusicBlockToThisMusicBlock() ) {
					logger.debug( "Next musci block has been found: {}", musicBlock );
					return musicBlock;
				}
			}
			MusicBlock musicBlock = currentMusicBlock.getNext();
			logger.debug( "Can't find next music block other than form the original composition: {}", musicBlock );
			return musicBlock;
		}
	}
}
