package ru.pavelyurkin.musiccomposer.core.service.composer;

import static com.google.common.collect.Iterables.getLast;
import static ru.pavelyurkin.musiccomposer.core.utils.ModelUtils.getRelativeFormBlocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.equality.form.FormEquality;
import ru.pavelyurkin.musiccomposer.core.service.equality.form.RelativelyComparable;
import ru.pavelyurkin.musiccomposer.core.service.exception.ComposeException;

/**
 * Class provides form element
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FormBlockProvider {

  private final FormEquality formEquality;

  /**
   * Generates new form block considering previously generated blocks and it's form.
   *
   * @param length
   * @param lexicon
   * @param form                      - form, from part of witch new Block is going to be generated
   * @param previousSteps
   * @param firstMusicBlockExclusions - while composing first block it can't be one of them
   * @return
   */
  public Optional<FormCompositionStep> getFormElement(double length, Lexicon lexicon,
                                                      ComposeStepProvider composeStepProvider, Form form,
                                                      List<FormCompositionStep> previousSteps,
                                                      List<ComposeBlock> firstMusicBlockExclusions) {
    log.info("Composing form element : {}, length : {}", form.getValue(), length);
    List<CompositionStep> compositionSteps =
        composeSteps(length, lexicon, composeStepProvider, previousSteps, Optional.of(form),
            firstMusicBlockExclusions);
    return Optional.ofNullable(!compositionSteps.isEmpty() ? new FormCompositionStep(compositionSteps, form) : null);
  }

  /**
   * Returns composition step list filled with compose blocks that summary covers input length sharp
   *
   * @param length
   * @param lexicon
   * @param composeStepProvider
   * @param previousFormCompositionSteps
   * @return
   */
  public List<CompositionStep> composeSteps(double length, Lexicon lexicon, ComposeStepProvider composeStepProvider,
                                            List<CompositionStep> previousFormCompositionSteps) {
    log.info("Composing element regardless form, length : {}", length);
    List<FormCompositionStep> formCompositionSteps = !previousFormCompositionSteps.isEmpty() ?
        Collections.singletonList(new FormCompositionStep(previousFormCompositionSteps, null)) :
        Collections.emptyList();
    try {
      return composeSteps(length, lexicon, composeStepProvider, formCompositionSteps, Optional.empty(),
          new ArrayList<>());
    } catch (ComposeException composeException) {
      log.info("{}. Composing as there were no history.", composeException.getMessage());
      return composeSteps(length, lexicon, composeStepProvider, Collections.emptyList(), Optional.empty(),
          new ArrayList<>());
    }
  }

  /**
   * Returns composition step list filled with compose blocks that summary covers input length sharp regarding
   * previous form steps
   *
   * @param length
   * @param lexicon
   * @param composeStepProvider
   * @param previousFormCompositionSteps
   * @param form
   * @param exclusionsToFirstBlock
   * @return
   */
  private List<CompositionStep> composeSteps(double length, Lexicon lexicon, ComposeStepProvider composeStepProvider,
                                             List<FormCompositionStep> previousFormCompositionSteps,
                                             Optional<Form> form, List<ComposeBlock> exclusionsToFirstBlock) {

    CompositionStep prefirstCompositionStep = previousFormCompositionSteps.isEmpty() ?
        CompositionStep.getEmptyCompositionStep(exclusionsToFirstBlock) :
        getLast(getLast(previousFormCompositionSteps).getCompositionSteps());
    List<CompositionStep> compositionSteps = new ArrayList<>();
    double currentLength = 0;

    for (int step = 0; step < length / lexicon.getMinRhythmValue(); step++) {
      log.info("Current state {}", step);
      CompositionStep lastCompositionStep =
          !compositionSteps.isEmpty() ? getLast(compositionSteps) : prefirstCompositionStep;

      Optional<CompositionStep> nextCompositionStep = step != 0 || !previousFormCompositionSteps.isEmpty() ?
          composeStepProvider.getNext(length, compositionSteps, previousFormCompositionSteps, form) :
          composeStepProvider.getFirst(lexicon, prefirstCompositionStep.getNextMusicBlockExclusions());

      if (nextCompositionStep.isPresent()
          && currentLength + nextCompositionStep.get().getTransposedBlock().getRhythmValue() <= length) {
        compositionSteps.add(nextCompositionStep.get());
        log.debug("Composed {}", nextCompositionStep.get().getTransposedBlock().toString());
        currentLength += nextCompositionStep.get().getTransposedBlock().getRhythmValue();
        if (currentLength == length) {
          if (form.isPresent()) {
            // FORM CHECK
            Pair<Boolean, Double> formCheckPassed = isFormCheckPassed(
                new MusicBlock(compositionSteps
                    .stream()
                    .map(CompositionStep::getTransposedBlock)
                    .collect(Collectors.toList())),
                getRelativeFormBlocks(previousFormCompositionSteps, form.get(), true),
                getRelativeFormBlocks(previousFormCompositionSteps, form.get(), false)
            );
            if (!formCheckPassed.getKey()) {
              int stepToRevert = getStepToRevert(step, formCheckPassed.getValue());
              log.debug("ComposeBlock check failed in terms of form. Reverting to step {}", stepToRevert);
              // ( stepToRevert ) -> ... -> ( step - 1 ) -> ( step )
              compositionSteps.get(stepToRevert)
                  .addNextExclusion(compositionSteps.get(stepToRevert + 1).getOriginComposeBlock());
              ListIterator<CompositionStep> iterator = compositionSteps.listIterator(compositionSteps.size());
              while (iterator.previousIndex() != stepToRevert) {
                CompositionStep previousStep = iterator.previous();
                currentLength -= previousStep.getOriginComposeBlock().getRhythmValue();
                iterator.remove();
              }
              step = stepToRevert;
              continue;
            }
          }
          return compositionSteps;
        }
      } else {
        if (step != 0) {
          CompositionStep preLastCompositionStep = step != 1 ? compositionSteps.get(step - 2) : prefirstCompositionStep;
          preLastCompositionStep.addNextExclusion(lastCompositionStep.getOriginComposeBlock());
          // subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with
          // previous
          if (compositionSteps.get(step - 1).getOriginComposeBlock() != null) {
            currentLength -= compositionSteps.get(step - 1).getOriginComposeBlock().getRhythmValue();
          }
          compositionSteps.remove(step - 1);
          step = step - 2;
          continue;
        } else {
          throw new ComposeException("There is no possible ways to compose new piece considering such parameters");
        }
      }
    }
    return Collections.emptyList();
  }

  /**
   * Should return step to revert - so new composition will be handled to next to this step.
   * Step calculated according to step count and measure that shows difference with what form comparison was failed.
   * Step should be no greater then step-2. There is no point returning to step-1 cause starting to compose next =
   * last block (last step) block provider will give best
   * variant in terms of form.
   *
   * @param step
   * @param diffMeasure
   * @return
   */
  private int getStepToRevert(int step, Double diffMeasure) {
    int calculatedStepToReturn = (int) (step * diffMeasure);
    if (calculatedStepToReturn == 0) {
      return 0;
    }
    if (calculatedStepToReturn > step - 2) {
      return step - 2;
    }
    return calculatedStepToReturn;
  }

  /**
   * Checks if new block can be used in composition in terms of form. Returning diff measure as well.
   *
   * @param block
   * @param similarFormSteps
   * @param differentFormSteps
   * @return
   */
  private Pair<Boolean, Double> isFormCheckPassed(MusicBlock block, List<MusicBlock> similarFormSteps,
                                                  List<MusicBlock> differentFormSteps) {
    double maxDiffMeasure = 0;
    // checking that new block is different to differentFormSteps
    for (MusicBlock differentStep : differentFormSteps) {
      Pair<RelativelyComparable.ResultOfComparison, Double> comparison =
          formEquality.isEqual(block.getInstrumentParts(), differentStep.getInstrumentParts());
      if (comparison.getKey() != RelativelyComparable.ResultOfComparison.DIFFERENT) {
        return Pair.of(false, comparison.getValue());
      }
      if (maxDiffMeasure < comparison.getValue()) {
        maxDiffMeasure = comparison.getValue();
      }
    }
    // checking that new block is similar to similarFormSteps
    for (MusicBlock similarStep : similarFormSteps) {
      Pair<RelativelyComparable.ResultOfComparison, Double> comparison =
          formEquality.isEqual(block.getInstrumentParts(), similarStep.getInstrumentParts());
      if (comparison.getKey() != RelativelyComparable.ResultOfComparison.EQUAL) {
        return Pair.of(false, comparison.getValue());
      }
      if (maxDiffMeasure < comparison.getValue()) {
        maxDiffMeasure = comparison.getValue();
      }
    }
    return Pair.of(true, maxDiffMeasure);
  }
}
