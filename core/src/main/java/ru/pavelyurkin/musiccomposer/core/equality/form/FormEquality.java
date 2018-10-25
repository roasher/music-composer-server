package ru.pavelyurkin.musiccomposer.core.equality.form;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Class that decides if two Lists fo melodies are form equal
 */
@Component
public class FormEquality implements RelativelyComparable<List<InstrumentPart>>{

	/**
	 * Min value of equality metric to consider two blocks form equal
	 */
	@Value( "${FormEquality.instrumentEqualityPassThreshold}" )
	private double instrumentEqualityPassThreshold;
	/**
	 * Max value of equality metric to consider two blocks form different
	 */
	@Value( "${FormEquality.instrumentEqualityFailThreshold}" )
	private double instrumentEqualityFailThreshold;

	@Autowired
	private EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer;

	Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Consider if music blocks are form equal.
	 * Music blocks considered form equal if enough of their instrument parts is equal.
	 * @param firstMusicBlockInstrumentParts
	 * @param secondMusicBlockInstrumentParts
	 * @return
	 */
	@Override
	public Pair<ResultOfComparison, Double> isEqual( List<InstrumentPart> firstMusicBlockInstrumentParts, List<InstrumentPart> secondMusicBlockInstrumentParts ) {
		if ( firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size() ) {
			logger.info( "Input collections of melodies has different sizes so they can't be considered equal" );
			return Pair.of( ResultOfComparison.UNDEFINED, Double.MAX_VALUE );
		}

		double successTestPersentage = equalityMetricAnalyzer.getEqualityMetric( firstMusicBlockInstrumentParts, secondMusicBlockInstrumentParts );
		double diffMeasure;
		if ( successTestPersentage >= instrumentEqualityPassThreshold ) {
			diffMeasure = getMeasureOfDifference( successTestPersentage, instrumentEqualityPassThreshold );
			logger.debug( "Successful tests percentage {} higher than pass threshold {}. Diff measure = {}. Music Blocks considered form equal.", successTestPersentage, instrumentEqualityPassThreshold, diffMeasure );
			return Pair.of( ResultOfComparison.EQUAL, diffMeasure );
		} else if ( successTestPersentage <= instrumentEqualityFailThreshold ) {
			diffMeasure = getMeasureOfDifference( successTestPersentage, instrumentEqualityFailThreshold );
			logger.debug( "Successful tests percentage {} lower than fail threshold {}. Diff measure = {}. Music Blocks considered non equal", successTestPersentage, instrumentEqualityFailThreshold, diffMeasure );
			return Pair.of( ResultOfComparison.DIFFERENT, diffMeasure );
		} else {
			diffMeasure = Math.min( getMeasureOfDifference( successTestPersentage, instrumentEqualityFailThreshold ),
					getMeasureOfDifference( successTestPersentage, instrumentEqualityPassThreshold ) );
			logger.debug( "Successful tests percentage {} higher than the fail threshold {} but lower that pass threshold {}. Diff measure = {}. Blocks form equality considered undefined",
					successTestPersentage, instrumentEqualityFailThreshold, instrumentEqualityPassThreshold, diffMeasure );
			return Pair.of( ResultOfComparison.UNDEFINED, diffMeasure );
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
