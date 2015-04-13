package utils;

import composer.CompositionStep;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import model.ComposeBlock;
import model.MusicBlock;
import model.composition.Composition;
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
		double multiplier = Math.pow( 10, decimal );
        double roundedValue = Math.round( value*multiplier )/multiplier;
        return roundedValue;
    }

	public static boolean ListOfListsOfMelodiesAreEqual( List<List<Melody>> firstListOfListsOfMelodies, List<List<Melody>> secondListOfListsOfMelodies ) {
		if ( firstListOfListsOfMelodies == null || secondListOfListsOfMelodies == null || firstListOfListsOfMelodies.size() != secondListOfListsOfMelodies.size() ) {
			return false;
		}
		for ( int currentMelodyBlockNumber = 0; currentMelodyBlockNumber < firstListOfListsOfMelodies.size(); currentMelodyBlockNumber ++ ) {
			List< Melody > firstMelodyBlock = firstListOfListsOfMelodies.get( currentMelodyBlockNumber );
			List< Melody > secondMelodyBlock = secondListOfListsOfMelodies.get( currentMelodyBlockNumber );
			if ( !listOfMelodiesAreEqual( firstMelodyBlock, secondMelodyBlock ) ) {
				return false;
			}
		}
		return true;
	}

	public static boolean listOfMelodiesAreEqual( List<Melody> firstMelodyList, List<Melody> secondMelodyList ) {
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


	public static boolean containsMelody( MusicBlock firstMusicBlock, List<MusicBlock> musicBlockList ) {
		for ( MusicBlock musicBlock : musicBlockList ) {
			if ( listOfMelodiesAreEqual( musicBlock.getMelodyList(), firstMusicBlock.getMelodyList() ) ) {
				return true;
			};
		}
		return false;
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
	 * Returns composition, build on input compose blocks
	 * @param composeBlockList
	 * @return
	 */
	public static Composition gatherComposition( List<ComposeBlock> composeBlockList ) {
		List<Part> parts = new ArrayList<>();
		for ( int partNumber = 0; partNumber < composeBlockList.get( 0 ).getMusicBlock().getMelodyList().size(); partNumber++ ) {
			parts.add( new Part() );
		}
		for ( ComposeBlock composeBlock : composeBlockList ) {
			for ( int partNumber = 0; partNumber < parts.size(); partNumber++ ) {
				int melodiesAmount = parts.get( partNumber ).size();
				Melody melody = composeBlock.getMusicBlock().getMelodyList().get( partNumber );
				Melody newMelody = null;
//				if ( melodiesAmount == 0 ) {
					// First melody in partNumber part
					newMelody = new Melody( melody.getNoteArray() );
//				} else {
				// TODO Need to clone all music blocks before gathering composition if we want to change rhythm values of some
//					// Need to bind first note of melody with previous if it has same pitch
//					Note newPhraseFirstNote = melody.getNoteArray()[0];
//					Phrase previousPhrase = parts.get( partNumber ).getPhrase( melodiesAmount - 1 );
//					Note previousPhraseLastNote = previousPhrase.getNote( previousPhrase.getNoteArray().length - 1 );
//					if ( newPhraseFirstNote.getPitch() == previousPhraseLastNote.getPitch() ) {
//						previousPhraseLastNote.setRhythmValue( previousPhraseLastNote.getRhythmValue() + newPhraseFirstNote.getRhythmValue(), true );
//						Note[] newNoteArray = Arrays.copyOfRange( melody.getNoteArray(), 1, melody.getNoteArray().length );
//						if ( newNoteArray.length == 0 ) {
//							continue;
//						}
//						newMelody = new Melody( newNoteArray );
//					} else {
//						newMelody = new Melody( melody.getNoteArray() );
//					}
//				}
				parts.get( partNumber ).add( newMelody );
			}
		}
		Composition composition = new Composition( parts );
		return composition;
	}

    /**
	 * Returns possible variants of how length be combined with by input double - quantity map
	 * @param doubleQuantityMap - map of values and it's quantities
	 * @param length
	 * @return
	 */
	public static List<List<Double>> getVariantsOfDistribution( Map<Double,Integer> doubleQuantityMap, double length ) {
		List<List<Double>> output = new ArrayList<>(  );
		getVariantsOfDistributionWrapper( new ArrayList<Double>(  ), doubleQuantityMap, length, output );
		Collections.sort( output, new Comparator<List<Double>>() {
			@Override
			public int compare( List<Double> o1, List<Double> o2 ) {
				return o1.size() - o2.size();
			}
		} );
		return output;
	}

	/**
	 * Recursive help function.
	 * @param analyzedList - constant left part of the toAnalyzeMap
	 * @param toAnalyzeMap - right part of the toAnalyzeMap which we are going to iterate
	 * @param value - decreasing value, that we want to reach by summing array elements
	 * @param results - global variable, which contains successfull lists
	 */
	private static void getVariantsOfDistributionWrapper( ArrayList<Double> analyzedList, Map<Double,Integer> toAnalyzeMap, double value, List<List<Double>> results ) {
		// Brute Force
		if ( toAnalyzeMap.isEmpty() ) {
			return;
		}

		for ( Map.Entry<Double,Integer> entry : toAnalyzeMap.entrySet() ) {

			if ( entry.getValue() == null || entry.getValue() == 0 ) {
				continue;
			}

			ArrayList<Double> analyzed = new ArrayList<>( analyzedList );
			Map<Double,Integer> toAnalyze = new HashMap<>( toAnalyzeMap );

			Double listValue = entry.getKey();
			if ( listValue == value ) {
				analyzed.add( listValue );
				results.add( analyzed );
				continue;
			}
			if ( listValue > value ) {
				continue;
			}


			analyzed.add( listValue );
			toAnalyze.put( entry.getKey(), entry.getValue() - 1 );
			getVariantsOfDistributionWrapper( analyzed, toAnalyze, value - listValue, results );
		}
	}
}
