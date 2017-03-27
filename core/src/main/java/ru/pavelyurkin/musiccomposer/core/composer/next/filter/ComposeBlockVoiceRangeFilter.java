package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Created by wish on 03.02.2016.
 * Filter restricts going out of range for compose block's melodies
 */
@Component
public class ComposeBlockVoiceRangeFilter extends AbstractComposeBlockFilter {

	private List<Range> melodyRange;

	public static class Range {

		int lowPitch;
		int highPitch;

		public Range( int lowPitch, int highPitch ) {
			this.lowPitch = lowPitch;
			this.highPitch = highPitch;
		}
	}

	public ComposeBlockVoiceRangeFilter( ComposeBlockFilter composeBlockFilter, List<Range> melodyRange ) {
		super( composeBlockFilter );
		this.melodyRange = melodyRange;
	}

	public ComposeBlockVoiceRangeFilter( List<Range> melodyRange ) {
		this.melodyRange = melodyRange;
	}

	public ComposeBlockVoiceRangeFilter() {}

	@Override
	public List<ComposeBlock> filterIt( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		List<ComposeBlock> out = new ArrayList<>();
		MusicBlock lastTrasposedBlock = previousCompositionSteps.get( previousCompositionSteps.size() - 1 ).getTransposedBlock();
		nextBlock:
		for ( ComposeBlock possibleNext : possibleNextComposeBlocks ) {
			if ( possibleNext.getMelodyList().size() > melodyRange.size() ) throw new RuntimeException( "Number of melodies is "
				+ "greater than number of ranges" );
			int trasposePitch = ModelUtils.getTransposePitch( Optional.of( lastTrasposedBlock ), possibleNext.getMusicBlock() );
			MusicBlock transposedBlock = possibleNext.getMusicBlock().transposeClone( trasposePitch );
			for ( int melodyNumber = 0; melodyNumber < transposedBlock.getMelodyList().size(); melodyNumber++ ) {
				Melody melody = transposedBlock.getMelodyList().get( melodyNumber );
				List<Integer> pitches = melody.getNoteList()
						.stream()
						.mapToInt( value -> ( ( Note ) value ).getPitch() )
						.filter( value -> value != Note.REST )
						.boxed()
						.collect( Collectors.toList() );
				if ( pitches.size() != 0 && ( Collections.max( pitches ) > melodyRange.get( melodyNumber ).highPitch || Collections.min( pitches ) < melodyRange.get( melodyNumber ).lowPitch ) ) {
					continue nextBlock;
				}
			}
			out.add( possibleNext );
		}
		return out;
	}

}