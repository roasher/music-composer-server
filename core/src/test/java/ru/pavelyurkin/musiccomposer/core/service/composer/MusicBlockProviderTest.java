package ru.pavelyurkin.musiccomposer.core.service.composer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

public class MusicBlockProviderTest {

  private MusicBlockProvider musicBlockProvider = new MusicBlockProvider();

  @Test
  public void getAllPossibleNextMapTest() {

    List<MusicBlock> musicBlocks = getMusicBlockMocks();

    MusicBlockProvider mockProvider = Mockito.spy(musicBlockProvider);

    doReturn(false).when(mockProvider).isPossibleNext(any(MusicBlock.class), any(MusicBlock.class));
    doReturn(true).when(mockProvider).isPossibleNext(musicBlocks.get(0), musicBlocks.get(2));
    doReturn(true).when(mockProvider).isPossibleNext(musicBlocks.get(1), musicBlocks.get(1));
    doReturn(true).when(mockProvider).isPossibleNext(musicBlocks.get(0), musicBlocks.get(3));
    doReturn(true).when(mockProvider).isPossibleNext(musicBlocks.get(2), musicBlocks.get(1));

    Map<Integer, Set<Integer>> allPossibleNextVariants = mockProvider.getAllPossibleNextVariants(musicBlocks);

    assertEquals(musicBlocks.size(), allPossibleNextVariants.size());
    assertThat(allPossibleNextVariants.get(0), containsInAnyOrder(2, 3));
    assertThat(allPossibleNextVariants.get(1), containsInAnyOrder(1));
    assertThat(allPossibleNextVariants.get(2), containsInAnyOrder(1));
    assertThat(allPossibleNextVariants.get(3), hasSize(0));
    assertThat(allPossibleNextVariants.get(4), hasSize(0));
  }

  private List<MusicBlock> getMusicBlockMocks() {

    MusicBlock musicBlock0 = mock(MusicBlock.class);
    MusicBlock musicBlock1 = mock(MusicBlock.class);
    MusicBlock musicBlock2 = mock(MusicBlock.class);
    MusicBlock musicBlock3 = mock(MusicBlock.class);
    MusicBlock musicBlock4 = mock(MusicBlock.class);

    List<MusicBlock> musicBlockList = new ArrayList<>();
    musicBlockList.add(musicBlock0);
    musicBlockList.add(musicBlock1);
    musicBlockList.add(musicBlock2);
    musicBlockList.add(musicBlock3);
    musicBlockList.add(musicBlock4);

    return musicBlockList;
  }

  @Test
  public void isPossibleNextIfPreviousWasRests() throws Exception {
    MusicBlock musicBlock = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(76, 1)),
        new InstrumentPart(new Note(72, 1)),
        new InstrumentPart(new Note(69, 1)),
        new InstrumentPart(new Note(57, 1))
    ), null, Arrays.asList(Note.REST));
    assertFalse(musicBlockProvider.isPossibleNext(musicBlock, musicBlock));
  }

  @Test
  public void isPossibleNextIfPreviousPitchesAreParallel() throws Exception {
    MusicBlock currentMusicBlock = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(76, 1)),
        new InstrumentPart(new Note(72, 1)),
        new InstrumentPart(new Rest(1)),
        new InstrumentPart(new Note(57, 1))
    ), null);
    MusicBlock possibleNext = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(76, 1))
    ), null, Arrays.asList(76 + 10, 72 + 10, Note.REST, 57 + 10));
    assertTrue(musicBlockProvider.isPossibleNext(currentMusicBlock, possibleNext));
  }

  @Test
  public void isPossibleNextIfPreviousPitchesAreParallelWithChords() throws Exception {
    MusicBlock currentMusicBlock = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(10, 1)),
        new InstrumentPart(Arrays.asList(
            new Chord(Arrays.asList(1, 2, 3), 0.5),
            new NewMelody(new Note(11, 0.5, 5)))
        ),
        new InstrumentPart(new Rest(1)),
        new InstrumentPart(Arrays.asList(
            new NewMelody(new Note(72, 0.5, 5)),
            new Chord(Arrays.asList(12, 13, 14), 0.5))
        )
    ), null);
    MusicBlock possibleNext = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(15, 1))
    ), null, Arrays.asList(10 - 1, 11 - 1, Note.REST, 12 - 1, 13 - 1, 14 - 1));
    assertTrue(musicBlockProvider.isPossibleNext(currentMusicBlock, possibleNext));
  }

  @Test
  public void canNotBeNextIfNotParallel() throws Exception {
    MusicBlock currentMusicBlock = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(76, 1)),
        new InstrumentPart(new Rest(1)),
        new InstrumentPart(new Note(57, 1))
    ), null);
    MusicBlock possibleNext = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(76, 1))
    ), null, Arrays.asList(76 + 10, Note.REST, 57 + 9));
    assertFalse(musicBlockProvider.isPossibleNext(currentMusicBlock, possibleNext));
  }

  @Test
  public void canNotBeNexIfDifferentNumberOfPitches() throws Exception {
    MusicBlock currentMusicBlock = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(76, 1)),
        new InstrumentPart(new Note(72, 1)),
        new InstrumentPart(new Note(57, 1))
    ), null);
    MusicBlock possibleNext = new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(76, 1))
    ), null, Arrays.asList(76 + 10, 72 + 10, Note.REST, 57 + 10));
    assertFalse(musicBlockProvider.isPossibleNext(currentMusicBlock, possibleNext));
  }
}
