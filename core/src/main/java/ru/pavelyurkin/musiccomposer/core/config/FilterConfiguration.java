package ru.pavelyurkin.musiccomposer.core.config;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.A2;
import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.C2;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.C6;
import static jm.constants.Pitches.F2;
import static jm.constants.Pitches.F3;
import static jm.constants.Pitches.F4;
import static jm.constants.Pitches.F5;

import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.ComposeStepFilterImpl;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.FastComposeStepFilterImpl;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.RangeFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.RepetitionFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.RestFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.key.SameKeyFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.VarietyFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.VoiceRangeFilter;
import ru.pavelyurkin.musiccomposer.core.model.Key;

@Configuration
public class FilterConfiguration {

  @Bean
  @Profile("bach & !compose")
  public ComposeStepFilter fastBachFilter() {
    return new FastComposeStepFilterImpl(getBachFilters(), 5);
  }

  @Bean
  @Profile("bach & compose")
  public ComposeStepFilter bachFilter() {
    return new ComposeStepFilterImpl(getBachFilters());
  }

  @Bean
  @Profile("mozart")
  public ComposeStepFilter mozartFilter() {
    return new ComposeStepFilterImpl(
        List.of(
            new RangeFilter(C2, C6),
            new RestFilter(QUARTER_NOTE),
            new SameKeyFilter(Key.C_MAJOR),
            new VarietyFilter(4, 10),
            new RepetitionFilter()
        )
    );
  }

  @NotNull
  private List<MusicBlockFilter> getBachFilters() {
    return List.of(
        new VoiceRangeFilter(Arrays.asList(
            new VoiceRangeFilter.Range(C4, C6),
            new VoiceRangeFilter.Range(F3, F5),
            new VoiceRangeFilter.Range(A2, A4),
            new VoiceRangeFilter.Range(F2, F4)
        )),
        new RestFilter(EIGHTH_NOTE),
        new SameKeyFilter(Key.C_MAJOR),
        new VarietyFilter(6, -1),
        new RepetitionFilter()
    );
  }

}
