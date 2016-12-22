package ru.pavelyurkin.musiccomposer.core.decomposer.form.rhythm_old.equality;

import java.util.List;

/**
 * Class examines the ru.pavelyurkin.musiccomposer.equality between rhythm_old patterns which represents simple list of rhythm_old values
 * Created by night wish on 18.10.14.
 */
public class RhythmEqualityPatternTest {

	/**
	 * Music Block consists with "phrases" that are played by several instruments.
	 * It is possible that several instruments in firstMusicBlock are identical to secondMusicBlock, and others are not
	 * This variable declares max acceptable percentage of unequal parts in the comparison.
	 */
	private int maxUnequalPartsPercentage;

	public boolean isEqual( List<List<Double>> firstRhythmPattern, List<List<Double>> secondRhythmPattern ) {

		if ( firstRhythmPattern.size() != secondRhythmPattern.size() ) {
			throw new IllegalArgumentException( "Unexpected use of equal test. Input lists has different number of instruments" );
		}

		int numberOfEqualParts = 0;

		for ( int currentInstrument = 0; currentInstrument < firstRhythmPattern.size(); currentInstrument++ ) {
			if ( isCountsOfRhythmEntriesEquals( firstRhythmPattern.get( currentInstrument ), secondRhythmPattern.get( currentInstrument ) ) ) {
				numberOfEqualParts++;
			}
		}
		double unequalPartsPercentage = 1 - numberOfEqualParts * 1. / firstRhythmPattern.size();
		return unequalPartsPercentage <= maxUnequalPartsPercentage;
	}

	public boolean isCountsOfRhythmEntriesEquals( List<Double> firstSingleVoiceRhythmPattern, List<Double> secontSingleVoiceRhythmPattern ) {

		if ( firstSingleVoiceRhythmPattern.size() != secontSingleVoiceRhythmPattern.size() ) {
			throw new IllegalArgumentException( "Unexpected use of equal test. Input lists has different number of notes" );
		}

		for ( int noteNumber = 0; noteNumber < firstSingleVoiceRhythmPattern.size(); noteNumber ++ ) {
			Double firstPatternRhythmValue = firstSingleVoiceRhythmPattern.get( noteNumber );
			Double secondPattenRhythmValue = secontSingleVoiceRhythmPattern.get( noteNumber );

			if ( firstPatternRhythmValue != secondPattenRhythmValue ) {
				return false;
			}
		}
		return true;
	}

	public int getMaxUnequalPartsPercentage() {
		return maxUnequalPartsPercentage;
	}

	public void setMaxUnequalPartsPercentage( int maxUnequalPartsPercentage ) {
		this.maxUnequalPartsPercentage = maxUnequalPartsPercentage;
	}
}
