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
	 * Retruns Map<Integer, List<Integer>> where Integer - music block number from input collection, corresponding list - collection of possible next music
	 * block numbers
	 */
	public Map<Integer, List<Integer>> getAllPossibleNextVariants( List<MusicBlock> musicBlocks ) {
		Map<Integer, List<Integer>> map = new HashMap<>( musicBlocks.size() );

		for ( int firstMusicBlockNumber = 0; firstMusicBlockNumber < musicBlocks.size(); firstMusicBlockNumber++ ) {
			List<Integer> possibleNextMusicBlockNumbers = new ArrayList<>();
			map.put( firstMusicBlockNumber, possibleNextMusicBlockNumbers );
			MusicBlock firstMusicBlock = musicBlocks.get( firstMusicBlockNumber );
			for ( int possibleNextMusicBlockNumber = 1; possibleNextMusicBlockNumber < musicBlocks.size(); possibleNextMusicBlockNumber++ ) {
				MusicBlock possibleNextMusicBlock = musicBlocks.get( possibleNextMusicBlockNumber );
				if ( isPossibleNext( firstMusicBlock, possibleNextMusicBlock ) ) {
					possibleNextMusicBlockNumbers.add( possibleNextMusicBlockNumber );
				}
			}
		}
		return map;
	}


	public boolean isPossibleNext( MusicBlock musicBlock, MusicBlock possibleNext ) {
		if ( possibleNext.getBlockMovementFromPreviousToThis() == null ) return false;
		boolean intervalPatternEquality = musicBlock.getEndIntervalPattern().equals( getPreviousEndIntervalPattern( possibleNext ) );
		boolean correlatingTime = isTimeCorrelated( musicBlock.getStartTime() + musicBlock.getRhythmValue(), possibleNext.getStartTime() );
		return intervalPatternEquality && correlatingTime;
	}

	// TODO need to write tests using rests
	public List<Integer> getPreviousEndIntervalPattern( MusicBlock musicBlock ) {
		List<Integer> startIntervalPattern = musicBlock.getStartIntervalPattern();
		List<Integer> voiceMovements = musicBlock.getBlockMovementFromPreviousToThis().getVoiceMovements();
		List<Integer> previousEndIntervalPattern = new ArrayList<>(  );
		for ( int intervalNumber = 0; intervalNumber < startIntervalPattern.size(); intervalNumber++ ) {
			previousEndIntervalPattern.add( startIntervalPattern.get( intervalNumber ) - voiceMovements.get( intervalNumber ) + voiceMovements.get(
					intervalNumber + 1 ) );
		}
		return previousEndIntervalPattern;
	}

}
