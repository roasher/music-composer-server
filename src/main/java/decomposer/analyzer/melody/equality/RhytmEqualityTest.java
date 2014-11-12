package decomposer.analyzer.melody.equality;

import model.Melody;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pavel Yurkin on 17.08.14.
 */
public class RhytmEqualityTest implements EqualityTest {

    private int maxNumberOfRhythmicallyDifferentNotes;
    private double maxRhythmDeviationSteps;

    @Override
    public boolean test( Melody firstMelody, Melody secondMelody ) {

        double[] firstRhythmArray = firstMelody.getRhythmArray();
        double[] secondRhythmArray = secondMelody.getRhythmArray();
        double[] coefficient = new double[firstMelody.length()];
		Map<Double, Integer> countMap = new HashMap<>(  );

		int currentMaxNumberOfRhythmicallyDifferentNotes = 0;

        for ( int currentRhytmValue = 0; currentRhytmValue < firstRhythmArray.length; currentRhytmValue ++ ) {
			coefficient[currentRhytmValue] = firstRhythmArray[ currentRhytmValue ]/secondRhythmArray[ currentRhytmValue ];
			Integer count = countMap.get( coefficient[currentRhytmValue] );
			if ( count != null ) {
				countMap.put( coefficient[currentRhytmValue], count + 1 );
			} else {
				countMap.put( coefficient[currentRhytmValue], 1 );
			}
		}

		Double mostCommonCoefficient = null;
		// getting most common value
		int maxOccuranceNumber = Collections.max( countMap.values() );
		for ( Map.Entry<Double, Integer> currentEntry : countMap.entrySet() ) {
			if ( currentEntry.getValue().intValue() == maxOccuranceNumber ) {
				mostCommonCoefficient = currentEntry.getKey();
			}
		}

		// Cycle over coefficient
		for ( int currentCoefficientNumber = 0; currentCoefficientNumber < coefficient.length ; currentCoefficientNumber++ ) {
			if ( coefficient[currentCoefficientNumber] == mostCommonCoefficient.doubleValue() ) continue;

			double rightPart = 0;
			double leftPart = 0;
//			if ( mostCommonCoefficient >= 1 ) {
//				leftPart = Math.abs( firstRhythmArray[currentCoefficientNumber] - mostCommonCoefficient*secondRhythmArray[currentCoefficientNumber] );
//				rightPart = maxRhythmDeviationSteps*firstRhythmArray[currentCoefficientNumber];
//			} else {
//				leftPart = Math.abs( firstRhythmArray[currentCoefficientNumber]/mostCommonCoefficient - secondRhythmArray[currentCoefficientNumber] );
//				rightPart = maxRhythmDeviationSteps*secondRhythmArray[currentCoefficientNumber];
//			}

			leftPart = Math.abs( firstRhythmArray[currentCoefficientNumber] - mostCommonCoefficient*secondRhythmArray[currentCoefficientNumber] );
			rightPart = maxRhythmDeviationSteps*Math.max( firstRhythmArray[currentCoefficientNumber], mostCommonCoefficient*secondRhythmArray[currentCoefficientNumber] );

				if ( leftPart <= rightPart ) {
					currentMaxNumberOfRhythmicallyDifferentNotes++;
				} else {
					return false;
				}
		}

		return currentMaxNumberOfRhythmicallyDifferentNotes <= maxNumberOfRhythmicallyDifferentNotes;
    }

    @Override
    public int getMaxNumberOfDiversedNotes() {
        return maxNumberOfRhythmicallyDifferentNotes;
    }

    public int getMaxNumberOfRhythmicallyDifferentNotes() {
        return maxNumberOfRhythmicallyDifferentNotes;
    }

    public void setMaxNumberOfRhythmicallyDifferentNotes( int maxNumberOfRhythmicallyDifferentNotes ) {
        this.maxNumberOfRhythmicallyDifferentNotes = maxNumberOfRhythmicallyDifferentNotes;
    }

    public double getMaxRhythmDeviationSteps() {
        return maxRhythmDeviationSteps;
    }

    public void setMaxRhythmDeviationSteps( double maxRhythmDeviationSteps ) {
        this.maxRhythmDeviationSteps = maxRhythmDeviationSteps;
    }
}
