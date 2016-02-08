import composer.ComposeBlockProvider;
import composer.CompositionComposer;
import composer.first.FirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.next.filter.*;
import composer.step.CompositionStep;
import decomposer.CompositionDecomposer;
import jm.JMC;
import jm.util.Write;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import persistance.dao.LexiconDAO_stub;
import utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static jm.JMC.*;

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
		//				List< Composition > compositionList = compositionLoader.getCompositions(
		//				  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" ),
		//				  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.biosphere(midi).mid" ),
		//				  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Another Phoenix (midi)_2.mid" ),
		//				  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\Метания беспокойного разума.mid" )
		//				);

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "C:\\Users\\wish\\Documents\\testBach" ) );
		//		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "C:\\Users\\wish\\Documents\\Bach chorals" ) );

		compositionDecomposer.setLexiconDAO( applicationContext.getBean( LexiconDAO_stub.class ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		//		lexiconDAO.persist( lexicon );

		FirstBlockProvider firstStepProvider = new FirstBlockProvider() {
			@Override
			public Optional<ComposeBlock> getFirstBlock( Lexicon lexicon, List<ComposeBlock> exclusions ) {
				ComposeBlock composeBlock1 = lexicon.getComposeBlockList().stream().filter( composeBlock -> !exclusions.contains( composeBlock ) ).findFirst()
						.get();
				return Optional.ofNullable( composeBlock1 );
			}
		};

		NextBlockProvider nextBlockProvider = new NextBlockProvider() {
			@Override
			public Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps ) {
				CompositionStep lastCompositionStep = previousCompositionSteps.get( previousCompositionSteps.size() - 1 );
				List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks() );
				possibleNextComposeBlocks.removeAll( lastCompositionStep.getNextMusicBlockExclusions() );

				ComposeBlockFilter composeBlockFilter = new ComposeBlockVarietyFilter( 6,
						new ComposeBlockRestFilter( QUARTER_NOTE,
								new ComposeBlockVoiceRangeFilter( Arrays.asList(
										new ComposeBlockVoiceRangeFilter.Range( C4, C6 ),
										new ComposeBlockVoiceRangeFilter.Range( F3, F5 ),
										new ComposeBlockVoiceRangeFilter.Range( A2, A4 ),
										new ComposeBlockVoiceRangeFilter.Range( F2, F4 )
								) ) ) );
				Optional<ComposeBlock> lastOfPossibles = composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps ).stream()
						.reduce( ( composeBlock1, composeBlock2 ) -> composeBlock2 );
				return lastOfPossibles;
			}
		};

		Composition composition = compositionComposer
				.compose( new ComposeBlockProvider( firstStepProvider, nextBlockProvider ), lexicon, "ABCD", 32 * JMC.WHOLE_NOTE );
		//		assertEquals( 16., composition.getEndTime(), 0 );

		//		View.notate( composition );
		//		Utils.suspend();
		//		Play.midi( composition );
		Write.midi( composition, "output\\1.mid" );

	}
}
