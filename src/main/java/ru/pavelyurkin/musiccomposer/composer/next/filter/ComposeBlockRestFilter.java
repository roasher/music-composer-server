package ru.pavelyurkin.musiccomposer.composer.next.filter;

import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wish on 02.02.2016.
 * Filter declines all blocks that are rests and their rhythm value is longer than x
 */
@Component
public class ComposeBlockRestFilter extends AbstractComposeBlockFilter {

	private double maxRestRhythmValue;

	public ComposeBlockRestFilter() {
	}

	public ComposeBlockRestFilter( double maxRestRhythmValue, ComposeBlockFilter composeBlockFilter ) {
		super( composeBlockFilter );
		this.maxRestRhythmValue = maxRestRhythmValue;
	}

	public ComposeBlockRestFilter( double maxRestRhythmValue ) {
		this.maxRestRhythmValue = maxRestRhythmValue;
	}

	@Override
	public List<ComposeBlock> filterIt( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		List<ComposeBlock> out = new ArrayList<>();
		for ( ComposeBlock composeBlock : possibleNextComposeBlocks ) {
			boolean hasNonRestInIt = composeBlock.getMelodyList().stream().flatMap( melody -> melody.getNoteList().stream() )
					.filter( note -> !( ( Note ) note ).isRest() ).findAny().isPresent();
			if ( hasNonRestInIt || composeBlock.getRhythmValue() <= maxRestRhythmValue ) {
				out.add( composeBlock );
			}
		}
		return out;
	}
}
