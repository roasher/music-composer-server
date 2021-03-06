package ru.pavelyurkin.musiccomposer.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import ru.pavelyurkin.musiccomposer.core.service.decomposer.melody.analyzer.MelodyEqualityAnalyzerImpl;

@SpringBootApplication
@ComponentScan(basePackages = "ru.pavelyurkin.musiccomposer", excludeFilters = {
    // Not using yet
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = MelodyEqualityAnalyzerImpl.class),
    @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "ru.pavelyurkin.musiccomposer.core.service.equality.melody.*")}
)
public class WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

}
