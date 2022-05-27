package ru.pavelyurkin.musiccomposer.core.service.decomposer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pavelyurkin.musiccomposer.core.service.decomposer.melody.analyzer.MelodyEqualityAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.PlaceInTheComposition;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Class searches Signatures in the composition
 */
@Slf4j
public class SignatureDecomposer {

  private int minSignatureLength;
  private int maxSignatureLength;
  private MelodyEqualityAnalyzer melodyEqualityAnalyzer;

  /**
   * Nested class used for holding information about ru.pavelyurkin.musiccomposer.equality pare presence
   * If hasEqual is true it means that we have already searched such type of
   * Signatures and found some similar, so there is no need in search again
   */
  private class TracedMelody extends Melody {
    private boolean hasEqual = false;

    private TracedMelody(List<Note> notes) {
      super(notes);
    }

    @Override
    public boolean equals(Object signature) {
      return super.equals(signature);
    }
  }

  /**
   * Construct all possible signatures that can be made from list of notes
   *
   * @param composition
   * @return
   */
  private Set<TracedMelody> getAllPossibleSignatures(Composition composition) {
    log.debug("Getting all possible signatures from {}", composition.getCompositionInfo().getTitle());
    // We will be searching signatures only in the top voice for now.
    Part currentPart = composition.getPart(0);

    // In the import composition will be only one phase that includes all the notes played by instrument
    Phrase currentPhrase = currentPart.getPhrase(0);
    List<Note> notes = currentPhrase.getNoteList();

    Set<TracedMelody> signatures = new HashSet<>();
    for (int signatureLength = minSignatureLength; signatureLength <= maxSignatureLength; signatureLength++) {
      for (int startIndex = 0, endIndex = signatureLength; endIndex < notes.size(); startIndex++, endIndex++) {
        PlaceInTheComposition placeInTheComposition = new PlaceInTheComposition(
            composition.getCompositionInfo(),
            // first start time
            currentPhrase.getNoteStartTime(startIndex),
            // last note end time
            currentPhrase.getNoteStartTime(endIndex) + notes.get(endIndex).getRhythmValue());
        List<Note> signatureNotes = notes.subList(startIndex, endIndex);
        TracedMelody newSignature = new TracedMelody(signatureNotes);
        newSignature.setPlaceInTheComposition(placeInTheComposition);
        signatures.add(newSignature);
      }
    }
    return signatures;
  }

  /**
   * Analyzes list of compositions and come out with list of signatures
   *
   * @param compositionList
   * @return
   */
  public Map<Melody, Set<Melody>> analyzeSignatures(List<Composition> compositionList) {
    log.info("Starting analyzing composition for signatures");
    Map<Melody, Set<Melody>> signatures = new HashMap<>();

    log.info("Step 1: creating all possible signatures");
    // lists of signatures inside list corresponding to compositions
    List<Set<TracedMelody>> signaturesToAnalyze = new ArrayList<>();
    for (int currentCompositionNumber = 0; currentCompositionNumber < compositionList.size();
         currentCompositionNumber++) {
      Composition currentComposition = compositionList.get(currentCompositionNumber);
      Set<TracedMelody> possibleSignatures = getAllPossibleSignatures(currentComposition);
      signaturesToAnalyze.add(possibleSignatures);
    }
//        log.info( "Number of analyzing compositions = {}, total amount of possible signatures = {}",
//        signaturesToAnalyze.size(), getTotalSignaturesNumber( signaturesToAnalyze ) );

    log.info("Step 2: finding signatures that can be considered equal");
    for (int currentCompositionNumberHavingEtalonSignature = 0;
         currentCompositionNumberHavingEtalonSignature < signaturesToAnalyze.size();
         currentCompositionNumberHavingEtalonSignature++) {
      log.info("Analyzing {} composition", currentCompositionNumberHavingEtalonSignature + 1);
      int currentEtalonSignatureNumber = 0;
      for (Melody etalonMelody : signaturesToAnalyze.get(currentCompositionNumberHavingEtalonSignature)) {
        log.debug(String.format("Analyzing %d out of %d signatures in %d out of %d compositions",
            ++currentEtalonSignatureNumber,
            signaturesToAnalyze.get(currentCompositionNumberHavingEtalonSignature).size(),
            currentCompositionNumberHavingEtalonSignature + 1,
            signaturesToAnalyze.size()));
        // Etalon and Comparing signatures can't be in one composition
        for (int currentCompositionNumberHavingComparingSignature = currentCompositionNumberHavingEtalonSignature + 1;
             currentCompositionNumberHavingComparingSignature < signaturesToAnalyze.size();
             currentCompositionNumberHavingComparingSignature++) {
          for (TracedMelody compareSignature : signaturesToAnalyze
              .get(currentCompositionNumberHavingComparingSignature)) {
            // We are going to compare only if compareSignature hasn't been considered equal with other one
            if (!compareSignature.hasEqual) {
              boolean equalityTestPassed = melodyEqualityAnalyzer.isEqual(etalonMelody, compareSignature);
              if (equalityTestPassed) {
                log.debug("{} signature has been considered equal with {}", etalonMelody, compareSignature);
                compareSignature.hasEqual = true;
                // Organizing signatures into a block of equal signatures
                Set<Melody> melodySet = signatures.get(etalonMelody);
                if (melodySet == null) {
                  melodySet = new HashSet<>();
                  melodySet.add(compareSignature);
                  signatures.put(etalonMelody, melodySet);
                } else {
                  melodySet.add(compareSignature);
                }
              }
            }
          }
        }
      }
    }
    return signatures;
  }

  /**
   * Returns total signatures number from lists of list
   *
   * @param lists
   * @return
   */
  private int getTotalSignaturesNumber(List<List<TracedMelody>> lists) {
    int sum = 0;
    for (List currentList : lists) {
      sum += currentList.size();
    }
    return sum;
  }

  public int getMinSignatureLength() {
    return minSignatureLength;
  }

  public void setMinSignatureLength(int minSignatureLength) {
    this.minSignatureLength = minSignatureLength;
  }

  public int getMaxSignatureLength() {
    return maxSignatureLength;
  }

  public void setMaxSignatureLength(int maxSignatureLength) {
    this.maxSignatureLength = maxSignatureLength;
  }

  public MelodyEqualityAnalyzer getMelodyEqualityAnalyzer() {
    return melodyEqualityAnalyzer;
  }

  public void setMelodyEqualityAnalyzer(MelodyEqualityAnalyzer melodyEqualityAnalyzer) {
    this.melodyEqualityAnalyzer = melodyEqualityAnalyzer;
  }
}
