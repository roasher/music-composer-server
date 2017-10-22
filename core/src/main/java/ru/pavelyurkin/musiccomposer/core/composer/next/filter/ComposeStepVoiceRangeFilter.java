package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import org.springframework.stereotype.Component;

import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Created by wish on 03.02.2016.
 * Filter restricts going out of range for compose block's melodies
 */
@Component
@Data
public class ComposeStepVoiceRangeFilter extends AbstractComposeStepFilter {

	private List<Range> melodyRange;

	public static class Range {

		int lowPitch;
		int highPitch;

		public Range( int lowPitch, int highPitch ) {
			this.lowPitch = lowPitch;
			this.highPitch = highPitch;
		}
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
		if ( block.getMelodyList().size() > melodyRange.size() ) throw new RuntimeException( "Number of melodies is greater than number of ranges" );
		for ( int melodyNumber = 0; melodyNumber < block.getMelodyList().size(); melodyNumber++ ) {
			Melody melody = block.getMelodyList().get( melodyNumber );
			List<Integer> pitches = melody.getNoteList()
					.stream()
					.mapToInt( value -> ( ( Note ) value ).getPitch() )
					.filter( value -> value != Note.REST )
					.boxed()
					.collect( Collectors.toList() );
			if ( pitches.size() != 0 && ( Collections.max( pitches ) > melodyRange.get( melodyNumber ).highPitch || Collections.min( pitches ) < melodyRange.get( melodyNumber ).lowPitch ) ) {
				return false;
			}
		}
		return true;
	}

}
