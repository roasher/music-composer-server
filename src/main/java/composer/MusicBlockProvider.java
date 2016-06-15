package composer;

import static utils.ModelEqualityUtils.isTimeCorrelated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import model.MusicBlock;

/**
 * Created by pyurkin on 15.01.15.
 */
@Component
public class MusicBlockProvider {

	/**
	 * Retruns Map<Integer, List<Integer>> where Integer - music block number from input collection,
	 * corresponding list - collection of possible next music block numbers
	 *
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
				if ( firstMusicBlockNumber != 0 && nextAreSubstitutable( musicBlocks.get( firstMusicBlockNumber - 1 ), musicBlocks.get( secondMusicBlockNumber - 1 ) )
						&& !map.get( firstMusicBlockNumber - 1 ).contains( secondMusicBlockNumber ) ) {
					map.get( firstMusicBlockNumber - 1 ).add( secondMusicBlockNumber );
				}
			}
		}
		return map;
	}

	private boolean isSame( List<Integer> firstIntervalPattern, double firstStartTime, List<Integer> secondIntervalPattern, double secondStartTime ) {
		boolean correlatingTime = isTimeCorrelated( firstStartTime, secondStartTime );
		boolean intervalPatternEquality = firstIntervalPattern.equals( secondIntervalPattern );
		return intervalPatternEquality && correlatingTime;
	}

	/**
	 * Blocks are substitutable if THEIR PREVIOUSES have same interval patterns and time correlating
	 *
	 * @return
	 */
	public boolean nextAreSubstitutable( MusicBlock firstMusicBlock, MusicBlock secondMusicBlock ) {
		return isSame( firstMusicBlock.getEndIntervalPattern(), firstMusicBlock.getStartTime(), secondMusicBlock.getEndIntervalPattern(), secondMusicBlock.getStartTime() );
	}

}
