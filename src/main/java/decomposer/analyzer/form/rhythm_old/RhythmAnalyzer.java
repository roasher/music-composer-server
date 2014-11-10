package decomposer.analyzer.form.rhythm_old;

import decomposer.analyzer.form.rhythm_old.equality.RhythmEqualityTest;
import jm.music.data.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class analyzes rhythm_old
 * Created by night wish on 18.10.14.
 */
public class RhythmAnalyzer {

	private RhythmEqualityTest rhythmEqualityTest;
	/**
	 * List of Rhythm Data that belongs to each analyzed list of lists of notes
	 * This collection represents rhythmical form aspect of the piece
	 */
	private List<RhythmData> listOfRhythmData = new ArrayList<>();

	/**
	 * Class-wrapper for rhythmic data of the piece
	 */
	private class RhythmData {
		private int rhythmFormValue;
		private List<Map<Double, Double>> listOfCountsOfRhythmEntries;
	}

	/**
	 * Returns int value represents part of the form regarding already analyzed musicBlocks
	 * @param listOfNotes
	 * @return
	 */
	public int getRhythmFormValue( List<List<Note>> listOfNotes ) {
		RhythmData rhythmData = new RhythmData();
		rhythmData.listOfCountsOfRhythmEntries = getListOfCountsOfRhythmEntries( listOfNotes );

		if ( listOfRhythmData.isEmpty() ) {
			rhythmData.rhythmFormValue = 0;
			listOfRhythmData.add( rhythmData );
		} else {
			List<Map<Double, Double>> newListOfCountsOfRhythmEntries = getListOfCountsOfRhythmEntries( listOfNotes );
			if ( rhythmEqualityTest.isEqual( listOfRhythmData.get( listOfRhythmData.size() - 1 ).listOfCountsOfRhythmEntries, newListOfCountsOfRhythmEntries ) ) {
				rhythmData.rhythmFormValue = listOfRhythmData.get( listOfRhythmData.size() - 1 ).rhythmFormValue;
			} else {
				rhythmData.rhythmFormValue = listOfRhythmData.get( listOfRhythmData.size() - 1 ).rhythmFormValue + 1;
			}
		}

		listOfRhythmData.add( rhythmData );
		return rhythmData.rhythmFormValue;
	}

	/**
	 * Returns list of Maps. List elements stands for instrument parts
	 * @param listOfNotes
	 * @return
	 */
	public List<Map<Double, Double>> getListOfCountsOfRhythmEntries( List< List< Note> > listOfNotes ) {
		List<Map<Double, Double>> listOfCountOfRhythmEntries = new ArrayList<>();
		for ( int currentInstrument = 0; currentInstrument < listOfNotes.size(); currentInstrument++ ) {
			Map<Double, Double> firstCountOfRhythmEntryMap = getCountOfRhythmEntryMap( listOfNotes.get( currentInstrument ) );
			listOfCountOfRhythmEntries.add( firstCountOfRhythmEntryMap );
		}
		return listOfCountOfRhythmEntries;
	}

	/**
	 * Returns Map of ( rhythmical value - normalized number of rhythmical values in the notes list )
	 * @param notes
	 * @return
	 */
	public Map<Double, Double> getCountOfRhythmEntryMap( List<Note> notes ) {
		Map<Double, Double> countOfRhythmEntry = new HashMap<>();
		for ( Note currentNote : notes ) {
			Double oldValue = countOfRhythmEntry.get( currentNote.getRhythmValue() );
			if ( oldValue != null ) {
				// dividing by "notes.length" we do normalizing
				Double newValue = new Double( oldValue.doubleValue() + 1. / notes.size() );
				countOfRhythmEntry.put( currentNote.getRhythmValue(), newValue );
			} else {
				Double newValue = new Double( 1. / notes.size() );
				countOfRhythmEntry.put( currentNote.getRhythmValue(), newValue );
			}
		}
		return countOfRhythmEntry;
	}

	public RhythmEqualityTest getRhythmEqualityTest() {
		return rhythmEqualityTest;
	}

	public void setRhythmEqualityTest( RhythmEqualityTest rhythmEqualityTest ) {
		this.rhythmEqualityTest = rhythmEqualityTest;
	}

	public List<RhythmData> getListOfRhythmData() {
		return listOfRhythmData;
	}
}
