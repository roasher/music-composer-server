import composer.CompositionComposer;
import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.step.CompositionStep;
import decomposer.CompositionDecomposer;
import jm.JMC;
import jm.util.Write;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import model.composition.CompositionInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by night wish on 24.01.2016.
 */
public class Controller {

	public static final ApplicationContext applicationContext = new ClassPathXmlApplicationContext( "classpath:spring.configuration.xml" );

	private CompositionLoader compositionLoader = applicationContext.getBean( CompositionLoader.class );
	private CompositionComposer compositionComposer = applicationContext.getBean( CompositionComposer.class );
	private CompositionDecomposer compositionDecomposer = applicationContext.getBean( CompositionDecomposer.class );

	public static void main( String... args ) throws IOException {
		new Controller().getRealPieceTest();
	}

	public void getRealPieceTest() throws IOException {
		//		List< Composition > compositionList = compositionLoader.getCompositions(
		//		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" ),
		//		  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.biosphere(midi).mid" ),
		//		  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Another Phoenix (midi)_2.mid" ),
		//		  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\Метания беспокойного разума.mid" )
		//		);

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "D:\\YandexDisk\\Музыка\\Algorithmic music\\Database\\Test\\Bach chorals" ) );

		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		//		lexiconDAO.persist( lexicon );

		FirstBlockProvider firstStepProvider = new FirstBlockProvider() {
			@Override
			public ComposeBlock getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions ) {
				ComposeBlock composeBlock1 = lexicon.getAllPossibleFirst().stream().filter( composeBlock -> !exclusions.contains( composeBlock ) ).findFirst().get();
				return composeBlock1;
			}
		};

		NextBlockProvider nextBlockProvider = new NextBlockProvider() {
			@Override
			public ComposeBlock getNextBlock( Lexicon lexicon, List<CompositionStep> previousCompositionSteps ) {
				CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
				List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getComposeBlock().getPossibleNextComposeBlocks() );
				possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions() );
				for ( int possibleNextNumber = possibleNextComposeBlocks.size() - 1; possibleNextNumber > 0; possibleNextNumber-- ) {
					ComposeBlock composeBlock = possibleNextComposeBlocks.get( possibleNextNumber );
					if ( previousCompositionSteps.size() > 6 ) {
						Set<CompositionInfo> compositionInfos = previousCompositionSteps.stream().skip( previousCompositionSteps.size() - 6 )
								.map( compositionStep -> compositionStep.getComposeBlock().getCompositionInfo() ).collect( Collectors.toSet() );
						if ( compositionInfos.size() != 1 ) {
							return composeBlock;
						} else {
							continue;
						}
					} else {
						return composeBlock;
					}
				}
				return null;
			}
		};

		Composition composition = compositionComposer.compose( firstStepProvider, nextBlockProvider, lexicon, "ABCD", 16 * JMC.WHOLE_NOTE );
		//		assertEquals( 16., composition.getEndTime(), 0 );

		//		View.notate( composition );
		//		Utils.suspend();
		//		Play.midi( composition );
		Write.midi( composition, "output\\1.mid" );

	}
}
