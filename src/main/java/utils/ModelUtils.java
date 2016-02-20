package utils;

import jm.constants.Pitches;
import jm.music.data.Note;
import jm.music.data.Score;
import model.ComposeBlock;
import model.melody.Melody;
import model.composition.CompositionInfo;
import model.composition.Meter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class aggregates useful utilities upon Model objects
 * Created by Pavel Yurkin on 20.07.14.
 */
public class ModelUtils {

	/**
	 * Checking that all melodies starts from the same time and returning that time
	 * @param melodyList
	 * @return
	 */
	public static double retrieveStartTime( List<Melody> melodyList ) {
		double currentStartTime = melodyList.get( 0 ).getStartTime();
		for ( int currentInstrument = 1; currentInstrument < melodyList.size(); currentInstrument++ ) {
			if ( currentStartTime != melodyList.get( currentInstrument ).getStartTime() ) {
				throw new IllegalArgumentException( String.format( "Several instrument parts has different start times, for example: 0 and %s ", currentInstrument ) );
			}
		}
		return currentStartTime;
	}

	/**
	 * Checking that all melodies has same rhythm value and returns this value
	 * @param melodyList
	 * @return
	 */
	public static double retrieveRhythmValue( List<Melody> melodyList ) {
		double currentRhytmValue = sumAllRhytmValues( melodyList.get( 0 ) );
		for ( int currentInstrument = 1; currentInstrument < melodyList.size(); currentInstrument++ ) {
			if ( currentRhytmValue != sumAllRhytmValues( melodyList.get( currentInstrument ) ) ) {
				throw new IllegalArgumentException( String.format( "Several instruments has different rhytmValues, for example: 0 and %s ", currentInstrument ) );
			}
		}
		return currentRhytmValue;
	}

	/**
	 * Retrieves interval pattern between first notes of melodies.
	 * @param melodyList
	 * @return
	 */
	public static List<Integer> retrieveFirstIntervalPattern( List<Melody> melodyList ) {
		List<Integer> firstVertical = new ArrayList<Integer>();
		for ( int currentInstrument = 0; currentInstrument < melodyList.size(); currentInstrument++ ) {
			firstVertical.add( melodyList.get( currentInstrument ).getPitchArray()[0] );
		}
		return retrieveIntervalPattern( firstVertical );
	}

	/**
	 * Retrieves interval pattern between last notes of melodies.
	 * @param melodyList
	 * @return
	 */
	public static List<Integer> retrieveLastIntervalPattern( List<Melody> melodyList ) {
		List<Integer> lastVertical = new ArrayList<Integer>();
		for ( int currentInstrument = 0; currentInstrument < melodyList.size(); currentInstrument++ ) {
			int lastNoteNumber = melodyList.get( currentInstrument ).size() - 1;
			lastVertical.add( melodyList.get( currentInstrument ).getPitchArray()[lastNoteNumber] );
		}
		return retrieveIntervalPattern( lastVertical );
	}

    /**
     * Retrieves interval pattern from note list, represents music vertical
     * @param notePitches
     * @return
     */
    public static List< Integer > retrieveIntervalPattern( List<Integer> notePitches ) {
        // To prevent input List changing we will create a copy
        List< Integer > copyInputNotePitches = new ArrayList< Integer >( notePitches );

        Collections.sort( copyInputNotePitches );

        List< Integer > intervalPattern = new ArrayList<Integer>( copyInputNotePitches.size() - 1 );
        for ( int currentPitchNumber = 0; currentPitchNumber < copyInputNotePitches.size() - 1; currentPitchNumber++ ) {
            intervalPattern.add( copyInputNotePitches.get( currentPitchNumber + 1 ) - copyInputNotePitches.get( currentPitchNumber ) );
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
		for ( Note currentNote : ( List<Note> ) melody.getNoteList() ) {
			rhytmSum += currentNote.getRhythmValue();
		}
		return rhytmSum;
	}

    public static CompositionInfo buildCompositionInfo( Score score, String author, Meter meter ) {
        CompositionInfo compositionInfo = new CompositionInfo();
        compositionInfo.setTitle( score.getTitle() );
        compositionInfo.setTempo( score.getTempo() );
        compositionInfo.setAuthor( author );
        compositionInfo.setMetre( meter );
        return compositionInfo;
    }

	public static double getMinRhythmValue( List< Melody > melodyList ) {
		double minRhythmValue = Double.MAX_VALUE;
		for ( Melody melody : melodyList ) {
			for ( Note note : ( List<Note> ) melody.getNoteList() ) {
				if ( note.getRhythmValue() < minRhythmValue ) {
					minRhythmValue = note.getRhythmValue();
				}
			}
		}
		return minRhythmValue;
	}

	public static String getNoteNameByPitch( int pitch ) {
		Class claz = Pitches.class;
		for ( Field field : claz.getDeclaredFields() ) {
			try {
				if ( field.getInt( null ) == pitch ) {
					return field.getName();
				}
			} catch ( IllegalAccessException e ) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * Returns pitch on which second compose block should be transposed according to first
	 * @param firstComposeBlock
	 * @param secondComposeBlock
	 * @return
	 */
	public static int getTransposePitch( Optional<ComposeBlock> firstComposeBlock, ComposeBlock secondComposeBlock ) {
		if ( !firstComposeBlock.isPresent() ) return 0;
		for ( int melodyNumber = 0; melodyNumber < firstComposeBlock.get().getMelodyList().size(); melodyNumber++ ) {
			Note lastNoteOfFirst = firstComposeBlock.get().getMelodyList().get( melodyNumber )
					.getNote( firstComposeBlock.get().getMelodyList().get( melodyNumber ).size() - 1 );
			Note firstNoteOfSecond = secondComposeBlock.getMelodyList().get( melodyNumber ).getNote( 0 );
			if ( lastNoteOfFirst.getPitch() != Note.REST && firstNoteOfSecond.getPitch() != Note.REST ) {
				return lastNoteOfFirst.getPitch() + secondComposeBlock.getBlockMovementFromPreviousToThis().getVoiceMovements().get( melodyNumber )
						- firstNoteOfSecond.getPitch();
			}
		}
		return 0;
	}

	public static List<Melody> trimToTime( List<Melody> melodies, double startTime, double endTime ) {
		return melodies.stream().map( melody -> trimToTime( melody, startTime, endTime ) ).collect( Collectors.toList() );
	}

	/**
	 * TODO stress tests
	 * @param melody
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Melody trimToTime( Melody melody, double startTime, double endTime ) {
		double noteStartTime = 0;
		Melody out = new Melody(  );
		for ( int noteNumber = 0; noteNumber < melody.getNoteList().size(); noteNumber++ ) {
			Note currentNote = ( Note ) melody.getNote( noteNumber );
			if ( noteStartTime < startTime && noteStartTime + currentNote.getRhythmValue() > startTime ) {
				out.add( new Note( currentNote.getPitch(), noteStartTime + currentNote.getRhythmValue() - startTime ) );
			} else if ( noteStartTime > startTime && noteStartTime + currentNote.getRhythmValue() < endTime ) {
				out.add( currentNote );
			} else if ( noteStartTime < endTime && noteStartTime + currentNote.getRhythmValue() > endTime ) {
				out.add( new Note( currentNote.getPitch(), endTime - noteStartTime ) );
				return out;
			}
			noteStartTime += currentNote.getRhythmValue();
		}
		return out;
	}
}
