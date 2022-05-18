package ru.pavelyurkin.musiccomposer.core;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;

import java.io.File;
import java.util.List;
import jm.JMC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilterImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilterImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.KeyVarietyFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RepetitionFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.RestFilter;
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

  @Bean(name = "filter")
  public ComposeStepFilter filterForTests() {
    return new ComposeStepFilterImpl(
        List.of(
            new RestFilter(EIGHTH_NOTE),
            new KeyVarietyFilter(1, 4 * WHOLE_NOTE),
            new RepetitionFilter()
        )
    );
  }

  @Bean
  public ComposeStepFilter bachFilter() {
    return new BachChoralFilterImpl();
  }

  @Bean
  public Lexicon testLexicon(CompositionDecomposer compositionDecomposer,
                             CompositionLoader compositionLoader,
                             @Value("${composer.pathToCompositions}") String compositionsPath) {
    List<Composition> compositionList = compositionLoader.getCompositionsFromFolder(new File(compositionsPath));
    return compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
  }
}
