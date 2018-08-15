package ru.pavelyurkin.musiccomposer.core.config;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "composer")
@Configuration
public class Config {

    @NotNull
    private String pathToCompositions;


}
