package ru.pavelyurkin.musiccomposer.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
/**
 * Class represents blocks which can be used in composing process
 * Created by pyurkin on 05.03.2015.
 */
public class ComposeBlock {

  @Delegate
  private MusicBlock musicBlock;

  // Lists of possible next and previous Compose Blocks that has convenient voice leading in the original composition
  // We are assuming that first member of list is the original composition member - so all blocks except firsts and
  // lasts will have at least one member in the list
  private List<ComposeBlock> possibleNextComposeBlocks = new ArrayList<>();
  private List<ComposeBlock> possiblePreviousComposeBlocks = new ArrayList<>();

  public ComposeBlock(MusicBlock musicBlock) {
    this.musicBlock = musicBlock;
  }

  public ComposeBlock(List<ComposeBlock> composeBlockList) {
    this.musicBlock = new MusicBlock(composeBlockList
        .stream()
        .map(ComposeBlock::getMusicBlock)
        .collect(Collectors.toList())
    );
    this.possiblePreviousComposeBlocks = composeBlockList.get(0).getPossiblePreviousComposeBlocks();
    this.possibleNextComposeBlocks = composeBlockList.get(composeBlockList.size() - 1).getPossibleNextComposeBlocks();
  }

  public boolean hasEqualsMusicBlock(ComposeBlock composeBlock) {
    return this.musicBlock.equals(composeBlock.getMusicBlock());
  }

  @Override
  public String toString() {
    return this.musicBlock.toString();
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (!(o instanceof ComposeBlock)) {
          return false;
      }

    ComposeBlock that = (ComposeBlock) o;

      if (!this.hasEqualsMusicBlock(that)) {
          return false;
      }

    if (!isEquals(this.possibleNextComposeBlocks, that.possibleNextComposeBlocks)) {
      return false;
    }
    if (!isEquals(this.possiblePreviousComposeBlocks, that.possiblePreviousComposeBlocks)) {
      return false;
    }
    return true;
  }

  /**
   * Two lists considered equals if they have equal size and every entry from one has similar entry from another
   *
   * @param firstComposeBlockList
   * @param secondComposeBlockList
   * @return
   */
  private boolean isEquals(List<ComposeBlock> firstComposeBlockList, List<ComposeBlock> secondComposeBlockList) {
      if (firstComposeBlockList.size() != secondComposeBlockList.size()) {
          return false;
      }

    for (ComposeBlock firstComposeBlock : firstComposeBlockList) {
      boolean isInList = false;
      for (ComposeBlock secondComposeBlock : secondComposeBlockList) {
        if ((firstComposeBlock == null && secondComposeBlock != null) ||
            (firstComposeBlock != null && secondComposeBlock == null)) {
          continue;
        } else if (firstComposeBlock == null && secondComposeBlock == null ||
                   firstComposeBlock.hasEqualsMusicBlock(secondComposeBlock)) {
          isInList = true;
          break;
        }
      }
      if (!isInList) {
        return false;
      }
    }
    return true;
  }
}
