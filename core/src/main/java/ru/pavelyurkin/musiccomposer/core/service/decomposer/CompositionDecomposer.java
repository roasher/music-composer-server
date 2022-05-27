package ru.pavelyurkin.musiccomposer.core.service.decomposer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.pavelyurkin.musiccomposer.core.service.composer.MusicBlockProvider;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.client.lexicon.LexiconDAO;
import ru.pavelyurkin.musiccomposer.core.service.composition.CompositionParser;
import ru.pavelyurkin.musiccomposer.core.utils.RecombineUtils;

/**
 * Class analyses and decomposes the composition, creating MusicBlocks
 * Created by Pavel Yurkin on 21.07.14.
 */
@RequiredArgsConstructor
@Data
@Slf4j
public class CompositionDecomposer {

  private final CompositionParser compositionParser;
  private final MusicBlockProvider musicBlockProvider;
  private final LexiconDAO lexiconDAO;

  /**
   * Decomposes composition into music block list
   *
   * @param composition
   * @param rhythmValue
   * @return
   */
  public List<MusicBlock> decomposeIntoMusicBlocks(Composition composition, double rhythmValue) {
    log.info("Decomposing composition {}", composition.getTitle());
    // Parsing composition to our model
    List<InstrumentPart> instrumentParts = compositionParser.parse(composition);
    // recombining result melodies into composeBlocks
    List<List<InstrumentPart>> recombineList = RecombineUtils.recombine(instrumentParts);
    // filling composition information
    List<MusicBlock> lexiconMusicBlocks = new ArrayList<MusicBlock>();
    double startTime = 0;
    for (int melodyBlockNumber = 0; melodyBlockNumber < recombineList.size(); melodyBlockNumber++) {
      MusicBlock musicBlock = new MusicBlock();
      musicBlock.setInstrumentParts(recombineList.get(melodyBlockNumber));
      musicBlock.setCompositionInfo(composition.getCompositionInfo());
      musicBlock.setStartTime(startTime);
      // binding with previous Music Block
      if (melodyBlockNumber != 0) {
        MusicBlock previousMusicBlock = lexiconMusicBlocks.get(melodyBlockNumber - 1);
        musicBlock.setPreviousBlockEndPitches(previousMusicBlock.getEndPitches());
      }
      lexiconMusicBlocks.add(musicBlock);

      startTime += musicBlock.getRhythmValue();
    }
    // removing duplicates
    List<MusicBlock> uniqueMusicBlocks = new ArrayList<>();
    lexiconMusicBlocks.forEach(musicBlock -> {
      if (!uniqueMusicBlocks.contains(musicBlock)) {
        uniqueMusicBlocks.add(musicBlock);
      }
    });

    return uniqueMusicBlocks;
  }

  public Lexicon decompose(Composition composition, double rhythmValue) {
    List<Composition> compositionList = new ArrayList<>();
    compositionList.add(composition);
    return decompose(compositionList, rhythmValue);
  }

  /**
   * Wraps Music Blocks into Compose Blocks
   *
   * @param musicBlocks
   * @return
   */
  public Lexicon getComposeBlocks(List<MusicBlock> musicBlocks) {
    log.info("Calculating Lexicon");
    Map<Integer, Set<Integer>> possibleNextMusicBlockNumbers =
        musicBlockProvider.getAllPossibleNextVariants(musicBlocks);
    Lexicon lexicon = new Lexicon(possibleNextMusicBlockNumbers, musicBlocks);
    log.info("Lexicon calculation done.");

    return lexicon;
  }

  /**
   * Decomposes compositions into composeBlocks
   *
   * @param compositionList
   * @param rhythmValue
   * @return
   */
  public Lexicon decompose(List<Composition> compositionList, double rhythmValue) {
    log.info("Getting persisted blocks");
    Lexicon dataBaseLexicon = lexiconDAO.fetch();
    if (dataBaseLexicon.getComposeBlocks().size() != 0) {
      log.info("Fetched Lexicon is NOT empty: {} compose blocks", dataBaseLexicon.getComposeBlocks().size());
    } else {
      log.info("Fetched Lexicon IS empty");
    }

    log.info("Deleting all blocks, build from other than input list compositions");
    trimToCompositions(dataBaseLexicon.getComposeBlocks(), compositionList);

    log.info("Combining blocks from new compositions");
    List<MusicBlock> musicBlockList = new ArrayList<>();
    for (Composition composition : compositionList) {
      if (!dataBaseLexicon.getCompositionsInLexicon().contains(composition.getCompositionInfo())) {
        musicBlockList.addAll(decomposeIntoMusicBlocks(composition, rhythmValue));
      }
    }
    Lexicon lexiconFromComposition = getComposeBlocks(musicBlockList);

    log.info("Combining blocks from compositions and blocks persistance");
    Lexicon combinedLexicon = union(dataBaseLexicon, lexiconFromComposition);

    log.info("At the end of the day got lexicon having {} compose blocks",
        combinedLexicon.getComposeBlocks().size());
    return combinedLexicon;
  }

  /**
   * Deletes from composeBlocks all blocks that does not belong to compositions in composition list
   *
   * @param composeBlockList
   * @param compositionList
   * @return
   */
  private void trimToCompositions(List<ComposeBlock> composeBlockList, List<Composition> compositionList) {
    for (Iterator<ComposeBlock> composeBlockIterator = composeBlockList.iterator(); composeBlockIterator.hasNext(); ) {
      ComposeBlock composeBlock = composeBlockIterator.next();

      if (!isFromCompositionList(composeBlock, compositionList)) {
        composeBlockIterator.remove();
      } else {
        // Deleting all non convenient possible next/previous
        composeBlock.getPossibleNextComposeBlocks()
            .removeIf(possibleNextComposeBlock -> !isFromCompositionList(possibleNextComposeBlock, compositionList));
        composeBlock.getPossiblePreviousComposeBlocks().removeIf(
            possiblePreviousComposeBlock -> !isFromCompositionList(possiblePreviousComposeBlock, compositionList));
      }
    }
  }

  /**
   * Finds if input block's original composition is in composition list
   *
   * @param composeBlock
   * @param compositionList
   * @return
   */
  private boolean isFromCompositionList(ComposeBlock composeBlock, List<Composition> compositionList) {
    boolean fromCompositionList = false;
    for (Composition composition : compositionList) {
      if (composeBlock.getCompositionInfo().equals(composition.getCompositionInfo())) {
        fromCompositionList = true;
        break;
      }
    }
    return fromCompositionList;
  }

  private boolean fromComposition(ComposeBlock composeBlock, CompositionInfo compositionInfo) {
    return composeBlock.getCompositionInfo().equals(compositionInfo);
  }

  /**
   * Unions lexicons but changes both inputs
   */
  private Lexicon union(Lexicon firstLexicon, Lexicon secondLexicon) {
    if (secondLexicon.getComposeBlocks().isEmpty()) {
      return firstLexicon;
    }
    if (firstLexicon.getComposeBlocks().isEmpty()) {
      return secondLexicon;
    }
    Map<Integer, Set<Integer>> unionMap =
        unionMaps(firstLexicon.getPossibleNextMusicBlockNumbers(), secondLexicon.getPossibleNextMusicBlockNumbers());
    // adding the possible next/previous
    List<ComposeBlock> firstComposeBlocks = firstLexicon.getComposeBlocks();
    List<ComposeBlock> secondComposeBlocks = secondLexicon.getComposeBlocks();
    for (int firstComposeBlockNumber = 0; firstComposeBlockNumber < firstComposeBlocks.size();
         firstComposeBlockNumber++) {
      ComposeBlock firstComposeBlock = firstComposeBlocks.get(firstComposeBlockNumber);
      for (int secondComposeBlockNumber = 0; secondComposeBlockNumber < secondComposeBlocks.size();
           secondComposeBlockNumber++) {
        ComposeBlock secondComposeBlock = secondComposeBlocks.get(secondComposeBlockNumber);
        if (musicBlockProvider.isPossibleNext(firstComposeBlock.getMusicBlock(), secondComposeBlock.getMusicBlock())) {
          firstComposeBlock.getPossibleNextComposeBlocks().add(secondComposeBlock);
          unionMap.get(firstComposeBlockNumber).add(firstComposeBlocks.size() + secondComposeBlockNumber);
          secondComposeBlock.getPossiblePreviousComposeBlocks().add(firstComposeBlock);
        }
        if (musicBlockProvider.isPossibleNext(secondComposeBlock.getMusicBlock(), firstComposeBlock.getMusicBlock())) {
          secondComposeBlock.getPossibleNextComposeBlocks().add(firstComposeBlock);
          unionMap.get(firstComposeBlocks.size() + secondComposeBlockNumber).add(firstComposeBlockNumber);
          firstComposeBlock.getPossiblePreviousComposeBlocks().add(secondComposeBlock);
        }
      }
    }
    List<ComposeBlock> union = new ArrayList<>(firstComposeBlocks);
    union.addAll(secondComposeBlocks);
    return new Lexicon(union, unionMap);
  }

  private Map<Integer, Set<Integer>> unionMaps(Map<Integer, Set<Integer>> firstMap,
                                               Map<Integer, Set<Integer>> secondMap) {
    Map<Integer, Set<Integer>> union = new HashMap<>(firstMap);
    for (Map.Entry<Integer, Set<Integer>> mapEntry : secondMap.entrySet()) {
      union.put(mapEntry.getKey() + firstMap.size(), mapEntry.getValue().stream()
          .map(integer -> integer + firstMap.size())
          .collect(Collectors.toSet())
      );
    }
    return union;
  }

}
