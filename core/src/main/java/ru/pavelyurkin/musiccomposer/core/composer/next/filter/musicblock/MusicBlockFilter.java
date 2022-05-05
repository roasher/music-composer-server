package ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.List;

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
