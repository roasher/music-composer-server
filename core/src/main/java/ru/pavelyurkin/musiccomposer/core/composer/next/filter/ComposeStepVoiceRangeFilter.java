package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.List;

/**
 * Created by wish on 03.02.2016.
 * Filter restricts going out of range for compose block's melodies
 */
@Component
@Data
public class ComposeStepVoiceRangeFilter extends AbstractComposeStepFilter {

	private List<Range> melodyRange;

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class Range {

		private int lowPitch;
		private int highPitch;

	}

	public ComposeStepVoiceRangeFilter( AbstractComposeStepFilter composeStepFilter, List<Range> melodyRange ) {
		super( composeStepFilter );
		this.melodyRange = melodyRange;
	}

	public ComposeStepVoiceRangeFilter( List<Range> melodyRange ) {
		this.melodyRange = melodyRange;
	}

	public ComposeStepVoiceRangeFilter() {}

	@Override
	public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
		if ( block.getInstrumentParts().size() > melodyRange.size() ) throw new RuntimeException( "Number of melodies is greater than number of ranges" );
		for ( int melodyNumber = 0; melodyNumber < block.getInstrumentParts().size(); melodyNumber++ ) {
			InstrumentPart instrumentPart = block.getInstrumentParts().get( melodyNumber );
			if ( instrumentPart.getMaxPitch() > melodyRange.get( melodyNumber ).highPitch || instrumentPart.getMinNonRestPitch() < melodyRange.get( melodyNumber ).lowPitch ) {
				return false;
			}
		}
		return true;
	}

}
