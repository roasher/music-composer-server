package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.key;

import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getNumberOfNotesOutOfKey;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;

/**
 * Filters blocks that are not from current key
 * Changing current key randomly after some filtering
 * Used for prototyping purposes.
 */
@Slf4j
public class RandomKeyFilter implements MusicBlockFilter {

  private Key currentKey;
  private int numberOfBlocksFilteredToChangeKey = 200;
  private int count = 0;

  public RandomKeyFilter(Key currentKey) {
    this.currentKey = currentKey;
  }

  @Override
  public boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks) {
    Set<Integer> allPossibleNextPitches = block.getInstrumentParts().stream()
        .flatMap(instrumentPart -> instrumentPart.getNoteGroups().stream())
        .flatMap(noteGroup -> noteGroup.getAllPitches().stream())
        .collect(Collectors.toSet());

    boolean fit = getNumberOfNotesOutOfKey(currentKey, allPossibleNextPitches) == 0;
    if (fit) {
      count++;
      if (count % numberOfBlocksFilteredToChangeKey == 0) {
        currentKey = Key.values()[new Random().nextInt(Key.values().length)];
        log.debug("Changed key to {}", currentKey);
      }

    }
    return fit;

  }


}
