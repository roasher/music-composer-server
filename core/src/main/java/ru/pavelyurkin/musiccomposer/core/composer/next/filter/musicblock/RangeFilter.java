package ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock;

import lombok.AllArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.List;
import java.util.OptionalInt;

/**
 * Created by wish on 02.02.2016.
 * Filter declines all compose blocks that will go out of range after transposing
 */
@AllArgsConstructor
public class RangeFilter implements MusicBlockFilter {

	private int lowestNotePitch;
	private int highestNotePitch;

	@Override
	public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
		OptionalInt max = block.getInstrumentParts().stream()
				.filter( instrumentPart -> !instrumentPart.isRest() )
				.mapToInt( InstrumentPart::getMaxPitch )
				.max();
		OptionalInt min = block.getInstrumentParts().stream()
				.filter( instrumentPart -> !instrumentPart.isRest() )
				.mapToInt( InstrumentPart::getMinNonRestPitch )
				.min();
		if (!max.isPresent() && !min.isPresent()) return true;
		return max.getAsInt() <= highestNotePitch && min.getAsInt() >= lowestNotePitch;
	}
}
