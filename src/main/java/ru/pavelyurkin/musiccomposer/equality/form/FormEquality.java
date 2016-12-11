package ru.pavelyurkin.musiccomposer.equality.form;

import ru.pavelyurkin.musiccomposer.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
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
	public ResultOfComparison isEqual( List<Melody> firstMusicBlockInstrumentParts, List<Melody> secondMusicBlockInstrumentParts ) {
		if ( firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size() ) {
			logger.info( "Input collections of melodies has different sizes so they can't be considered equal" );
			return ResultOfComparison.UNDEFINED;
		}

		double successTestPersentage = equalityMetricAnalyzer.getEqualityMetric( firstMusicBlockInstrumentParts, secondMusicBlockInstrumentParts );
		if ( successTestPersentage >= instrumentEqualityPassThreshold ) {
			logger.debug( "Successfull tests persentage {} higher than pass threshold {}. Music Blocks considered form equal", successTestPersentage, instrumentEqualityPassThreshold );
			return ResultOfComparison.EQUAL;
		} else if ( successTestPersentage <= instrumentEqualityFailThreshold ) {
			logger.debug( "Successfull tests persentage {} lower than fail threshold {}. Music Blocks considered non equal", successTestPersentage, instrumentEqualityPassThreshold );
			return ResultOfComparison.DIFFERENT;
		} else {
			logger.debug( "Successfull tests persentage {} higher than the fail threshold {} but lower that pass threshold {}. Blocks form equality considered undefined",
					successTestPersentage, instrumentEqualityPassThreshold, instrumentEqualityFailThreshold );
			return ResultOfComparison.UNDEFINED;
		}
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
