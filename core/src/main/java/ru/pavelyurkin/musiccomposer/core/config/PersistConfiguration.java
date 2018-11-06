package ru.pavelyurkin.musiccomposer.core.config;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistConfiguration {

	@Bean
	public DB Db(@Value( "${persistance.file}" ) String file) {
		return DBMaker
				.fileDB(file)
				.concurrencyDisable()
				.closeOnJvmShutdown()
				.make();
	}

}
