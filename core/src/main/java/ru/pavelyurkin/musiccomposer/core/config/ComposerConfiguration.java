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
import ru.pavelyurkin.musiccomposer.core.decomposer.form.FormDecomposer;
import ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer.MelodyEqualityAnalyzerImpl;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.FormEqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.persistance.dao.LexiconDAO;

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
			@Qualifier( "melodyMetricEqualityAnalyzer" ) EqualityMetricAnalyzer<Melody> equalityMetricAnalyzer ) {
		return new FormEqualityMetricAnalyzer( equalityMetricAnalyzer );
	}

	@Bean
	public CompositionDecomposer compositionDecomposer(
			FormDecomposer formDecomposer, MusicBlockProvider musicBlockProvider,
			@Qualifier( "lexiconDAO_database" ) LexiconDAO lexiconDAO ) {
		return new CompositionDecomposer( formDecomposer, musicBlockProvider, lexiconDAO );
	}

	@Bean(name = "nextStepProvider")
	@Profile( {"test", "prod"} )
	public NextStepProviderImpl nextStepProvider(
			EqualityMetricAnalyzer<List<Melody>> equalityMetricAnalyzer,
			BachChoralFilter bachChoralFilter
	) {
		return new NextStepProviderImpl(equalityMetricAnalyzer, bachChoralFilter);
	}

	@Bean(name = "nextStepProvider")
	@Profile( {"test-mozart", "prod-mozart"} )
	public NextStepProviderImpl mozartNextStepProvider(
			EqualityMetricAnalyzer<List<Melody>> equalityMetricAnalyzer,
			MozartFilter mozartFilter
	) {
		return new NextStepProviderImpl(equalityMetricAnalyzer, mozartFilter);
	}
}
