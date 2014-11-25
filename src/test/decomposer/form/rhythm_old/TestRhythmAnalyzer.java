package decomposer.form.rhythm_old;

import decomposer.form.rhythm_old.equality.RhythmEqualityTest;
import jm.music.data.Note;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertTrue;

public class TestRhythmAnalyzer {

	private RhythmAnalyzer rhythmAnalyzer = new RhythmAnalyzer();

	@Before
	public void setAnalyzer() {
		rhythmAnalyzer.setRhythmEqualityTest( new RhythmEqualityTest() );
		rhythmAnalyzer.getRhythmEqualityTest().setMaxUniqueRhythmValuesCount( 0 );
		rhythmAnalyzer.getRhythmEqualityTest().setMaxUnequalPartsPercentage( 0 );
		rhythmAnalyzer.getRhythmEqualityTest().setMaxRhythmPercentageEntryDeviation( 0.000001 );
	}

	@Test
	public void getRhythmFromValueTestCase1() {
		// FIRST MUSIC BLOCK
		List<Note> notes1 = new ArrayList<>(  );
		notes1.add( new Note( C1, SIXTEENTH_NOTE ) );
		notes1.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes1.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes1.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes1.add( new Note( C1, WHOLE_NOTE ) );
		notes1.add( new Note( C1, QUARTER_NOTE ) );

		List<Note> notes2 = new ArrayList<>(  );
		notes2.add( new Note( C0, WHOLE_NOTE ) );

		List<List<Note>> listOfNotes1 = new ArrayList<>();
		listOfNotes1.add( notes1 );
		listOfNotes1.add( notes2 );

		assertTrue( rhythmAnalyzer.getRhythmFormValue( listOfNotes1 ) == 0 );
		assertTrue( rhythmAnalyzer.getRhythmFormValue( listOfNotes1 ) == 0 );
		assertTrue( rhythmAnalyzer.getRhythmFormValue( listOfNotes1 ) == 0 );
	}

	/**
	 * even number of notes in list
	 */
	@Test
	public void getCountOfRhythmEntryMapTestCase1() {
		List<Note> notes = new ArrayList<>(  );
		notes.add( new Note( C1, SIXTEENTH_NOTE ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, WHOLE_NOTE ) );
		notes.add( new Note( C1, QUARTER_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, EIGHTH_NOTE_TRIPLET ) );

		Map< Double, Double > testValue = rhythmAnalyzer.getCountOfRhythmEntryMap( notes );

		Map< Double, Double > etalon = new HashMap<>(  );
		etalon.put( EIGHTH_NOTE_TRIPLET, 2./notes.size() );
		etalon.put( EIGHTH_NOTE, 2./notes.size() );
		etalon.put( QUARTER_NOTE, 1./notes.size() );
		etalon.put( WHOLE_NOTE, 1./notes.size() );
		etalon.put( SIXTEENTH_NOTE_TRIPLET, 3./notes.size() );
		etalon.put( SIXTEENTH_NOTE, 1./notes.size() );

		assertTrue( rhythmAnalyzer.getRhythmEqualityTest().isCountsOfRhythmEntriesEquals( testValue, etalon ) );
	}

	/**
	 * odd number of notes in list
	 */
	@Test
	public void getCountOfRhythmEntryMapTestCase2() {
		List<Note> notes = new ArrayList<>(  );
		notes.add( new Note( C1, SIXTEENTH_NOTE ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, WHOLE_NOTE ) );
		notes.add( new Note( C1, QUARTER_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE_TRIPLET ) );

		Map< Double, Double > testValue = rhythmAnalyzer.getCountOfRhythmEntryMap( notes );

		Map< Double, Double > etalon = new HashMap<>(  );
		etalon.put( EIGHTH_NOTE_TRIPLET, 1./notes.size() );
		etalon.put( EIGHTH_NOTE, 2./notes.size() );
		etalon.put( QUARTER_NOTE, 1./notes.size() );
		etalon.put( WHOLE_NOTE, 1./notes.size() );
		etalon.put( SIXTEENTH_NOTE_TRIPLET, 3./notes.size() );
		etalon.put( SIXTEENTH_NOTE, 1./notes.size() );

		assertTrue( rhythmAnalyzer.getRhythmEqualityTest().isCountsOfRhythmEntriesEquals( testValue, etalon ) );
	}

	/**
	 * odd number of notes in list
	 */
	@Test
	public void getCountOfRhythmEntryMapTestCase3() {
		List<Note> notes = new ArrayList<>(  );
		notes.add( new Note( C1, SIXTEENTH_NOTE ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, SIXTEENTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, WHOLE_NOTE ) );
		notes.add( new Note( C1, QUARTER_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE ) );
		notes.add( new Note( C1, EIGHTH_NOTE_TRIPLET ) );
		notes.add( new Note( C1, HALF_NOTE ) );
		notes.add( new Note( C1, HALF_NOTE ) );

		Map< Double, Double > testValue = rhythmAnalyzer.getCountOfRhythmEntryMap( notes );

		Map< Double, Double > etalon = new HashMap<>(  );
		etalon.put( EIGHTH_NOTE_TRIPLET, 1./notes.size() );
		etalon.put( EIGHTH_NOTE, 2./notes.size() );
		etalon.put( QUARTER_NOTE, 1./notes.size() );
		etalon.put( HALF_NOTE, 2./notes.size() );
		etalon.put( WHOLE_NOTE, 1./notes.size() );
		etalon.put( SIXTEENTH_NOTE_TRIPLET, 3./notes.size() );
		etalon.put( SIXTEENTH_NOTE, 1./notes.size() );

		assertTrue( rhythmAnalyzer.getRhythmEqualityTest().isCountsOfRhythmEntriesEquals( testValue, etalon ) );
	}
}
