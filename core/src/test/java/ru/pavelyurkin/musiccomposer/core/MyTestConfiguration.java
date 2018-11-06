package ru.pavelyurkin.musiccomposer.core;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer.MelodyEqualityAnalyzerImpl;
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
@ComponentScan( basePackages = "ru.pavelyurkin.musiccomposer.core", excludeFilters = { @ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE, value = Application.class ) } )
// todo refactor
public class MyTestConfiguration {

	@Bean
	public MelodyEqualityAnalyzerImpl melodyEqualityAnalyzer(
			@Qualifier( "getCountourEquality" ) Equality countourEquality,
			@Qualifier( "getIntervalsEquality" ) Equality intervalsEquality,
			@Qualifier( "getInversionEquality" ) Equality inversionEquality,
			@Qualifier( "getOrderEquality" ) Equality orderEquality,
			@Qualifier( "getRhythmEquality" ) Equality rhythmEquality ) {
		return new MelodyEqualityAnalyzerImpl( countourEquality, intervalsEquality, inversionEquality, orderEquality,
				rhythmEquality );
	}

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

	@Bean
	public DB Db(@Value( "${persistance.file}" ) String file) {
		return DBMaker
				.fileDB(file)
				.concurrencyDisable()
				.closeOnJvmShutdown()
				.fileDeleteAfterClose()
				.make();
	}
}
