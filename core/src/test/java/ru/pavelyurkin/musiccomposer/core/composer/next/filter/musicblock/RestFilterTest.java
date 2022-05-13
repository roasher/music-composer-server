package ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock;

import static jm.JMC.C0;
import static jm.JMC.EIGHTH_NOTE;
import static jm.JMC.HALF_NOTE;
import static jm.JMC.HALF_NOTE_TRIPLET;
import static jm.JMC.QUARTER_NOTE;
import static jm.JMC.QUARTER_NOTE_TRIPLET;
import static jm.JMC.WHOLE_NOTE;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

/**
 * Created by wish on 03.02.2016.
 */
public class RestFilterTest {
  @Test
  public void test() {
    RestFilter composeBlockRestFilter = new RestFilter(QUARTER_NOTE);
    assertTrue(composeBlockRestFilter.filterIt(getRestBlock(QUARTER_NOTE), null));
    assertTrue(composeBlockRestFilter.filterIt(getRestBlock(QUARTER_NOTE_TRIPLET), null));
    assertFalse(composeBlockRestFilter.filterIt(getRestBlock(HALF_NOTE), null));
    assertFalse(composeBlockRestFilter.filterIt(getRestBlock(WHOLE_NOTE), null));
    assertTrue(composeBlockRestFilter.filterIt(getRestBlock(EIGHTH_NOTE), null));
    assertTrue(composeBlockRestFilter.filterIt(getNonRestBlock(HALF_NOTE_TRIPLET), null));
    assertTrue(composeBlockRestFilter.filterIt(getNonRestBlock(WHOLE_NOTE), null));
  }

  private MusicBlock getRestBlock(double restRhtyhmValue) {
    return new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Rest(restRhtyhmValue)),
        new InstrumentPart(new Rest(restRhtyhmValue)),
        new InstrumentPart(new Rest(restRhtyhmValue))
    ), null);
  }

  private MusicBlock getNonRestBlock(double rhtyhmValue) {
    return new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Rest(rhtyhmValue)),
        new InstrumentPart(new Rest(rhtyhmValue)),
        new InstrumentPart(new Note(C0, rhtyhmValue))
    ), null);
  }
}
