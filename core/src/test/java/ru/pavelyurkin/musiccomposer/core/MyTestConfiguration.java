package ru.pavelyurkin.musiccomposer.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.pavelyurkin.musiccomposer.core.equality.melody.EqualNumberOfNotesRequired;
import ru.pavelyurkin.musiccomposer.core.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.equality.melody.RhythmEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.ContourMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.IntervalsMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.InversionMelodyMovementEquality;
import ru.pavelyurkin.musiccomposer.core.equality.melodymovement.OrderMelodyMovementEquality;

@TestConfiguration
@EnableAutoConfiguration
@PropertySource( "classpath:test-application.properties" )
@EnableJpaRepositories( basePackages = "ru.pavelyurkin.musiccomposer.core.persistance.dao" )
@ComponentScan( basePackages = "ru.pavelyurkin.musiccomposer.core", excludeFilters = { @ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE, value = Application.class ) } )
public class MyTestConfiguration {

	@Bean
	public Equality getCountourEquality() {
		return new EqualNumberOfNotesRequired( new ContourMelodyMovementEquality() );
	}

	@Bean
	public Equality getIntervalsEquality() {
		return new EqualNumberOfNotesRequired( new IntervalsMelodyMovementEquality() );
	}

	@Bean
	public Equality getInversionEquality() {
		return new EqualNumberOfNotesRequired( new InversionMelodyMovementEquality() );
	}

	@Bean
	public Equality getOrderEquality() {
		return new EqualNumberOfNotesRequired( new OrderMelodyMovementEquality() );
	}

	@Bean
	public Equality getRhythmEquality() {
		return new EqualNumberOfNotesRequired( new RhythmEquality() );
	}

}
