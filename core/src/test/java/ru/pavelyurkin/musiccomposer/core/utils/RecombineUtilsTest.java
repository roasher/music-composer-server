package ru.pavelyurkin.musiccomposer.core.utils;

import static jm.JMC.DOTTED_HALF_NOTE;
import static jm.JMC.EIGHTH_NOTE;
import static jm.JMC.HALF_NOTE;
import static jm.JMC.QUARTER_NOTE;
import static jm.JMC.WHOLE_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

/**
 * Created by pyurkin on 25.11.14.
 */
public class RecombineUtilsTest {

  @Test
  public void testRecombine() {
    List<InstrumentPart> melodyList0 = Arrays.asList(
        new InstrumentPart(
            new Rest(HALF_NOTE),
            new Note(11, QUARTER_NOTE),
            new Note(12, EIGHTH_NOTE),
            new Note(13, EIGHTH_NOTE)
        ),
        new InstrumentPart(Arrays.asList(
            new NewMelody(new Note(21, EIGHTH_NOTE)),
            new Chord(Arrays.asList(221, 222), EIGHTH_NOTE),
            new NewMelody(new Note(23, DOTTED_HALF_NOTE))
        )
        ),
        new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(31, 32, 33), WHOLE_NOTE))),
        new InstrumentPart(
            new Note(41, HALF_NOTE),
            new Note(42, HALF_NOTE)
        )
    );

    assertThat(RecombineUtils.recombine(melodyList0), is(Arrays.asList(
        Arrays.asList(
            new InstrumentPart(new Rest(EIGHTH_NOTE)),
            new InstrumentPart(new Note(21, EIGHTH_NOTE)),
            new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(31, 32, 33), EIGHTH_NOTE))),
            new InstrumentPart(new Note(41, EIGHTH_NOTE))
        ),
        Arrays.asList(
            new InstrumentPart(new Rest(EIGHTH_NOTE)),
            new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(221, 222), EIGHTH_NOTE))),
            new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(31, 32, 33), EIGHTH_NOTE))),
            new InstrumentPart(new Note(41, EIGHTH_NOTE))
        ),
        Arrays.asList(
            new InstrumentPart(new Rest(QUARTER_NOTE)),
            new InstrumentPart(new Note(23, QUARTER_NOTE)),
            new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(31, 32, 33), QUARTER_NOTE))),
            new InstrumentPart(new Note(41, QUARTER_NOTE))
        ),
        Arrays.asList(
            new InstrumentPart(new Note(11, QUARTER_NOTE)),
            new InstrumentPart(new Note(23, QUARTER_NOTE)),
            new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(31, 32, 33), QUARTER_NOTE))),
            new InstrumentPart(new Note(42, QUARTER_NOTE))
        ),
        Arrays.asList(
            new InstrumentPart(new Note(12, EIGHTH_NOTE)),
            new InstrumentPart(new Note(23, EIGHTH_NOTE)),
            new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(31, 32, 33), EIGHTH_NOTE))),
            new InstrumentPart(new Note(42, EIGHTH_NOTE))
        ),
        Arrays.asList(
            new InstrumentPart(new Note(13, EIGHTH_NOTE)),
            new InstrumentPart(new Note(23, EIGHTH_NOTE)),
            new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(31, 32, 33), EIGHTH_NOTE))),
            new InstrumentPart(new Note(42, EIGHTH_NOTE))
        )
    )));

  }

  @Test
  public void recombineChords() throws Exception {
    // input
    List<InstrumentPart> inputMelodyBlock = Arrays.asList(
        new InstrumentPart(
            new NewMelody(new Rest(QUARTER_NOTE)),
            new Chord(Arrays.asList(60, 61), QUARTER_NOTE)
        ),
        new InstrumentPart(
            new Note(58, HALF_NOTE)
        )
    );

    assertThat(RecombineUtils.recombine(inputMelodyBlock), is(Arrays.asList(
        Arrays.asList(
            new InstrumentPart(new NewMelody(new Rest(QUARTER_NOTE))),
            new InstrumentPart(new Note(58, QUARTER_NOTE))
        ),
        Arrays.asList(
            new InstrumentPart(new Chord(Arrays.asList(60, 61), QUARTER_NOTE)),
            new InstrumentPart(new Note(58, QUARTER_NOTE))
        )
    )));
  }
}
