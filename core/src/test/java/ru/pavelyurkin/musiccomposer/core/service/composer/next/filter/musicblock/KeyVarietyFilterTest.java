package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock;

import static jm.constants.Pitches.A1;
import static jm.constants.Pitches.AF1;
import static jm.constants.Pitches.B1;
import static jm.constants.Pitches.BF1;
import static jm.constants.Pitches.C1;
import static jm.constants.Pitches.CS1;
import static jm.constants.Pitches.D1;
import static jm.constants.Pitches.DS1;
import static jm.constants.Pitches.E1;
import static jm.constants.Pitches.EF1;
import static jm.constants.Pitches.F1;
import static jm.constants.Pitches.G1;
import static jm.constants.Pitches.GF1;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import jm.music.data.Note;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.key.KeyVarietyFilter;

public class KeyVarietyFilterTest {

  private KeyVarietyFilter keyVarietyFilter;

  @Test
  public void returnTrueOnFirstBlocksIfItFitsCurrentKey() {
    keyVarietyFilter = new KeyVarietyFilter(0, 8);
    keyVarietyFilter.setCurrentKey(Key.C_MAJOR);
    assertTrue(keyVarietyFilter.filterIt(getTestMusicBlock(List.of(C1, D1, E1)), List.of()));
    assertFalse(keyVarietyFilter.filterIt(getTestMusicBlock(List.of(C1, CS1)), List.of()));
  }

  @Test
  public void returnTrueIfNumberOfNotesOutOfKeyIsLessOrEqualThanMaxNumber() {
    keyVarietyFilter = new KeyVarietyFilter(1, 7);

    assertTrue(keyVarietyFilter.filterIt(getTestMusicBlock(List.of(C1, C1, CS1)),
        List.of(getTestMusicBlock(List.of(C1, D1, E1, F1, G1, A1, B1)))));

    assertTrue(keyVarietyFilter.filterIt(getTestMusicBlock(List.of(C1, C1, BF1, EF1, GF1)),
        // filter should not count frist two cause they are out of 7
        List.of(getTestMusicBlock(List.of(B1, E1, C1, D1, EF1, F1, G1, A1, BF1)))));

    assertTrue(new KeyVarietyFilter(0, 7)
        .filterIt(getTestMusicBlock(List.of(C1, C1, D1)),
            List.of(getTestMusicBlock(List.of(C1, D1, E1, F1, G1, A1, B1)))));

    assertTrue(new KeyVarietyFilter(0, 4)
        .filterIt(getTestMusicBlock(List.of(BF1)),
            List.of(getTestMusicBlock(List.of(B1, C1, D1, E1, F1, G1, A1)))));
  }

  @Test
  public void returnFalseIfNumberOfNotesOutOfKeyIsGreaterThanMax() {
    keyVarietyFilter = new KeyVarietyFilter(1, 7);

    assertFalse(keyVarietyFilter.filterIt(getTestMusicBlock(List.of(C1, C1, EF1, BF1)),
        List.of(getTestMusicBlock(List.of(C1, D1, E1, F1, G1, A1, B1)))));

    assertFalse(keyVarietyFilter.filterIt(getTestMusicBlock(List.of(C1, C1, BF1, GF1, AF1)),
        // filter should not count first two cause they are out of 7
        List.of(getTestMusicBlock(List.of(B1, E1, C1, D1, EF1, F1, G1, A1, BF1)))));

    assertFalse(new KeyVarietyFilter(0, 7)
        .filterIt(getTestMusicBlock(List.of(C1, C1, CS1)),
            List.of(getTestMusicBlock(List.of(C1, D1, E1, F1, G1, A1, B1)))));
  }

  @Test
  public void returnFalseIfNoKeyCanBeCalculatedFromPreviousMusicBlocks() {
    assertFalse(new KeyVarietyFilter(0, 7)
        .filterIt(getTestMusicBlock(List.of(C1, C1, CS1)),
            List.of(getTestMusicBlock(List.of(C1, CS1, D1, DS1, E1, F1, G1)))));
  }

  private MusicBlock getTestMusicBlock(List<Integer> pitches) {
    return new MusicBlock(0.0, List.of(new InstrumentPart(
        pitches.stream().map(pitch -> new Note(pitch, 1.0)).collect(Collectors.toList())
            .toArray(new Note[] {}))), null);
  }
}
