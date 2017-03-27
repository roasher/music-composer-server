package ru.pavelyurkin.musiccomposer.core.decomposer.form.rhythm_old;

import ru.pavelyurkin.musiccomposer.core.decomposer.form.rhythm_old.equality.RhythmEqualityPatternTest;
import jm.music.data.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Class analyzes rhythm_old
 * Created by night wish on 18.10.14.
 */
public class RhythmPatternAnalyzer {

	private RhythmEqualityPatternTest rhythmEqualityPatternTest;
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
		private List<List<Double>> rhythmPattern;
	}

	/**
	 * Returns int value represents part of the form regarding already analyzed musicBlocks
	 * @param listOfNotes
	 * @return
	 */
	public int getRhythmFormValue( List<List<Note>> listOfNotes ) {
		RhythmData rhythmData = new RhythmData();
		rhythmData.rhythmPattern = getRhythmPattern( listOfNotes );

		boolean needCreateNewFormValue = true;
		List<List<Double>> rhythmPattern = getRhythmPattern( listOfNotes );
		for ( RhythmData alreadyAnalyzedRhythmData : listOfRhythmData ) {
			if ( rhythmEqualityPatternTest.isEqual( alreadyAnalyzedRhythmData.rhythmPattern, rhythmPattern ) ) {
				rhythmData.rhythmFormValue = alreadyAnalyzedRhythmData.rhythmFormValue;
				needCreateNewFormValue = false;
			}
		}

		if ( needCreateNewFormValue ) {
			rhythmData.rhythmFormValue = getNewFormValue( listOfRhythmData );
		}

		listOfRhythmData.add( rhythmData );
		return rhythmData.rhythmFormValue;
	}

	/**
	 * Returns rhythm_old pattern. List elements stands for instrument parts
	 * @param listOfNotes
	 * @return
	 */
	private List<List<Double>> getRhythmPattern( List<List<Note>> listOfNotes ) {
		List<List<Double>> rhythmPattern = new ArrayList<>();
		for ( int currentInstrument = 0; currentInstrument < listOfNotes.size(); currentInstrument++ ) {
			List<Double> oneVoiceRhythmPattern = getOneVoiceRhythmPattern( listOfNotes.get( currentInstrument ) );
			rhythmPattern.add( oneVoiceRhythmPattern );
		}
		return rhythmPattern;
	}

	/**
	 * Returns List of rhythm_old values
	 * @param notes
	 * @return
	 */
	private List<Double> getOneVoiceRhythmPattern( List<Note> notes ) {
		List<Double> rhythmPattern = new ArrayList<>();
		for ( Note currentNote : notes ) {
			rhythmPattern.add( currentNote.getRhythmValue() );
		}
		return rhythmPattern;
	}

	private int getNewFormValue( List<RhythmData> listOfRhythmData ) {
		int max = 0;
		for ( RhythmData rhythmData : listOfRhythmData ) {
			if ( rhythmData.rhythmFormValue > max ) {
				max = rhythmData.rhythmFormValue;
			}
		}
		return max;
	}

	public RhythmEqualityPatternTest getRhythmEqualityPatternTest() {
		return rhythmEqualityPatternTest;
	}

	public void setRhythmEqualityPatternTest( RhythmEqualityPatternTest rhythmEqualityPatternTest ) {
		this.rhythmEqualityPatternTest = rhythmEqualityPatternTest;
	}

	public List<RhythmData> getListOfRhythmData() {
		return listOfRhythmData;
	}
}