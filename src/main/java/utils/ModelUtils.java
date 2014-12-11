package utils;

import jm.music.data.Note;
import jm.music.data.Score;
import model.MusicBlock;
import model.melody.Melody;
import model.composition.CompositionInfo;
import model.composition.Meter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class aggregates useful utilities upon Model objects
 * Created by Pavel Yurkin on 20.07.14.
 */
public class ModelUtils {
    /**
     * Retrieves interval pattern from note list, represents music vertical
     * @param notePitches
     * @return
     */
    public static List< Integer > getIntervalPattern( List< Integer > notePitches ) {
        // To prevent input List changing we will create a copy
        List< Integer > copyInputNotePiches = new ArrayList< Integer >( notePitches );

        Collections.sort( copyInputNotePiches );

        List< Integer > intervalPattern = new ArrayList<Integer>( copyInputNotePiches.size() - 1 );
        for ( int currentPitchNumber = 0; currentPitchNumber < copyInputNotePiches.size() - 1; currentPitchNumber++ ) {
            intervalPattern.add( copyInputNotePiches.get( currentPitchNumber + 1 ) - copyInputNotePiches.get( currentPitchNumber ) );
        }
        return intervalPattern;
    }

    /**
     * Summarises all rhythm values in the array
     * @param notes
     * @return
     */
    public static double sumAllRhytmValues( List< Note > notes ) {
        double rhytmSum = 0;
        for ( Note currentNote : notes ) {
            rhytmSum += currentNote.getRhythmValue();
        }
        return rhytmSum;
    }

	/**
	 * Summarises all rhythm values in the array
	 * @param melody
	 * @return
	 */
	public static double sumAllRhytmValues( Melody melody ) {
		double rhytmSum = 0;
		for ( Note currentNote : melody.getNoteArray() ) {
			rhytmSum += currentNote.getRhythmValue();
		}
		return rhytmSum;
	}

    public static CompositionInfo getCompositionInfo( Score score, String author, Meter meter ) {
        CompositionInfo compositionInfo = new CompositionInfo();
        compositionInfo.setTitle( score.getTitle() );
        compositionInfo.setTempo( score.getTempo() );
        compositionInfo.setAuthor( author );
        compositionInfo.setMetre( meter );
        return compositionInfo;
    }

//	/**
//	 * Wraps melodies into music blocks setting null to all fields other than melody list
//	 * Function made for testing purpose
//	 * @param melodyBlockList
//	 * @return
//	 */
//	public static List<MusicBlock > simpleWrap( List< List< Melody > > melodyBlockList ) {
//		List< MusicBlock > musicBlockList = new ArrayList<>(  );
//		for ( List< Melody > melodies : melodyBlockList ) {
//			musicBlockList.add( new MusicBlock( melodies, null ) );
//		}
//		return musicBlockList;
//	}

	public static double getMinRhythmValue( List< Melody > melodyList ) {
		double minRhythmValue = Double.MAX_VALUE;
		for ( Melody melody : melodyList ) {
			for ( Note note : melody.getNoteArray() ) {
				if ( note.getRhythmValue() < minRhythmValue ) {
					minRhythmValue = note.getRhythmValue();
				}
			}
		}
		return minRhythmValue;
	}
}
