package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock;

import java.util.List;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

public interface MusicBlockFilter {

  /**
   * Returns true if block should stay
   *
   * @param block - next block candidate
   * @param previousBlocks - previously composed blocks
   * @return true if block is considered valid to compose with
   */
  boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks);

}
