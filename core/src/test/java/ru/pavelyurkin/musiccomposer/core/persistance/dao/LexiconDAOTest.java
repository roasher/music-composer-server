package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import jm.JMC;
import jm.music.data.Note;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.pavelyurkin.musiccomposer.core.decomposer.CompositionDecomposer;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static jm.JMC.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by pyurkin on 15.04.2015.
 */
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class } )
public class LexiconDAOTest extends AbstractSpringTest {

	@Autowired
	@Qualifier( "lexiconDAO_database" )
	private LexiconDAO lexiconDAO;
	@Autowired
	private CompositionLoader compositionLoader;
	@Autowired
	private CompositionDecomposer compositionDecomposer;

	@Test
	@DatabaseSetup( "/persistance/dao/LexiconDAOTest-blank.xml" )
	public void storeIdentityTest() throws IOException {

		List<Composition> compositionList = compositionLoader.getCompositionsFromFolder( new File( "src/test/resources/simpleMelodies" ) );
		Lexicon lexicon = compositionDecomposer.decompose( compositionList, JMC.WHOLE_NOTE );

		lexiconDAO.persist( lexicon );
		Lexicon fetchedLexicon = lexiconDAO.fetch();

		assertTrue( lexicon.equals( fetchedLexicon ) );
	}

	@Test
	@DatabaseSetup( "/persistance/dao/LexiconDAOTest-blank.xml" )
	@ExpectedDatabase( assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/persistance/dao/LexiconDAOTest-with-data.xml" )
	public void persistTest() throws IOException {
		Lexicon lexicon = getTestLexicon();
		lexiconDAO.persist( lexicon );
	}

	@Test
	@DatabaseSetup( "/persistance/dao/LexiconDAOTest-with-data.xml" )
	public void fetchTest() {
		Lexicon lexicon = lexiconDAO.fetch();
		assertEquals( lexicon.getComposeBlockList(), getTestLexicon().getComposeBlockList() );
	}

	private Lexicon getTestLexicon() {

		Note note1 = new Note( C4, WHOLE_NOTE, 0, 0 );
		Note note2 = new Note( D4, HALF_NOTE, 0, 0 );
		Note note3 = new Note( E4, EIGHTH_NOTE, 0, 0 );
		Note note4 = new Note( DS4, EIGHTH_NOTE, 0, 0 );
		Note note5 = new Note( B4, WHOLE_NOTE, 0, 0 );

		InstrumentPart melody1 = new InstrumentPart( Arrays.asList(
				new Chord( Arrays.asList( note3.getPitch(), note4.getPitch() ), note3.getRhythmValue() ),
				new NewMelody( note2 ),
				new NewMelody( note2 )
		) );

		InstrumentPart melody2 = new InstrumentPart( note3, note4, note3, note4, note5 );
		InstrumentPart melody3 = new InstrumentPart( note2, note2, note2 );
		InstrumentPart melody4 = new InstrumentPart( Arrays.asList(
				new Chord( Arrays.asList( C4, C5 ), WHOLE_NOTE ),
				new NewMelody( note2, note1 )
		) );

		CompositionInfo compositionInfo = new CompositionInfo();
		compositionInfo.setAuthor( "TEST_AUTHOR" );
		compositionInfo.setTitle( "TEST_TITLE" );
		compositionInfo.setTempo( 4.0 );

		List<Integer> previousMusicBlockPitches1 = Arrays.asList( 30, 20 );
		List<Integer> previousMusicBlockPitches2 = Arrays.asList( 32, 22 );

		ComposeBlock composeBlock0 = new ComposeBlock( new MusicBlock( 0, Arrays.asList( melody1, melody2 ), compositionInfo, previousMusicBlockPitches1 ) );
		ComposeBlock composeBlock1 = new ComposeBlock( new MusicBlock( 0, Arrays.asList( melody1, melody3 ), compositionInfo, previousMusicBlockPitches1 ) );
		ComposeBlock composeBlock2 = new ComposeBlock( new MusicBlock( 0, Arrays.asList( melody3, melody4 ), compositionInfo, previousMusicBlockPitches2 ) );

		composeBlock0.setPossibleNextComposeBlocks( Arrays.asList( composeBlock2 ) );
		composeBlock2.setPossiblePreviousComposeBlocks( Arrays.asList( composeBlock0 ) );
		composeBlock2.setPossibleNextComposeBlocks( Arrays.asList( composeBlock0 ) );
		composeBlock0.setPossiblePreviousComposeBlocks( Arrays.asList( composeBlock2 ) );

		List<ComposeBlock> composeBlocks = Arrays.asList( composeBlock0, composeBlock1, composeBlock2 );

		List<Integer> possibleNext0 = Arrays.asList( 2 );
		List<Integer> possibleNext2 = Arrays.asList( 0 );
		Map<Integer, List<Integer>> mapOfNexts = new HashMap<>();
		mapOfNexts.put( 0, possibleNext0 );
		mapOfNexts.put( 1, Collections.emptyList() );
		mapOfNexts.put( 2, possibleNext2 );

		Lexicon lexicon = new Lexicon( composeBlocks, mapOfNexts );

		return lexicon;

	}
}
