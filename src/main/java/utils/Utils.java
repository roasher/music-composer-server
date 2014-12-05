package utils;

import jm.music.data.Note;
import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static jm.JMC.*;

/**
 * Created by night wish on 25.08.14.
 */
public class Utils {

	private static Logger logger = LoggerFactory.getLogger( Utils.class );

	private static final double maxRhythmValue;
	public static final List<Double> rhythmValues = new ArrayList<>();

	static {
		rhythmValues.add( WHOLE_NOTE );
		rhythmValues.add( DOTTED_HALF_NOTE );
		rhythmValues.add( DOUBLE_DOTTED_HALF_NOTE );
		rhythmValues.add( 2.5 );
		rhythmValues.add( HALF_NOTE );
		rhythmValues.add( HALF_NOTE_TRIPLET );
		rhythmValues.add( QUARTER_NOTE );
		rhythmValues.add( QUARTER_NOTE_TRIPLET );
		rhythmValues.add( DOTTED_QUARTER_NOTE );
		rhythmValues.add( DOUBLE_DOTTED_QUARTER_NOTE );
		rhythmValues.add( EIGHTH_NOTE );
		rhythmValues.add( DOTTED_EIGHTH_NOTE );
		rhythmValues.add( EIGHTH_NOTE_TRIPLET );
		rhythmValues.add( DOUBLE_DOTTED_EIGHTH_NOTE );

		rhythmValues.add( SIXTEENTH_NOTE );
		rhythmValues.add( DOTTED_SIXTEENTH_NOTE );
		rhythmValues.add( SIXTEENTH_NOTE_TRIPLET );

		rhythmValues.add( THIRTYSECOND_NOTE );
		rhythmValues.add( THIRTYSECOND_NOTE_TRIPLET );

		rhythmValues.add( 0. );

		// FIXME костыль. Таких значений не должно быть
		rhythmValues.add( 2.7 );
		rhythmValues.add( 0.8 );

		Collections.sort( rhythmValues );
		maxRhythmValue = Collections.max( rhythmValues );
	};

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

	/**
	 * Import of midi file can be not so precise as we want to
	 * This function rounds import value to the JMC library has
	 * @param rhythmValue
	 * @return
	 */
	public static double roundRhythmValue( double rhythmValue ) {
		// TODO Будут траблы со сложными лигами, например половина ноты на восьмую триоль. Нужно подумать.
		// How many max rhythm values can fit in input rhythmValue
		int maxRhythmValueNumber = ( int ) ( rhythmValue / maxRhythmValue );

		double valueWithinListRange = rhythmValue - maxRhythmValueNumber * maxRhythmValue;
		double roundRhythmValue = getClosestListElement( valueWithinListRange, rhythmValues ) + maxRhythmValueNumber * maxRhythmValue;

		return roundRhythmValue;
	}

	/**
	 * Returns list element that is closest to the value
	 * List must be ascending sorted
	 * @param value
	 * @param list
	 * @return
	 */
	public static double getClosestListElement( double value, List<Double> list ) {
//		logger.info( "input value = {}", value );
		int place = Collections.binarySearch( list, value );
		if ( place >= 0 ) {
			return list.get( place );
		} else {
			double top = rhythmValues.get( - place - 1 );
			double bottom = rhythmValues.get( - place -2 );
			if ( top - value < value - bottom ) {
				return top;
			}
			if ( top - value > value - bottom ) {
				return bottom;
			}
			throw new RuntimeException( "It is impossible to choose closest list element : there are 2 values having equal distance with " + value  );
		}
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

	public static boolean ListOfMelodiesIsEquals( List< Melody > firstMelodyList, List< Melody > secondMelodyList ) {
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

}
