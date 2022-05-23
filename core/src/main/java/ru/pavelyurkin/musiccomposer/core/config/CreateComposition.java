package ru.pavelyurkin.musiccomposer.core.config;

import static jm.constants.Durations.WHOLE_NOTE;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import jm.util.Write;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;

@Configuration
@Profile("compose")
public class CreateComposition implements ApplicationRunner {

  @Autowired
  private ComposeStepProvider composeStepProvider;
  @Autowired
  private CompositionComposer compositionComposer;
  @Autowired
  private Lexicon lexicon;
  @Autowired
  private Environment environment;

  @Value("${wholeNotesNumber:20}")
  private double wholeNotesNumber;

  @Override
  public void run(ApplicationArguments args) {

    Composition composition = compositionComposer.compose(composeStepProvider, lexicon, wholeNotesNumber * WHOLE_NOTE);

    String fileName = String.format("output/%s-%s.mid", String.join(",", environment.getActiveProfiles()),
        DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault()).format(Instant.now()));

    Write.midi(composition, fileName);
  }

}
