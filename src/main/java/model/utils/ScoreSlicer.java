package model.utils;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import model.MusicBlock;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Slices score into pieces
 * Created by night wish on 08.11.14.
 */
public class ScoreSlicer {

	/**
	 * Slices music blocks with certain timePeriod from input score.
	 * @param score
	 * @param timePeriod
	 * @return
	 */
	public List< MusicBlock > slice( Score score, double timePeriod ) {

		List< List< List < Note > > > composition = new ArrayList<>(  );

		for ( Part part : score.getPartArray() ) {
			Phrase phrase = part.getPhraseArray()[0];
			List< List< Note > > noteList = slice( phrase, timePeriod );
			composition.add( noteList );
		}

		List< MusicBlock > musicBlocks = new ArrayList<>();
		for ( int musicBlockNumber = 0; musicBlockNumber < composition.get( 0 ).size(); musicBlockNumber ++ ) {
			List< List< Note > > musicBlockListOfNotes = new ArrayList<>();
			for ( List< List< Note > > instrumentPart : composition ) {
				musicBlockListOfNotes.add( instrumentPart.get( musicBlockNumber ) );
			}
			musicBlocks.add( new MusicBlock( musicBlockListOfNotes, null ) );
		}

		return musicBlocks;
	}

	/**
	 * Slice phrase into parts of timePeriod length
	 * @param phrase
	 * @param timePeriod
	 * @return
	 */
	public List< List< Note > > slice( Phrase phrase, double timePeriod ) {

		State state = new State( timePeriod );
		for( Note note : phrase.getNoteArray() ) {
			state.add( note );
		}
		return state.slices;
	}

	private class State {
		private List< List< Note > > slices = new ArrayList<>();
		private double timePeriod;
		private State( double timePeriod ) { this.timePeriod = timePeriod; }
		public void add( Note note ) {

			// Finding current state: slice and it's last note time
			List< Note > slice = null;
			double lastNoteEndTime = 0;
			if ( slices.size() == 0 ) {
				slice = new ArrayList<>();
				slices.add( slice );
				lastNoteEndTime = 0;
			} else {
				slice = slices.get( slices.size() - 1 );
				for ( Note currentSliceNote : slice ) {
					lastNoteEndTime += currentSliceNote.getRhythmValue();
				}
				// fulfill slice check
				if ( lastNoteEndTime == timePeriod ) {
					lastNoteEndTime = 0;
					slice = new ArrayList<>();
					slices.add( slice );
				}
			}

			// Adding note
			if ( lastNoteEndTime + note.getRhythmValue() <= timePeriod ) {
				Note newNote = new Note( note.getPitch(), note.getRhythmValue() );
				slice.add( newNote );
			} else {
				Note newNote = new Note( note.getPitch(), Utils.round( timePeriod - lastNoteEndTime ) );
				slice.add( newNote );

				Note newNoteToAdd = new Note( note.getPitch(), Utils.round( note.getRhythmValue() - newNote.getRhythmValue() ) );
				// Recursive call
				add( newNoteToAdd );
			}
		}
	}
}
