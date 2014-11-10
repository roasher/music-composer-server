package decomposer.analyzer.form;

import decomposer.analyzer.melody.equality.EqualityTest;
import decomposer.analyzer.signature.SignatureEqualityAnalyzer;
import model.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class analyzes if two signatures can be considered equal
 * Created by night wish on 26.07.14.
 */
@Component
public class FormEqualityAnalyzerImpl implements SignatureEqualityAnalyzer {

    /**
     * Min percentage of passed sub tests necessary to consider equality of two signatures
     */
    private double equalityTestPassThreshold;

	@Autowired @Qualifier( "formIntervalsEqualityTest" )
    private EqualityTest intervalsEqualityTest;
	@Autowired @Qualifier( "formRhythmEqualityTest" )
    private EqualityTest rhythmEqualityTest;
	@Autowired @Qualifier( "formKeyEqualityTest" )
	private EqualityTest keyEqualityTest;

    private Logger logger = LoggerFactory.getLogger( getClass() );

    public boolean isEqual( Signature firstSignature, Signature secondSignature ) {

		EqualityTest[] testArray = new EqualityTest[] {
		  intervalsEqualityTest,
		  rhythmEqualityTest,
		  keyEqualityTest
		};
		int numberOfTestsPassed = 0;
		int numberOfTestsFailed = 0;

		for ( int currentTestNumber = 0; currentTestNumber < testArray.length;  currentTestNumber ++ ) {
			boolean testPassed = testArray[ currentTestNumber ].test( firstSignature, secondSignature );
			if ( testPassed ) {
				numberOfTestsPassed++;
				logger.debug( "{} test succeed", testArray[ currentTestNumber ].getClass().getSimpleName() );
			} else {
				numberOfTestsFailed++;
				logger.debug( "{} test failed", testArray[ currentTestNumber ].getClass().getSimpleName() );
			}
			if ( ( testArray.length - numberOfTestsFailed )/testArray.length < equalityTestPassThreshold ) {
				logger.debug( "Number of failed test is too high - {}. Aborting others", numberOfTestsFailed );
			}
		}

        double positivePersentage = 1.0*numberOfTestsPassed/testArray.length;
        logger.debug( "Percent of positive tests = {}, pass threshold = {}", positivePersentage, this.equalityTestPassThreshold );

        if ( equalityTestPassThreshold <= positivePersentage ) {
            logger.debug( "Signatures considered equal" );
            return true;
        } else {
            logger.debug( "Signatures considered different" );
            return false;
        }
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
