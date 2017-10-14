package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wish on 02.02.2016.
 * Filter declines all compose blocks that will go out of range after transposing
 */
@Component
public class ComposeStepRangeFilter extends AbstractComposeStepFilter {

	private int lowestNotePitch;
	private int highestNotePitch;

	public ComposeStepRangeFilter() {
	}

	public ComposeStepRangeFilter( int lowestNotePitch, int highestNotePitch, AbstractComposeStepFilter composeStepFilter ) {
		super( composeStepFilter );
		this.lowestNotePitch = lowestNotePitch;
		this.highestNotePitch = highestNotePitch;
	}

	public ComposeStepRangeFilter( int lowestNotePitch, int highestNotePitch ) {
		this.lowestNotePitch = lowestNotePitch;
		this.highestNotePitch = highestNotePitch;
	}

	@Override
	public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
		List<Integer> pitches = block.getMelodyList()
				.stream()
				.flatMap( melody -> melody.getNoteList().stream() )
				.mapToInt( value -> ( ( Note ) value ).getPitch() )
				.filter( value -> value != Note.REST )
				.boxed()
				.collect( Collectors.toList() );
		return pitches.isEmpty() || ( Collections.max( pitches ) <= highestNotePitch && Collections.min( pitches ) >= lowestNotePitch );
	}
}
