package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static ru.pavelyurkin.musiccomposer.core.utils.ModelUtils.isExactEquals;
import static ru.pavelyurkin.musiccomposer.core.utils.Utils.compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

/**
 * Filters out blocks if they would cause repetition
 */
@NoArgsConstructor
public class RepetitionFilter implements MusicBlockFilter {

  @Override
  public boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks) {
    List<MusicBlock> musicBlocksToCheck = new ArrayList<>(previousBlocks);
    musicBlocksToCheck.add(block);
    Map<Double, Integer> rhythmValueRepetitions = getRepetitions(musicBlocksToCheck, 4 * WHOLE_NOTE);
    for (Map.Entry<Double, Integer> rhythmValueRepetition : rhythmValueRepetitions.entrySet()) {
      if (rhythmValueRepetition.getValue() > getMaxNumberOfRepetitions(rhythmValueRepetition.getKey())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns number of repetitions for different rhythm values, searching summary within range
   * For example if Map<HALF_NOTE, 3> is returned means that in (now-range;now) time there is
   * pattern HALF_NOTE length that repeated 3 times.
   *
   * @param musicBlocksToCheck - input music blocks
   * @param range              - range that repetitions would be counted within
   * @return Map<rythm value, repetiton count>
   */
  Map<Double, Integer> getRepetitions(List<MusicBlock> musicBlocksToCheck, double range) {
    // Rhythm values and number of actual repetitions ouput map
    Map<Double, Integer> outputRepetitionMap = new HashMap<>();
    // list represents repetition
    List<MusicBlock> repetition = new ArrayList<>();
    // this is list of blocks consists of everything EXCEPT repetition
    List<MusicBlock> musicBlocks = new ArrayList<>(musicBlocksToCheck);
    // Repetition can consist of 1 to N music blocks. Iterating through them trying to count number of repetitions
    for (int blocksInRepetition = 1; blocksInRepetition < musicBlocksToCheck.size(); blocksInRepetition++) {
      // inserting at first place
      repetition.add(0, musicBlocksToCheck.get(musicBlocksToCheck.size() - blocksInRepetition));
      musicBlocks.remove(musicBlocksToCheck.size() - blocksInRepetition);
      if (repetition.stream().mapToDouble(MusicBlock::getRhythmValue).sum() < range) {
        // We need preform several comparisons. For example here (1)(2)(3) (1)(2)(3) | (1)(2)(3) we need to compare
        // last 3 notes with second and first
        // Also number of comparison when cycle will break will show us number of repetitions
        for (int numberOfComparison = 0; numberOfComparison < musicBlocks.size() / repetition.size();
             numberOfComparison++) {
          // In each comparison we need to compare all blocks in the repetition with corresponding blocks in
          // musicBlock list
          // (1)(2)(3) (1)(2)(3) | (1)(2)(3)
          //				|			|
          //				------------
          //				0 comparison
          //     |		 			|
          //	   ----------------------
          //       1 comparison
          List<MusicBlock> musicBlocksToCompare =
              musicBlocks.subList(musicBlocks.size() - (numberOfComparison + 1) * repetition.size()
                  , musicBlocks.size() - (numberOfComparison) * repetition.size());
          if (melodyExactEquality(repetition, musicBlocksToCompare)) {
            outputRepetitionMap
                .put(repetition.stream().mapToDouble(MusicBlock::getRhythmValue).sum(), numberOfComparison + 2);
          } else {
            // next comparison makes sense only if previous is succeeded
            break;
          }
        }
      } else {
        break;
      }
    }
    return outputRepetitionMap;
  }

  private boolean melodyExactEquality(List<MusicBlock> firstMusicBlocks, List<MusicBlock> secondMusicBlocks) {
    return isExactEquals(new MusicBlock(firstMusicBlocks).getInstrumentParts(),
        new MusicBlock(secondMusicBlocks).getInstrumentParts());
  }

  // TODO parametrise it
  int getMaxNumberOfRepetitions(double rhythmValue) {
    if (compare(rhythmValue, EIGHTH_NOTE) <= 0) {
      return 6;
    }
    if (compare(rhythmValue, QUARTER_NOTE) <= 0) {
      return 4;
    }
    if (compare(rhythmValue, HALF_NOTE) <= 0) {
      return 3;
    }
    if (compare(rhythmValue, WHOLE_NOTE) <= 0) {
      return 2;
    }
    if (compare(rhythmValue, WHOLE_NOTE) >= 0) {
      return 2;
    }
    throw new RuntimeException("This line shouldn't got executed");
  }

}
