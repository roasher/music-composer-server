package composer;

import model.MusicBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyurkin on 15.01.15.
 */
@Component
public class MusicBlockProvider {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Returns first music block from lexicon except exclusion list that was found possible to be next to input currentMusicBlock
	 * @param currentMusicBlock
	 * @param lexicon
	 * @param exclusion
	 * @return
	 */
	public MusicBlock getFirstConvenientMusicBlock( MusicBlock currentMusicBlock, List<MusicBlock> lexicon, List<MusicBlock> exclusion ) {
		List<MusicBlock> cloneLexicon = new ArrayList<>( lexicon );
		cloneLexicon.removeAll( exclusion );
		MusicBlock musicBlock = getFirstConvenientMusicBlock( currentMusicBlock, cloneLexicon );
		return musicBlock;
	}

	/**
	 * Returns first music block from lexicon that was found possible to be next to input currentMusicBlock
	 * @param currentMusicBlock
	 * @param lexicon
	 * @return
	 */
	public MusicBlock getFirstConvenientMusicBlock( MusicBlock currentMusicBlock, List<MusicBlock> lexicon ) {
		logger.debug( "Searching possible music block next to {}", currentMusicBlock );
		if ( currentMusicBlock == null ) {
			logger.debug( "Searching for first music block from the lexicon" );
			for ( MusicBlock musicBlock : lexicon ) {
				if ( musicBlock.getPrevious() == null ) {
					return musicBlock;
				}
			}
		} else if ( currentMusicBlock.getNext() == null ) {
			logger.info( "There is no music block after this one in the original composition." );
		} else {
			for ( MusicBlock musicBlock : lexicon ) {
				if ( canSubstitute( currentMusicBlock.getNext(), musicBlock ) ) {
					logger.info( "Possible next music block has been found: {}, {}", musicBlock.getCompositionInfo().getTitle(), musicBlock );
					canSubstitute( currentMusicBlock.getNext(), musicBlock );
					return musicBlock;
				}
			}
		}
		// If we didn't find the proper music block will return music block from the original composition if it is in input lexicon
		if ( currentMusicBlock != null && lexicon.contains( currentMusicBlock.getNext() ) ) {
			logger.warn( "Can't find proper music block. Returning next from the original composition." );
			return currentMusicBlock.getNext();
		} else {
			logger.warn( "Can't find proper music block. Returning null." );
			return null;
		}
	}

	/**
	 * Retrieves all possible music blocks that can go after currentMusicBlock
	 * If currentMusicBlock is null - function will return all first music blocks from the lexicon
	 * @param currentMusicBlock
	 * @param lexicon
	 * @return
	 */
	public List<MusicBlock> getAllPossibleVariants( MusicBlock currentMusicBlock, List<MusicBlock> lexicon ) {
		logger.debug( "Searching for all possible music block next to {}", currentMusicBlock );
		List<MusicBlock> musicBlockList = new ArrayList<>(  );
		if ( currentMusicBlock == null ) {
			logger.debug( "Searching for first music blocks from the lexicon" );
			for ( MusicBlock musicBlock : lexicon ) {
				if ( musicBlock.getPrevious() == null ) {
					musicBlockList.add( musicBlock );
				}
			}
		} else if ( currentMusicBlock.getNext() == null ) {
			logger.info( "There is no music block after this one in the original composition." );
		} else {
			for ( MusicBlock musicBlock : lexicon ) {
				if ( canSubstitute( currentMusicBlock.getNext(), musicBlock ) ) {
					logger.info( "Possible next music block has been found: {}, {}", musicBlock.getCompositionInfo().getTitle(), musicBlock );
					canSubstitute( currentMusicBlock.getNext(), musicBlock );
					musicBlockList.add( musicBlock );
				}
			}
		}
		return musicBlockList;
	}

	/**
	 * Answers if second music block can be placed after first.
	 * @param originBlock
	 * @param substitutorBlock
	 * @return
	 */
	public boolean canSubstitute( MusicBlock originBlock, MusicBlock substitutorBlock ) {
		boolean totalEquality = originBlock.equals( substitutorBlock );
		boolean startIntervalPatternEquality = originBlock.getStartIntervalPattern().equals( substitutorBlock.getStartIntervalPattern() );
		boolean blockMovementEquality = originBlock.getBlockMovementFromPreviousToThis().equals( substitutorBlock.getBlockMovementFromPreviousToThis() );
		boolean correlatingTime = onCorrelatedTime( originBlock, substitutorBlock );

		boolean canSubstitute = !totalEquality && startIntervalPatternEquality && blockMovementEquality && correlatingTime;
		if ( canSubstitute ) {
			logger.debug( "{} and {} has been considered substitutable", originBlock, substitutorBlock );
		}
		return canSubstitute;
	}

	/**
	 * Checks if one block can be substituted with another in terms of time accents
	 * @param originBlock
	 * @param substitutorBlock
	 * @return
	 */
	public boolean onCorrelatedTime( MusicBlock originBlock, MusicBlock substitutorBlock ) {
		int originStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( originBlock.getStartTime() );
		int substitutorStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( substitutorBlock.getStartTime() );
		if ( originStartTimeDecimalPlacesNumber == substitutorStartTimeDecimalPlacesNumber ) {
			return true;
		}
		return false;
	}
}
