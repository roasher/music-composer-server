package ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.C2;
import static jm.constants.Pitches.C6;

import java.util.List;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.AbstractComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.KeyVarietyFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RangeFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RepetitionFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RestFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.VarietyFilter;

/**
 * Created by wish on 18.02.2016.
 */
public class MozartFilter extends AbstractComposeStepFilter {

  public MozartFilter() {
    super(List.of(
        new RangeFilter(C2, C6),
        new RestFilter(QUARTER_NOTE),
        new KeyVarietyFilter(1, 2 * WHOLE_NOTE),
        new VarietyFilter(4, 10),
        new RepetitionFilter()
    ));
  }

}
