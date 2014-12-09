package utils;

import jm.JMC;
import jm.music.data.Note;
import model.melody.Melody;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by night wish on 25.08.14.
 */
public class Utils {

    /**
     * Waits for input, so one can see and analyze nonated smth
     */
    public static void suspend() {
        // Pausing to analyze
        InputStreamReader inputStreamReader = new InputStreamReader( System.in );
        try {
            inputStreamReader.read();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Rounds double to second decimal
     * @param value
     * @return
     */
    private static double round( double value, int decimal ) {
		double multiplyer = Math.pow( 10, decimal );
        double roundedValue = Math.round( value*multiplyer )/multiplyer;
        return roundedValue;
    }

	public static boolean ListOfMelodyBlocksIsEquals( List< List< Melody > > firstMelodyBlockList, List< List< Melody > > secondMelodyBlockList ) {
		if ( firstMelodyBlockList == null || secondMelodyBlockList == null || firstMelodyBlockList.size() != secondMelodyBlockList.size() ) {
			return false;
		}
		for ( int currentMelodyBlockNumber = 0; currentMelodyBlockNumber < firstMelodyBlockList.size(); currentMelodyBlockNumber ++ ) {
			List< Melody > firstMelodyBlock = firstMelodyBlockList.get( currentMelodyBlockNumber );
			List< Melody > secondMelodyBlock = secondMelodyBlockList.get( currentMelodyBlockNumber );
			if ( !listOfMelodiesIsEquals( firstMelodyBlock, secondMelodyBlock ) ) {
				return false;
			}
		}
		return true;
	}

	public static boolean listOfMelodiesIsEquals( List<Melody> firstMelodyList, List<Melody> secondMelodyList ) {
		if ( firstMelodyList == null || secondMelodyList == null || firstMelodyList.size() != secondMelodyList.size() ) {
			return false;
		}
		for ( int currentListeNumber = 0; currentListeNumber < firstMelodyList.size(); currentListeNumber ++ ) {
			List< Note > noteList1 = firstMelodyList.get( currentListeNumber ).getNoteList();
			List< Note > noteList2 = secondMelodyList.get( currentListeNumber ).getNoteList();
			if ( !isEquals( noteList1, noteList2 ) ) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEquals( List< Note > noteList1, List< Note > noteList2 ) {
		if ( noteList1 == null || noteList2 == null || noteList1.size() != noteList2.size() ) {
			return false;
		}
		for ( int currentNoteNumber = 0; currentNoteNumber < noteList1.size(); currentNoteNumber ++ ) {
			Note note1 = noteList1.get( currentNoteNumber );
			Note note2 = noteList2.get( currentNoteNumber );
			if ( !note1.equals( note2 ) ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Recombining melody block into smaller lists.
	 * In result this ends with <b>list</b> of music blocks that has exact notes that input block has
	 * but melodies will be divided note by note so there will be no pitch changing in any of result melody blocks
	 *
	 * @param inputMelodyBlockList
	 * @return
	 */
	public static List< List< Melody > > recombineMelodyBlock( List<Melody> inputMelodyBlockList ) {

		// The edge set
		Set< Double > edgeSet = new HashSet<>(  );
		for ( Melody melody : inputMelodyBlockList ) {
			edgeSet.addAll( getEdgeList( melody ) );
		}

		Double[] edgeArray = edgeSet.toArray( new Double[0] );
		Arrays.sort( edgeArray );

		List< List< Melody > > melodyBlockList = new ArrayList<>(  );
		double prevoiusEdge = 0;
		for ( double edge : edgeArray ) {
			List< Melody > melodyBlock = new ArrayList<>(  );
			for ( Melody melody : inputMelodyBlockList ) {
				int noteNumber = getNoteNumber( melody.getNoteArray(), edge );
				Note notePlayingAtThisMoment = melody.getNote( noteNumber );

				Melody newMelody = new Melody( new Note[]{ new Note( notePlayingAtThisMoment.getPitch(), edge - prevoiusEdge ) } );
				newMelody.setForm( melody.getForm() );
				newMelody.setStartTime( prevoiusEdge + melody.getStartTime() );
				melodyBlock.add( newMelody );
			}
			prevoiusEdge = edge;
			melodyBlockList.add( melodyBlock );
		}

		return melodyBlockList;
	}

	/**
	 * Returns the edge list of melody notes
	 * Sets first edge to rhythm value of the first note, and incrementing this value by
	 * rhythm value of all notes one by one
	 * @param melody
	 * @return
	 */
	public static List< Double > getEdgeList( Melody melody ) {
		List< Double > edgeList = new ArrayList<>();
		double lastEdge = 0;
		for ( int noteNumber = 0; noteNumber < melody.size(); noteNumber ++ ) {
			Note note = melody.getNote( noteNumber );
			edgeList.add( lastEdge + note.getRhythmValue() );
			lastEdge = edgeList.get( edgeList.size() - 1 );
		}
		return edgeList;
	}

	/**
	 * Returns number of the note in the Note Array that sounds in particular time
	 * If input time is finish time to one and start time to another, the first one will be returned
	 * @param notes - note array
	 * @param time
	 * @return
	 */
	public static int getNoteNumber( Note[] notes, double time ) {
		double startTime = 0;
		for ( int currentNoteNumber = 0; currentNoteNumber < notes.length; currentNoteNumber ++ ) {
			double rhythm = notes[ currentNoteNumber ].getRhythmValue();
			if ( startTime < time && time <= startTime + rhythm ) {
				return currentNoteNumber;
			}
			startTime += rhythm;
		}
		return notes.length + 1;
	}
}
