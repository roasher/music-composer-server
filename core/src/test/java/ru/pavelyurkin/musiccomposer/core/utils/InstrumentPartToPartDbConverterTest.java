package ru.pavelyurkin.musiccomposer.core.utils;

import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.C3;
import static jm.constants.Pitches.D4;
import static jm.constants.Pitches.D5;
import static jm.constants.Pitches.G3;

import java.util.Arrays;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Rest;
import jm.music.data.Score;
import jm.util.View;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;
import ru.pavelyurkin.musiccomposer.core.service.composer.InstrumentPartToPartConverter;
import ru.pavelyurkin.musiccomposer.core.service.composition.CompositionParser;

@ExtendWith(MockitoExtension.class)
public class InstrumentPartToPartDbConverterTest {

  @InjectMocks
  private InstrumentPartToPartConverter converter;

  @Mock
  private CompositionParser compositionParser;

  @Test
  @Disabled("Test by eye")
  public void testConvert() throws Exception {
    InstrumentPart instrumentPart = new InstrumentPart(Arrays.asList(
        new NewMelody(new Note(C3, QUARTER_NOTE), new Rest(QUARTER_NOTE)),
        new Chord(Arrays.asList(G3, D4), HALF_NOTE),
        new NewMelody(new Note(D5, QUARTER_NOTE))
    ));

    Part convert = converter.convert(instrumentPart);

    Score score = new Score(convert);
    View.notate(score);
    Utils.suspend();
  }
}
