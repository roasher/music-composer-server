package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.List;

/**
 * Created by wish on 02.02.2016.
 * Filter declines all blocks that are rests and their rhythm value is longer than x
 */
@Component
public class ComposeStepRestFilter extends AbstractComposeStepFilter {

	private double maxRestRhythmValue;

	public ComposeStepRestFilter() {
	}

	public ComposeStepRestFilter( double maxRestRhythmValue, AbstractComposeStepFilter composeStepFilter ) {
		super( composeStepFilter );
		this.maxRestRhythmValue = maxRestRhythmValue;
	}

	public ComposeStepRestFilter( double maxRestRhythmValue ) {
		this.maxRestRhythmValue = maxRestRhythmValue;
	}


	@Override
	public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
		return !block.isRest() || block.getRhythmValue() <= maxRestRhythmValue;
	}

}
