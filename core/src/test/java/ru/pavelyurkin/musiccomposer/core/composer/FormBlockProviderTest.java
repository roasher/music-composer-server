package ru.pavelyurkin.musiccomposer.core.composer;

import org.junit.Assert;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Rest;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static jm.JMC.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
	private ComposeStepProvider composeStepProvider;

	@Test
	public void formBlockProviderTest() {
		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src/test/resources/simpleMelodies" ) );
		Lexicon lexiconFromFirst = compositionDecomposer.decompose( compositionList.get( 0 ), JMC.WHOLE_NOTE );

		double lenght = JMC.WHOLE_NOTE;
		Optional<FormCompositionStep> optFormCompositionStep = formBlockProvider
				.getFormElement( lenght, lexiconFromFirst, composeStepProvider, new Form( 'A' ), Collections.emptyList(), Collections.emptyList() );

		assertTrue( optFormCompositionStep.isPresent() );
		optFormCompositionStep.ifPresent( formCompositionStep -> {
			// checking length
			Assert.assertEquals( lenght, ModelUtils.sumAllRhythmValues(
					formCompositionStep.getCompositionSteps()
							.stream()
							.map( CompositionStep::getTransposedBlock )
							.collect( Collectors.toList() ) ), 0 );
			// checking quality of composing
			for ( int compositionStepNumber = 1; compositionStepNumber < formCompositionStep.getCompositionSteps().size(); compositionStepNumber++ ) {
				ComposeBlock currentOrigin = formCompositionStep.getCompositionSteps().get( compositionStepNumber ).getOriginComposeBlock();
				ComposeBlock previousOrigin = formCompositionStep.getCompositionSteps().get( compositionStepNumber - 1 ).getOriginComposeBlock();
				assertTrue( previousOrigin.getPossibleNextComposeBlocks().contains( currentOrigin ) );

				MusicBlock currentTransposedBlock = formCompositionStep.getCompositionSteps().get( compositionStepNumber ).getTransposedBlock();
				MusicBlock previousTransposedBlock = formCompositionStep.getCompositionSteps().get( compositionStepNumber - 1 ).getTransposedBlock();
				BlockMovement blockMovement = new BlockMovement( previousTransposedBlock.getMelodyList(), currentTransposedBlock.getMelodyList() );
				assertEquals( blockMovement.getVoiceMovements().size(), currentTransposedBlock.getMelodyList().size() );
				assertEquals( blockMovement.getVoiceMovements().size(), previousTransposedBlock.getMelodyList().size() );
				for ( int instrumentNumber = 0; instrumentNumber < blockMovement.getVoiceMovements().size(); instrumentNumber++ ) {
					assertEquals( currentOrigin.getBlockMovementFromPreviousToThis().getVoiceMovement( instrumentNumber ), blockMovement.getVoiceMovement( instrumentNumber ) );
				}
			}
		} );
	}

	@Test
	public void transposePitchTest() {
		List<MusicBlock> blocks = Arrays.asList(
				new MusicBlock( 0, null, Arrays.asList(
						new Melody( new Rest( QUARTER_NOTE ) ),
						new Melody( new Note( C3, QUARTER_NOTE ) ) ),
						new BlockMovement( -256, -256 ) ),
				new MusicBlock( 0, null, Arrays.asList(
						new Melody( new Rest( EIGHTH_NOTE ) ),
						new Melody( new Note( C4, EIGHTH_NOTE ) ) ),
						new BlockMovement( 0, 0 ) ),
				new MusicBlock( 0, null, Arrays.asList(
						new Melody( new Note( D5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ), new Note( F5, EIGHTH_NOTE ), new Note( E5, EIGHTH_NOTE ) ),
						new Melody( new Note( C4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( D4, EIGHTH_NOTE ), new Note( E4, EIGHTH_NOTE ) ) ),
						new BlockMovement( -256, 2 ) ),
				new MusicBlock( 0, null, Arrays.asList(
						new Melody( new Note( B5, QUARTER_NOTE ) ),
						new Melody( new Rest( QUARTER_NOTE ) ) ),
						new BlockMovement( 0, -256 ) ) );

		Assert.assertEquals( C3 - C4, ModelUtils.getTransposePitch( Optional.of( blocks.get( 0 ) ), blocks.get( 1 ) ) );
		Assert.assertEquals( C4 - C4 + 2, ModelUtils.getTransposePitch( Optional.of( blocks.get( 1 ) ), blocks.get( 2 ) ) );
		Assert.assertEquals( E5 - B5, ModelUtils.getTransposePitch( Optional.of( blocks.get( 2 ) ), blocks.get( 3 ) ) );

	}
}
