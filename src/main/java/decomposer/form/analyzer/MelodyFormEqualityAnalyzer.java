package decomposer.form.analyzer;

import decomposer.melody.equality.EqualityTest;
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
    private EqualityTest intervalsEqualityTest;
	@Autowired @Qualifier( "formRhythmEqualityTestWrapper" )
    private EqualityTest rhythmEqualityTest;
	@Autowired @Qualifier( "formKeyEqualityTest" )
	private EqualityTest keyEqualityTest;

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

		List<EqualityTest> tests = Arrays.asList(
				intervalsEqualityTest,
				rhythmEqualityTest,
				keyEqualityTest
		);

		return tests.stream().mapToDouble( equalityTest -> equalityTest.getEqualityMetric( firstMelody, secondMelody ) ).average().getAsDouble();
	}

	public double getEqualityTestPassThreshold() {
		return equalityTestPassThreshold;
	}

	public void setEqualityTestPassThreshold( double equalityTestPassThreshold ) {
		this.equalityTestPassThreshold = equalityTestPassThreshold;
	}

	public EqualityTest getIntervalsEqualityTest() {
		return intervalsEqualityTest;
	}

	public void setIntervalsEqualityTest( EqualityTest intervalsEqualityTest ) {
		this.intervalsEqualityTest = intervalsEqualityTest;
	}

	public EqualityTest getRhythmEqualityTest() {
		return rhythmEqualityTest;
	}

	public void setRhythmEqualityTest( EqualityTest rhythmEqualityTest ) {
		this.rhythmEqualityTest = rhythmEqualityTest;
	}

	public EqualityTest getKeyEqualityTest() {
		return keyEqualityTest;
	}

	public void setKeyEqualityTest( EqualityTest keyEqualityTest ) {
		this.keyEqualityTest = keyEqualityTest;
	}
}
