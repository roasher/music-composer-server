package ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.key;

import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getNumberOfNotesOutOfKey;
import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getPossibleKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

/**
 * Filters musicblocks that are not from previous blocks key
 * TODO: problem - can't compose with that filter. Need to fix it to be able composition to change keys
 */
@Slf4j
public class KeyVarietyFilter implements MusicBlockFilter {

  private final int maxNotesNumberOutOfKey;
  private final double rhythmValue;

  // random key that would be used until composition would exceed rhythmValue
  @Getter
  @Setter
  private Key currentKey;
  private final Random random = new Random();

  public KeyVarietyFilter(int maxNotesNumberOutOfKey, double rhythmValue) {
    this.maxNotesNumberOutOfKey = maxNotesNumberOutOfKey;
    this.rhythmValue = rhythmValue;
    currentKey = Key.values()[random.nextInt(Key.values().length)];
  }

  @Override
  public boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks) {
    List<MusicBlock> musicBlocksToTrim = new ArrayList<>();

    double sumRhythmValue = 0;
    for (int blockNumber = previousBlocks.size() - 1; blockNumber >= 0; blockNumber--) {
      musicBlocksToTrim.add(previousBlocks.get(blockNumber));
      sumRhythmValue = ModelUtils.sumAllRhythmValues(musicBlocksToTrim);
      if (sumRhythmValue >= rhythmValue) {
        break;
      }
    }

    Set<Integer> allPossibleNextPitches = block.getInstrumentParts().stream()
        .flatMap(instrumentPart -> instrumentPart.getNoteGroups().stream())
        .flatMap(noteGroup -> noteGroup.getAllPitches().stream())
        .collect(Collectors.toSet());

    if (sumRhythmValue < rhythmValue) {
      log.debug("Not much composed yet.");
      return getNumberOfNotesOutOfKey(currentKey, allPossibleNextPitches) <= maxNotesNumberOutOfKey;
    }

    MusicBlock musicBlockToTrim = new MusicBlock(musicBlocksToTrim);

    List<InstrumentPart> previousParts = ModelUtils
        .trimToTime(musicBlockToTrim.getInstrumentParts(), sumRhythmValue - rhythmValue, sumRhythmValue);
    Set<Integer> allPreviousPitches = previousParts.stream()
        .flatMap(instrumentPart -> instrumentPart.getNoteGroups().stream())
        .flatMap(noteGroup -> noteGroup.getAllPitches().stream())
        .collect(Collectors.toSet());


    List<Key> previousPossibleKeys = getPossibleKeys(0, allPreviousPitches);
    if (previousPossibleKeys.isEmpty()) {
      log.debug("No key fits for last {}", rhythmValue);
      return false;
    }

    OptionalInt minNumberOfOutNotesOutOfKey = previousPossibleKeys.stream()
        .mapToInt(value -> getNumberOfNotesOutOfKey(value, allPossibleNextPitches)).min();
    return minNumberOfOutNotesOutOfKey.getAsInt() <= maxNotesNumberOutOfKey;

  }

}
