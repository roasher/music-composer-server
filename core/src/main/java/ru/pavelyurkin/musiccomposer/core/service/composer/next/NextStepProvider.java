package ru.pavelyurkin.musiccomposer.core.service.composer.next;

import static com.google.common.collect.Iterables.getLast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.FormCompositionStep;

/**
 * Parent class getting Next Block
 */
public abstract class NextStepProvider {

  public Optional<CompositionStep> getNext(List<CompositionStep> previousCompositionSteps,
                                           List<FormCompositionStep> previousFormCompositionSteps, Optional<Form> form,
                                           double length) {

    CompositionStep lastCompositionStep = !previousCompositionSteps.isEmpty() ?
        getLast(previousCompositionSteps) :
        getLast(getLast(previousFormCompositionSteps).getCompositionSteps());
    List<ComposeBlock> possibleNextComposeBlocks =
        new ArrayList<>(lastCompositionStep.getOriginComposeBlock().getPossibleNextComposeBlocks());
    possibleNextComposeBlocks.removeAll(lastCompositionStep.getNextMusicBlockExclusions());

    // Leaving all blocks adding whose will keep whole piece in range
    double previouslyComposedRhythmValue =
        previousCompositionSteps.stream().mapToDouble(value -> value.getOriginComposeBlock().getRhythmValue()).sum();
    List<ComposeBlock> blocksToChooseFrom = possibleNextComposeBlocks.stream()
        .filter(composeBlock -> previouslyComposedRhythmValue + composeBlock.getRhythmValue() <= length)
        .collect(Collectors.toList());
    // Creating steps for further processing
    List<CompositionStep> compositionStepsToChooseFrom = blocksToChooseFrom.stream().map(composeBlock -> {
      CompositionStep compositionStep = new CompositionStep(composeBlock,
          composeBlock.getMusicBlock().transposeClone(lastCompositionStep.getTransposedBlock()));
      return compositionStep;
    }).collect(Collectors.toList());
    return getNext(compositionStepsToChooseFrom, previousCompositionSteps, previousFormCompositionSteps, form);

  }

  public abstract Optional<CompositionStep> getNext(List<CompositionStep> blocksToChooseFrom,
                                                    List<CompositionStep> previousCompositionSteps,
                                                    List<FormCompositionStep> formCompositionSteps,
                                                    Optional<Form> form);
}
