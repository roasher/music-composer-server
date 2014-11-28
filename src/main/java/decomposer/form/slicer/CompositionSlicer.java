package decomposer.form.slicer;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import model.MusicBlock;
import model.composition.Composition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import utils.ModelUtils;
import utils.Utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Slices composition into pieces
 * Created by night wish on 08.11.14.
 */
@Component
public class CompositionSlicer {

	Logger logger = LoggerFactory.getLogger( getClass() );
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

		// Composition list should has equal number of slices in each instrument
		int nubmerOfSlices = compositionList.get( 0 ).size();
		for ( List<List<Note>> slices : compositionList ) {
			if ( slices.size() != nubmerOfSlices ) throw new RuntimeException( "Sliced composition has differed number of slices for different instrument." );
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
		// Adding rests if phrase starts not from the beginning
		if ( Double.compare( phrase.getStartTime(), 0 ) != 0. ) {
			state.add( new Note( JMC.REST, phrase.getStartTime() ) );
		}

		for( Note note : phrase.getNoteArray() ) {
			state.add( note );
		}
		// Filling last slice with rests if it shorter than time period
		List<Note> lastSlice = state.slices.get( state.slices.size() - 1 );
		double lastSliceRhythmValue = ModelUtils.sumAllRhytmValues( lastSlice );
		if ( lastSliceRhythmValue < timePeriod ) {
			lastSlice.add( new Note( JMC.REST, timePeriod - lastSliceRhythmValue ) );
		}

		checkSlicesOccupancy( state.slices, timePeriod );
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
				Note newNote = new Note( note.getPitch(), timePeriod - lastNoteEndTime );
				slice.add( newNote );

				Note newNoteToAdd = new Note( note.getPitch(), note.getRhythmValue() - newNote.getRhythmValue() );
				// Recursive call
//				logger.info( "pitch {}, rhythm value {}", newNoteToAdd.getPitch(), newNoteToAdd.getRhythmValue() );
				add( newNoteToAdd );
			}
		}
	}

	/**
	 * Checks if all slices has length equal to timePeriod
	 * @param slices
	 * @param timePeriod
	 */
	public void checkSlicesOccupancy( List<List<Note>> slices, double timePeriod ) {
		for ( List<Note> slice : slices ) {
			BigDecimal rhythmValuesSum = BigDecimal.ZERO;
			for ( Note sliceNote : slice ) {
				rhythmValuesSum = rhythmValuesSum.add( BigDecimal.valueOf( sliceNote.getRhythmValue() ) );
			}
			if ( rhythmValuesSum.round( MathContext.DECIMAL32 ).compareTo( BigDecimal.valueOf( timePeriod ) ) != 0 ) {
				throw new RuntimeException( "Slice occupancy check failed" );
			}
		}
	}
}
