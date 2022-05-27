package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.key;

import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getNumberOfNotesOutOfKey;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

/**
 * Filters musicblocks that are not form current key
 */
@Slf4j
@RequiredArgsConstructor
public class SameKeyFilter implements MusicBlockFilter {

  private final Key currentKey;

  @Override
  public boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks) {
    Set<Integer> allPossibleNextPitches = block.getInstrumentParts().stream()
        .flatMap(instrumentPart -> instrumentPart.getNoteGroups().stream())
        .flatMap(noteGroup -> noteGroup.getAllPitches().stream())
        .collect(Collectors.toSet());

    return getNumberOfNotesOutOfKey(currentKey, allPossibleNextPitches) == 0;

  }

}
