package ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom;

import lombok.experimental.Delegate;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.*;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.Arrays;
import java.util.List;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.*;
import static jm.constants.Pitches.F4;

/**
 * Created by wish on 18.02.2016.
 */
@Component
public class BachChoralFilter implements ComposeStepFilter {

    @Delegate
	private ComposeStepFilter composeStepFilter = new ComposeStepRepetitionFilter(
			new ComposeStepVarietyFilter( 6,
					new ComposeStepRestFilter( QUARTER_NOTE,
							new ComposeStepVoiceRangeFilter( Arrays.asList(
									new ComposeStepVoiceRangeFilter.Range( C4, C6 ),
									new ComposeStepVoiceRangeFilter.Range( F3, F5 ),
									new ComposeStepVoiceRangeFilter.Range( A2, A4 ),
									new ComposeStepVoiceRangeFilter.Range( F2, F4 )
							) ) ) ) );

}
