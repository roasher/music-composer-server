package composer.compositionComposer;

import composer.CompositionComposer;
import composer.first.FirstBlockProvider;
import composer.first.RandomFirstBlockProvider;
import composer.next.NextBlockProvider;
import composer.next.SimpleNextBlockProvider;
import decomposer.CompositionDecomposer;
import helper.AbstractSpringComposerTest;
import jm.JMC;
import jm.util.Play;
import jm.util.View;
import jm.util.Write;
import model.ComposeBlock;
import model.Lexicon;
import model.composition.Composition;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import persistance.dao.LexiconDAO;
import utils.CompositionLoader;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Created by pyurkin on 15.12.14.
 */
public class CompositionComposerTest extends AbstractSpringComposerTest {

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Autowired
	private CompositionLoader compositionLoader;

	@Autowired
	private CompositionComposer compositionComposer;

	@Autowired @Qualifier( "lexiconDAO_database" )
	private LexiconDAO lexiconDAO;

	@Test
//	@Ignore
	public void getSimplePieceTest1() {
		List< Composition > compositionList = compositionLoader.getCompositionsFromFolder( new File( "src\\test\\composer\\simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		Composition composition = compositionComposer.compose( new RandomFirstBlockProvider(), new SimpleNextBlockProvider(), lexicon, "ABCD", 4 * JMC.WHOLE_NOTE );
		assertEquals( 16., composition.getEndTime(), 0 );

//		assertNotNull( composition );
//		View.show( composition );
//		Utils.suspend();
	}

	@Test
    @Ignore
	public void getRealPieceTest1() throws IOException {
//		List< Composition > compositionList = compositionLoader.getCompositions(
//		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" ),
//		  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.biosphere(midi).mid" ),
//		  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Another Phoenix (midi)_2.mid" ),
//		  		  new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\Метания беспокойного разума.mid" )
//		);

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "C:\\Users\\wish\\Downloads\\Bach chorals" ) );

		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
//		lexiconDAO.persist( lexicon );

		FirstBlockProvider firstStepProvider = new FirstBlockProvider() {
			@Override
			public ComposeBlock getNextBlock( Lexicon lexicon, List<ComposeBlock> exclusions ) {
				ComposeBlock composeBlock1 = lexicon.getAllPossibleFirst().stream()
						.filter( composeBlock -> composeBlock.getCompositionInfo().equals( compositionList.get(1).getCompositionInfo() ) && !exclusions.contains( composeBlock ) ).findFirst().get();
				return composeBlock1;
			}
		};

		NextBlockProvider nextBlockProvider = new NextBlockProvider() {
			@Override
			public ComposeBlock getNextBlock( Lexicon lexicon, ComposeBlock previousComposeBlock, List<ComposeBlock> exclusions ) {
				List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>( previousComposeBlock.getPossibleNextComposeBlocks() );
				possibleNextComposeBlocks.removeAll( exclusions );
				if ( !possibleNextComposeBlocks.isEmpty() ) {
					ComposeBlock composeBlock = possibleNextComposeBlocks.get( possibleNextComposeBlocks.size() - 1 );
					return composeBlock;
				} else {
					return null;
				}
			}
		};

		Composition composition = compositionComposer.compose( firstStepProvider, nextBlockProvider, lexicon, "ABCD", 16 * JMC.WHOLE_NOTE );
//		assertEquals( 16., composition.getEndTime(), 0 );

//		View.notate( composition );
//		Utils.suspend();
//		Play.midi( composition );
		Write.midi( composition, "D:\\Projects\\test\\output\\1.mid" );

	}

}
