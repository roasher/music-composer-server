package ru.pavelyurkin.musiccomposer.core;

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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import jm.JMC;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilterImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.KeyVarietyFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RepetitionFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RestFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.VoiceRangeFilter;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.equality.melody.EqualNumberOfNotesRequired;
import ru.pavelyurkin.musiccomposer.core.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.equality.melody.RhythmEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.ContourMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.IntervalsMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.InversionMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.OrderMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

@TestConfiguration
@PropertySource("classpath:test-application.properties")
@ComponentScan(basePackages = "ru.pavelyurkin.musiccomposer.core",
    excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Application.class)})
// todo refactor
public class MyTestConfiguration {

  @Bean
  public Equality getContourEquality() {
    return new EqualNumberOfNotesRequired(new ContourMelodyMovementEquality());
  }

  @Bean
  public Equality getIntervalsEquality() {
    return new EqualNumberOfNotesRequired(new IntervalsMelodyMovementEquality());
  }

  @Bean
  public Equality getInversionEquality() {
    return new EqualNumberOfNotesRequired(new InversionMelodyMovementEquality());
  }

  @Bean
  public Equality getOrderEquality() {
    return new EqualNumberOfNotesRequired(new OrderMelodyMovementEquality());
  }

  @Bean
  public Equality getRhythmEquality() {
    return new EqualNumberOfNotesRequired(new RhythmEquality());
  }

  @Bean
  public ComposeStepFilter filterForTests() {
    return new ComposeStepFilterImpl(
        List.of(
            new VoiceRangeFilter(Arrays.asList(
                new VoiceRangeFilter.Range(C4, C6),
                new VoiceRangeFilter.Range(F3, F5),
                new VoiceRangeFilter.Range(A2, A4),
                new VoiceRangeFilter.Range(F2, F4)
            )),
            new RestFilter(EIGHTH_NOTE),
            new KeyVarietyFilter(1, 4 * WHOLE_NOTE),
            new RepetitionFilter()
        )
    );
  }

  @Bean
  public DB Db(@Value("${persistance.file}") String file) {
    return DBMaker
        .fileDB(file)
        .concurrencyDisable()
        .closeOnJvmShutdown()
        .fileDeleteAfterClose()
        .make();
  }

  @Bean
  public Lexicon testLexicon(CompositionDecomposer compositionDecomposer,
                             CompositionLoader compositionLoader,
                             @Value("${composer.pathToCompositions}") String compositionsPath) {
    List<Composition> compositionList = compositionLoader.getCompositionsFromFolder(new File(compositionsPath));
    return compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
  }
}
