package ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom;

import ru.pavelyurkin.musiccomposer.core.composer.next.filter.*;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.*;

import java.util.Arrays;
import java.util.List;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;

/**
 * Created by wish on 18.02.2016.
 */
public class BachChoralFilter extends AbstractComposeStepFilter {

    public BachChoralFilter() {
        super(List.of(
                new VoiceRangeFilter(Arrays.asList(
                        new VoiceRangeFilter.Range(C4, C6),
                        new VoiceRangeFilter.Range(F3, F5),
                        new VoiceRangeFilter.Range(A2, A4),
                        new VoiceRangeFilter.Range(F2, F4)
                )),
                new RestFilter(EIGHTH_NOTE),
                new KeyVarietyFilter(1, 4 * WHOLE_NOTE),
                new VarietyFilter(-1, 6),
                new RepetitionFilter()
        ));
    }

}
