package decomposer.form.analyzer;

import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyurkin on 12.11.14.
 */
public class FormEqualityAnalyser {

	private double instrumentEqualityPassThreshold;
	@Autowired
	private MelodyFormEqualityAnalyzer formEqualityAnalyzer;

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

		double successTestPersentage = getAverageEqualityMetric( firstMusicBlockInstrumentParts, secondMusicBlockInstrumentParts );
		if ( successTestPersentage >= instrumentEqualityPassThreshold ) {
			logger.info( "Music Blocks considered form - equal" );
			return true;
		} else {
			logger.info( "Successfull tests persentage {} lower than the threshold {}. Music Blocks considered non equal", successTestPersentage, instrumentEqualityPassThreshold );
			return false;
		}
	}

	/**
	 * Returns numberOfEqualInstrumentParts devided by parts number
	 * @param firstMusicBlockInstrumentParts
	 * @param secondMusicBlockInstrumentParts
	 * @return
	 */
	@Deprecated
	private double getEqualityMetric( List<Melody> firstMusicBlockInstrumentParts, List<Melody> secondMusicBlockInstrumentParts ) {
		if ( firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size() ) {
			throw new RuntimeException( "Input collections of melodies has different sizes" );
		}
		int numberOfEqualInstrumentParts = 0;
		for ( int instrumentPartNumber = 0; instrumentPartNumber < firstMusicBlockInstrumentParts.size(); instrumentPartNumber ++ ) {
			if ( formEqualityAnalyzer.isEqual( firstMusicBlockInstrumentParts.get( instrumentPartNumber ), secondMusicBlockInstrumentParts.get( instrumentPartNumber ) ) ) {
				numberOfEqualInstrumentParts++;
			}
		}

		return numberOfEqualInstrumentParts * 1. / firstMusicBlockInstrumentParts.size();
	}

	/**
	 * Returns average of equality metrics from each part
	 * @param firstMusicBlockInstrumentParts
	 * @param secondMusicBlockInstrumentParts
	 * @return
	 */
	public double getAverageEqualityMetric( List<Melody> firstMusicBlockInstrumentParts, List<Melody> secondMusicBlockInstrumentParts ) {
		if ( firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size() ) {
			throw new RuntimeException( "Input collections of melodies has different sizes" );
		}
		List<Double> equalityMetrics = new ArrayList<>(  );
		for ( int instrumentPartNumber = 0; instrumentPartNumber < firstMusicBlockInstrumentParts.size(); instrumentPartNumber ++ ) {
			equalityMetrics.add( formEqualityAnalyzer.getEqualityMetric( firstMusicBlockInstrumentParts.get( instrumentPartNumber ), secondMusicBlockInstrumentParts.get( instrumentPartNumber ) ) );
		}

		return equalityMetrics.stream().mapToDouble( Double::doubleValue ).average().getAsDouble();
	}

	public double getInstrumentEqualityPassThreshold() {
		return instrumentEqualityPassThreshold;
	}

	public void setInstrumentEqualityPassThreshold( double instrumentEqualityPassThreshold ) {
		this.instrumentEqualityPassThreshold = instrumentEqualityPassThreshold;
	}
}
