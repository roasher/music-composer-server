package ru.pavelyurkin.musiccomposer.core;

import jm.JMC;
import jm.util.Write;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.config.Config;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by night wish on 24.01.2016.
 */
public class Controller {

	public static void main( String... args ) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run( Application.class, args );

		CompositionLoader compositionLoader = context.getBean( CompositionLoader.class );
		List<Composition> compositionList = compositionLoader
				.getCompositionsFromFolder( new File( context.getBean( Config.class ).getPathToCompositions() ) );

		CompositionDecomposer compositionDecomposer = context.getBean( CompositionDecomposer.class );

		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
//		compositionDecomposer.getLexiconDAO().persist( lexicon );

		ComposeStepProvider composeStepProvider = context.getBean( ComposeStepProvider.class );

		CompositionComposer compositionComposer = context.getBean( CompositionComposer.class );
		Composition composition = compositionComposer.compose( composeStepProvider, lexicon, 30 * 20 * JMC.WHOLE_NOTE );

		Write.midi( composition, "output/mozart-5.mid" );

	}
}
