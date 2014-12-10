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
        // Pausing for human analysis
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
			Melody firstMelody = firstMelodyList.get( currentListeNumber );
			Melody secondMelody = secondMelodyList.get( currentListeNumber );
			if ( !firstMelody.equals( secondMelody ) ) {
				return false;
			}
		}
		return true;
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
