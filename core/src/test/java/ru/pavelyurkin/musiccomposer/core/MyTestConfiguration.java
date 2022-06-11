package ru.pavelyurkin.musiccomposer.core;

import java.io.File;
import java.util.List;
import jm.JMC;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.service.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.first.FirstStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.SimpleNextStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.ComposeStepFilterImpl;
import ru.pavelyurkin.musiccomposer.core.service.composition.loader.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.service.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.service.equality.melody.EqualNumberOfNotesRequired;
import ru.pavelyurkin.musiccomposer.core.service.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.service.equality.melody.RhythmEquality;
import ru.pavelyurkin.musiccomposer.core.service.equality.melodymovement.ContourMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.service.equality.melodymovement.IntervalsMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.service.equality.melodymovement.InversionMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.service.equality.melodymovement.OrderMelodyMovementEquality;

@TestConfiguration
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
  public ComposeStepFilter filter() {
    return new ComposeStepFilterImpl(List.of());
  }

  @Bean
  @Scope("prototype")
  public NextStepProvider nextStepProvider(ComposeStepFilter filter) {
    return new SimpleNextStepProvider(filter);
  }

  @Bean
  @Scope("prototype")
  public ComposeStepProvider composeStepProvider(
      @Qualifier("simpleFirstStepProvider") FirstStepProvider firstStepProvider,
      NextStepProvider nextStepProvider) {
    return new ComposeStepProvider(firstStepProvider, nextStepProvider);
  }

  @Bean
  @Primary
  public Lexicon testLexicon(CompositionDecomposer compositionDecomposer,
                             CompositionLoader compositionLoader,
                             @Value("${composer.pathToCompositions}") String compositionsPath) {
    List<Composition> compositionList = compositionLoader.getCompositionsFromFolder(new File(compositionsPath));
    return compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
  }

  @Bean
  public DB Db(@Value("${persistance.file}") String file) {
    return DBMaker
        .fileDB(file)
        .concurrencyDisable()
        .fileDeleteAfterClose()
        .closeOnJvmShutdown()
        .make();
  }
}
