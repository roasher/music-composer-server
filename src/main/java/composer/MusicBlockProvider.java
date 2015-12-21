package composer;

import model.BlockMovement;
import model.ComposeBlock;
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

//	/**
//	 * Returns first music block from musicBlockList except exclusion list that was found possible to be next to input currentMusicBlock
//	 * @param currentMusicBlock
//	 * @param musicBlockList
//	 * @param exclusion
//	 * @return
//	 */
//	public MusicBlock getFirstConvenientMusicBlock( MusicBlock currentMusicBlock, List<MusicBlock> musicBlockList, List<MusicBlock> exclusion ) {
//		List<MusicBlock> cloneLexicon = new ArrayList<>( musicBlockList );
//		cloneLexicon.removeAll( exclusion );
//		MusicBlock musicBlock = getFirstConvenientMusicBlock( currentMusicBlock, cloneLexicon );
//		return musicBlock;
//	}

//	/**
//	 * Returns first music block from musicBlockList that was found possible to be next to input currentMusicBlock
//	 * @param currentMusicBlock
//	 * @param musicBlockList
//	 * @return
//	 */
//	public MusicBlock getFirstConvenientMusicBlock( MusicBlock currentMusicBlock, List<MusicBlock> musicBlockList ) {
//		logger.debug( "Searching possible music block next to {}", currentMusicBlock );
//		if ( currentMusicBlock == null ) {
//			logger.debug( "Searching for first music block from the musicBlockList" );
//			for ( MusicBlock musicBlock : musicBlockList ) {
//				if ( musicBlock.getPrevious() == null ) {
//					return musicBlock;
//				}
//			}
//		} else if ( currentMusicBlock.getNext() == null ) {
//			logger.info( "There is no music block after this one in the original composition." );
//		} else {
//			for ( MusicBlock musicBlock : musicBlockList ) {
//				if ( canSubstitute( currentMusicBlock.getNext(), musicBlock ) ) {
//					logger.info( "Possible next music block has been found: {}, {}", musicBlock.buildCompositionInfo().getTitle(), musicBlock );
//					canSubstitute( currentMusicBlock.getNext(), musicBlock );
//					return musicBlock;
//				}
//			}
//		}
//		// If we didn't find the proper music block will return music block from the original composition if it is in input musicBlockList
//		if ( currentMusicBlock != null && musicBlockList.contains( currentMusicBlock.getNext() ) ) {
//			logger.warn( "Can't find proper music block. Returning next from the original composition." );
//			return currentMusicBlock.getNext();
//		} else {
//			logger.warn( "Can't find proper music block. Returning null." );
//			return null;
//		}
//	}

	/**
	 * Retrieves all possible music blocks that can go after currentMusicBlock
	 * If currentMusicBlock is null - function will return all first music blocks from the musicBlockList
	 * First possible next variant - Music block from original composition
	 * @param currentMusicBlock
	 * @param musicBlockList - music block lexicon
	 * @return
	 */
	public List<MusicBlock> getAllPossibleNextVariants( MusicBlock currentMusicBlock, List<MusicBlock> musicBlockList ) {
		logger.debug( "Searching for all possible music block next to {}", currentMusicBlock );
		List<MusicBlock> possibleNext = new ArrayList<>(  );
		if ( currentMusicBlock == null ) {
			logger.debug( "Searching for first music blocks from the musicBlockList" );
			for ( MusicBlock musicBlock : musicBlockList ) {
				if ( musicBlock.getPrevious() == null ) {
					possibleNext.add( musicBlock );
				}
			}
		} else if ( currentMusicBlock.getNext() == null ) {
			logger.info( "There is no music block after this one in the original composition." );
		} else {
			possibleNext.add( currentMusicBlock.getNext() );
			for ( MusicBlock musicBlock : musicBlockList ) {
				if ( currentMusicBlock.getNext() != musicBlock && canSubstitute( currentMusicBlock.getNext(), musicBlock ) ) {
					logger.debug( "Possible next music block has been found: {}", musicBlock );
					possibleNext.add( musicBlock );
				}
			}
		}
		return possibleNext;
	}

	/**
	 * Answers if origin can be replaced by substitutor
	 * @param originBlock
	 * @param substitutorBlock
	 * @return
	 */
	public boolean canSubstitute( MusicBlock originBlock, MusicBlock substitutorBlock ) {
		return canSubstitute( originBlock.getStartIntervalPattern(), originBlock.getBlockMovementFromPreviousToThis(), originBlock.getStartTime(),
				substitutorBlock.getStartIntervalPattern(), substitutorBlock.getBlockMovementFromPreviousToThis(), substitutorBlock.getStartTime() );
	}

	public boolean canSubstitute( ComposeBlock originBlock, ComposeBlock substitutorBlock ) {
		return canSubstitute( originBlock.getStartIntervalPattern(), originBlock.getBlockMovementFromPreviousToThis(), originBlock.getStartTime(),
				substitutorBlock.getStartIntervalPattern(), substitutorBlock.getBlockMovementFromPreviousToThis(), substitutorBlock.getStartTime() );
	}

	private boolean canSubstitute(
			List<Integer> firstStartIntervalPattern,
			BlockMovement fromPreviousToFirst,
			double firstStartTime,

			List<Integer> secondStartIntervalPattern,
			BlockMovement fromPreviousToSecond,
			double secondStartTime ) {

		//		boolean totalEquality = originBlock.equals( substitutorBlock );
		boolean startIntervalPatternEquality = firstStartIntervalPattern.equals( secondStartIntervalPattern );
		boolean blockMovementEquality;
		if ( fromPreviousToFirst == null && fromPreviousToSecond == null ) {
			blockMovementEquality = true;
		} else if ( fromPreviousToFirst != null && fromPreviousToSecond != null ) {
			blockMovementEquality = fromPreviousToFirst.equals( fromPreviousToSecond );
		} else {
			blockMovementEquality = false;
		}

		boolean correlatingTime = onCorrelatedTime( firstStartTime, secondStartTime );

		//		boolean canSubstitute = !totalEquality && startIntervalPatternEquality && blockMovementEquality && correlatingTime;

		boolean canSubstitute = startIntervalPatternEquality && blockMovementEquality && correlatingTime;
		return canSubstitute;
	}

	/**
	 * Check if two times have equal "strength"
	 * @param firstStartTime
	 * @param secondStartTime
	 * @return
	 */
	private boolean onCorrelatedTime( double firstStartTime, double secondStartTime ) {
		int originStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( firstStartTime );
		int substitutorStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( secondStartTime );
		if ( originStartTimeDecimalPlacesNumber == substitutorStartTimeDecimalPlacesNumber ) {
			return true;
		}
		return false;
	}
}
