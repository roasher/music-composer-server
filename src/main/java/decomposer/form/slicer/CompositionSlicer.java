package decomposer.form.slicer;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import model.MusicBlock;
import model.composition.Composition;
import org.springframework.stereotype.Component;
import utils.ModelUtils;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Slices composition into pieces
 * Created by night wish on 08.11.14.
 */
@Component
public class CompositionSlicer {

	/**
	 * Slices music blocks with certain timePeriod from input composition.
	 * @param composition
	 * @param timePeriod
	 * @return
	 */
	public List< MusicBlock > slice( Composition composition, double timePeriod ) {

		List< List< List< Note > > > compositionList = new ArrayList<>(  );

		for ( Part part : composition.getPartArray() ) {
			Phrase phrase = part.getPhraseArray()[0];
			List< List< Note > > noteList = slice( phrase, timePeriod );
			compositionList.add( noteList );
		}

		List< MusicBlock > musicBlocks = new ArrayList<>();
		for ( int musicBlockNumber = 0; musicBlockNumber < compositionList.get( 0 ).size(); musicBlockNumber ++ ) {
			List< List< Note > > musicBlockListOfNotes = new ArrayList<>();
			for ( List< List< Note > > instrumentPart : compositionList ) {
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
		// Filling last slice with rests if it shorter than time period
		List<Note> lastSlice = state.slices.get( state.slices.size() - 1 );
		double lastSliceRhythmValue = ModelUtils.sumAllRhytmValues( lastSlice );
		if ( lastSliceRhythmValue < timePeriod ) {
			lastSlice.add( new Note( JMC.REST, timePeriod - lastSliceRhythmValue ) );
		}
		return state.slices;
	}

	/**
	 * Class consumes notes and divide them into slices timePeriod length
	 */
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
				Note newNote = new Note( note.getPitch(), Utils.roundRhythmValue( timePeriod - lastNoteEndTime ) );
				slice.add( newNote );

				Note newNoteToAdd = new Note( note.getPitch(), Utils.roundRhythmValue( note.getRhythmValue() - newNote.getRhythmValue() ) );
				// Recursive call
				add( newNoteToAdd );
			}
		}
	}
}
