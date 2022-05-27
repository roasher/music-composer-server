package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock;

import java.util.List;
import lombok.AllArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

/**
 * Filter declines all blocks that are rests and their rhythm value is longer than x
 */
@AllArgsConstructor
public class RestFilter implements MusicBlockFilter {

  private double maxRestRhythmValue;

  @Override
  public boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks) {
    return !block.isRest() || block.getRhythmValue() <= maxRestRhythmValue;
  }

}
