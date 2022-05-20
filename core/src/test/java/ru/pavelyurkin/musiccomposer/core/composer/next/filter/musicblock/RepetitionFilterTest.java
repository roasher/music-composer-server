package ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock;

import static jm.constants.Durations.DOTTED_HALF_NOTE;
import static jm.constants.Durations.DOTTED_QUARTER_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.C1;
import static jm.constants.Pitches.C2;
import static jm.constants.Pitches.C3;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import jm.music.data.Note;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

public class RepetitionFilterTest {

  private RepetitionFilter composeBlockRepetitionFilter = new RepetitionFilter();

  @Test
  public void filterOutRepetitionsLiveExample() {
    MusicBlock firstBlock = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(69, 0.500)),
        new InstrumentPart(new Note(64, 0.500)),
        new InstrumentPart(new Note(60, 0.500)),
        new InstrumentPart(new Note(45, 0.500))),
        null);
    List<MusicBlock> blocks = Arrays.asList(firstBlock,
        new MusicBlock(0, Arrays
            .asList(new InstrumentPart(new Note(69, 0.500)), new InstrumentPart(new Note(64, 0.500)),
                new InstrumentPart(new Note(59, 0.500)), new InstrumentPart(new Note(45, 0.500))), null),
        new MusicBlock(0, Arrays
            .asList(new InstrumentPart(new Note(69, 0.500)), new InstrumentPart(new Note(66, 0.500)),
                new InstrumentPart(new Note(57, 0.500)), new InstrumentPart(new Note(50, 0.500))), null),
        new MusicBlock(0, Arrays
            .asList(new InstrumentPart(new Note(69, 0.500)), new InstrumentPart(new Note(66, 0.500)),
                new InstrumentPart(new Note(56, 0.500)), new InstrumentPart(new Note(50, 0.500))), null),
        new MusicBlock(0, Arrays
            .asList(new InstrumentPart(new Note(71, 0.500)), new InstrumentPart(new Note(66, 0.500)),
                new InstrumentPart(new Note(54, 0.500)), new InstrumentPart(new Note(51, 0.500))), null),
        new MusicBlock(0, Arrays
            .asList(new InstrumentPart(new Note(71, 0.500)), new InstrumentPart(new Note(66, 0.500)),
                new InstrumentPart(new Note(66, 0.500)), new InstrumentPart(new Note(51, 0.500))), null),
        new MusicBlock(0, Arrays
            .asList(new InstrumentPart(new Note(71, 0.500)), new InstrumentPart(new Note(68, 0.500)),
                new InstrumentPart(new Note(64, 0.500)), new InstrumentPart(new Note(52, 0.500))), null),
        new MusicBlock(0, Arrays
            .asList(new InstrumentPart(new Note(71, 0.500)), new InstrumentPart(new Note(69, 0.500)),
                new InstrumentPart(new Note(64, 0.500)), new InstrumentPart(new Note(54, 0.500))), null)
    );

    List<MusicBlock> previousBlocks = new ArrayList<>();
    previousBlocks.addAll(blocks);
    previousBlocks.addAll(blocks);
    previousBlocks.addAll(blocks);

    assertFalse(composeBlockRepetitionFilter.filterIt(firstBlock, previousBlocks));
  }

  @Test
  public void filterOutEighthNoteRepetition() {
    MusicBlock firstBlock =
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(24, EIGHTH_NOTE))), null);
    List<MusicBlock> blocks = Arrays.asList(firstBlock,
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(25, EIGHTH_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(26, EIGHTH_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(27, EIGHTH_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(28, EIGHTH_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(29, EIGHTH_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(30, EIGHTH_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(31, EIGHTH_NOTE))), null));

    List<MusicBlock> previousBlocks = new ArrayList<>();
    previousBlocks.addAll(blocks);
    previousBlocks.addAll(blocks);
    previousBlocks.addAll(blocks);

    assertFalse(composeBlockRepetitionFilter.filterIt(firstBlock, previousBlocks));
  }

  // getRepetitions tests
  @Test
  public void repetitionsInOnePitchBlocks() throws Exception {
    List<MusicBlock> onePitchBlock = Arrays.asList(
        new MusicBlock(0,
            Collections.singletonList(new InstrumentPart(new Note(C1, EIGHTH_NOTE), new Note(C1, EIGHTH_NOTE))), null),
        new MusicBlock(0,
            Collections.singletonList(new InstrumentPart(new Note(C1, EIGHTH_NOTE), new Note(C1, EIGHTH_NOTE))), null),
        new MusicBlock(0,
            Collections.singletonList(new InstrumentPart(new Note(C1, EIGHTH_NOTE), new Note(C1, EIGHTH_NOTE))), null),
        new MusicBlock(0,
            Collections.singletonList(new InstrumentPart(new Note(C1, EIGHTH_NOTE), new Note(C1, EIGHTH_NOTE))), null),
        new MusicBlock(0,
            Collections.singletonList(new InstrumentPart(new Note(C1, EIGHTH_NOTE), new Note(C1, EIGHTH_NOTE))), null),
        new MusicBlock(0,
            Collections.singletonList(new InstrumentPart(new Note(C1, EIGHTH_NOTE), new Note(C1, EIGHTH_NOTE))), null));
    Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions(onePitchBlock, 2 * WHOLE_NOTE);
    assertThat(repetitions0.size(), is(3));
    assertThat(repetitions0.get(QUARTER_NOTE), is(6));
    assertThat(repetitions0.get(HALF_NOTE), is(3));
    assertThat(repetitions0.get(DOTTED_HALF_NOTE), is(2));
  }

  @Test
  public void repetitionsOfThreeNotes() throws Exception {
    List<MusicBlock> blocks = Arrays.asList(
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C3, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C3, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C3, QUARTER_NOTE))), null));

    Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions(blocks, 2 * WHOLE_NOTE);
    assertThat(repetitions0.size(), is(1));
    assertThat(repetitions0.get(DOTTED_HALF_NOTE), is(3));
  }

  @Test
  public void zeroRepetitionsOfTwoNotesIfWrongRhythmValue() throws Exception {
    List<MusicBlock> blocks = Arrays.asList(
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, DOTTED_QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null));
    Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions(blocks, 2 * WHOLE_NOTE);
    assertThat(repetitions0.size(), is(0));
  }

  @Test
  public void repetitionsOfTwoNotesOutOfBounds() throws Exception {
    List<MusicBlock> blocks = Arrays.asList(
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null),

        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C1, QUARTER_NOTE))), null),
        new MusicBlock(0, Collections.singletonList(new InstrumentPart(new Note(C2, QUARTER_NOTE))), null));

    Map<Double, Integer> repetitions0 = composeBlockRepetitionFilter.getRepetitions(blocks, 2 * WHOLE_NOTE);

    assertThat(repetitions0.size(), is(3));
    assertThat(repetitions0.get(HALF_NOTE), is(7));
    assertThat(repetitions0.get(WHOLE_NOTE), is(3));
    assertThat(repetitions0.get(WHOLE_NOTE + HALF_NOTE), is(2));
  }

}