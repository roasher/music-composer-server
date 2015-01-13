package composer;

import jm.music.data.Part;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
	public Composition simpleCompose( List<MusicBlock > musicBlockList, double rhythmValue ) {
		List< MusicBlock > musicBlocks = getFormElement( musicBlockList, rhythmValue );
		return gatherComposition( musicBlocks );
	}

	/**
	 * Composes form element
	 * @param musicBlockList
	 * @return
	 */
	public List<MusicBlock> getFormElement( List<MusicBlock> musicBlockList, double rhythmValue ) {
		// TODO need to think how to implement form
		List<MusicBlock> formElement = new ArrayList<>();
		double currentRhythmValue = 0;

		while ( currentRhythmValue < rhythmValue && musicBlockList != null ) {
			MusicBlock musicBlock;
			if ( formElement.isEmpty() ) {
				musicBlock = chooseNextMusicBlock( null, musicBlockList );
			} else {
				musicBlock = chooseNextMusicBlock( formElement.get( formElement.size() - 1 ), musicBlockList );
			}
			if ( musicBlock == null ) {
				return formElement;
			}
			formElement.add( musicBlock );
			currentRhythmValue += musicBlock.getRhythmValue();
			if ( currentRhythmValue > rhythmValue ) {
				return formElement;
			}
		}
		return formElement;
	}

	/**
	 * If input music block is null - choose and return first music block, otherwise
	 * return music block that has same interval pattern with original next music block and same block movement
	 * @param currentMusicBlock
	 * @param musicBlockList
	 * @return
	 */
	public MusicBlock chooseNextMusicBlock( MusicBlock currentMusicBlock, List< MusicBlock > musicBlockList ) {
		if ( currentMusicBlock == null ) {
			MusicBlock firstMusicBlock =  musicBlockList.get( ( int ) Math.random() );
			logger.debug( "First music block has been chosen: \n {}", firstMusicBlock );
			return firstMusicBlock;
		} else if ( currentMusicBlock.getNext() == null ) {
			logger.debug( "There is no music block after this one in the original composition. Returning null" );
			return null;
		} else {
			for ( MusicBlock musicBlock : musicBlockList ) {
				if ( canFollow( currentMusicBlock, musicBlock ) ) {
					logger.debug( "Next music block has been found: {}", musicBlock );
					return musicBlock;
				}
			}
			MusicBlock musicBlock = currentMusicBlock.getNext();
			logger.debug( "Can't find next music block other than form the original composition: {}", musicBlock );
			return musicBlock;
		}
	}

	/**
	 * Answers if second music block can be placed after first.
	 * @param current
	 * @param next
	 * @return
	 */
	public boolean canFollow( MusicBlock current, MusicBlock next ) {
		boolean currentFollowedByNextInOrigin = current.getNext().equals( next );
		boolean followedInOriginHasSameIntervalPattern = current.getNext().getStartIntervalPattern().equals( next.getStartIntervalPattern() );
		boolean hasSameBlockMovement = current.getBlockMovementFromThisToNext().equals( next.getBlockMovementFromPreviousToThis() );
		return !currentFollowedByNextInOrigin && followedInOriginHasSameIntervalPattern && hasSameBlockMovement;
	}

	private Composition gatherComposition( List<MusicBlock> musicBlockList ) {
		List<Part> parts = new ArrayList<>();
		for ( int partNumber = 0; partNumber < musicBlockList.get( 0 ).getMelodyList().size(); partNumber++ ) {
			parts.add( new Part() );
		}
		for ( MusicBlock musicBlock : musicBlockList ) {
			for ( int partNumber = 0; partNumber < parts.size(); partNumber++ ) {
				// creating new object to zero start time saving original phrases
				Melody melody = new Melody( musicBlock.getMelodyList().get( partNumber ).getNoteArray() );
				parts.get( partNumber ).add( melody );
			}
		}
		Composition composition = new Composition( parts );
		return composition;
	}
}
