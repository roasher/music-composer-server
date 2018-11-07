package ru.pavelyurkin.musiccomposer.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.MusicBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.first.FirstStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProviderImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.MozartFilter;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.FormEqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.persistance.dao.LexiconDAO;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionParser;
import ru.pavelyurkin.musiccomposer.core.utils.Recombinator;

import java.util.List;

@Configuration
public class ComposerConfiguration {

	@Bean
	public ComposeStepProvider composeStepProvider(
			@Qualifier( "randomFirstStepProvider" ) FirstStepProvider firstStepProvider,
			@Qualifier( "nextStepProvider" ) NextStepProvider nextStepProvider ) {
		return new ComposeStepProvider( firstStepProvider, nextStepProvider );
	}

	@Bean
	public FormEqualityMetricAnalyzer formEqualityMetricAnalyzer(
			@Qualifier( "melodyMetricEqualityAnalyzer" ) EqualityMetricAnalyzer<InstrumentPart> equalityMetricAnalyzer ) {
		return new FormEqualityMetricAnalyzer( equalityMetricAnalyzer );
	}

	@Bean
	public CompositionDecomposer compositionDecomposer(
			MusicBlockProvider musicBlockProvider,
			Recombinator recombinator,
			CompositionParser compositionParser,
			@Qualifier( "lexiconDAO_mapdb" ) LexiconDAO lexiconDAO ) {
		return new CompositionDecomposer( recombinator, compositionParser, musicBlockProvider, lexiconDAO );
	}

	@Bean
	public NextStepProviderImpl nextStepProvider(
			EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer,
			@Qualifier("defaultFilter") ComposeStepFilter filter
	) {
		return new NextStepProviderImpl(equalityMetricAnalyzer, filter);
	}

	@Bean(name = "defaultFilter")
	@Profile( {"bach-test", "bach-prod", "test"} )
	public ComposeStepFilter defaultFilter1() {
		return new BachChoralFilter();
	}

	@Bean(name = "defaultFilter")
	@Profile( {"mozart-test", "mozart-prod"} )
	public ComposeStepFilter defaultFilter2() {
		return new MozartFilter();
	}
}
