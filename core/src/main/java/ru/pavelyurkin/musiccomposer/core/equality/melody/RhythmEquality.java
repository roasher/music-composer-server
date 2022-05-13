package ru.pavelyurkin.musiccomposer.core.equality.melody;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Created by Pavel Yurkin on 17.08.14.
 */
@Component
public class RhythmEquality implements Equality {

  @Value("${RhythmEquality.maxNumberOfRhythmicallyDifferentNotes}")
  private int maxNumberOfRhythmicallyDifferentNotes;

  @Value("${RhythmEquality.maxRhythmDeviationSteps}")
  private double maxRhythmDeviationSteps;

  @Override
  public boolean test(Melody firstMelody, Melody secondMelody) {
    double equalityMetrics = getEqualityMetric(firstMelody, secondMelody);
    double currentNumberOfRhythmicallyDifferentNotes = (1 - equalityMetrics) * firstMelody.size();
    return currentNumberOfRhythmicallyDifferentNotes <= maxNumberOfRhythmicallyDifferentNotes;
  }

  private double getEqualityMetric(Melody firstMelody, Melody secondMelody) {

    double[] firstRhythmArray = firstMelody.getRhythmArray();
    double[] secondRhythmArray = secondMelody.getRhythmArray();
    double[] coefficient = new double[firstMelody.length()];
    Map<Double, Integer> countMap = new HashMap<>();

    int currentMaxNumberOfRhythmicallyDifferentNotes = 0;

    for (int currentRhytmValue = 0; currentRhytmValue < firstRhythmArray.length; currentRhytmValue++) {
      coefficient[currentRhytmValue] = firstRhythmArray[currentRhytmValue] / secondRhythmArray[currentRhytmValue];
      Integer count = countMap.get(coefficient[currentRhytmValue]);
      if (count != null) {
        countMap.put(coefficient[currentRhytmValue], count + 1);
      } else {
        countMap.put(coefficient[currentRhytmValue], 1);
      }
    }

    Double mostCommonCoefficient = null;
    // getting most common value
    int maxOccuranceNumber = Collections.max(countMap.values());
    for (Map.Entry<Double, Integer> currentEntry : countMap.entrySet()) {
      if (currentEntry.getValue().intValue() == maxOccuranceNumber) {
        mostCommonCoefficient = currentEntry.getKey();
      }
    }

    // Cycle over coefficient
    for (int currentCoefficientNumber = 0; currentCoefficientNumber < coefficient.length; currentCoefficientNumber++) {
      if (coefficient[currentCoefficientNumber] == mostCommonCoefficient.doubleValue()) {
        continue;
      }

      double rightPart = 0;
      double leftPart = 0;
      //			if ( mostCommonCoefficient >= 1 ) {
      //				leftPart = Math.abs( firstRhythmArray[currentCoefficientNumber] -
      //				mostCommonCoefficient*secondRhythmArray[currentCoefficientNumber] );
      //				rightPart = maxRhythmDeviationSteps*firstRhythmArray[currentCoefficientNumber];
      //			} else {
      //				leftPart = Math.abs( firstRhythmArray[currentCoefficientNumber]/mostCommonCoefficient -
      //				secondRhythmArray[currentCoefficientNumber] );
      //				rightPart = maxRhythmDeviationSteps*secondRhythmArray[currentCoefficientNumber];
      //			}

      leftPart = Math.abs(firstRhythmArray[currentCoefficientNumber]
                          - mostCommonCoefficient * secondRhythmArray[currentCoefficientNumber]);
      rightPart = maxRhythmDeviationSteps * Math.max(firstRhythmArray[currentCoefficientNumber],
          mostCommonCoefficient * secondRhythmArray[currentCoefficientNumber]);

      if (leftPart <= rightPart) {
        currentMaxNumberOfRhythmicallyDifferentNotes++;
      } else {
        return 0;
      }
    }

    return (firstMelody.size() - currentMaxNumberOfRhythmicallyDifferentNotes) * 1. / firstMelody.size();
  }

  @Override
  public int getMaxNumberOfDiversedNotes() {
    return maxNumberOfRhythmicallyDifferentNotes;
  }

  public int getMaxNumberOfRhythmicallyDifferentNotes() {
    return maxNumberOfRhythmicallyDifferentNotes;
  }

  public void setMaxNumberOfRhythmicallyDifferentNotes(int maxNumberOfRhythmicallyDifferentNotes) {
    this.maxNumberOfRhythmicallyDifferentNotes = maxNumberOfRhythmicallyDifferentNotes;
  }

  public double getMaxRhythmDeviationSteps() {
    return maxRhythmDeviationSteps;
  }

  public void setMaxRhythmDeviationSteps(double maxRhythmDeviationSteps) {
    this.maxRhythmDeviationSteps = maxRhythmDeviationSteps;
  }
}
