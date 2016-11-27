package ru.pavelyurkin.musiccomposer.equality.equalityMetric;

import ru.pavelyurkin.musiccomposer.model.melody.Melody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FormEqualityMetricAnalyzer implements EqualityMetricAnalyzer<List<Melody>> {

	@Autowired
	@Qualifier("melodyMetricEqualityAnalyzer")
	private EqualityMetricAnalyzer<Melody> equlityMetricAnalyzer;

	@Override
	/**
	 * Returns average of ru.pavelyurkin.musiccomposer.equality metrics from each part
	 * @param firstMusicBlockInstrumentParts
	 * @param secondMusicBlockInstrumentParts
	 * @return
	 */
	public double getEqualityMetric( List<Melody> firstMusicBlockInstrumentParts, List<Melody> secondMusicBlockInstrumentParts ) {
		if ( firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size() ) {
			throw new RuntimeException( "Input collections of melodies has different sizes" );
		}
		List<Double> equalityMetrics = new ArrayList<>(  );
		for ( int instrumentPartNumber = 0; instrumentPartNumber < firstMusicBlockInstrumentParts.size(); instrumentPartNumber ++ ) {
			equalityMetrics.add( equlityMetricAnalyzer.getEqualityMetric( firstMusicBlockInstrumentParts.get( instrumentPartNumber ), secondMusicBlockInstrumentParts.get( instrumentPartNumber ) ) );
		}

		return equalityMetrics.stream().mapToDouble( Double::doubleValue ).average().getAsDouble();
	}

}
