package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import com.google.common.collect.ImmutableSet;
import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

import java.io.IOException;
import java.util.*;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LexiconDAO_mapdbTest extends AbstractSpringTest {

	@Autowired
	@Qualifier("lexiconDAO_mapdb")
	private LexiconDAO lexiconDAO;

	@Test
	public void fetchTest() throws IOException {
		lexiconDAO.persist( getTestLexicon() );
		assertThat( lexiconDAO.fetch().getComposeBlocks(), is( getTestLexicon().getComposeBlocks() ) );
	}

	private Lexicon getTestLexicon() {

		Note note1 = new Note( C4, WHOLE_NOTE, 0, 0 );
		Note note2 = new Note( D4, HALF_NOTE, 0, 0 );
		Note note3 = new Note( E4, EIGHTH_NOTE, 0, 0 );
		Note note4 = new Note( DS4, EIGHTH_NOTE, 0, 0 );
		Note note5 = new Note( B4, WHOLE_NOTE, 0, 0 );

		InstrumentPart melody1 = new InstrumentPart(
				Arrays.asList( new Chord( Arrays.asList( note3.getPitch(), note4.getPitch() ), WHOLE_NOTE ),
						new NewMelody( note2 ), new NewMelody( note2 ) ) );

		InstrumentPart melody2 = new InstrumentPart( note3, note4, note3, note4, note5, note2 );
		InstrumentPart melody3 = new InstrumentPart( note2, note2, note2, new Rest( HALF_NOTE ) );
		InstrumentPart melody4 = new InstrumentPart(
				Arrays.asList( new Chord( Arrays.asList( C4, C5 ), HALF_NOTE ), new NewMelody( note2, note1 ) ) );

		CompositionInfo compositionInfo = new CompositionInfo();
		compositionInfo.setAuthor( "TEST_AUTHOR" );
		compositionInfo.setTitle( "TEST_TITLE" );
		compositionInfo.setTempo( 4.0 );

		List<Integer> previousMusicBlockPitches1 = Arrays.asList( 30, 20 );
		List<Integer> previousMusicBlockPitches2 = Arrays.asList( 32, 22 );

		ComposeBlock composeBlock0 = new ComposeBlock(
				new MusicBlock( 0, Arrays.asList( melody1, melody2 ), compositionInfo, previousMusicBlockPitches1 ) );
		ComposeBlock composeBlock1 = new ComposeBlock(
				new MusicBlock( 0, Arrays.asList( melody1, melody3 ), compositionInfo, previousMusicBlockPitches1 ) );
		ComposeBlock composeBlock2 = new ComposeBlock(
				new MusicBlock( 0, Arrays.asList( melody3, melody4 ), compositionInfo, previousMusicBlockPitches2 ) );

		composeBlock0.setPossibleNextComposeBlocks( Arrays.asList( composeBlock2 ) );
		composeBlock2.setPossiblePreviousComposeBlocks( Arrays.asList( composeBlock0 ) );
		composeBlock2.setPossibleNextComposeBlocks( Arrays.asList( composeBlock0 ) );
		composeBlock0.setPossiblePreviousComposeBlocks( Arrays.asList( composeBlock2 ) );

		List<ComposeBlock> composeBlocks = Arrays.asList( composeBlock0, composeBlock1, composeBlock2 );

		Set<Integer> possibleNext0 = ImmutableSet.of( 2 );
		Set<Integer> possibleNext2 = ImmutableSet.of( 0 );
		Map<Integer, Set<Integer>> mapOfNexts = new HashMap<>();
		mapOfNexts.put( 0, possibleNext0 );
		mapOfNexts.put( 1, Collections.emptySet() );
		mapOfNexts.put( 2, possibleNext2 );

		Lexicon lexicon = new Lexicon( composeBlocks, mapOfNexts );

		return lexicon;

	}

}