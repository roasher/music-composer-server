package ru.pavelyurkin.musiccomposer.core.utils;

import static jm.constants.Durations.DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE_TRIPLET;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.QUARTER_NOTE_TRIPLET;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Durations.THIRTYSECOND_NOTE_TRIPLET;
import static jm.constants.Durations.WHOLE_NOTE;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;

/**
 * Created by pyurkin on 08.12.14.
 */
public class RhythmValueHandlerTest extends AbstractSpringTest {

  @Autowired
  private RhythmValueHandler rhythmValueHandler;

  @Test
  public void roundRhythmTest() {

    assertEquals(rhythmValueHandler.roundRhythmValue(0.9833333333333 + WHOLE_NOTE * 4), QUARTER_NOTE + WHOLE_NOTE * 4);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.5166666666666 + WHOLE_NOTE * 4), EIGHTH_NOTE + WHOLE_NOTE * 4);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.4833333333333), EIGHTH_NOTE);
    assertEquals(rhythmValueHandler.roundRhythmValue(1.4833333333333), DOTTED_QUARTER_NOTE);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.49999999999999906), EIGHTH_NOTE);

    assertEquals(rhythmValueHandler.roundRhythmValue(0.01666666666667993), 0.);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.083), THIRTYSECOND_NOTE_TRIPLET);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.043), THIRTYSECOND_NOTE_TRIPLET);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.041), 0.);

    assertEquals(rhythmValueHandler.roundRhythmValue(0.34), EIGHTH_NOTE_TRIPLET);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.32), EIGHTH_NOTE_TRIPLET);

    // WTF ??? EIGHTH_NOTE_TRIPLET + WHOLE_NOTE * 11 = 44.333333333333336 o_O
    assertEquals(rhythmValueHandler.roundRhythmValue(0.34 + WHOLE_NOTE * 11), EIGHTH_NOTE_TRIPLET + WHOLE_NOTE * 11);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.32 + WHOLE_NOTE * 10), EIGHTH_NOTE_TRIPLET + WHOLE_NOTE * 10);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.333333333302 + WHOLE_NOTE * 5),
        EIGHTH_NOTE_TRIPLET + WHOLE_NOTE * 5);

    assertEquals(rhythmValueHandler.roundRhythmValue(0.9833333333333333), 1.);
    assertEquals(rhythmValueHandler.roundRhythmValue(0.9999999999999999), 1.);

    assertEquals(rhythmValueHandler.roundRhythmValue(1.25), QUARTER_NOTE + SIXTEENTH_NOTE);
  }

  @Test
  public void approximateValueTest() {
    assertEquals(rhythmValueHandler.getApproximatedValue(0.9833333333333), QUARTER_NOTE);
    assertEquals(rhythmValueHandler.getApproximatedValue(0.5166666666666), EIGHTH_NOTE);
    assertEquals(rhythmValueHandler.getApproximatedValue(0.4833333333333), EIGHTH_NOTE);
    assertEquals(rhythmValueHandler.getApproximatedValue(1.483333333333), DOTTED_QUARTER_NOTE);
    assertEquals(rhythmValueHandler.getApproximatedValue(0.49999999999999906), EIGHTH_NOTE);

    assertEquals(rhythmValueHandler.getApproximatedValue(1.0166666666666657), QUARTER_NOTE);
    assertEquals(rhythmValueHandler.getApproximatedValue(0.999999999999999), QUARTER_NOTE);
    assertEquals(rhythmValueHandler.getApproximatedValue(0.9875), QUARTER_NOTE);
    assertEquals(rhythmValueHandler.getApproximatedValue(1.5000000000000029), DOTTED_QUARTER_NOTE);
    assertEquals(rhythmValueHandler.getApproximatedValue(1.4875), DOTTED_QUARTER_NOTE);

    assertEquals(rhythmValueHandler.getApproximatedValue(0.666664), QUARTER_NOTE_TRIPLET);
    assertEquals(rhythmValueHandler.getApproximatedValue(0.665), QUARTER_NOTE_TRIPLET);

    assertEquals(rhythmValueHandler.getApproximatedValue(0.34), EIGHTH_NOTE_TRIPLET);
    assertEquals(rhythmValueHandler.getApproximatedValue(0.32), EIGHTH_NOTE_TRIPLET);
    assertEquals(rhythmValueHandler.getApproximatedValue(0.333333333302), EIGHTH_NOTE_TRIPLET);

    assertEquals(rhythmValueHandler.getApproximatedValue(1.25), QUARTER_NOTE + SIXTEENTH_NOTE);
  }
}
