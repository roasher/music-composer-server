package ru.pavelyurkin.musiccomposer.core.config;

import java.io.File;
import java.util.List;
import jm.JMC;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import ru.pavelyurkin.musiccomposer.core.client.lexicon.LexiconDAO;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.service.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.MusicBlockProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.first.FirstStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.NextStepProviderImpl;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.service.composition.CompositionParser;
import ru.pavelyurkin.musiccomposer.core.service.composition.loader.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.service.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.service.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.service.equality.equalityMetric.FormEqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.service.multipleclients.ComposingParameters;

@Configuration
public class CommonComposerConfiguration {

  @Bean
  public FormEqualityMetricAnalyzer formEqualityMetricAnalyzer(
      @Qualifier("melodyMetricEqualityAnalyzer") EqualityMetricAnalyzer<InstrumentPart> equalityMetricAnalyzer) {
    return new FormEqualityMetricAnalyzer(equalityMetricAnalyzer);
  }

  @Bean
  public CompositionDecomposer compositionDecomposer(
      MusicBlockProvider musicBlockProvider,
      CompositionParser compositionParser,
      @Qualifier("lexiconDAO_mapdb") LexiconDAO lexiconDAO) {
    return new CompositionDecomposer(compositionParser, musicBlockProvider, lexiconDAO);
  }

  @Bean
  public DB Db(@Value("${persistance.file}") String file) {
    return DBMaker
        .fileDB(file)
        .concurrencyDisable()
        .closeOnJvmShutdown()
        .make();
  }

  /**
   * Bach chorale lexicon decomposed from particular composition list.
   * Precalculated file would be used, but every block from compositions other than those is
   * ${composer.pathToCompositions} woudl be omitted
   */
  @Bean
  @Profile("!composition_list && !persist-lexicon")
  public Lexicon decomposedBachLexicon(@Qualifier("lexiconDAO_mapdb") LexiconDAO lexiconDAO) {
    return lexiconDAO.fetch();
  }

  /**
   * Bach chorale lexicon fetched straight from precalculated file.
   */
  @Bean
  @Profile("composition_list || persist-lexicon")
  public Lexicon precalculatedBachLexicon(CompositionDecomposer compositionDecomposer,
                             CompositionLoader compositionLoader,
                             @Value("${composer.pathToCompositions}") String compositionsPath) {
    List<Composition> compositionList = compositionLoader.getCompositionsFromFolder(new File(compositionsPath));
    return compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
  }

  /**
   * Returns default Composing Parameters that would be used for composing
   */
  @Bean
  @Scope("prototype")
  public ComposingParameters composingParameters(ComposeStepProvider composeStepProvider,
                                                 Lexicon lexicon) {
    ComposingParameters composingParameters = new ComposingParameters();
    composingParameters.setComposeStepProvider(composeStepProvider);
    composingParameters.setLexicon(lexicon);
    return composingParameters;
  }

  @Bean
  @Scope("prototype")
  public ComposeStepProvider composeStepProvider(
      @Qualifier("randomFirstStepProvider") FirstStepProvider firstStepProvider,
      NextStepProvider nextStepProvider) {
    return new ComposeStepProvider(firstStepProvider, nextStepProvider);
  }

  @Bean
  @Scope("prototype")
  public NextStepProvider nextStepProvider(
      EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer,
      ComposeStepFilter filter) {
    return new NextStepProviderImpl(equalityMetricAnalyzer, filter);
  }

}
