package model.utils;

import jm.music.data.Note;
import jm.music.data.Score;
import model.Melody;
import model.MusicBlock;
import model.PlaceInTheComposition;
import model.composition.CompositionInfo;
import model.composition.Meter;

import java.io.IOException;
import java.io.InputStreamReader;
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
     * Summarises all rhythm_old values in the array
     * @param notes
     * @return
     */
    public static int sumAllRhytmValues( List< Note > notes ) {
        int rhytmSum = 0;
        for ( Note currentNote : notes ) {
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

	public static List<Melody> getMelodies( MusicBlock musicBlock ) {
		List<Melody> melodyList = new ArrayList<>(  );
		for ( List<Note> melodyNotes : musicBlock.getNotes() ) {
			Melody melody = new Melody( (Note[]) melodyNotes.toArray() );
			melody.setPlaceInTheComposition( new PlaceInTheComposition( musicBlock.getCompositionInfo(), 0, 0 ) );
			melodyList.add( melody );
//			Note[] notes = null;
//			melodyNotes.toArray( notes );
		}
		return melodyList;
	}
}
