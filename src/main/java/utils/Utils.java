package utils;

import jm.music.data.Note;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by nigth wish on 25.08.14.
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
     * Rounds double
     * @param value
     * @return
     */
    public static double round( double value ) {
        double roundedValue = Math.round( value*100 )/100.;
        return roundedValue;
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

	public static boolean ListOfListsIsEquals( List< List< Note > > listOfLists1, List< List< Note > > listOfLists2 ) {
		if ( listOfLists1 == null || listOfLists2 == null || listOfLists1.size() != listOfLists2.size() ) {
			return false;
		}
		for ( int currentListeNumber = 0; currentListeNumber < listOfLists1.size(); currentListeNumber ++ ) {
			List< Note > noteList1 = listOfLists1.get( currentListeNumber );
			List< Note > noteList2 = listOfLists2.get( currentListeNumber );
			if ( !isEquals( noteList1, noteList2 ) ) {
				return false;
			}
		}
		return true;
	}
}
