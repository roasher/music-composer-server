package ru.pavelyurkin.musiccomposer.core.equality.form;

import javafx.util.Pair;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Class that decides if two Lists fo melodies are form equal
 */
public class FormEquality implements RelativelyComparable<List<Melody>>{

	/**
	 * Min value of equality metric to consider two blocks form equal
	 */
	private double instrumentEqualityPassThreshold;
	/**
	 * Max value of equality metric to consider two blocks form different
	 */
	private double instrumentEqualityFailThreshold;

	@Autowired
	private EqualityMetricAnalyzer<List<Melody>> equalityMetricAnalyzer;

	Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Consider if music blocks are form equal.
	 * Music blocks considered form equal if enough of their instrument parts is equal.
	 * @param firstMusicBlockInstrumentParts
	 * @param secondMusicBlockInstrumentParts
	 * @return
	 */
	@Override
	public Pair<ResultOfComparison, Double> isEqual( List<Melody> firstMusicBlockInstrumentParts, List<Melody> secondMusicBlockInstrumentParts ) {
		if ( firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size() ) {
			logger.info( "Input collections of melodies has different sizes so they can't be considered equal" );
			return new Pair<>( ResultOfComparison.UNDEFINED, Double.MAX_VALUE );
		}

		double successTestPersentage = equalityMetricAnalyzer.getEqualityMetric( firstMusicBlockInstrumentParts, secondMusicBlockInstrumentParts );
		double diffMeasure;
		if ( successTestPersentage >= instrumentEqualityPassThreshold ) {
			diffMeasure = getMeasureOfDifference( successTestPersentage, instrumentEqualityPassThreshold );
			logger.debug( "Successful tests percentage {} higher than pass threshold {}. Diff measure = {}. Music Blocks considered form equal.", successTestPersentage, instrumentEqualityPassThreshold, diffMeasure );
			return new Pair<>( ResultOfComparison.EQUAL, diffMeasure );
		} else if ( successTestPersentage <= instrumentEqualityFailThreshold ) {
			diffMeasure = getMeasureOfDifference( successTestPersentage, instrumentEqualityFailThreshold );
			logger.debug( "Successful tests percentage {} lower than fail threshold {}. Diff measure = {}. Music Blocks considered non equal", successTestPersentage, instrumentEqualityFailThreshold, diffMeasure );
			return new Pair<>( ResultOfComparison.DIFFERENT, diffMeasure );
		} else {
			diffMeasure = Math.min( getMeasureOfDifference( successTestPersentage, instrumentEqualityFailThreshold ),
					getMeasureOfDifference( successTestPersentage, instrumentEqualityPassThreshold ) );
			logger.debug( "Successful tests percentage {} higher than the fail threshold {} but lower that pass threshold {}. Diff measure = {}. Blocks form equality considered undefined",
					successTestPersentage, instrumentEqualityFailThreshold, instrumentEqualityPassThreshold, diffMeasure );
			return new Pair<>( ResultOfComparison.UNDEFINED, diffMeasure );
		}
	}

	public static double getMeasureOfDifference( double first, double second ) {
		double diff = Math.abs( first - second );
		if ( diff > 1 ) return 0;
		return 1 - diff;
	}

	@PostConstruct
	public void init() {
		if ( instrumentEqualityFailThreshold > instrumentEqualityPassThreshold ) {
			throw new IllegalArgumentException( "Illegal configuration of FormEqualityBean: fail threshold " + instrumentEqualityFailThreshold + " is greater than pass threshold "
			+ instrumentEqualityPassThreshold );
		}
	}

	public void setInstrumentEqualityPassThreshold( double instrumentEqualityPassThreshold ) {
		this.instrumentEqualityPassThreshold = instrumentEqualityPassThreshold;
	}

	public void setInstrumentEqualityFailThreshold( double instrumentEqualityFailThreshold ) {
		this.instrumentEqualityFailThreshold = instrumentEqualityFailThreshold;
	}
}
