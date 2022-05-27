package ru.pavelyurkin.musiccomposer.core.service.decomposer;

import static jm.JMC.WHOLE_NOTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.List;
import jm.JMC;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.service.composition.loader.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

public class CompositionDecomposerTest extends AbstractSpringTest {

  @Autowired
  private CompositionLoader compositionLoader;
  @Autowired
  private CompositionDecomposer compositionDecomposer;

  @Test
  public void singleVoiceMelodyTest() {
    Lexicon lexicon = compositionDecomposer.decompose(
        compositionLoader.getComposition(new File(this.getClass().getResource("gen_1.mid").getFile())), JMC.WHOLE_NOTE);
    if (ModelUtils.isTimeCorrelated(1.1, 1.0)) {
      //	if time correlation is disabled
      assertEquals(6, lexicon.getComposeBlocks().size());
      lexicon.getComposeBlocks().forEach(composeBlock -> {
        if (composeBlock.getPreviousBlockEndPitches().isPresent()) {
          assertEquals(6, composeBlock.getPossiblePreviousComposeBlocks().size());
          assertEquals(5, composeBlock.getPossibleNextComposeBlocks().size());
        } else {
          assertEquals(0, composeBlock.getPossiblePreviousComposeBlocks().size());
          assertEquals(5, composeBlock.getPossibleNextComposeBlocks().size());
        }
      });
    } else {
      //	if time correlation is enabled
      assertEquals(11, lexicon.getComposeBlocks().size());
      lexicon.getComposeBlocks().forEach(composeBlock -> {
        if (composeBlock.getPreviousBlockEndPitches().isPresent()) {
          if (composeBlock.getPossiblePreviousComposeBlocks().contains(lexicon.get(0))) {
            assertEquals(6, composeBlock.getPossiblePreviousComposeBlocks().size());
          } else {
            assertEquals(5, composeBlock.getPossiblePreviousComposeBlocks().size());
          }
          assertEquals(5, composeBlock.getPossibleNextComposeBlocks().size());
        } else {
          assertEquals(0, composeBlock.getPossiblePreviousComposeBlocks().size());
          assertEquals(5, composeBlock.getPossibleNextComposeBlocks().size());
        }
      });
    }
  }

  @Test
  public void validBlockPossibleSurroundingTest() {
    List<Composition> compositionList = compositionLoader
        .getCompositionsFromFolder(new File("src/test/resources/simpleMelodies"), Collections.<String>emptyList());
    Lexicon lexicon = compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
    for (ComposeBlock composeBlock : lexicon.getComposeBlocks()) {
      boolean isFirst = composeBlock.getPossiblePreviousComposeBlocks().isEmpty()
                        && composeBlock.getPossibleNextComposeBlocks().size() >= 1;
      boolean isLast = composeBlock.getPossiblePreviousComposeBlocks().size() >= 1
                       && composeBlock.getPossibleNextComposeBlocks().isEmpty();
      boolean isMiddle = composeBlock.getPossiblePreviousComposeBlocks().size() >= 1
                         && composeBlock.getPossibleNextComposeBlocks().size() >= 1;
      assertTrue(isFirst || isLast || isMiddle);
    }
  }

  @Test
  public void possibleNextComposeBlocksTest() {
    List<Composition> compositionList = compositionLoader
        .getCompositionsFromFolder(new File("src/test/resources/simpleMelodies"), Collections.emptyList());
    Lexicon lexicon = compositionDecomposer.decompose(compositionList, WHOLE_NOTE);

    ComposeBlock firstComposeBlock = null;
    ComposeBlock secondPossibleComposeBlock = null;
    for (ComposeBlock composeBlock : lexicon.getComposeBlocks()) {
      if (composeBlock.getStartTime() == 8.0 && composeBlock.getCompositionInfo().getTitle().contains("first")) {
        firstComposeBlock = composeBlock;
      }
      if (composeBlock.getStartTime() == 8.5 && composeBlock.getCompositionInfo().getTitle().contains("second")) {
        secondPossibleComposeBlock = composeBlock;
      }
    }
    assertNotNull(firstComposeBlock);
    assertNotNull(secondPossibleComposeBlock);

    List<ComposeBlock> listOfPossibleMusicBlocks = firstComposeBlock.getPossibleNextComposeBlocks();
    assertTrue(listOfPossibleMusicBlocks.contains(secondPossibleComposeBlock));
  }
}
