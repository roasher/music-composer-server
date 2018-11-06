package ru.pavelyurkin.musiccomposer.core.model;

import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

import java.util.Arrays;

import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MusicBlockTest {

	@Test
	public void sameBlockAfterTranspositionIfPreviousPitchesIsLastPitches() throws Exception {
		MusicBlock currentBlock = new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( C5, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( C5, QUARTER_NOTE ) ) ), null,
				Arrays.asList( Note.REST, C3 ) );
		assertThat( currentBlock.transposeClone( new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Rest( QUARTER_NOTE ) ),
				new InstrumentPart( new Note( C3, QUARTER_NOTE ) ) ), null ) ).getInstrumentParts(),
				is( currentBlock.getInstrumentParts() ) );
	}

	@Test
	public void transposingAccordingToPreviousPitchesAndMusicBlockLastPitches() throws Exception {
		MusicBlock currentBlock = new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( C5, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( B4, QUARTER_NOTE ) ) ), null,
				Arrays.asList( C5, C5 ) );
		MusicBlock transposeClone = currentBlock.transposeClone( new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( C4, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( C4, QUARTER_NOTE ) ) ), null ) );
		assertThat( transposeClone.getInstrumentParts(), is( Arrays.asList(
						new InstrumentPart( new Note( C4, QUARTER_NOTE ) ),
						new InstrumentPart( new Note( B3, QUARTER_NOTE ) ) ) ) );
	}

	@Test
	public void transposingAccordingToPreviousPitchesAndMusicBlockLastPitches1() throws Exception {
		MusicBlock currentBlock = new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( C5, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( B4, QUARTER_NOTE ) ) ), null,
				Arrays.asList( C4, B3 ) );
		MusicBlock transposeClone = currentBlock.transposeClone( new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( F4, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( E4, QUARTER_NOTE ) ) ), null ) );
		assertThat( transposeClone.getInstrumentParts(), is( Arrays.asList(
				new InstrumentPart( new Note( F5, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( E5, QUARTER_NOTE ) ) ) ) );
	}

	@Test
	public void returnThisIfPreviousPitchesAllRests() throws Exception {
		MusicBlock currentBlock = new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( C5, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( B4, QUARTER_NOTE ) ) ), null,
				Arrays.asList( Note.REST, Note.REST ) );
		MusicBlock transposeClone = currentBlock.transposeClone( new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Rest( QUARTER_NOTE ) ),
				new InstrumentPart( new Rest( QUARTER_NOTE ) ) ), null ) );
		assertThat( transposeClone.getInstrumentParts(), is( currentBlock.getInstrumentParts() ) );
	}

	@Test
	public void transposingChordsAccordingToPreviousPitchesAndMusicBlockLastPitches() throws Exception {
		MusicBlock currentBlock = new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( C5, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( B4, QUARTER_NOTE ) ) ), null,
				Arrays.asList( G3, C3, A3 ) );
		MusicBlock previousBlock = new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( G4, QUARTER_NOTE ) ),
				new InstrumentPart( Arrays.asList( new Chord( Arrays.asList( C4, A4 ), QUARTER_NOTE ) ) ) ), null );
		MusicBlock transposeClone = currentBlock.transposeClone( previousBlock );
		assertThat( transposeClone.getInstrumentParts(), is( Arrays.asList(
				new InstrumentPart( new Note( C6, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( B5, QUARTER_NOTE ) ) ) ) );
	}

	@Test
	public void testConstuctFromSeveralMusicBlocks() throws Exception {
		MusicBlock musicBlock1 = new MusicBlock( 0, Arrays.asList(
				new InstrumentPart( new Note( C5, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( B4, QUARTER_NOTE ) ) ), null,
				Arrays.asList( G3, C3, A3 ) );
		MusicBlock musicBlock2 = new MusicBlock( 1000, Arrays.asList(
				new InstrumentPart( new Note( C6, WHOLE_NOTE ) ),
				new InstrumentPart( Arrays.asList( new Chord( Arrays.asList( 55, 56 ), WHOLE_NOTE ) ) ) ), null,
				Arrays.asList( C0 ) );

		assertThat( new MusicBlock( Arrays.asList( musicBlock1, musicBlock2 ) ), is(
				new MusicBlock( 0, Arrays.asList(
						new InstrumentPart( Arrays.asList( new NewMelody( new Note( C5, QUARTER_NOTE ), new Note( C6, WHOLE_NOTE ) ) ) ),
						new InstrumentPart( Arrays.asList( new NewMelody( new Note( B4, QUARTER_NOTE ) ),
								new Chord( Arrays.asList( 55, 56 ), WHOLE_NOTE ) ) ) ),
						null,
						Arrays.asList( G3, C3, A3 ) )
		) );
	}
}