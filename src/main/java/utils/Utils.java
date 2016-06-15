package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
