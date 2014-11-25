package decomposer.melody.analyzer;

import decomposer.melody.equality.*;
import model.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class analyzes if two melodies can be considered equal
 * Created by night wish on 26.07.14.
 */
@Component
public class MelodyEqualityAnalyzerImpl implements MelodyEqualityAnalyzer {

    /**
     * Min percentage of passed sub tests necessary to consider equality of two melodies
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

    public boolean isEqual( Melody firstMelody, Melody secondMelody ) {
        // If melodies size abs difference more than fragmentationEqualityTest.
//        int absSizeDifference = Math.abs( firstMelody.getSize() - secondMelody.getSize() );
//        if ( absSizeDifference > interpolationEqualityTest.getMaxNumberOfDiversedNotes() || absSizeDifference > fragmentationEqualityTest.getMaxNumberOfDiversedNotes() ) {
//            return false;
//        }
//
//        logger.debug( "Starting melody comparison {} {}", firstMelody.toString(), secondMelody.toString() );
//        boolean fragmentationTest = fragmentationEqualityTest.test( firstMelody, secondMelody );
//        if ( fragmentationTest ) {
//            logger.debug( "Fragmentation test succeed. No more tests required, melodies considered equal." );
//            return true;
//        }
//
//        boolean interpolationTest = interpolationEqualityTest.test( firstMelody, secondMelody );
//        if ( interpolationTest ) {
//            logger.debug( "Interpolation test succeed. No more tests required, melodies considered equal." );
//            return true;
//        }

        boolean rhythmTest = rhythmEqualityTest.test( firstMelody, secondMelody );
        if ( !rhythmTest ) {
            logger.debug( "rhythm_old test failed. No more tests required, melodies considered not equal." );
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
			boolean testPassed = testArray[ currentTestNumber ].test( firstMelody, secondMelody );
			if ( testPassed ) {
				numberOfTestsPassed++;
				logger.debug( "{} test succeed", testArray[ currentTestNumber ].getClass().getSimpleName() );
			} else {
				numberOfTestsFailed++;
				logger.debug( "{} test failed", testArray[ currentTestNumber ].getClass().getSimpleName() );
			}
			if ( 1 - numberOfTestsFailed*1./testArray.length < equalityTestPassThreshold ) {
				logger.debug( "Number of failed tes t is too high - {}. Aborting others", numberOfTestsFailed );
				return false;
			}
		}

        double positivePersentage = 1.0*numberOfTestsPassed/testArray.length;
        logger.debug( "Percent of positive tests = {}, pass threshold = {}", positivePersentage, this.equalityTestPassThreshold );

        if ( equalityTestPassThreshold <= positivePersentage ) {
            logger.debug( "Melodies considered equal" );
            return true;
        } else {
            logger.debug( "Melodies considered different" );
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
