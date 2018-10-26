package ru.pavelyurkin.musiccomposer.core.utils;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import org.junit.Ignore;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

import java.util.Arrays;

import static jm.JMC.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CompositionParserTest {

	private CompositionParser compositionParser = new CompositionParser();

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

		assertThat( compositionParser.parse( composition ), is( Arrays.asList(
						new InstrumentPart( Arrays.asList( new NewMelody(
								new Note( C5, WHOLE_NOTE ),
								new Note( D5, DOTTED_HALF_NOTE ),
								new Note( E5, DOTTED_HALF_NOTE )
						) ), 0 )
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

		assertThat( compositionParser.parse( composition ), is( Arrays.asList(
						new InstrumentPart( Arrays.asList(
								new NewMelody( new Note( C5, DOTTED_HALF_NOTE ) ),
								new Chord( Arrays.asList( C5, C1 ), QUARTER_NOTE ),
								new Chord( Arrays.asList( D5, C1 ), DOTTED_HALF_NOTE ),
								new Chord( Arrays.asList( E5, C1 ), HALF_NOTE ),
								new NewMelody( new Note( E5, QUARTER_NOTE ) )
						), 0
						)
				)
				)
		);
	}

	@Test
	@Ignore
	// can't hanlde that
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

		assertThat( compositionParser.parse( composition ), is( Arrays.asList(
						new InstrumentPart( Arrays.asList(
								new NewMelody( new Note( D5, DOTTED_HALF_NOTE ) ),
								new Chord( Arrays.asList( C1, C2 ), QUARTER_NOTE )
						), 0
						)
				)
				)
		);
	}

	@Test
	@Ignore
	// can't hanlde that
	public void sliceIfPhraseIsChordOnTopOfMelody() throws Exception {
		Score score = new Score();

		Phrase firstPhrase = new Phrase();
		firstPhrase.add( new Note( D5, DOTTED_HALF_NOTE ) );

		Phrase secondPhrase = new Phrase();
		secondPhrase.addChord( new int[] {C1, C2} ,QUARTER_NOTE );

		Part part = new Part();
		part.add( firstPhrase );
		part.add( secondPhrase );
		score.add( part );
		Composition composition = new Composition( score );

		assertThat( compositionParser.parse( composition ), is( Arrays.asList(
				new InstrumentPart( Arrays.asList(
						new Chord( Arrays.asList( D5, C1, C2 ), QUARTER_NOTE ),
						new NewMelody( new Note( D5, HALF_NOTE ) )
				), 0
				)
				)
				)
		);
	}
}
