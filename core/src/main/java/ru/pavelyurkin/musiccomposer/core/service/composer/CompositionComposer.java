package ru.pavelyurkin.musiccomposer.core.service.composer;

import static com.google.common.collect.Iterables.getLast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.exception.ComposeException;

/**
 * Class handles composition of new piece using lexicon
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CompositionComposer {

  private final FormBlockProvider formBlockProvider;
  private final InstrumentPartToPartConverter instrumentPartToPartConverter;

  /**
   * Composing piece considering given lexicon and step to start from.
   * Returns composition and last compose step to being able to start composing process from this point
   *
   * @param lexicon
   * @param compositionLength
   * @return
   */
  public Pair<Composition, List<CompositionStep>> compose(ComposeStepProvider composeStepProvider, Lexicon lexicon,
                                                          double compositionLength,
                                                          List<CompositionStep> previousCompositionSteps) {
    List<CompositionStep> compositionSteps =
        formBlockProvider.composeSteps(compositionLength, lexicon, composeStepProvider, previousCompositionSteps);
    List<List<InstrumentPart>> blocks = compositionSteps
        .stream()
        .map(compositionStep -> compositionStep.getTransposedBlock().getInstrumentParts())
        .collect(Collectors.toList());
    Composition composition = gatherComposition(blocks);
    List<CompositionStep> steps = new ArrayList<>();
    steps.addAll(previousCompositionSteps);
    steps.addAll(compositionSteps);
    return Pair.of(composition, steps);
  }

  /**
   * Composing piece considering given lexicon and composition compositionLength
   *
   * @param lexicon
   * @param compositionLength
   * @return
   */
  public Composition compose(ComposeStepProvider composeStepProvider, Lexicon lexicon, double compositionLength) {
    return compose(composeStepProvider, lexicon, compositionLength, Collections.emptyList()).getKey();
  }

  /**
   * Composing piece considering given lexicon, form pattern and composition length
   *
   * @param lexicon
   * @param form
   * @param compositionLength
   * @return
   */
  public Composition compose(ComposeStepProvider composeStepProvider, Lexicon lexicon, String form,
                             double compositionLength) {
    List<FormCompositionStep> formCompositionSteps =
        composeSteps(composeStepProvider, lexicon, form, compositionLength);
    List<List<InstrumentPart>> blocks = formCompositionSteps
        .stream()
        .flatMap(formCompositionStep -> formCompositionStep.getCompositionSteps()
            .stream()
            .map(compositionStep -> compositionStep.getTransposedBlock().getInstrumentParts()))
        .collect(Collectors.toList());
    return gatherComposition(blocks);
  }

  /**
   * Main composing function if form is set.
   * Assuming we are on k-th step of composing.
   * Composing k+1 block according given form.
   * If it is impossible, than recomposing k-th block.
   * If it is impossible, going back to k-1 and so on.
   *
   * @param lexicon
   * @param form
   * @param compositionLength
   * @return
   */
  public List<FormCompositionStep> composeSteps(ComposeStepProvider composeStepProvider, Lexicon lexicon, String form,
                                                double compositionLength) {

    List<FormCompositionStep> compositionSteps = new ArrayList<>();
    List<ComposeBlock> firstComposeBlockExclusions = new ArrayList<>();

    double stepLength = compositionLength / form.length();
    for (int formElementNumber = 0; formElementNumber < form.length(); formElementNumber++) {

      Form current = new Form(form.charAt(formElementNumber));
      Optional<FormCompositionStep> nextStep =
          formBlockProvider.getFormElement(stepLength, lexicon, composeStepProvider, current, compositionSteps,
              firstComposeBlockExclusions);

      if (nextStep.isPresent()) {
        compositionSteps.add(nextStep.get());
      } else {
        if (formElementNumber != 0) {
          ComposeBlock exclusionBlock = getLast(compositionSteps).getCompositionSteps().get(0).getOriginComposeBlock();
          if (formElementNumber != 1) {
            FormCompositionStep preLastCompositionStep = compositionSteps.get(formElementNumber - 2);
            getLast(preLastCompositionStep.getCompositionSteps()).addNextExclusion(exclusionBlock);
          } else {
            firstComposeBlockExclusions.add(exclusionBlock);
          }
          // subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with
          // previous
          compositionSteps.remove(formElementNumber - 1);
          formElementNumber = formElementNumber - 2;
          continue;
        } else {
          throw new ComposeException("There is no possible ways to compose new piece considering such parameters");
        }
      }
    }
    return compositionSteps;
  }

  /**
   * Creates composition, build on input compose instrumentParts without changing the input
   *
   * @param instrumentParts
   * @return
   */
  public Composition gatherComposition(List<List<InstrumentPart>> instrumentParts) {
    List<Part> parts = new ArrayList<>();
    // creating parts and add first block
    for (int partNumber = 0; partNumber < instrumentParts.get(0).size(); partNumber++) {
      parts.add(instrumentPartToPartConverter.convert(instrumentParts.get(0).get(partNumber)));
    }
    // gluing
    for (int blockNumber = 1; blockNumber < instrumentParts.size(); blockNumber++) {
      for (int partNumber = 0; partNumber < parts.size(); partNumber++) {
        InstrumentPart instrumentPart = instrumentParts.get(blockNumber).get(partNumber);
        Phrase phrase = instrumentPartToPartConverter.convert(instrumentPart).getPhrase(0);
        phrase.setAppend(true);
        Part part = parts.get(partNumber);
        Phrase previousPhrase = (Phrase) part.getPhraseList().get(part.getPhraseList().size() - 1);

        Note previousNote = (Note) previousPhrase.getNoteList().get(previousPhrase.size() - 1);
        Note firstNote = (Note) phrase.getNoteList().get(0);

        if (previousNote.samePitch(firstNote)) {
          previousNote.setRhythmValue(previousNote.getRhythmValue() + firstNote.getRhythmValue());
          previousNote.setDuration(previousNote.getDuration() + firstNote.getDuration());
          phrase.getNoteList().remove(0);
        }

        if (!phrase.getNoteList().isEmpty()) {
          part.add(phrase);
        }
      }
    }

    Composition composition = new Composition(parts);
    return composition;
  }

  /**
   * Creates composition based on sum of the input
   *
   * @param compositions
   * @return
   */
  public Composition gatherComposition(Composition... compositions) {
    List<List<InstrumentPart>> collect = Arrays.stream(compositions)
        .map(composition -> ((List<Part>) composition.getPartList())
            .stream()
            .map(instrumentPartToPartConverter::convertTo)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
    return gatherComposition(collect);
  }

}
