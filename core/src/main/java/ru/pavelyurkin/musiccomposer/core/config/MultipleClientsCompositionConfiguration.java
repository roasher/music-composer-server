package ru.pavelyurkin.musiccomposer.core.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.composer.first.FirstStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProviderImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilterImpl;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.service.ComposeService;
import ru.pavelyurkin.musiccomposer.core.service.ComposingParameters;

@Configuration
@Profile("web")
public class MultipleClientsCompositionConfiguration {

  @Bean
  public ComposeService composeService(ApplicationContext applicationContext, CompositionComposer compositionComposer) {
    return new ComposeService(applicationContext, compositionComposer);
  }

  /**
   * Returns default Composing Parameters
   */
  @Bean
  @Scope("prototype")
  public ComposingParameters defaultComposingParameters(ComposeStepProvider defaultComposeStepProvider,
                                                        Lexicon defaultLexicon) {
    ComposingParameters composingParameters = new ComposingParameters();
    composingParameters.setComposeStepProvider(defaultComposeStepProvider);
    composingParameters.setLexicon(defaultLexicon);
    return composingParameters;
  }

  @Bean
  @Scope("prototype")
  public ComposeStepProvider defaultComposeStepProvider(
      @Qualifier("randomFirstStepProvider") FirstStepProvider firstStepProvider,
      @Qualifier("defaultNextStepProvider") NextStepProvider nextStepProvider) {
    return new ComposeStepProvider(firstStepProvider, nextStepProvider);
  }

  @Bean
  @Scope("prototype")
  public NextStepProviderImpl defaultNextStepProvider(
      EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer,
      @Qualifier("defaultFilter") ComposeStepFilter filter) {
    return new NextStepProviderImpl(equalityMetricAnalyzer, filter);
  }

  @Bean
  @Profile( {"bach-test", "bach-prod"})
  public ComposeStepFilter defaultFilter() {
    return new BachChoralFilterImpl();
  }
}
