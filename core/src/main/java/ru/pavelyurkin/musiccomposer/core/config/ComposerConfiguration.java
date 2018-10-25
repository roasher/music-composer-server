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
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.MozartFilter;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.decomposer.form.FormDecomposer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.FormEqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.persistance.dao.LexiconDAO;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionSlicer;

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
			FormDecomposer formDecomposer, MusicBlockProvider musicBlockProvider,
			CompositionSlicer compositionSlicer,
			@Qualifier( "lexiconDAO_stub" ) LexiconDAO lexiconDAO ) {
		return new CompositionDecomposer( formDecomposer, compositionSlicer, musicBlockProvider, lexiconDAO );
	}

	@Bean(name = "nextStepProvider")
	@Profile( {"test", "prod"} )
	public NextStepProviderImpl nextStepProvider(
			EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer,
			BachChoralFilter bachChoralFilter
	) {
		return new NextStepProviderImpl(equalityMetricAnalyzer, bachChoralFilter);
	}

	@Bean(name = "nextStepProvider")
	@Profile( {"test-mozart", "prod-mozart"} )
	public NextStepProviderImpl mozartNextStepProvider(
			EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer,
			MozartFilter mozartFilter
	) {
		return new NextStepProviderImpl(equalityMetricAnalyzer, mozartFilter);
	}
}
