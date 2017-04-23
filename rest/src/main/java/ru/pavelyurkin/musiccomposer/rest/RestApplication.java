package ru.pavelyurkin.musiccomposer.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer.MelodyEqualityAnalyzerImpl;

@SpringBootApplication
@PropertySource( value = "classpath:core.properties")
@ComponentScan( basePackages = "ru.pavelyurkin.musiccomposer" ,excludeFilters = {
		// Not using yet
		@ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE, value = MelodyEqualityAnalyzerImpl.class ),
		@ComponentScan.Filter( type = FilterType.REGEX, pattern = "ru.pavelyurkin.musiccomposer.core.equality.melody.*" ) }
)
public class RestApplication {

	public static void main( String[] args ) {
		SpringApplication.run( RestApplication.class, args );
	}

}
