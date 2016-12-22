package ru.pavelyurkin.musiccomposer.core.utils;

import jm.constants.Pitches;
import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
			Integer currentPitch = copyInputNotePitches.get(currentPitchNumber);
			Integer nextPitch = copyInputNotePitches.get( currentPitchNumber + 1 );
			if ( ( currentPitch != Note.REST && nextPitch == Note.REST ) || ( currentPitch == Note.REST && nextPitch != Note.REST ) ) {
				intervalPattern.add( Note.REST );
			} else {
				intervalPattern.add( nextPitch - currentPitch );
			}
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
		return sumAllRhytmValues( melody.getNoteList() );
	}

	public static double sumAllRhythmValues( List<ComposeBlock> musicBlocks ) {
		return musicBlocks.stream().mapToDouble( ComposeBlock::getRhythmValue ).sum();
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
     * To be able to calculate transpose pitch there should be at least one pair of non rest notes
     * in lastNoteOfFirst and firstNoteOfSecond ComposeBlocks and also firstNoteOfSecond movementFromPreviousToThis
     * shouldn't be movement from rest
	 * @param firstComposeBlock
	 * @param secondComposeBlock
	 * @return
	 */
	public static int getTransposePitch( Optional<ComposeBlock> firstComposeBlock, ComposeBlock secondComposeBlock ) {
		if ( !firstComposeBlock.isPresent() ) return 0;
		// check if all pauses
		if ( secondComposeBlock.getMelodyList()
				.stream()
				.flatMap( melody -> melody.getNoteList().stream() )
				.filter( note ->  !( ( Note ) note ).isRest() ).count() == 0 ) return 0;
		for ( int melodyNumber = 0; melodyNumber < firstComposeBlock.get().getMelodyList().size(); melodyNumber++ ) {
			Note lastNoteOfFirst = firstComposeBlock.get().getMelodyList().get( melodyNumber )
					.getNote( firstComposeBlock.get().getMelodyList().get( melodyNumber ).size() - 1 );
			Note firstNoteOfSecond = secondComposeBlock.getMelodyList().get( melodyNumber ).getNote( 0 );
			int noteMovement = secondComposeBlock.getBlockMovementFromPreviousToThis().getVoiceMovements().get(melodyNumber);
			if ( lastNoteOfFirst.getPitch() != Note.REST && firstNoteOfSecond.getPitch() != Note.REST &&
                    noteMovement != BlockMovement.MOVEMENT_FROM_REST) {
				return lastNoteOfFirst.getPitch() + noteMovement - firstNoteOfSecond.getPitch();
			}
		}
		throw new IllegalArgumentException("Can't calculate pitch to transpose");
	}

	public static List<Melody> trimToTime( List<Melody> melodies, double startTime, double endTime ) {
		return melodies.stream().map( melody -> trimToTime( melody, startTime, endTime ) ).collect( Collectors.toList() );
	}

	/**
	 * @param melody
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Melody trimToTime( Melody melody, double startTime, double endTime ) {
		if ( startTime < 0 || endTime > melody.getRythmValue()  || startTime > endTime ) {
			throw new IllegalArgumentException( "Cant trim with this parameters: startTime = " + startTime + " ,endTime = " + endTime );
		}
		BigDecimal noteStartTime = BigDecimal.ZERO;
		Melody out = new Melody(  );
		for ( int noteNumber = 0; noteNumber < melody.size(); noteNumber++ ) {
			Note currentNote = melody.getNote( noteNumber );
			BigDecimal rhythmValue = BigDecimal.valueOf( currentNote.getRhythmValue() );
			BigDecimal noteEndTime = noteStartTime.add( rhythmValue );
			//   noteStTime             noteEndTime					noteEndTime may be after endTime
			// ------<>--------|------------<>------------------|----------<>-------
			//	 			startTime		  				endTime
			if ( noteStartTime.doubleValue() <= startTime && noteEndTime.doubleValue() > startTime ) {
				BigDecimal min = BigDecimal.valueOf( Math.min( endTime, noteEndTime.doubleValue() ) );
				out.add( new Note( currentNote.getPitch(), min.subtract( BigDecimal.valueOf( startTime ) ).doubleValue() ) );
			} else
				//             noteStTime     noteEndTime
				// ------|--------<>------------<>------------------|-------------------
				//	 startTime		  				              endTime
				if ( noteStartTime.doubleValue() >= startTime && noteEndTime.doubleValue() <= endTime ) {
				out.add( currentNote );
			} else
				//             noteStTime     								noteEndTime
				// ------|--------<>---------------------------------|-----------<>-------
				//	 startTime		  				              endTime
				if ( noteStartTime.doubleValue() < endTime && noteEndTime.doubleValue() > endTime ) {
				out.add( new Note( currentNote.getPitch(), BigDecimal.valueOf( endTime ).subtract( noteStartTime ).doubleValue() ) );
				return out;
			}
			noteStartTime = noteStartTime.add( rhythmValue );
		}
		return out;
	}

	/**
	 * Check if two times have equal "strength"
	 *
	 * @param firstStartTime
	 * @param secondStartTime
	 * @return
	 */
	public static boolean isTimeCorrelated( double firstStartTime, double secondStartTime ) {
//		return true;
		//TODO Disabled for testing purposes (Don't know if needed decide later)
		int originStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( firstStartTime );
		int substitutorStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( secondStartTime );
		if ( originStartTimeDecimalPlacesNumber == substitutorStartTimeDecimalPlacesNumber ) {
			return true;
		}
		return false;
	}

	/**
	 * Needle blocks from first to the last transposing due to block movement. Solid Block at the end.
	 * @param composeBlocks
	 * @return
	 */
	public static ComposeBlock gatherBlocksWithTransposition(List<ComposeBlock> composeBlocks) {
		List<ComposeBlock> transposedBlocks = new ArrayList<>(  );
		transposedBlocks.add( composeBlocks.get( 0 ) );
		for ( int blockNumber = 1; blockNumber < composeBlocks.size(); blockNumber++ ) {
			ComposeBlock previousBlock = composeBlocks.get( blockNumber - 1 );
			ComposeBlock currentBlock = composeBlocks.get( blockNumber );
			int transposePitch = getTransposePitch( Optional.of( previousBlock ), currentBlock );
			transposedBlocks.add( currentBlock.transposeClone( transposePitch ) );
		}
		return new ComposeBlock( transposedBlocks );
	}
}
