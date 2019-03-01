package ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom;

import lombok.experimental.Delegate;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.*;

import java.util.Arrays;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.*;

/**
 * Created by wish on 18.02.2016.
 */
public class BachChoralFilter implements ComposeStepFilter {

    @Delegate
	private ComposeStepFilter composeStepFilter = new ComposeStepRepetitionFilter(
			new ComposeStepVarietyFilter( -1, 6,
					new KeyVarietyFilter( 1, 2 * WHOLE_NOTE,
							new ComposeStepRestFilter( QUARTER_NOTE,
									new ComposeStepVoiceRangeFilter( Arrays.asList(
										new ComposeStepVoiceRangeFilter.Range( C4, C6 ),
										new ComposeStepVoiceRangeFilter.Range( F3, F5 ),
										new ComposeStepVoiceRangeFilter.Range( A2, A4 ),
										new ComposeStepVoiceRangeFilter.Range( F2, F4 )
							) ) ) ) ) );

}
