package ru.pavelyurkin.musiccomposer.core;

import jm.JMC;
import jm.util.View;
import jm.util.Write;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeStepProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.composer.next.NextStepProviderImpl;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilter;
import ru.pavelyurkin.musiccomposer.core.config.Config;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.utils.Utils;

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
		CompositionComposer compositionComposer = context.getBean( CompositionComposer.class );
		CompositionDecomposer compositionDecomposer = context.getBean( CompositionDecomposer.class );

		List<Composition> compositionList = compositionLoader
				.getCompositionsFromFolder( new File( context.getBean( Config.class ).getPathToCompositions() ) );

		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		//		compositionDecomposer.getLexiconDAO().persist( lexicon );

		ComposeStepFilter bachChoralFilter = context.getBean( BachChoralFilter.class );

		NextStepProviderImpl nextBlockProvider = context.getBean( NextStepProviderImpl.class );
		nextBlockProvider.setComposeStepFilter( bachChoralFilter );

		ComposeStepProvider composeBlockProvider = context.getBean( ComposeStepProvider.class );
		composeBlockProvider.setNextStepProvider( nextBlockProvider );

		Composition composition = compositionComposer.compose( composeBlockProvider, lexicon, 10 * JMC.WHOLE_NOTE );

		//				View.notate( composition );
		//				Utils.suspend();
		//				Play.midi( composition );
		Write.midi( composition, "output/2.mid" );

	}
}
