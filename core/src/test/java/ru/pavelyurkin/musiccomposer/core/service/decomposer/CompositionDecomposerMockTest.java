package ru.pavelyurkin.musiccomposer.core.service.decomposer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import jm.music.data.Note;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.service.composer.MusicBlockProvider;

@ExtendWith(MockitoExtension.class)
public class CompositionDecomposerMockTest {

  @InjectMocks
  private CompositionDecomposer compositionDecomposer;

  @Mock
  private MusicBlockProvider musicBlockProvider;

  @Test
  public void isTwoLinked() {

    MusicBlock musicBlock0 = mock(MusicBlock.class);
    MusicBlock musicBlock1 = mock(MusicBlock.class);
    MusicBlock musicBlock2 = mock(MusicBlock.class);
    MusicBlock musicBlock3 = mock(MusicBlock.class);

    List<MusicBlock> inputMusicBlock = new ArrayList<MusicBlock>();
    inputMusicBlock.add(musicBlock0);
    inputMusicBlock.add(musicBlock1);
    inputMusicBlock.add(musicBlock2);
    inputMusicBlock.add(musicBlock3);

    Map<Integer, Set<Integer>> map = new HashMap<>();
    map.put(0, Set.of(1, 2));
    map.put(1, Set.of(0, 2, 3));
    map.put(2, Set.of(0, 1));
    map.put(3, Set.of(1));

    when(musicBlockProvider.getAllPossibleNextVariants(any())).thenReturn(map);

    List<ComposeBlock> composeBlockList = compositionDecomposer.getComposeBlocks(inputMusicBlock).getComposeBlocks();

    ComposeBlock composeBlock0 = composeBlockList.get(0);
    ComposeBlock composeBlock1 = composeBlockList.get(1);
    ComposeBlock composeBlock2 = composeBlockList.get(2);
    ComposeBlock composeBlock3 = composeBlockList.get(3);

    assertTrue(composeBlock0.getPossibleNextComposeBlocks().contains(composeBlock1));
    assertTrue(composeBlock0.getPossibleNextComposeBlocks().contains(composeBlock2));
    assertTrue(composeBlock0.getPossiblePreviousComposeBlocks().contains(composeBlock1));
    assertTrue(composeBlock0.getPossiblePreviousComposeBlocks().contains(composeBlock2));

    assertTrue(composeBlock1.getPossibleNextComposeBlocks().contains(composeBlock0));
    assertTrue(composeBlock1.getPossibleNextComposeBlocks().contains(composeBlock2));
    assertTrue(composeBlock1.getPossibleNextComposeBlocks().contains(composeBlock3));
    assertTrue(composeBlock1.getPossiblePreviousComposeBlocks().contains(composeBlock0));
    assertTrue(composeBlock1.getPossiblePreviousComposeBlocks().contains(composeBlock2));
    assertTrue(composeBlock1.getPossiblePreviousComposeBlocks().contains(composeBlock3));

    assertTrue(composeBlock2.getPossibleNextComposeBlocks().contains(composeBlock0));
    assertTrue(composeBlock2.getPossibleNextComposeBlocks().contains(composeBlock1));
    assertTrue(composeBlock2.getPossiblePreviousComposeBlocks().contains(composeBlock0));
    assertTrue(composeBlock2.getPossiblePreviousComposeBlocks().contains(composeBlock1));

    assertTrue(composeBlock3.getPossibleNextComposeBlocks().contains(composeBlock1));
    assertTrue(composeBlock3.getPossiblePreviousComposeBlocks().contains(composeBlock1));
  }

  @Test
  public void getComposeBlocksTest() {

    MusicBlock musicBlock0 = getMusicTestBlock0(60);
    MusicBlock musicBlock1 = getMusicTestBlock0(61);
    MusicBlock musicBlock2 = getMusicTestBlock0(62);
    MusicBlock musicBlock3 = getMusicTestBlock0(63);
    MusicBlock musicBlock4 = getMusicTestBlock0(64);

    List<MusicBlock> inputMusicBlock = new ArrayList<MusicBlock>();
    inputMusicBlock.add(musicBlock0);
    inputMusicBlock.add(musicBlock1);
    inputMusicBlock.add(musicBlock2);
    inputMusicBlock.add(musicBlock3);
    inputMusicBlock.add(musicBlock4);

    Map<Integer, Set<Integer>> map = new HashMap<>();
    map.put(0, Set.of(1, 3, 4));
    map.put(1, Set.of(0, 4));
    map.put(2, Set.of(0, 3, 4));
    map.put(3, Set.of(0, 2));
    map.put(4, Set.of(0, 1, 2));

    when(musicBlockProvider.getAllPossibleNextVariants(any())).thenReturn(map);

    List<ComposeBlock> composeBlockList = compositionDecomposer.getComposeBlocks(inputMusicBlock).getComposeBlocks();
    assertEquals(inputMusicBlock.size(), composeBlockList.size());

    assertEquals(3, composeBlockList.get(0).getPossibleNextComposeBlocks().size());
    assertEquals(2, composeBlockList.get(1).getPossibleNextComposeBlocks().size());
    assertEquals(3, composeBlockList.get(2).getPossibleNextComposeBlocks().size());
    assertEquals(2, composeBlockList.get(3).getPossibleNextComposeBlocks().size());
    assertEquals(3, composeBlockList.get(4).getPossibleNextComposeBlocks().size());

    assertEquals(4, composeBlockList.get(0).getPossiblePreviousComposeBlocks().size());
    assertEquals(2, composeBlockList.get(1).getPossiblePreviousComposeBlocks().size());
    assertEquals(2, composeBlockList.get(2).getPossiblePreviousComposeBlocks().size());
    assertEquals(2, composeBlockList.get(3).getPossiblePreviousComposeBlocks().size());
    assertEquals(3, composeBlockList.get(4).getPossiblePreviousComposeBlocks().size());

    assertTrue(isValidForwardRoute(composeBlockList, musicBlock0, musicBlock3, musicBlock0, musicBlock3, musicBlock2,
        musicBlock4));
    assertTrue(isValidForwardRoute(composeBlockList, musicBlock1, musicBlock0, musicBlock1, musicBlock4, musicBlock2,
        musicBlock3));
    assertTrue(isValidForwardRoute(composeBlockList, musicBlock2, musicBlock0, musicBlock3));
    assertTrue(isValidForwardRoute(composeBlockList, musicBlock2, musicBlock0, musicBlock3, musicBlock2));
    assertTrue(isValidForwardRoute(composeBlockList, musicBlock4, musicBlock1, musicBlock0, musicBlock3));

    assertFalse(isValidForwardRoute(composeBlockList, musicBlock0, musicBlock2));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock1, musicBlock1));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock1, musicBlock2));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock2, musicBlock1));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock3, musicBlock4));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock4, musicBlock3));

    assertFalse(isValidForwardRoute(composeBlockList, musicBlock0, musicBlock3, musicBlock2, musicBlock1));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock1, musicBlock4, musicBlock3));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock2, musicBlock3, musicBlock1));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock3, musicBlock0, musicBlock3, musicBlock0, musicBlock2,
        musicBlock4));
    assertFalse(isValidForwardRoute(composeBlockList, musicBlock4, musicBlock2, musicBlock1, musicBlock0, musicBlock1));

    assertTrue(isValidBackwardRoute(composeBlockList, musicBlock0, musicBlock3, musicBlock0, musicBlock3, musicBlock2,
        musicBlock4));
    assertTrue(isValidBackwardRoute(composeBlockList, musicBlock1, musicBlock0, musicBlock1, musicBlock4, musicBlock2,
        musicBlock3));
    assertTrue(isValidBackwardRoute(composeBlockList, musicBlock2, musicBlock3, musicBlock2));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock2, musicBlock4, musicBlock0, musicBlock2));
    assertTrue(isValidBackwardRoute(composeBlockList, musicBlock4, musicBlock1, musicBlock0, musicBlock3));

    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock1, musicBlock2));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock1, musicBlock1));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock1, musicBlock3));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock2, musicBlock1));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock3, musicBlock4));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock4, musicBlock3));

    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock0, musicBlock3, musicBlock2, musicBlock1));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock1, musicBlock4, musicBlock3));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock2, musicBlock3, musicBlock1));
    assertFalse(isValidBackwardRoute(composeBlockList, musicBlock3, musicBlock0, musicBlock3, musicBlock0, musicBlock3,
        musicBlock4));
    assertFalse(
        isValidBackwardRoute(composeBlockList, musicBlock4, musicBlock2, musicBlock1, musicBlock0, musicBlock1));
  }

  @NotNull
  private MusicBlock getMusicTestBlock0(int pitch) {
    return new MusicBlock(0, List.of(new InstrumentPart(new Note(pitch, 0))), null);
  }

  private boolean isValidForwardRoute(List<ComposeBlock> composeBlocks, MusicBlock... route) {
    List<ComposeBlock> possibleNexts = composeBlocks;
    for (MusicBlock musicBlock : route) {
      Optional<ComposeBlock> step = possibleNexts.stream()
          // exact math cause music blocks equality might be overwridden
          .filter(composeBlock -> composeBlock.getMusicBlock() == musicBlock)
          .findFirst();
      if (step.isEmpty()) {
        return false;
      } else {
        possibleNexts = step.get().getPossibleNextComposeBlocks();
      }
    }

    return true;
  }

  private boolean isValidBackwardRoute(List<ComposeBlock> composeBlocks, MusicBlock... route) {
    Stack<MusicBlock> stack = new Stack<>();
    Arrays.stream(route).forEach(stack::add);
    return isValidForwardRoute(composeBlocks, stack.toArray(new MusicBlock[] {}));
  }
}
