package ru.pavelyurkin.musiccomposer.core.config;

import java.io.File;
import java.util.List;
import jm.JMC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.MusicBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.first.FirstStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProviderImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilterImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.MozartFilterImpl;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.FormEqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.persistance.dao.LexiconDAO;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionParser;
import ru.pavelyurkin.musiccomposer.core.utils.Recombinator;

@Configuration
public class ComposerConfiguration {

  @Bean
  public ComposeStepProvider composeStepProvider(
      @Qualifier("randomFirstStepProvider") FirstStepProvider firstStepProvider,
      @Qualifier("nextStepProvider") NextStepProvider nextStepProvider) {
    return new ComposeStepProvider(firstStepProvider, nextStepProvider);
  }

  @Bean
  public FormEqualityMetricAnalyzer formEqualityMetricAnalyzer(
      @Qualifier("melodyMetricEqualityAnalyzer") EqualityMetricAnalyzer<InstrumentPart> equalityMetricAnalyzer) {
    return new FormEqualityMetricAnalyzer(equalityMetricAnalyzer);
  }

  @Bean
  public CompositionDecomposer compositionDecomposer(
      MusicBlockProvider musicBlockProvider,
      Recombinator recombinator,
      CompositionParser compositionParser,
      @Qualifier("lexiconDAO_mapdb") LexiconDAO lexiconDAO) {
    return new CompositionDecomposer(recombinator, compositionParser, musicBlockProvider, lexiconDAO);
  }

  @Bean
  public NextStepProviderImpl nextStepProvider(
      EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer,
      @Qualifier("filter") ComposeStepFilter filter) {
    return new NextStepProviderImpl(equalityMetricAnalyzer, filter);
  }

  @Bean(name = "filter")
  @Profile( {"bach-test", "bach-prod"})
  public ComposeStepFilter bachFilter() {
    return new BachChoralFilterImpl();
  }

  @Bean(name = "filter")
  @Profile( {"mozart-test", "mozart-prod"})
  public ComposeStepFilter MozartFilter() {
    return new MozartFilterImpl();
  }

  /**
   * Bach chorale lexicon
   */
  @Bean
  @Profile( {"bach-test", "bach-prod"})
  public Lexicon bachLexicon(CompositionDecomposer compositionDecomposer,
                             CompositionLoader compositionLoader,
                             @Value("${composer.pathToCompositions}") String compositionsPath) {
    List<Composition> compositionList = compositionLoader.getCompositionsFromFolder(new File(compositionsPath));
    return compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
  }

}
