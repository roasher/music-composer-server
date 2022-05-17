package ru.pavelyurkin.musiccomposer.core;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import jm.JMC;
import jm.util.Write;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.config.Config;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

/**
 * Created by night wish on 24.01.2016.
 */
public class Controller {

  public static void main(String... args) throws IOException {

    ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
        .profiles("bach-prod")
        .run(args);

    Lexicon lexicon = context.getBean(Lexicon.class);
//		compositionDecomposer.getLexiconDAO().persist( lexicon );
    ComposeStepProvider composeStepProvider = context.getBean(ComposeStepProvider.class);
    CompositionComposer compositionComposer = context.getBean(CompositionComposer.class);

    Composition composition = compositionComposer.compose(composeStepProvider, lexicon, 20 * JMC.WHOLE_NOTE);

    Environment environment = context.getBean(Environment.class);

    String fileName = String.format("output/%s-%s.mid", String.join(",", environment.getActiveProfiles()),
            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault()).format(Instant.now()));

    Write.midi(composition, fileName);

  }
}
