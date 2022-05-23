package ru.pavelyurkin.musiccomposer.core.equality.equalityMetric;

import static jm.JMC.DOTTED_QUARTER_NOTE;
import static jm.JMC.EIGHTH_NOTE;
import static jm.JMC.HALF_NOTE;
import static jm.JMC.QUARTER_NOTE;
import static jm.JMC.SIXTEENTH_NOTE;
import static jm.JMC.WHOLE_NOTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.pavelyurkin.musiccomposer.core.utils.Utils.isEquals;

import java.util.List;
import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

public class MelodyMetricEqualityAnalyzerTest extends AbstractSpringTest {

  @Autowired
  private MelodyMetricEqualityAnalyzer melodyMetricEqualityAnalyzer;

  @Test
  public void getStrongTimeMetric() throws Exception {
    assertEquals(List.of(true), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(WHOLE_NOTE)));
    assertEquals(List.of(true, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(HALF_NOTE, HALF_NOTE)));
    assertEquals(List.of(true, false, true, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE)));
    assertEquals(List.of(true, true, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(HALF_NOTE, QUARTER_NOTE, QUARTER_NOTE)));
    assertEquals(List.of(true, false, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(QUARTER_NOTE, QUARTER_NOTE, HALF_NOTE)));
    assertEquals(List.of(true, true, false, true, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(HALF_NOTE, EIGHTH_NOTE, EIGHTH_NOTE, EIGHTH_NOTE, EIGHTH_NOTE)));
    assertEquals(List.of(true, false, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(DOTTED_QUARTER_NOTE, EIGHTH_NOTE, HALF_NOTE)));
    assertEquals(List.of(true, false, true, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(DOTTED_QUARTER_NOTE, EIGHTH_NOTE, EIGHTH_NOTE, EIGHTH_NOTE)));
    assertEquals(List.of(true, false, true, false, true, false, false),
        melodyMetricEqualityAnalyzer.getStrongTimeMetric(
            List.of(EIGHTH_NOTE, EIGHTH_NOTE, SIXTEENTH_NOTE, SIXTEENTH_NOTE, SIXTEENTH_NOTE, SIXTEENTH_NOTE,
                HALF_NOTE)));

    assertEquals(List.of(true, false, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE, QUARTER_NOTE)));
    assertEquals(List.of(true, false, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE, WHOLE_NOTE)));
    assertEquals(List.of(true, false, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE, HALF_NOTE)));
    assertEquals(List.of(true, false, true, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE, 2 * WHOLE_NOTE)));
    assertEquals(List.of(true, false, true, false, true, false, true, false, true, false),
        melodyMetricEqualityAnalyzer.getStrongTimeMetric(
            List.of(QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE,
                QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE,
                QUARTER_NOTE, 2 * WHOLE_NOTE)));

    assertEquals(List.of(true, false, true, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(EIGHTH_NOTE, DOTTED_QUARTER_NOTE, EIGHTH_NOTE, DOTTED_QUARTER_NOTE)));
    assertEquals(List.of(true, true, false, true, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(WHOLE_NOTE, EIGHTH_NOTE, DOTTED_QUARTER_NOTE, EIGHTH_NOTE, DOTTED_QUARTER_NOTE)));
    assertEquals(List.of(true, false, false, false), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
        List.of(DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE)));
  }

  @Test
  @Disabled("enable when melody equality metric will be fixed")
  public void getEqualityMetric() throws Exception {
    InstrumentPart etalon = new InstrumentPart(
        new Note(60, HALF_NOTE),
        new Note(65, HALF_NOTE));
    assertTrue(isEquals(melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, etalon), 1));

    // very little pitch change
    InstrumentPart melody0 = new InstrumentPart(new Note(60, HALF_NOTE), new Note(64, HALF_NOTE));
    // pitch change
    InstrumentPart melody1 = new InstrumentPart(new Note(60, HALF_NOTE), new Note(62, HALF_NOTE));
    // change direction (big change)
    InstrumentPart melody2 = new InstrumentPart(new Note(60, HALF_NOTE), new Note(58, HALF_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody0) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody1)
    );
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody1) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody2)
    );

    // melism first note weak time
    InstrumentPart melody3 = new InstrumentPart(
        new Note(60, EIGHTH_NOTE), new Note(62, EIGHTH_NOTE), new Note(60, QUARTER_NOTE),
        new Note(65, HALF_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody3) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody0)
    );
    // melism first note strong time
    InstrumentPart melody4 = new InstrumentPart(
        new Note(60, QUARTER_NOTE), new Note(62, EIGHTH_NOTE), new Note(60, EIGHTH_NOTE),
        new Note(65, HALF_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody3) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody4)
    );
    // melism both notes notes strong time
    InstrumentPart melody5 = new InstrumentPart(
        new Note(60, EIGHTH_NOTE), new Note(62, EIGHTH_NOTE), new Note(60, QUARTER_NOTE),
        new Note(65, EIGHTH_NOTE), new Note(64, EIGHTH_NOTE), new Note(65, QUARTER_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody4) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody5)
    );
    // pauses weak time
    InstrumentPart melody6 = new InstrumentPart(
        new Note(60, QUARTER_NOTE), new Rest(QUARTER_NOTE),
        new Note(65, QUARTER_NOTE), new Rest(QUARTER_NOTE));
    // pauses strong time
    InstrumentPart melody7 = new InstrumentPart(
        new Rest(QUARTER_NOTE), new Note(60, QUARTER_NOTE),
        new Rest(QUARTER_NOTE), new Note(65, QUARTER_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody6) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody7)
    );

    //split first note into 3
    InstrumentPart melody10 = new InstrumentPart(
        new Note(60, QUARTER_NOTE), new Note(63, EIGHTH_NOTE), new Note(64, EIGHTH_NOTE),
        new Note(65, HALF_NOTE));
    //split first note into 4
    InstrumentPart melody11 = new InstrumentPart(
        new Note(60, EIGHTH_NOTE), new Note(59, EIGHTH_NOTE), new Note(62, EIGHTH_NOTE), new Note(63, EIGHTH_NOTE),
        new Note(65, HALF_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody10) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody11)
    );

    //One note longer than first
    InstrumentPart melody12 = new InstrumentPart(
        new Note(60, EIGHTH_NOTE), new Note(59, DOTTED_QUARTER_NOTE),
        new Note(65, HALF_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody12) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody1)
    );

    //Change first note (strong time)
    InstrumentPart melody13 = new InstrumentPart(
        new Note(58, EIGHTH_NOTE), new Note(60, DOTTED_QUARTER_NOTE),
        new Note(65, HALF_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody13) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody1)
    );

    //2 notes change
    InstrumentPart melody14 = new InstrumentPart(
        new Note(56, QUARTER_NOTE), new Note(60, QUARTER_NOTE),
        new Note(64, HALF_NOTE));
    assertTrue(
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody13) >
        melodyMetricEqualityAnalyzer.getEqualityMetric(etalon, melody14)
    );
  }

  @Test
  public void transformMelodyToNewRhythmValues() throws Exception {
    List<Double> newRhythmValues = List.of(
        0.25,
        0.25,
        0.5,
        0.33333333333333326,
        0.16666666666666674,
        2.5
    );
    List<Note> notes = List.of(
        new Note(72, 0.25),
        new Note(71, 0.25),
        new Note(72, 0.8333333333333333),
        new Rest(2.6666666666666665)
    );
    // asserting no exception
    melodyMetricEqualityAnalyzer.transformMelodyToNewRhythmValues(List.of(new NewMelody(notes)), newRhythmValues);
  }

}