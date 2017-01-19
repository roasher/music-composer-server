package ru.pavelyurkin.musiccomposer.core.equality.form;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer.MelodyEqualityAnalyzer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class analyzes if two melodies can belong to one form element
 * Created by night wish on 26.07.14.
 */
@Component
public class MelodyFormEqualityAnalyzer implements MelodyEqualityAnalyzer {

    /**
     * Min percentage of passed sub tests necessary to consider ru.pavelyurkin.musiccomposer.equality of two melodies
     */
    @Value( "${MelodyFormEqualityAnalyzer.equalityTestPassThreshold}" )
    private double equalityTestPassThreshold;

	@Autowired
	private EqualityMetricAnalyzer<Melody> equalityMetricAnalyzer;

    private Logger logger = LoggerFactory.getLogger( getClass() );

    public boolean isEqual( Melody firstMelody, Melody secondMelody ) {

		double positivePersentage = equalityMetricAnalyzer.getEqualityMetric( firstMelody, secondMelody );
        logger.debug( "Percent of positive tests = {}, pass threshold = {}", positivePersentage, this.equalityTestPassThreshold );

        if ( equalityTestPassThreshold <= positivePersentage ) {
            logger.debug( "Melodies considered to belong to same form element" );
            return true;
        } else {
            logger.debug( "Melodies considered different in term of form" );
            return false;
        }
    }

	public double getEqualityTestPassThreshold() {
		return equalityTestPassThreshold;
	}

	public void setEqualityTestPassThreshold( double equalityTestPassThreshold ) {
		this.equalityTestPassThreshold = equalityTestPassThreshold;
	}

}
