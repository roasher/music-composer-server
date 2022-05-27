package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock;

import java.util.List;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

/**
 * Created by wish on 02.02.2016.
 */
public interface MusicBlockFilter {

  /**
   * Returns true if block should stay
   *
   * @param block
   * @param previousBlocks
   * @return
   */
  boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks);

}
