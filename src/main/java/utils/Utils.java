package utils;

import composer.CompositionStep;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import model.MusicBlock;
import model.composition.Composition;
import model.composition.CompositionInfo;
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
			if ( !listOfMelodiesAreEquals( firstMelodyBlock, secondMelodyBlock ) ) {
				return false;
			}
		}
		return true;
	}

	public static boolean listOfMelodiesAreEquals( List<Melody> firstMelodyList, List<Melody> secondMelodyList ) {
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

	public static int getDecimalPlaces( double value ) {
		String[] places = String.valueOf( value ).split( "\\." );
		return places[1].length() == 1 && places[1].charAt( 0 ) == '0' ? 0 : places[1].length();
	}

	/**
	 * Returns composition, build on input music blocks
	 * @param musicBlockList
	 * @return
	 */
	public static Composition gatherComposition( List<MusicBlock> musicBlockList ) {
		List<Part> parts = new ArrayList<>();
		for ( int partNumber = 0; partNumber < musicBlockList.get( 0 ).getMelodyList().size(); partNumber++ ) {
			parts.add( new Part() );
		}
		for ( MusicBlock musicBlock : musicBlockList ) {
			for ( int partNumber = 0; partNumber < parts.size(); partNumber++ ) {
				int melodiesAmount = parts.get( partNumber ).size();
				Melody melody = musicBlock.getMelodyList().get( partNumber );
				Melody newMelody = null;
				if ( melodiesAmount == 0 ) {
					// First melody in partNumber part
					newMelody = new Melody( melody.getNoteArray() );
				} else {
					// Need to bind first note of melody with previous if it has same pitch
					Note newPhraseFirstNote = musicBlock.getMelodyList().get( partNumber ).getNoteArray()[0];
					Phrase previousPhrase = parts.get( partNumber ).getPhrase( melodiesAmount - 1 );
					Note previousPhraseLastNote = previousPhrase.getNote( previousPhrase.getNoteArray().length - 1 );
					if ( newPhraseFirstNote.getPitch() == previousPhraseLastNote.getPitch() ) {
						previousPhraseLastNote.setRhythmValue( previousPhraseLastNote.getRhythmValue() + newPhraseFirstNote.getRhythmValue(), true );
						Note[] newNoteArray = Arrays.copyOfRange( melody.getNoteArray(), 1, melody.getNoteArray().length );
						if ( newNoteArray.length == 0 ) {
							continue;
						}
						newMelody = new Melody( newNoteArray );
					} else {
						newMelody = new Melody( melody.getNoteArray() );
					}
				}
				parts.get( partNumber ).add( newMelody );
			}
		}
		Composition composition = new Composition( parts );
		return composition;
	}

	/**
	 * Gathers all music blocks in one music block, setting composition info.
	 * @param musicBlockList
	 * @param info
	 * @return
	 */
	public static MusicBlock gatherMusicBlocks( List<MusicBlock> musicBlockList, CompositionInfo info ) {
		if ( musicBlockList != null && musicBlockList.size() > 0 ) {
			List<Melody> melodyList = new ArrayList<>(  );
			for ( int melodyNubmer = 0; melodyNubmer < musicBlockList.get( 0 ).getMelodyList().size(); melodyNubmer ++ ) {
				melodyList.add( new Melody(  ) );
			}
			for ( MusicBlock currentMusicBlock : musicBlockList ) {
				for ( int melodyNumber = 0; melodyNumber < currentMusicBlock.getMelodyList().size(); melodyNumber ++ ) {
					melodyList.get( melodyNumber ).addNoteList( currentMusicBlock.getMelodyList().get( melodyNumber ).getNoteArray() );
				}
			}
			return new MusicBlock( melodyList, info );
		} else {
			return null;
		}
	}

	public static List<MusicBlock> getMusicBlocksList( List<CompositionStep> compositionSteps ) {
		List<MusicBlock> musicBlockList = new ArrayList<>(  );
		for ( CompositionStep compositionStep : compositionSteps ) {
			musicBlockList.add( compositionStep.getMusicBlock() );
		}
		return musicBlockList;
	}

	/**
	 * Returns possible variants of how lenght can be combines using input list elements
	 * @param list
	 * @param length
	 * @return
	 */
	public static List<List<Double>> getVariantsOfDistribution( List<Double> list, double length ) {
		// TODO implementation
		return null;
	}
}
