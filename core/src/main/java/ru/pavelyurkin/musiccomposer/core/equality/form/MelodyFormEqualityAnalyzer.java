package ru.pavelyurkin.musiccomposer.core.equality.form;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer.MelodyEqualityAnalyzer;
import ru.pavelyurkin.musiccomposer.core.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Class analyzes if two melodies can belong to one form element
 * Created by night wish on 26.07.14.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MelodyFormEqualityAnalyzer implements MelodyEqualityAnalyzer {

    /**
     * Min percentage of passed sub tests necessary to consider ru.pavelyurkin.musiccomposer.equality of two melodies
     */
    @Value( "${MelodyFormEqualityAnalyzer.equalityTestPassThreshold}" )
    private double equalityTestPassThreshold;

	private final EqualityMetricAnalyzer<Melody> equalityMetricAnalyzer;


    public boolean isEqual( Melody firstMelody, Melody secondMelody ) {

		double positivePersentage = equalityMetricAnalyzer.getEqualityMetric( firstMelody, secondMelody );
        log.debug( "Percent of positive tests = {}, pass threshold = {}", positivePersentage, this.equalityTestPassThreshold );

        if ( equalityTestPassThreshold <= positivePersentage ) {
            log.debug( "Melodies considered to belong to same form element" );
            return true;
        } else {
            log.debug( "Melodies considered different in term of form" );
            return false;
        }
    }

}
