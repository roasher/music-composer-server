package ru.pavelyurkin.musiccomposer.decomposer.form.rhythm_old.equality;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static jm.constants.Durations.*;
import static junit.framework.Assert.*;

public class TestRhythmEquality {

	private RhythmEqualityTest rhythmEqualityTest = new RhythmEqualityTest();

	@Test
	public void isCountsOfRhythmEntriesEqualsTestCase1() {

		Map<Double, Double> firstCountOfRhythmEntryMap = new HashMap<>();
		firstCountOfRhythmEntryMap.put( EIGHTH_NOTE, 0.5 );
		firstCountOfRhythmEntryMap.put( WHOLE_NOTE, 0.2 );
		firstCountOfRhythmEntryMap.put( SIXTEENTH_NOTE_TRIPLET, 0.21 );
		firstCountOfRhythmEntryMap.put( QUARTER_NOTE, 0.3333 );

		Map<Double, Double> secontCountOfRhythmEntryMap = new HashMap<>();
		secontCountOfRhythmEntryMap.put( EIGHTH_NOTE, 0.5 );
		secontCountOfRhythmEntryMap.put( WHOLE_NOTE, 0.2 );
		secontCountOfRhythmEntryMap.put( SIXTEENTH_NOTE_TRIPLET, 0.21 );
		secontCountOfRhythmEntryMap.put( QUARTER_NOTE, 0.3333 );

		rhythmEqualityTest.setMaxRhythmPercentageEntryDeviation( 0 );
		rhythmEqualityTest.setMaxUnequalPartsPercentage( 0 );
		rhythmEqualityTest.setMaxUniqueRhythmValuesCount( 0 );
		assertTrue( rhythmEqualityTest.isCountsOfRhythmEntriesEquals( firstCountOfRhythmEntryMap, secontCountOfRhythmEntryMap ) );

		secontCountOfRhythmEntryMap.put( QUARTER_NOTE, 0.34 );
		assertFalse( rhythmEqualityTest.isCountsOfRhythmEntriesEquals( firstCountOfRhythmEntryMap, secontCountOfRhythmEntryMap ) );

		rhythmEqualityTest.setMaxRhythmPercentageEntryDeviation( 0.02 );
		assertTrue( rhythmEqualityTest.isCountsOfRhythmEntriesEquals( firstCountOfRhythmEntryMap, secontCountOfRhythmEntryMap ) );

		rhythmEqualityTest.setMaxRhythmPercentageEntryDeviation( 0 );
		secontCountOfRhythmEntryMap.put( QUARTER_NOTE, 0.3333 );
		secontCountOfRhythmEntryMap.put( EIGHTH_NOTE_TRIPLET, 0.3333 );
		assertFalse( rhythmEqualityTest.isCountsOfRhythmEntriesEquals( firstCountOfRhythmEntryMap, secontCountOfRhythmEntryMap ) );

		rhythmEqualityTest.setMaxUniqueRhythmValuesCount( 1 );
		assertTrue( rhythmEqualityTest.isCountsOfRhythmEntriesEquals( firstCountOfRhythmEntryMap, secontCountOfRhythmEntryMap ) );
	}
}
