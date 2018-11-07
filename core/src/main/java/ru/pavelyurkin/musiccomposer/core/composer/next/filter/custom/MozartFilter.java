package ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom;

import lombok.experimental.Delegate;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.*;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.C2;
import static jm.constants.Pitches.C6;

/**
 * Created by wish on 18.02.2016.
 */
public class MozartFilter implements ComposeStepFilter {

	@Delegate
	private ComposeStepFilter composeStepFilter = new ComposeStepRepetitionFilter(
			new ComposeStepVarietyFilter( -1, 6,
					new ComposeStepRestFilter( QUARTER_NOTE,
							new ComposeStepRangeFilter( C2, C6 ) ) ) );

}
