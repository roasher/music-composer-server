package utils;

import jm.music.data.Note;
import jm.music.data.Part;
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
	 * TODO refactor using binary search
	 */
	public static int getNoteNumber( List<Note> notes, double time ) {
		double startTime = 0;
		for ( int currentNoteNumber = 0; currentNoteNumber < notes.size(); currentNoteNumber ++ ) {
			double rhythm = notes.get( currentNoteNumber ).getRhythmValue();
			if ( startTime < time && time <= startTime + rhythm ) {
				return currentNoteNumber;
			}
			startTime += rhythm;
		}
		return notes.size() + 1;
	}

	public static int getDecimalPlaces( double value ) {
		String[] places = String.valueOf( value ).split( "\\." );
		return places[1].length() == 1 && places[1].charAt( 0 ) == '0' ? 0 : places[1].length();
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

//	public static boolean areLexiconsSimilar( Lexicon firstLexicon, Lexicon secondLexicon ) {
//		if ( firstLexicon.getMinRhythmValue() != secondLexicon.getMinRhythmValue() ) return false;
//		// Compositions check
//		if ( firstLexicon.getCompositionsInLexicon().size() != secondLexicon.getCompositionsInLexicon().size() ) return false;
//		for ( CompositionInfo firstLexiconCompositionInfo : firstLexicon.getCompositionsInLexicon() ) {
//			boolean found = false;
//			for ( CompositionInfo secondLexiconCompositionInfo : secondLexicon.getCompositionsInLexicon() ) {
//				if ( firstLexiconCompositionInfo.equals( secondLexiconCompositionInfo ) ) found = true;
//			}
//			if ( !found ) return false;
//		}
//		// Compose blocks check
//		for ( int composeBlockNumber = 0; composeBlockNumber < firstLexicon.getComposeBlockList().size(); composeBlockNumber++ ) {
//			ComposeBlock firstComposeBlock = firstLexicon.getComposeBlockList().get( composeBlockNumber );
//			ComposeBlock secondComposeBlock = secondLexicon.getComposeBlockList().get( composeBlockNumber );
//			if ( firstComposeBlock.isSimilar(  ))
//		}
//		return true;
//	}
}
