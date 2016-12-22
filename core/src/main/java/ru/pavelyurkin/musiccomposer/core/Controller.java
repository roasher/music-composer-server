package ru.pavelyurkin.musiccomposer.core;

import ru.pavelyurkin.musiccomposer.core.composer.ComposeBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.composer.next.FormNextBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeBlockFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.custom.BachChoralFilter;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import jm.JMC;
import jm.util.Write;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by night wish on 24.01.2016.
 */
public class Controller {

	public static final ApplicationContext applicationContext = new ClassPathXmlApplicationContext( "classpath:spring.configuration.xml" );

	private CompositionLoader compositionLoader = applicationContext.getBean( CompositionLoader.class );
	private CompositionComposer compositionComposer = applicationContext.getBean( CompositionComposer.class );
	private CompositionDecomposer compositionDecomposer = applicationContext.getBean( CompositionDecomposer.class );

	public static void main( String... args ) throws IOException {
		new Controller().getRealPiece();
	}

	public void getRealPiece() throws IOException {
		//				List< Composition > compositionList = compositionLoader.getCompositions(
		//				  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" ),
		//				  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.biosphere(midi).mid" ),
		//				  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Another Phoenix (midi)_2.mid" ),
		//				  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\Метания беспокойного разума.mid" )
		//				);

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "/home/night_wish/Music/Bach chorals cut/" ) );
		//		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "C:\\Users\\wish\\Documents\\Bach chorals" ) );

		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
//		compositionDecomposer.getLexiconDAO().persist( lexicon );

		ComposeBlockFilter bachChoralFilter = applicationContext.getBean( BachChoralFilter.class );
		FormNextBlockProvider formNextBlockProvider = applicationContext.getBean( FormNextBlockProvider.class );
		formNextBlockProvider.setComposeBlockFilter( bachChoralFilter );

		ComposeBlockProvider composeBlockProvider = applicationContext.getBean( ComposeBlockProvider.class );
		composeBlockProvider.setNextBlockProvider( formNextBlockProvider );

		Composition composition = compositionComposer.compose( composeBlockProvider, lexicon, "ABAB", 8 * JMC.WHOLE_NOTE );
		//		assertEquals( 16., composition.getEndTime(), 0 );

		//		View.notate( composition );
		//		Utils.suspend();
		//		Play.midi( composition );
		Write.midi( composition, "output/6.mid" );

	}
}
