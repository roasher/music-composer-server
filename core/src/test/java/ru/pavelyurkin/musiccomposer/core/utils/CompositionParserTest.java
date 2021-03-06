package ru.pavelyurkin.musiccomposer.core.utils;

import static jm.JMC.C1;
import static jm.JMC.C2;
import static jm.JMC.C5;
import static jm.JMC.D5;
import static jm.JMC.DOTTED_HALF_NOTE;
import static jm.JMC.E5;
import static jm.JMC.HALF_NOTE;
import static jm.JMC.QUARTER_NOTE;
import static jm.JMC.WHOLE_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Rest;
import jm.music.data.Score;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;
import ru.pavelyurkin.musiccomposer.core.service.composition.CompositionParser;

public class CompositionParserTest {

  private CompositionParser compositionParser = new CompositionParser();

  @Test
  public void sliceSingleVoiceComposition() throws Exception {
    Score score = new Score();

    Phrase firstInstr = new Phrase();
    firstInstr.add(new Note(C5, WHOLE_NOTE));
    firstInstr.add(new Note(D5, DOTTED_HALF_NOTE));
    firstInstr.add(new Note(E5, DOTTED_HALF_NOTE));
    Part part = new Part(firstInstr);
    score.add(part);
    Composition composition = new Composition(score);

    assertThat(compositionParser.parse(composition), is(List.of(
        new InstrumentPart(List.of(new NewMelody(
            new Note(C5, WHOLE_NOTE),
            new Note(D5, DOTTED_HALF_NOTE),
            new Note(E5, DOTTED_HALF_NOTE)
        )), 0)
        )
    ));
  }

  @Test
  public void sliceIfPhrasesFormChord() throws Exception {
    Score score = new Score();

    Phrase firstPhrase = new Phrase();
    firstPhrase.add(new Note(C5, WHOLE_NOTE));
    firstPhrase.add(new Note(D5, DOTTED_HALF_NOTE));
    firstPhrase.add(new Note(E5, DOTTED_HALF_NOTE));

    Phrase secondPhrase = new Phrase();
    secondPhrase.add(new Note(C1, QUARTER_NOTE + WHOLE_NOTE + QUARTER_NOTE));
    secondPhrase.setStartTime(DOTTED_HALF_NOTE);

    Part part = new Part();
    part.add(firstPhrase);
    part.add(secondPhrase);
    score.add(part);
    Composition composition = new Composition(score);

    assertThat(compositionParser.parse(composition), is(List.of(
        new InstrumentPart(List.of(
            new NewMelody(new Note(C5, DOTTED_HALF_NOTE)),
            new Chord(List.of(C5, C1), QUARTER_NOTE),
            new Chord(List.of(D5, C1), DOTTED_HALF_NOTE),
            new Chord(List.of(E5, C1), HALF_NOTE),
            new NewMelody(new Note(E5, QUARTER_NOTE))
        ), 0
        )
        )
        )
    );
  }

  @Test
  public void manyRestsClashToRestMelody() throws Exception {
    Phrase phrase1 = new Phrase(new Rest(WHOLE_NOTE));
    phrase1.setAppend(false);
    Phrase phrase2 = new Phrase(new Rest(HALF_NOTE));
    phrase2.setAppend(false);
    Phrase phrase3 = new Phrase(new Rest(HALF_NOTE));
    phrase3.setAppend(false);
    Part part = new Part();
    part.add(phrase1);
    part.add(phrase2);
    part.add(phrase3);

    assertThat(compositionParser.convert(part), is(
        new InstrumentPart(List.of(new NewMelody(new Rest(WHOLE_NOTE))), 0)
    ));
  }

  @Test
  public void sliceIfPhraseIsChord() throws Exception {
    Score score = new Score();

    Phrase firstPhrase = new Phrase();
    firstPhrase.add(new Note(D5, DOTTED_HALF_NOTE));

    CPhrase secondPhrase = new CPhrase();
    secondPhrase.addChord(new int[] {C1, C2}, QUARTER_NOTE);

    Part part = new Part();
    part.add(firstPhrase);
    part.addCPhrase(secondPhrase);
    score.add(part);
    Composition composition = new Composition(score);

    assertThat(compositionParser.parse(composition), is(List.of(
        new InstrumentPart(List.of(
            new NewMelody(new Note(D5, DOTTED_HALF_NOTE)),
            new Chord(List.of(C1, C2), QUARTER_NOTE)
        ), 0
        )
        )
        )
    );
  }

  @Test
  public void sliceIfPhraseIsChordOnTopOfMelody() throws Exception {
    Score score = new Score();

    Phrase firstPhrase = new Phrase();
    firstPhrase.add(new Note(D5, DOTTED_HALF_NOTE));

    CPhrase secondPhrase = new CPhrase();
    secondPhrase.addChord(new int[] {C1, C2}, QUARTER_NOTE);
    secondPhrase.setStartTime(0);
    secondPhrase.setAppend(false);

    Part part = new Part();
    part.add(firstPhrase);
    part.addCPhrase(secondPhrase);
    score.add(part);
    Composition composition = new Composition(score);

    assertThat(compositionParser.parse(composition), is(List.of(
        new InstrumentPart(List.of(
            new Chord(List.of(D5, C1, C2), QUARTER_NOTE),
            new NewMelody(new Note(D5, HALF_NOTE))
        ), 0
        )
        )
        )
    );
  }

  @Test
  public void cropIfCompositionStartsWithDelay() throws Exception {
    Score score = new Score();

    Phrase firstPhrase = new Phrase();
    firstPhrase.add(new Note(D5, DOTTED_HALF_NOTE));
    firstPhrase.setStartTime(QUARTER_NOTE);

    Part part = new Part();
    part.add(firstPhrase);
    score.add(part);

    Composition composition = new Composition(score);

    assertThat(compositionParser.parse(composition), is(List.of(
        new InstrumentPart(List.of(new NewMelody(new Rest(QUARTER_NOTE), new Note(D5, DOTTED_HALF_NOTE))), 0))
        )
    );
  }
}
