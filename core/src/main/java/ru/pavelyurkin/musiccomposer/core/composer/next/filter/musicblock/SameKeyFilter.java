package ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock;

import static ru.pavelyurkin.musiccomposer.core.model.Keys.allKeys;
import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getNumberOfNotesOutOfKey;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

/**
 * Filters musicblocks that are not from previous blocks key
 */
@Slf4j
public class SameKeyFilter implements MusicBlockFilter {

  private final int maxNotesNumberOutOfKey;
  private final double rhythmValue;

  // Key of the composition
  @Getter
  @Setter
  private Key currentKey;
  private final Random random = new Random();

  public SameKeyFilter(int maxNotesNumberOutOfKey, double rhythmValue) {
    this.maxNotesNumberOutOfKey = maxNotesNumberOutOfKey;
    this.rhythmValue = rhythmValue;
    currentKey = allKeys.get(random.nextInt(allKeys.size()));
  }

  @Override
  public boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks) {
    Set<Integer> allPossibleNextPitches = block.getInstrumentParts().stream()
        .flatMap(instrumentPart -> instrumentPart.getNoteGroups().stream())
        .flatMap(noteGroup -> noteGroup.getAllPitches().stream())
        .collect(Collectors.toSet());

    return getNumberOfNotesOutOfKey(currentKey, allPossibleNextPitches) <= maxNotesNumberOutOfKey;

  }

}
