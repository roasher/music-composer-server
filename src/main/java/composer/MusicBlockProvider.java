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
			for ( int secondMusicBlockNumber = 0; secondMusicBlockNumber < musicBlocks.size(); secondMusicBlockNumber++ ) {
				MusicBlock firstMusicBlock = musicBlocks.get( firstMusicBlockNumber );
				MusicBlock secondMusicBlock = musicBlocks.get( secondMusicBlockNumber );
				if ( secondMusicBlock == firstMusicBlock.getNext() ) {
					possibleNextMusicBlockNumbers.add( 0, secondMusicBlockNumber );
				} else if ( isPossibleNext( firstMusicBlock, secondMusicBlock ) ) {
					possibleNextMusicBlockNumbers.add( secondMusicBlockNumber );
				}
			}
		}
		return map;
	}

	public boolean isPossibleNext( MusicBlock musicBlock, MusicBlock possibleNext ) {
		MusicBlock previous = possibleNext.getPrevious();
		if ( previous == null && musicBlock == null ) return true;
		if ( previous == null || musicBlock == null ) return false;
		boolean correlatingTime = onCorrelatedTime(  musicBlock.getStartTime(), previous.getStartTime() );
		boolean intervalPatternEquality = musicBlock.getEndIntervalPattern().equals(previous.getEndIntervalPattern());
		return intervalPatternEquality && correlatingTime;
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
