package ru.pavelyurkin.musiccomposer.core.composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;

/**
 * Class handles routine to decide possible nexts from each of input blocks
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
		boolean correlatingTime = ModelUtils.isTimeCorrelated( musicBlock.getStartTime() + musicBlock.getRhythmValue(), possibleNext.getStartTime() );
		return intervalPatternEquality && correlatingTime;
	}

	public List<Integer> getPreviousEndIntervalPattern( MusicBlock musicBlock ) {
		List<Integer> voiceMovements = musicBlock.getBlockMovementFromPreviousToThis().getVoiceMovements();
		List<Integer> preFirstVertical = new ArrayList<>();
		for ( int melodyNumber = 0; melodyNumber < musicBlock.getMelodyList().size(); melodyNumber++ ) {
			int pitch = musicBlock.getMelodyList().get( melodyNumber ).getNote( 0 ).getPitch();
			int voiceMovement = voiceMovements.get( melodyNumber );
			preFirstVertical.add( pitch != Note.REST && voiceMovement != BlockMovement.MOVEMENT_FROM_REST ? pitch - voiceMovement : Note.REST );
		}
		return ModelUtils.retrieveIntervalPattern( preFirstVertical );
	}

}
