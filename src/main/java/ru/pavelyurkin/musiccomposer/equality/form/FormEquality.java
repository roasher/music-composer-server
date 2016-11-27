package ru.pavelyurkin.musiccomposer.equality.form;

import ru.pavelyurkin.musiccomposer.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Class that decides if two Lists fo melodies are form equal
 */
public class FormEquality {

	private double instrumentEqualityPassThreshold;
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
	public boolean isEqual( List<Melody> firstMusicBlockInstrumentParts, List<Melody> secondMusicBlockInstrumentParts ) {
		if ( firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size() ) {
			logger.info( "Input collections of melodies has different sizes so they can't be considered equal" );
			return false;
		}

		double successTestPersentage = equalityMetricAnalyzer.getEqualityMetric( firstMusicBlockInstrumentParts, secondMusicBlockInstrumentParts );
		if ( successTestPersentage >= instrumentEqualityPassThreshold ) {
			logger.info( "Music Blocks considered form - equal" );
			return true;
		} else {
			logger.info( "Successfull tests persentage {} lower than the threshold {}. Music Blocks considered non equal", successTestPersentage, instrumentEqualityPassThreshold );
			return false;
		}
	}

	public double getInstrumentEqualityPassThreshold() {
		return instrumentEqualityPassThreshold;
	}

	public void setInstrumentEqualityPassThreshold( double instrumentEqualityPassThreshold ) {
		this.instrumentEqualityPassThreshold = instrumentEqualityPassThreshold;
	}
}
