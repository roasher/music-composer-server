package ru.pavelyurkin.musiccomposer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer.MelodyEqualityAnalyzerImpl;

@SpringBootApplication
@ComponentScan( excludeFilters = {
		// Not using yet
		@ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE, value = MelodyEqualityAnalyzerImpl.class ),
		@ComponentScan.Filter( type = FilterType.REGEX, pattern = "ru.pavelyurkin.musiccomposer.core.equality.melody.*" ) }
)
public class RestApplication {

	public static void main( String[] args ) {
		SpringApplication.run( RestApplication.class, args );
	}

}