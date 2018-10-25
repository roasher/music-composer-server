package ru.pavelyurkin.musiccomposer.core.composer;

import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class handles routine to decide possible nexts from each of input blocks
 */
@Component
public class MusicBlockProvider {

	/**
	 * Returns Map<Integer, List<Integer>> where Integer - music block number from input collection, corresponding list - collection of possible next music
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
		if ( !possibleNext.getPreviousBlockEndPitches().isPresent() ) return false;
		boolean patternEquality = isEndPitchesTransposable(musicBlock.getEndPitches(), possibleNext.getPreviousBlockEndPitches().get());
		boolean correlatingTime = ModelUtils.isTimeCorrelated( musicBlock.getStartTime() + musicBlock.getRhythmValue(), possibleNext.getStartTime() );
		return patternEquality && correlatingTime;
	}

	private boolean isEndPitchesTransposable( List<Integer> firstPitches, List<Integer> secondPitches ) {

		if ( firstPitches.size() != secondPitches.size() ) {
			return false;
		}

		List<Integer> subtractions = IntStream.range( 0, firstPitches.size() )
				.map( operand -> secondPitches.get( operand ) - firstPitches.get( operand ) )
				.distinct()
				.boxed()
				.collect( Collectors.toList() );

		// subtractions may consist of transpose pitches and may be zeros if there was rests
		if ( subtractions.size() > 2 ) {
			return false;
		}
		return true;
	}
}
