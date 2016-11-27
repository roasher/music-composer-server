package ru.pavelyurkin.musiccomposer.composer;

import ru.pavelyurkin.musiccomposer.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.helper.AbstractSpringTest;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Rest;
import ru.pavelyurkin.musiccomposer.model.BlockMovement;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.model.Lexicon;
import ru.pavelyurkin.musiccomposer.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.model.melody.Form;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.pavelyurkin.musiccomposer.utils.CompositionLoader;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static jm.JMC.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.pavelyurkin.musiccomposer.utils.ModelUtils.getTransposePitch;
import static ru.pavelyurkin.musiccomposer.utils.ModelUtils.sumAllRhythmValues;

/**
 * Created by pyurkin on 17.02.15.
 */
public class FormBlockProviderTest extends AbstractSpringTest {
	@Autowired
	private CompositionLoader compositionLoader;
	@Autowired
	private CompositionDecomposer compositionDecomposer;
	@Autowired
	private FormBlockProvider formBlockProvider;
	@Autowired
	@Qualifier( "simpleComposeBlockProvider" )
	private ComposeBlockProvider composeBlockProvider;

	@Test
	public void formBlockProviderTest() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src/test/resource/simpleMelodies" ) );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );

		double lenght = JMC.WHOLE_NOTE;
		Optional<FormCompositionStep> optFormCompositionStep = formBlockProvider
				.getFormElement( composeBlockProvider, new Form( 'A' ), lenght, Arrays.asList( new FormCompositionStep() ), lexiconFromFirst );

		assertTrue( optFormCompositionStep.isPresent() );
		optFormCompositionStep.ifPresent( formCompositionStep -> {
			// checking lenght
			assertEquals( lenght, sumAllRhythmValues( formCompositionStep.getTransposedComposeBlocks() ), 0 );
			// checking quality of composing
			for ( int composeBlockNumber = 1;
				  composeBlockNumber < formCompositionStep.getOriginComposeBlocks().size(); composeBlockNumber++ ) {
				ComposeBlock currentOrigin = formCompositionStep.getOriginComposeBlocks().get( composeBlockNumber );
				ComposeBlock previousOrigin = formCompositionStep.getOriginComposeBlocks().get( composeBlockNumber - 1 );
				assertTrue( previousOrigin.getPossibleNextComposeBlocks().contains( currentOrigin ) );

				ComposeBlock currentTransposed = formCompositionStep.getTransposedComposeBlocks().get( composeBlockNumber );
				ComposeBlock previousTransposed = formCompositionStep.getTransposedComposeBlocks().get( composeBlockNumber - 1 );
				BlockMovement blockMovement = new BlockMovement( previousTransposed.getMelodyList(), currentTransposed.getMelodyList() );
				assertEquals( blockMovement.getVoiceMovements().size(), currentTransposed.getMelodyList().size() );
				assertEquals( blockMovement.getVoiceMovements().size(), previousTransposed.getMelodyList().size() );
				for ( int instrumentNumber = 0; instrumentNumber < blockMovement.getVoiceMovements().size(); instrumentNumber++ ) {
					assertEquals( currentOrigin.getBlockMovementFromPreviousToThis().getVoiceMovement( instrumentNumber ), blockMovement.getVoiceMovement( instrumentNumber ) );
				}
			}
		} );
	}

	@Test
	public void transposePitchTest() {
		List<ComposeBlock> composeBlocks = Arrays.asList(
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( QUARTER_NOTE ) ),
						new Melody( new Note( C3, QUARTER_NOTE ) ) ),
						new BlockMovement( -256, -256 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Rest( EIGHTH_NOTE ) ),
						new Melody( new Note( C4, EIGHTH_NOTE ) ) ),
						new BlockMovement( 0, 0 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Note( D5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ), new Note( F5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ) ),
						new Melody( new Note( C4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( E4, EIGHTH_NOTE ) ) ),
						new BlockMovement( -256, 2 ) ),
				new ComposeBlock( 0, null, Arrays.asList(
						new Melody( new Note( B5, QUARTER_NOTE ) ),
						new Melody( new Rest( QUARTER_NOTE ) ) ),
						new BlockMovement( 0, -256 ) ) );

		assertEquals( C3 - C4, getTransposePitch( Optional.of( composeBlocks.get( 0 ) ), composeBlocks.get( 1 ) ) );
		assertEquals( C4 - C4 + 2, getTransposePitch( Optional.of( composeBlocks.get( 1 ) ), composeBlocks.get( 2 ) ) );
		assertEquals( E5 - B5, getTransposePitch( Optional.of( composeBlocks.get( 2 ) ), composeBlocks.get( 3 ) ) );

	}
}
