package ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom;

import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeBlockFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeBlockRestFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeBlockVarietyFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeBlockVoiceRangeFilter;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.F4;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class BachChoralFilter implements ComposeBlockFilter {
	@Override
	public List<ComposeBlock> filter( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		ComposeBlockFilter composeBlockFilter = new ComposeBlockVarietyFilter( 6,
				new ComposeBlockRestFilter( QUARTER_NOTE,
						new ComposeBlockVoiceRangeFilter( Arrays.asList(
								new ComposeBlockVoiceRangeFilter.Range( C4, C6 ),
								new ComposeBlockVoiceRangeFilter.Range( F3, F5 ),
								new ComposeBlockVoiceRangeFilter.Range( A2, A4 ),
								new ComposeBlockVoiceRangeFilter.Range( F2, F4 )
						) ) ) );
		return composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps );
	}
}
