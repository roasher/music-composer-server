package ru.pavelyurkin.musiccomposer.core.utils;

import jm.music.data.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static jm.JMC.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CompositionSlicerTest extends AbstractSpringTest {

	@Autowired
	private CompositionSlicer compositionSlicer;
	@Autowired
	private CompositionLoader compositionLoader;

	@Test
	public void sliceSingleVoiceComposition() throws Exception {
		Score score = new Score();

		Phrase firstInstr = new Phrase();
		firstInstr.add( new Note( C5, WHOLE_NOTE ) );
		firstInstr.add( new Note( D5, DOTTED_HALF_NOTE ) );
		firstInstr.add( new Note( E5, DOTTED_HALF_NOTE ) );
		Part part = new Part( firstInstr );
		score.add( part );
		Composition composition = new Composition( score );

		assertThat( compositionSlicer.slice( composition, HALF_NOTE ), is( Arrays.asList(
				Arrays.asList(
						new InstrumentPart(
								new Note( C5, HALF_NOTE ),
								new Note( C5, HALF_NOTE ),
								new Note( D5, HALF_NOTE ),
								new Note( D5, QUARTER_NOTE ),
								new Note( E5, QUARTER_NOTE ),
								new Note( E5, HALF_NOTE )
								)
				)
				)
		) );
	}

	@Test
	public void sliceIfPhrasesFormChord() throws Exception {
		Score score = new Score();

		Phrase firstPhrase = new Phrase();
		firstPhrase.add( new Note( C5, WHOLE_NOTE ) );
		firstPhrase.add( new Note( D5, DOTTED_HALF_NOTE ) );
		firstPhrase.add( new Note( E5, DOTTED_HALF_NOTE ) );

		Phrase secondPhrase = new Phrase();
		secondPhrase.add( new Note( C1, QUARTER_NOTE + WHOLE_NOTE + QUARTER_NOTE ) );
		secondPhrase.setStartTime( DOTTED_HALF_NOTE );

		Part part = new Part();
		part.add( firstPhrase );
		part.add( secondPhrase );
		score.add( part );
		Composition composition = new Composition( score );

		assertThat( compositionSlicer.slice( composition, HALF_NOTE ), is( Arrays.asList(
				Arrays.asList(
						new InstrumentPart( Arrays.asList(
								new NewMelody( new Note( C5, HALF_NOTE ) ),
								new NewMelody( new Note( C5, QUARTER_NOTE ) ),
								new Chord( Arrays.asList( C5, C1 ), QUARTER_NOTE ),

								new Chord( Arrays.asList( D5, C1 ), HALF_NOTE ),
								new Chord( Arrays.asList( D5, C1 ), QUARTER_NOTE ),
								new Chord( Arrays.asList( E5, C1 ), QUARTER_NOTE ),

								new Chord( Arrays.asList( E5, C1 ), QUARTER_NOTE ),
								new NewMelody( new Note( E5, QUARTER_NOTE ) )
						)
						)
				)
				)
		) );
	}

	@Test
	public void sliceIfPhraseIsChord() throws Exception {
		Score score = new Score();

		Phrase firstPhrase = new Phrase();
		firstPhrase.add( new Note( D5, DOTTED_HALF_NOTE ) );

		Phrase secondPhrase = new Phrase();
		secondPhrase.addChord( new int[] {C1, C2} ,QUARTER_NOTE );
		secondPhrase.setStartTime( DOTTED_HALF_NOTE );

		Part part = new Part();
		part.add( firstPhrase );
		part.add( secondPhrase );
		score.add( part );
		Composition composition = new Composition( score );

		assertThat( compositionSlicer.slice( composition, HALF_NOTE ), is( Arrays.asList(
				Arrays.asList(
						new InstrumentPart( Arrays.asList(
								new NewMelody( new Note( C5, HALF_NOTE ) ),
								new NewMelody( new Note( C5, QUARTER_NOTE ) ),
								new Chord( Arrays.asList( C1, C2 ), QUARTER_NOTE )
						)
						)
				)
				)
		) );
	}

	@Test
	public void testCase3() {
		// notes to slice
		List<Note> noteListToSlice = new ArrayList<>();

		noteListToSlice.add( new Note( C0, HALF_NOTE ) );
		noteListToSlice.add( new Note( C0, HALF_NOTE ) );

		noteListToSlice.add( new Note( C0, QUARTER_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );

		// already sliced notes + rest in the end
		List<Melody> sliceEtalon = new ArrayList<>();

		Melody slice1 = new Melody();
		slice1.add( new Note( C0, HALF_NOTE ) );
		slice1.add( new Note( C0, HALF_NOTE ) );
		sliceEtalon.add( slice1 );

		Melody slice2 = new Melody();
		slice2.add( new Note( C0, QUARTER_NOTE ) );
		slice2.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice2.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice2.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice2.add( new Note( REST, WHOLE_NOTE - QUARTER_NOTE - 3 * SIXTEENTH_NOTE ) );
		slice2.setStartTime( slice1.getEndTime() );
		sliceEtalon.add( slice2 );

		Phrase phrase = new Phrase();
		for ( Note note : noteListToSlice ) {
			phrase.add( note );
		}
		List<Melody> sliceToTest = compositionSlicer.slice( phrase, WHOLE_NOTE );

		assertThat( sliceToTest, is( sliceEtalon ) );
	}

	@Test
	public void restSlice() {
		Phrase phrase = new Phrase();
		phrase.add( new Note( REST, WHOLE_NOTE ) );
		phrase.add( new Note( REST, QUARTER_NOTE ) );
		phrase.add( new Note( REST, QUARTER_NOTE ) );
		phrase.add( new Note( REST, HALF_NOTE ) );
		phrase.add( new Note( REST, EIGHTH_NOTE ) );
		phrase.add( new Note( REST, EIGHTH_NOTE ) );

		List<Melody> sliceToTest = compositionSlicer.slice( phrase, WHOLE_NOTE );
		assertThat( sliceToTest, hasSize( 3 ) );
	}

	@Test
	// expect no exceptions
	public void badNoteRhythmValues() {
		Phrase phrase = new Phrase();
		phrase.add( new Note( 64, 0.5 ) );
		phrase.add( new Note( 60, 0.5 ) );
		phrase.add( new Note( 62, 0.9666666666666667 ) );
		phrase.add( new Note( 57, 0.9666666666666667 ) );
		phrase.add( new Note( 59, 0.5 ) );
		phrase.add( new Note( 60, 0.5 ) );
		phrase.add( new Note( 60, 0.08333333333333333 ) );

		List<Melody> sliceToTest = compositionSlicer.slice( phrase, WHOLE_NOTE );
		assertThat( sliceToTest, hasSize( 2 ) );
	}

	@Test
	public void restInTheBeginningTestSlice() {
		Composition composition = compositionLoader.getComposition( new File( CompositionLoaderTest.class.getResource( "2.Scarecrow's_song(midi).mid" ).getFile() ) );

//		View.notate( composition );
//		suspend();

		List<List<InstrumentPart>> musicBlockList = compositionSlicer.slice( composition, WHOLE_NOTE );

		assertThat( musicBlockList.get( 0 ).get( 3 ).getNoteGroups(), is( Collections.singleton( new NewMelody( new Rest( WHOLE_NOTE ) ) ) ) );
	}

	@Test
	public void startNotFromTheBeginnigPhraseSliceTest() {
		Phrase phrase = new Phrase();
		phrase.setStartTime( WHOLE_NOTE + QUARTER_NOTE );
		phrase.add( new Note( C4, WHOLE_NOTE ) );
		phrase.add( new Note( D4, QUARTER_NOTE ) );

//		View.notate( phrase );
//		suspend();

		List<Melody> sliceToTest = compositionSlicer.slice( phrase, WHOLE_NOTE );
		List<Melody> etalonSlice = new ArrayList<>();

		Melody slice1 = new Melody();
		slice1.add( new Note( REST, WHOLE_NOTE ) );
		etalonSlice.add( slice1 );

		Melody slice2 = new Melody();
		slice2.add( new Note( REST, QUARTER_NOTE ) );
		slice2.add( new Note( C4, DOTTED_HALF_NOTE ) );
		slice2.setStartTime( slice1.getEndTime() );
		etalonSlice.add( slice2 );

		Melody slice3 = new Melody();
		slice3.add( new Note( C4, QUARTER_NOTE ) );
		slice3.add( new Note( D4, QUARTER_NOTE ) );
		slice3.add( new Note( REST, HALF_NOTE ) );
		slice3.setStartTime( slice2.getEndTime() );
		etalonSlice.add( slice3 );

		assertThat( sliceToTest, is( etalonSlice ) );
	}

    @Test
    public void adjustToUnifiedEndTimeTest() {
        Phrase phrase1 = new Phrase( new Note[] {
                new Note( C0, WHOLE_NOTE ),
        });

        Phrase phrase2 = new Phrase( new Note[] {
                new Note( C0, WHOLE_NOTE ),
        });
        phrase2.setStartTime( HALF_NOTE );

        Phrase phrase3 = new Phrase( new Note[] {
                new Note( C0, WHOLE_NOTE ),
        });
        phrase3.setStartTime( phrase2.getStartTime() + QUARTER_NOTE );

        Composition composition = new Composition( new Part[] {
                new Part( phrase1 ),
                new Part( phrase2 ),
                new Part( phrase3 ),
        });

        compositionSlicer.adjustToUnifiedEndTime( composition );
        for ( Part part : composition.getPartArray() ) {
            assertThat( phrase3.getEndTime(), is( part.getPhrase( 0 ).getEndTime() ) );
			assertThat( 0, is( part.getPhrase( 0 ).getStartTime() ) );
        }
    }
}
