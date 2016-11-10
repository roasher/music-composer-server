package decomposer.form.analyzer;

import decomposer.melody.equality.Equality;
import decomposer.melody.analyzer.MelodyEqualityAnalyzer;
import model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;

/**
 * Class analyzes if two melodies can belong to one form element
 * Created by night wish on 26.07.14.
 */
public class MelodyFormEqualityAnalyzer implements MelodyEqualityAnalyzer {

    /**
     * Min percentage of passed sub tests necessary to consider equality of two melodies
     */
    private double equalityTestPassThreshold;

	@Autowired @Qualifier( "formIntervalsEqualityTestWrapper" )
    private Equality intervalsEquality;
	@Autowired @Qualifier( "formRhythmEqualityTestWrapper" )
    private Equality rhythmEquality;
	@Autowired @Qualifier( "formKeyEquality" )
	private Equality keyEquality;

    private Logger logger = LoggerFactory.getLogger( getClass() );

    public boolean isEqual( Melody firstMelody, Melody secondMelody ) {

		double positivePersentage = getEqualityMetric( firstMelody, secondMelody );
        logger.debug( "Percent of positive tests = {}, pass threshold = {}", positivePersentage, this.equalityTestPassThreshold );

        if ( equalityTestPassThreshold <= positivePersentage ) {
            logger.debug( "Melodies considered to belong to same form element" );
            return true;
        } else {
            logger.debug( "Melodies considered different in term of form" );
            return false;
        }
    }

	public double getEqualityMetric( Melody firstMelody, Melody secondMelody ) {

		List<Equality> tests = Arrays.asList( intervalsEquality, rhythmEquality, keyEquality
		);

		return tests.stream().mapToDouble( equalityTest -> equalityTest.getEqualityMetric( firstMelody, secondMelody ) ).average().getAsDouble();
	}

	public double getEqualityTestPassThreshold() {
		return equalityTestPassThreshold;
	}

	public void setEqualityTestPassThreshold( double equalityTestPassThreshold ) {
		this.equalityTestPassThreshold = equalityTestPassThreshold;
	}

	public Equality getIntervalsEquality() {
		return intervalsEquality;
	}

	public void setIntervalsEquality( Equality intervalsEquality ) {
		this.intervalsEquality = intervalsEquality;
	}

	public Equality getRhythmEquality() {
		return rhythmEquality;
	}

	public void setRhythmEquality( Equality rhythmEquality ) {
		this.rhythmEquality = rhythmEquality;
	}

	public Equality getKeyEquality() {
		return keyEquality;
	}

	public void setKeyEquality( Equality keyEquality ) {
		this.keyEquality = keyEquality;
	}
}
