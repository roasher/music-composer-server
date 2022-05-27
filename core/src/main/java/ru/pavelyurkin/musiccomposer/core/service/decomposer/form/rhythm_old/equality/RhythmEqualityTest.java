package ru.pavelyurkin.musiccomposer.core.service.decomposer.form.rhythm_old.equality;

import java.util.List;
import java.util.Map;

/**
 * Class examines the ru.pavelyurkin.musiccomposer.equality between "List of counts of Rhythm Entries"
 * List stands for number of instruments
 * Map maps rhythmical value and normalized number of notes having such value in the analyzing part of music
 * Created by night wish on 18.10.14.
 */
public class RhythmEqualityTest {

  /**
   * Music Block consists with "phrases" that are played by several instruments.
   * It is possible that several instruments in firstMusicBlock are identical to secondMusicBlock, and others are not
   * This variable declares max acceptable percentage of unequal parts in the comparison.
   */
  private int maxUnequalPartsPercentage;

  /**
   * There can be rhythm_old values in one phrase that aren't present in the other. This var declares max number of
   * sutch
   * values that we still considering that phrases CAN be rhythmically equals
   */
  private int maxUniqueRhythmValuesCount;
  /**
   * Declares max percent deviation between number of first phrase rhythm_old value and number of second phrase same
   * rhythm_old value
   */
  private double maxRhythmPercentageEntryDeviation;

  public boolean isEqual(List<Map<Double, Double>> firstListOfCountsOfRhythmEntries,
                         List<Map<Double, Double>> secondListOfCountsOfRhythmEntries) {

    if (firstListOfCountsOfRhythmEntries.size() != secondListOfCountsOfRhythmEntries.size()) {
      throw new IllegalArgumentException(
          "Unexpected use of equal test. Input lists has different number of instruments");
    }

    int numberOfEqualParts = 0;

    for (int currentInstrument = 0; currentInstrument < firstListOfCountsOfRhythmEntries.size(); currentInstrument++) {
      if (isCountsOfRhythmEntriesEquals(firstListOfCountsOfRhythmEntries.get(currentInstrument),
          secondListOfCountsOfRhythmEntries.get(currentInstrument))) {
        numberOfEqualParts++;
      }
    }
    double unequalPartsPercentage = 1 - numberOfEqualParts * 1. / firstListOfCountsOfRhythmEntries.size();
    return unequalPartsPercentage <= maxUnequalPartsPercentage;
  }

  public boolean isCountsOfRhythmEntriesEquals(Map<Double, Double> firstCountOfRhythmEntryMap,
                                               Map<Double, Double> secontCountOfRhythmEntryMap) {
    int uniqueRhythmValuesCount = 0;
    for (Map.Entry<Double, Double> entry : firstCountOfRhythmEntryMap.entrySet()) {
      Double secondCountRhythmEntry = secontCountOfRhythmEntryMap.get(entry.getKey());
      if (secondCountRhythmEntry != null) {
        if (Math.abs(secondCountRhythmEntry.doubleValue() - entry.getValue().doubleValue())
            > maxRhythmPercentageEntryDeviation) {
          return false;
        }
      } else {
        uniqueRhythmValuesCount++;
      }
    }

    for (Map.Entry<Double, Double> entry : secontCountOfRhythmEntryMap.entrySet()) {
      if (!firstCountOfRhythmEntryMap.containsKey(entry.getKey())) {
        uniqueRhythmValuesCount++;
      }
    }
    return uniqueRhythmValuesCount <= maxUniqueRhythmValuesCount;
  }

  public int getMaxUnequalPartsPercentage() {
    return maxUnequalPartsPercentage;
  }

  public void setMaxUnequalPartsPercentage(int maxUnequalPartsPercentage) {
    this.maxUnequalPartsPercentage = maxUnequalPartsPercentage;
  }

  public int getMaxUniqueRhythmValuesCount() {
    return maxUniqueRhythmValuesCount;
  }

  public void setMaxUniqueRhythmValuesCount(int maxUniqueRhythmValuesCount) {
    this.maxUniqueRhythmValuesCount = maxUniqueRhythmValuesCount;
  }

  public double getMaxRhythmPercentageEntryDeviation() {
    return maxRhythmPercentageEntryDeviation;
  }

  public void setMaxRhythmPercentageEntryDeviation(double maxRhythmPercentageEntryDeviation) {
    this.maxRhythmPercentageEntryDeviation = maxRhythmPercentageEntryDeviation;
  }
}
