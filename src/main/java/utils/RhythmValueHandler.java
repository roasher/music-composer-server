package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static jm.constants.Durations.*;
import static jm.constants.Durations.THIRTYSECOND_NOTE;
import static jm.constants.Durations.THIRTYSECOND_NOTE_TRIPLET;

/**
 * Class aggregates logic of rounding rhythm values after loading the comosition
 * Created by pyurkin on 08.12.14.
 */
@Component
public class RhythmValueHandler {

	private Logger logger = LoggerFactory.getLogger( Utils.class );

	private final double maxRhythmValue;
	public final List<Double> rhythmValues = new ArrayList<>();

	public RhythmValueHandler() {
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
	 * Import of midi file can be not so precise as we want to
	 * This function rounds import value to the JMC library has
	 * @param rhythmValue
	 * @return
	 */
	public double roundRhythmValue( double rhythmValue ) {
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
	public double getClosestListElement( double value, List<Double> list ) {
		//		logger.info( "input value = {}", value );
		int place = Collections.binarySearch( list, value );
		if ( place >= 0 ) {
			return list.get( place );
		} else {
			double top = rhythmValues.get( - place - 1 );
			double bottom = rhythmValues.get( - place - 2 );
			if ( top - value < value - bottom ) {
				return top;
			}
			if ( top - value > value - bottom ) {
				return bottom;
			}
			throw new RuntimeException( "It is impossible to choose closest list element : there are 2 values having equal distance with " + value  );
		}
	}
}
