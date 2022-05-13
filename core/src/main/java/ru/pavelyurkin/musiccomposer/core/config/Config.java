package ru.pavelyurkin.musiccomposer.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "composer")
@Configuration
public class Config {

  private String pathToCompositions;

}
