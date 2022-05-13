package ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.A2;
import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.C6;
import static jm.constants.Pitches.F2;
import static jm.constants.Pitches.F3;
import static jm.constants.Pitches.F4;
import static jm.constants.Pitches.F5;

import java.util.Arrays;
import java.util.List;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.FastComposeStepFilterImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.KeyVarietyFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RepetitionFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RestFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.VarietyFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.VoiceRangeFilter;

/**
 * Created by wish on 18.02.2016.
 */
public class BachChoralFilterImpl extends FastComposeStepFilterImpl {

  public BachChoralFilterImpl() {
    super(List.of(
        new VoiceRangeFilter(Arrays.asList(
            new VoiceRangeFilter.Range(C4, C6),
            new VoiceRangeFilter.Range(F3, F5),
            new VoiceRangeFilter.Range(A2, A4),
            new VoiceRangeFilter.Range(F2, F4)
        )),
        new RestFilter(EIGHTH_NOTE),
        new KeyVarietyFilter(1, 4 * WHOLE_NOTE),
        new VarietyFilter(6, -1),
        new RepetitionFilter()
    ), 5);
  }

}
