package composer;

import model.BlockMovement;
import model.ComposeBlock;
import model.MusicBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pyurkin on 15.01.15.
 */
@Component
public class MusicBlockProvider {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Retruns Map<Integer, List<Integer>> where Integer - number of music block from input collection,
	 * corresponding list - collection of possible next music block numbers
	 * @param musicBlocks
	 * @return
	 */
	public Map<Integer, List<Integer>> getAllPossibleNextVariants( List<MusicBlock> musicBlocks ) {
		Map<Integer, List<Integer>> map = new HashMap<>( musicBlocks.size() );
		for ( int firstMusicBlockNumber = 0; firstMusicBlockNumber < musicBlocks.size(); firstMusicBlockNumber++ ) {
			List<Integer> possibleNextMusicBlockNumbers = new ArrayList<>();
			map.put( firstMusicBlockNumber, possibleNextMusicBlockNumbers );
			for ( int secondMusicBlockNumber = 0; secondMusicBlockNumber < musicBlocks.size(); secondMusicBlockNumber++ ) {
				MusicBlock firstMusicBlock = musicBlocks.get( firstMusicBlockNumber );
				MusicBlock secondMusicBlock = musicBlocks.get( secondMusicBlockNumber );
				boolean isOriginallyNext = secondMusicBlock == firstMusicBlock.getNext();
				if ( isOriginallyNext ) {
					possibleNextMusicBlockNumbers.add( 0, secondMusicBlockNumber );
				} else if ( canSubstitute( firstMusicBlock.getNext(), secondMusicBlock ) ) {
					possibleNextMusicBlockNumbers.add( secondMusicBlockNumber );
				}
			}
		}
		return map;
	}

	/**
	 * Answers if origin can be replaced by substitutor
	 * @param originBlock
	 * @param substitutorBlock
	 * @return
	 */
	public boolean canSubstitute( MusicBlock originBlock, MusicBlock substitutorBlock ) {
		if ( originBlock == null || substitutorBlock == null ) return false;
		return canSubstitute( originBlock.getStartIntervalPattern(), originBlock.getBlockMovementFromPreviousToThis(), originBlock.getStartTime(),
				substitutorBlock.getStartIntervalPattern(), substitutorBlock.getBlockMovementFromPreviousToThis(), substitutorBlock.getStartTime() );
	}

	public boolean canSubstitute( ComposeBlock originBlock, ComposeBlock substitutorBlock ) {
		if ( originBlock == null || substitutorBlock == null ) return false;
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
