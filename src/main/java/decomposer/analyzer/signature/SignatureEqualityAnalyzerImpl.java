package decomposer.analyzer.signature;

import decomposer.analyzer.melody.equality.*;
import model.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class analyzes if two signatures can be considered equal
 * Created by night wish on 26.07.14.
 */
@Component
public class SignatureEqualityAnalyzerImpl implements SignatureEqualityAnalyzer {

    /**
     * Min percentage of passed sub tests necessary to consider equality of two signatures
     */
    private double equalityTestPassThreshold;

//    private EqualityTest fragmentationEqualityTest;
//    private EqualityTest interpolationEqualityTest;

    private EqualityTest countourEqualityTest;
    private EqualityTest intervalsEqualityTest;
    private EqualityTest inversionEqualityTest;
    private EqualityTest orderEqualityTest;
    private EqualityTest rhythmEqualityTest;

    private Logger logger = LoggerFactory.getLogger( getClass() );

    public boolean isEqual( Signature firstSignature, Signature secondSignature ) {
        // If signatures size abs difference more than fragmentationEqualityTest.
//        int absSizeDifference = Math.abs( firstSignature.getSize() - secondSignature.getSize() );
//        if ( absSizeDifference > interpolationEqualityTest.getMaxNumberOfDiversedNotes() || absSizeDifference > fragmentationEqualityTest.getMaxNumberOfDiversedNotes() ) {
//            return false;
//        }
//
//        logger.debug( "Starting signature comparison {} {}", firstSignature.toString(), secondSignature.toString() );
//        boolean fragmentationTest = fragmentationEqualityTest.test( firstSignature, secondSignature );
//        if ( fragmentationTest ) {
//            logger.debug( "Fragmentation test succeed. No more tests required, signatures considered equal." );
//            return true;
//        }
//
//        boolean interpolationTest = interpolationEqualityTest.test( firstSignature, secondSignature );
//        if ( interpolationTest ) {
//            logger.debug( "Interpolation test succeed. No more tests required, signatures considered equal." );
//            return true;
//        }

        boolean rhythmTest = rhythmEqualityTest.test( firstSignature, secondSignature );
        if ( !rhythmTest ) {
            logger.debug( "rhythm_old test failed. No more tests required, signatures considered not equal." );
            return false;
        }

		EqualityTest[] testArray = new EqualityTest[] {
		  countourEqualityTest,
		  intervalsEqualityTest,
		  inversionEqualityTest,
		  orderEqualityTest,
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

//    public EqualityTest getFragmentationEqualityTest() {
//        return fragmentationEqualityTest;
//    }
//
//    public void setFragmentationEqualityTest( EqualityTest fragmentationEqualityTest ) {
//        this.fragmentationEqualityTest = fragmentationEqualityTest;
//    }
//
//    public EqualityTest getInterpolationEqualityTest() {
//        return interpolationEqualityTest;
//    }
//
//    public void setInterpolationEqualityTest( EqualityTest interpolationEqualityTest ) {
//        this.interpolationEqualityTest = interpolationEqualityTest;
//    }

    public EqualityTest getCountourEqualityTest() {
        return countourEqualityTest;
    }

    public void setCountourEqualityTest( EqualityTest countourEqualityTest ) {
        this.countourEqualityTest = countourEqualityTest;
    }

    public EqualityTest getIntervalsEqualityTest() {
        return intervalsEqualityTest;
    }

    public void setIntervalsEqualityTest( EqualityTest intervalsEqualityTest ) {
        this.intervalsEqualityTest = intervalsEqualityTest;
    }

    public EqualityTest getInversionEqualityTest() {
        return inversionEqualityTest;
    }

    public void setInversionEqualityTest( EqualityTest inversionEqualityTest ) {
        this.inversionEqualityTest = inversionEqualityTest;
    }

    public EqualityTest getOrderEqualityTest() {
        return orderEqualityTest;
    }

    public void setOrderEqualityTest( EqualityTest orderEqualityTest ) {
        this.orderEqualityTest = orderEqualityTest;
    }

    public EqualityTest getRhythmEqualityTest() {
        return rhythmEqualityTest;
    }

    public void setRhythmEqualityTest(EqualityTest rhythmEqualityTest) {
        this.rhythmEqualityTest = rhythmEqualityTest;
    }
}
