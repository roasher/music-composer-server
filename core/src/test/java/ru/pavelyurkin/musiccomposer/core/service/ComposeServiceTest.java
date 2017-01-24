package ru.pavelyurkin.musiccomposer.core.service;

import jm.JMC;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.composer.ComposeBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class ComposeServiceTest extends AbstractSpringTest {

	@Autowired
	private ComposeService composeService;

	@Autowired
	private CompositionComposer compositionComposer;

	@Autowired
	private ComposeBlockProvider composeBlockProvider;

	@Autowired
	private CompositionLoader compositionLoader;

	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Before
	public void before() {
		ComposingParameters composingParameters = new ComposingParameters();
		composingParameters.setComposeBlockProvider( composeBlockProvider );

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src/test/resource/ru/pavelyurkin/musiccomposer/core/decomposer/" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );
		composingParameters.setLexicon( lexicon );

		composeService.setDefaultComposingParameters( composingParameters );
	}

	@Test
	public void getNextBarsFromComposition() throws Exception {
		String id1 = "id1";
		Composition composition1 = composeService.getNextBarsFromComposition( id1, 10 );
		String id2 = "id2";
		Composition composition11 = composeService.getNextBarsFromComposition( id2, 5 );
		Composition composition12 = composeService.getNextBarsFromComposition( id2, 5 );
		String id3 = "id3";
		Composition composition21 = composeService.getNextBarsFromComposition( id3, 3 );
		Composition composition22 = composeService.getNextBarsFromComposition( id3, 3 );
		Composition composition23 = composeService.getNextBarsFromComposition( id3, 4 );
		// Checking same lexicon
		assertSame( composeService.getComposingParameters( id1 ).getLexicon(), composeService.getComposingParameters( id2 ).getLexicon() );
		assertSame( composeService.getComposingParameters( id1 ).getLexicon(), composeService.getComposingParameters( id3 ).getLexicon() );
		// Checking same compose results if sum of bars are equal regardless of number of requests
		Composition compositionGather1 = compositionComposer.gatherComposition( composition11, composition12 );
		Composition compositionGather2 = compositionComposer.gatherComposition( composition21, composition22, composition23 );
		assertTrue( composition1.hasSameNoteContent( compositionGather1 ) );
		assertTrue( composition1.hasSameNoteContent( compositionGather2 ) );
		assertTrue( compositionGather1.hasSameNoteContent( compositionGather2 ) );
	}

}