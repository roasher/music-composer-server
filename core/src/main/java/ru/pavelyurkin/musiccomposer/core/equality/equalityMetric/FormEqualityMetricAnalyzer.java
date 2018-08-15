package ru.pavelyurkin.musiccomposer.core.equality.equalityMetric;

import lombok.RequiredArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FormEqualityMetricAnalyzer implements EqualityMetricAnalyzer<List<Melody>> {

	private final EqualityMetricAnalyzer<Melody> equalityMetricAnalyzer;

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
			equalityMetrics.add( equalityMetricAnalyzer.getEqualityMetric( firstMusicBlockInstrumentParts.get( instrumentPartNumber ), secondMusicBlockInstrumentParts.get( instrumentPartNumber ) ) );
		}

		return equalityMetrics.stream().mapToDouble( Double::doubleValue ).average().getAsDouble();
	}

}
