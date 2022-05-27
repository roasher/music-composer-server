package ru.pavelyurkin.musiccomposer.core.service.composer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

/**
 * Class handles routine to decide possible nexts from each of input blocks
 */
@Component
@Slf4j
public class MusicBlockProvider {

  /**
   * Returns Map<Integer, List<Integer>> where Integer - music block number from input collection, corresponding
   * list - collection of possible next music
   * block numbers
   */
  public Map<Integer, Set<Integer>> getAllPossibleNextVariants(List<MusicBlock> musicBlocks) {
    Map<Integer, Set<Integer>> map = new HashMap<>(musicBlocks.size());

    for (int firstMusicBlockNumber = 0; firstMusicBlockNumber < musicBlocks.size(); firstMusicBlockNumber++) {
      log.info("Calculating possible nexts for music block number {} of total {}", firstMusicBlockNumber,
          musicBlocks.size());
      Set<Integer> possibleNextMusicBlockNumbers = new HashSet<>();
      map.put(firstMusicBlockNumber, possibleNextMusicBlockNumbers);
      MusicBlock firstMusicBlock = musicBlocks.get(firstMusicBlockNumber);
      for (int possibleNextMusicBlockNumber = 1; possibleNextMusicBlockNumber < musicBlocks.size();
           possibleNextMusicBlockNumber++) {
        MusicBlock possibleNextMusicBlock = musicBlocks.get(possibleNextMusicBlockNumber);
        if (isPossibleNext(firstMusicBlock, possibleNextMusicBlock)) {
          possibleNextMusicBlockNumbers.add(possibleNextMusicBlockNumber);
        }
      }
    }
    return map;
  }

  public boolean isPossibleNext(MusicBlock musicBlock, MusicBlock possibleNext) {
    if (possibleNext.getPreviousBlockEndPitches().isEmpty()) {
      return false;
    }
    boolean canBeTransposed = false;
    try {
      possibleNext.transposeClone(musicBlock);
      canBeTransposed = true;
    } catch (Exception exception) {
      return false;
    }
    boolean correlatingTime = ModelUtils
        .isTimeCorrelated(musicBlock.getStartTime() + musicBlock.getRhythmValue(), possibleNext.getStartTime());
    return canBeTransposed && correlatingTime;
  }

}
