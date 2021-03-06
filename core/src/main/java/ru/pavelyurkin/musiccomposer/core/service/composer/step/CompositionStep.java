package ru.pavelyurkin.musiccomposer.core.service.composer.step;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

/**
 * Class represents step that program makes in order to create new form block
 * One step - one added originComposeBlock to the form block
 */
@Data
public class CompositionStep {

  /**
   * Compose Block that is base for current composition step
   */
  private ComposeBlock originComposeBlock;
  /**
   * Compose Block that is part of composed composition
   */
  private MusicBlock transposedBlock;

  /**
   * Valid Music Blocks which can come next to this, but their usage leads to dead end in future cause of small lexicon.
   */
  private List<ComposeBlock> nextMusicBlockExclusions = new ArrayList<>();

  public CompositionStep(ComposeBlock originComposeBlock, MusicBlock transposedBlock) {
    this.originComposeBlock = originComposeBlock;
    this.transposedBlock = transposedBlock;
  }

  public static CompositionStep getEmptyCompositionStep() {
    return new CompositionStep(null, null);
  }

  public static CompositionStep getEmptyCompositionStep(List<ComposeBlock> nextMusicBlockExclusions) {
    CompositionStep compositionStep = getEmptyCompositionStep();
    compositionStep.setNextMusicBlockExclusions(nextMusicBlockExclusions);
    return compositionStep;
  }

  public void addNextExclusion(ComposeBlock musicBlock) {
    this.nextMusicBlockExclusions.add(musicBlock);
  }

}
