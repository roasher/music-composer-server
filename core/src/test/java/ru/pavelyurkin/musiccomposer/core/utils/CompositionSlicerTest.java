package ru.pavelyurkin.musiccomposer.core.utils;

import static jm.JMC.C1;
import static jm.JMC.C5;
import static jm.JMC.D5;
import static jm.JMC.DOTTED_HALF_NOTE;
import static jm.JMC.E5;
import static jm.JMC.HALF_NOTE;
import static jm.JMC.QUARTER_NOTE;
import static jm.JMC.WHOLE_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

public class CompositionSlicerTest {

  private CompositionSlicer compositionSlicer = new CompositionSlicer();

  @Test
  public void sliceSingleVoiceComposition() throws Exception {
    List<InstrumentPart> instrumentParts = Arrays.asList(new InstrumentPart(Arrays.asList(
        new NewMelody(
            new Note(C5, WHOLE_NOTE),
            new Note(D5, DOTTED_HALF_NOTE),
            new Note(E5, DOTTED_HALF_NOTE))), 0));

    assertThat(compositionSlicer.slice(instrumentParts, HALF_NOTE), is(Arrays.asList(
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new NewMelody(new Note(C5, HALF_NOTE))), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new NewMelody(new Note(C5, HALF_NOTE))), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new NewMelody(new Note(D5, HALF_NOTE))), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new NewMelody(new Note(D5, QUARTER_NOTE), new Note(E5, QUARTER_NOTE))), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new NewMelody(new Note(E5, HALF_NOTE))), 0)
        )
        ))
    );
  }

  @Test
  public void sliceIfPhrasesFormChord() throws Exception {
    List<InstrumentPart> instrumentParts = Arrays.asList(new InstrumentPart(
        Arrays.asList(new NewMelody(new Note(C5, DOTTED_HALF_NOTE)),
            new Chord(Arrays.asList(C5, C1), QUARTER_NOTE),
            new Chord(Arrays.asList(D5, C1), DOTTED_HALF_NOTE),
            new Chord(Arrays.asList(E5, C1), HALF_NOTE),
            new NewMelody(new Note(E5, QUARTER_NOTE))), 0));

    assertThat(compositionSlicer.slice(instrumentParts, HALF_NOTE), is(Arrays.asList(
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new NewMelody(new Note(C5, HALF_NOTE))), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(
                new NewMelody(new Note(C5, QUARTER_NOTE)),
                new Chord(Arrays.asList(C5, C1), QUARTER_NOTE)), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new Chord(Arrays.asList(D5, C1), HALF_NOTE)), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(
                new Chord(Arrays.asList(D5, C1), QUARTER_NOTE),
                new Chord(Arrays.asList(E5, C1), QUARTER_NOTE)), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(
                new Chord(Arrays.asList(E5, C1), QUARTER_NOTE),
                new NewMelody(new Note(E5, QUARTER_NOTE))), 0)
        )
        )
    ));
  }

  @Test
  public void addRestToTheEnd() {
    List<InstrumentPart> instrumentParts = Arrays.asList(new InstrumentPart(
        Arrays.asList(new NewMelody(new Rest(DOTTED_HALF_NOTE)),
            new NewMelody(new Rest(HALF_NOTE))), 0));

    assertThat(compositionSlicer.slice(instrumentParts, HALF_NOTE), is(Arrays.asList(
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new NewMelody(new Rest(HALF_NOTE))), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(
                Arrays.asList(new NewMelody(new Rest(QUARTER_NOTE)), new NewMelody(new Rest(QUARTER_NOTE))), 0)
        ),
        Collections.singletonList(
            new InstrumentPart(Arrays.asList(new NewMelody(new Rest(HALF_NOTE))), 0)
        )
        )
    ));
  }

  @Test
  public void adjustToUnifiedEndTimeTest() {
    List<InstrumentPart> instrumentParts = Arrays.asList(
        new InstrumentPart(new Note(C1, HALF_NOTE)),
        new InstrumentPart(new Note(C5, WHOLE_NOTE))
    );

    compositionSlicer.adjustToUnifiedEndTime(instrumentParts);

    assertThat(instrumentParts, is(Arrays.asList(
        new InstrumentPart(Arrays.asList(new NewMelody(new Note(C1, HALF_NOTE), new Rest(HALF_NOTE)))),
        new InstrumentPart(new Note(C5, WHOLE_NOTE))
        )
    ));
  }
}
