package utils;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Rest;
import model.melody.Melody;
import model.MusicBlock;
import model.composition.Composition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import utils.ModelUtils;

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
	 * Slices composition into melodies, having certain timePeriod each.
	 * @param composition
	 * @param timePeriod
	 * @return
	 */
	public List< List< Melody > > slice( Composition composition, double timePeriod ) {

		List< List< Melody > > compositionList = new ArrayList<>(  );

        adjustToUnifiedEndTime( composition );
		for ( Part part : composition.getPartArray() ) {
			Phrase phrase = part.getPhraseArray()[0];
			List< Melody > noteList = slice( phrase, timePeriod );
			compositionList.add( noteList );
		}

		// Composition list should has equal number of slices in each instrument
		int numberOfSlices = compositionList.get( 0 ).size();
		for ( List<Melody> slices : compositionList ) {
			if ( slices.size() != numberOfSlices ) throw new RuntimeException( "Sliced composition has differed number of slices for different instrument." );
		}

		List< List< Melody > > compositionMelodies = new ArrayList<>();
		for ( int melodyBlockNumber = 0; melodyBlockNumber < compositionList.get( 0 ).size(); melodyBlockNumber ++ ) {
			List<Melody> melodyBlock = new ArrayList<>();
			for ( List< Melody > instrumentPart : compositionList ) {
				melodyBlock.add( instrumentPart.get( melodyBlockNumber ) );
			}
			compositionMelodies.add( melodyBlock );
		}

		return compositionMelodies;
	}

	/**
	 * Slice phrase into parts of timePeriod length
	 * @param phrase
	 * @param timePeriod
	 * @return
	 */
	public List< Melody > slice( Phrase phrase, double timePeriod ) {

		State state = new State( timePeriod );
		// Adding rests if phrase starts not from the beginning
		if ( Double.compare( phrase.getStartTime(), 0 ) != 0. ) {
			state.add( new Note( JMC.REST, phrase.getStartTime() ) );
		}

        for ( int noteNumber = 0; noteNumber < phrase.size(); noteNumber ++ ) {
            Note note = phrase.getNote( noteNumber );
            state.add( phrase.getNote( noteNumber ) );
        }

//		for( Note note : phrase.getNoteArray() ) {
//			state.add( note );
//		}
		// Filling last slice with rests if it shorter than time period
		Melody lastSlice = state.slices.get( state.slices.size() - 1 );
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
		private List< Melody > slices = new ArrayList<>();
		private double timePeriod;
		private State( double timePeriod ) { this.timePeriod = timePeriod; }

		public void add( Note note ) {
			// Finding current state: slice and it's last note time
			Melody slice = null;
			double lastNoteEndTime = 0;
			if ( slices.size() == 0 ) {
				slice = new Melody();
				slices.add( slice );
				lastNoteEndTime = 0;
			} else {
				slice = slices.get( slices.size() - 1 );
				for ( Note currentSliceNote : ( List<Note> ) slice.getNoteList() ) {
					lastNoteEndTime += currentSliceNote.getRhythmValue();
				}
				// fulfill slice check
				if ( lastNoteEndTime == timePeriod ) {
					lastNoteEndTime = 0;
					slice = new Melody();
					slice.setStartTime( slices.get( slices.size() - 1 ).getStartTime() + timePeriod );
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
	public void checkSlicesOccupancy( List<Melody> slices, double timePeriod ) {
		for ( Melody slice : slices ) {
			BigDecimal rhythmValuesSum = BigDecimal.ZERO;
			for ( Note sliceNote : ( List<Note> ) slice.getNoteList() ) {
				rhythmValuesSum = rhythmValuesSum.add( BigDecimal.valueOf( sliceNote.getRhythmValue() ) );
			}
			if ( rhythmValuesSum.round( MathContext.DECIMAL32 ).compareTo( BigDecimal.valueOf( timePeriod ) ) != 0 ) {
				throw new RuntimeException( "Slice occupancy check failed" );
			}
		}
	}

    /**
     * Makes all phrases has equal end time by adding rests if needed
     * @param composition
     */
    public void adjustToUnifiedEndTime( Composition composition ) {
        double compositionEndTime = composition.getEndTime();
        for ( Part part : composition.getPartArray() ) {
            Phrase phrase = part.getPhrase( 0 );
            if ( phrase.getEndTime() != compositionEndTime ) {
                phrase.add( new Rest( compositionEndTime - phrase.getEndTime() ) );
            }
        }
    }
}
