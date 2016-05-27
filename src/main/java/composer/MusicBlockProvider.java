package composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import model.ComposeBlock;
import model.MusicBlock;
import utils.Utils;

/**
 * Created by pyurkin on 15.01.15.
 */
@Component
public class MusicBlockProvider {

	/**
	 * Retruns Map<Integer, List<Integer>> where Integer - music block number from input collection,
	 * corresponding list - collection of possible next music block numbers
	 * @param musicBlocks
	 * @return
	 */
	public Map<Integer, List<Integer>> getAllPossibleNextVariants( List<MusicBlock> musicBlocks ) {
		Map<Integer, List<Integer>> map = new HashMap<>( musicBlocks.size() );
		for ( int firstMusicBlockNumber = 0; firstMusicBlockNumber < musicBlocks.size(); firstMusicBlockNumber++ ) {
			List<Integer> possibleNextMusicBlockNumbers = new ArrayList<>();
			map.put( firstMusicBlockNumber, possibleNextMusicBlockNumbers );
			for ( int secondMusicBlockNumber = 1; secondMusicBlockNumber < musicBlocks.size(); secondMusicBlockNumber++ ) {
				if ( secondMusicBlockNumber - firstMusicBlockNumber == 1 ) {
					possibleNextMusicBlockNumbers.add( 0, secondMusicBlockNumber );
				}
				if ( canSubstitute( firstMusicBlockNumber, secondMusicBlockNumber, musicBlocks ) ) {
					map.get( firstMusicBlockNumber - 1 ).add( secondMusicBlockNumber );
				}
			}
		}
		return map;
	}

	public boolean isSame( List<Integer> firstIntervalPattern, double firstStartTime, List<Integer> secondIntervalPattern, double
		secondStartTime) {
		boolean correlatingTime = onCorrelatedTime( firstStartTime, secondStartTime );
		boolean intervalPatternEquality = firstIntervalPattern.equals( secondIntervalPattern );
		return intervalPatternEquality && correlatingTime;
	}

	/**
	 * Blocks are substitutable if THEIR PREVIOUSES have same interval patterns and time correlating
	 * @return
   */
	public boolean canSubstitute( int firstBlockNumber, int secondBlockNumber, List<MusicBlock> musicBlocks ) {
		if ( firstBlockNumber > 0 && secondBlockNumber > 0 ) {
			MusicBlock preFirst = musicBlocks.get( firstBlockNumber - 1 );
			MusicBlock preSecond = musicBlocks.get( secondBlockNumber - 1 );
			return isSame( preFirst.getEndIntervalPattern(), preFirst.getStartTime(), preSecond.getEndIntervalPattern(), preSecond
				.getStartTime() );
		}
		return false;
	}

	public boolean canSubstitute(ComposeBlock firstComposeBlock, ComposeBlock secondComposeBlock) {
		// TODO impl
		return false;
	}
	/**
	 * Check if two times have equal "strength"
	 * @param firstStartTime
	 * @param secondStartTime
	 * @return
	 */
	public boolean onCorrelatedTime( double firstStartTime, double secondStartTime ) {
		int originStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( firstStartTime );
		int substitutorStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( secondStartTime );
		if ( originStartTimeDecimalPlacesNumber == substitutorStartTimeDecimalPlacesNumber ) {
			return true;
		}
		return false;
	}

}
