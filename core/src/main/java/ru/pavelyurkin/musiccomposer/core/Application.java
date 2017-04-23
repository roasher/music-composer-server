package ru.pavelyurkin.musiccomposer.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer.MelodyEqualityAnalyzerImpl;

/**
 * Created by night_wish on 17.01.17.
 */
@SpringBootApplication
@ComponentScan( excludeFilters = {
		// Not using yet
		@ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE, value = MelodyEqualityAnalyzerImpl.class ),
		@ComponentScan.Filter( type = FilterType.REGEX, pattern = "ru.pavelyurkin.musiccomposer.core.equality.melody.*" ) }
)
@PropertySource( "classpath:core.properties" )
@EnableJpaRepositories( basePackages = "ru.pavelyurkin.musiccomposer.core.persistance.dao" )
public class Application {

}
