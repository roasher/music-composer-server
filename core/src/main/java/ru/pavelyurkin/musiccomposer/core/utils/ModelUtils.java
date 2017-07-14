package ru.pavelyurkin.musiccomposer.core.utils;

import jm.constants.Pitches;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
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

	public static double sumAllRhythmValues( List<MusicBlock> musicBlocks ) {
		return musicBlocks.stream().mapToDouble( MusicBlock::getRhythmValue ).sum();
	}

	public static double getRhythmValue( Composition composition ) {
		return composition.getPartList().stream()
				.mapToDouble( part -> sumAllRhytmValues( ( List<Note> )
						( ( Part ) part ).getPhraseList().stream()
						.flatMap( phrase -> ( ( Phrase ) phrase ).getNoteList().stream() )
						.collect( Collectors.toList() ) ) )
				.max()
				.orElse( 0 );
	}

	public static String getNoteNameByPitch( int pitch ) {
		return Arrays.stream( Pitches.class.getDeclaredFields() )
				.filter( field -> {
					try {
						return field.getInt( null ) == pitch;
					} catch ( IllegalAccessException e ) {
						return false;
					}
				} )
				.map( Field::getName )
				.sorted( Comparator.comparingInt( String::length ) )
				.findFirst()
				.map( name -> name.charAt( 0 ) == 'F' ?
						'F' + replace(name.substring( 1 )) :
						replace( name ) )
				.orElse( "" );
	}

	private static String replace(String note) {
		return note.replace( "F", "b" ).replace( "S", "#" );
	}

	/**
	 * Returns pitch on which second block should be transposed according to first
     * To be able to calculate transpose pitch there should be at least one pair of non rest notes
     * in lastNoteOfFirst and firstNoteOfSecond blocks and also firstNoteOfSecond movementFromPreviousToThis
     * shouldn't be movement from rest
	 * @param firstBlock
	 * @param secondBlock
	 * @return
	 */
	public static int getTransposePitch( Optional<MusicBlock> firstBlock, MusicBlock secondBlock ) {
		if ( !firstBlock.isPresent() ) return 0;
		// check if all pauses
		if ( secondBlock.getMelodyList()
				.stream()
				.flatMap( melody -> melody.getNoteList().stream() )
				.filter( note ->  !( ( Note ) note ).isRest() ).count() == 0 ) return 0;
		for ( int melodyNumber = 0; melodyNumber < firstBlock.get().getMelodyList().size(); melodyNumber++ ) {
			Note lastNoteOfFirst = firstBlock.get().getMelodyList().get( melodyNumber )
					.getNote( firstBlock.get().getMelodyList().get( melodyNumber ).size() - 1 );
			Note firstNoteOfSecond = secondBlock.getMelodyList().get( melodyNumber ).getNote( 0 );
			int noteMovement = secondBlock.getBlockMovementFromPreviousToThis().getVoiceMovements().get(melodyNumber);
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
	 * @param blocks
	 * @return
	 */
	public static MusicBlock gatherBlocksWithTransposition(List<MusicBlock> blocks) {
		List<MusicBlock> transposedBlocks = new ArrayList<>(  );
		transposedBlocks.add( blocks.get( 0 ) );
		for ( int blockNumber = 1; blockNumber < blocks.size(); blockNumber++ ) {
			MusicBlock previousBlock = blocks.get( blockNumber - 1 );
			MusicBlock currentBlock = blocks.get( blockNumber );
			transposedBlocks.add( currentBlock.transposeClone( previousBlock ) );
		}
		return new MusicBlock( transposedBlocks );
	}

	public static Note clone( Note note ) {
		return new Note( note.getPitch(), note.getRhythmValue(), note.getDynamic(), note.getPan() );
	}

	public static Phrase clone( Phrase phrase ) {
		Note[] clonedNotes = ( Note[] ) phrase.getNoteList()
				.stream()
				.map( note -> clone( ( Note ) note ) )
				.toArray( Note[]:: new );
		return new Phrase( clonedNotes );
	}

	/**
	 * Returns blocks that are similar or different in terms of form
	 * @param formCompositionSteps
	 * @param form
	 * @param similar - if true returns similar, different otherwise
	 * @return
	 */
	public static List<MusicBlock> getRelativeFormBlocks( List<FormCompositionStep> formCompositionSteps, Form form, boolean similar ) {
		return formCompositionSteps
				.stream()
				.filter( formCompositionStep -> similar == form.equals( formCompositionStep.getForm() ) )
				.map( formCompositionStep -> new MusicBlock( formCompositionStep.getCompositionSteps()
						.stream()
						.map( CompositionStep::getTransposedBlock )
						.collect( Collectors.toList() ) ) )
				.collect( Collectors.toList());
	}

	public static boolean isExactEquals( List<Melody> firstMelodies, List<Melody> secondMelodies ) {
		if ( firstMelodies.size() != secondMelodies.size() ) return false;
		for ( int melodyNumber = 0; melodyNumber < firstMelodies.size(); melodyNumber++ ) {
			if ( !firstMelodies.get( melodyNumber ).isExactEquals( secondMelodies.get( melodyNumber ) ) ) {
				return false;
			}
		}
		return true;
	}
}
