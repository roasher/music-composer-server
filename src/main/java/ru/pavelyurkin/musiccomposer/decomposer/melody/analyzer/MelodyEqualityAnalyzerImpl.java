package ru.pavelyurkin.musiccomposer.decomposer.melody.analyzer;

import ru.pavelyurkin.musiccomposer.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
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
     * Min percentage of passed sub tests necessary to consider ru.pavelyurkin.musiccomposer.equality of two melodies
     */
    private double equalityTestPassThreshold;

//    private Equality fragmentationEqualityTest;
//    private Equality interpolationEqualityTest;

    private Equality countourEquality;
    private Equality intervalsEquality;
    private Equality inversionEquality;
    private Equality orderEquality;
    private Equality rhythmEquality;

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

        boolean rhythmTest = rhythmEquality.test( firstMelody, secondMelody );
        if ( !rhythmTest ) {
            logger.debug( "rhythm_old test failed. No more tests required, melodies considered not equal." );
            return false;
        }

		Equality[] testArray = new Equality[] { countourEquality, intervalsEquality, inversionEquality, orderEquality,
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
				logger.debug( "Number of failed test is too high - {}. Aborting others", numberOfTestsFailed );
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

//    public Equality getFragmentationEqualityTest() {
//        return fragmentationEqualityTest;
//    }
//
//    public void setFragmentationEqualityTest( Equality fragmentationEqualityTest ) {
//        this.fragmentationEqualityTest = fragmentationEqualityTest;
//    }
//
//    public Equality getInterpolationEqualityTest() {
//        return interpolationEqualityTest;
//    }
//
//    public void setInterpolationEqualityTest( Equality interpolationEqualityTest ) {
//        this.interpolationEqualityTest = interpolationEqualityTest;
//    }

    public Equality getCountourEquality() {
        return countourEquality;
    }

    public void setCountourEquality( Equality countourEquality ) {
        this.countourEquality = countourEquality;
    }

    public Equality getIntervalsEquality() {
        return intervalsEquality;
    }

    public void setIntervalsEquality( Equality intervalsEquality ) {
        this.intervalsEquality = intervalsEquality;
    }

    public Equality getInversionEquality() {
        return inversionEquality;
    }

    public void setInversionEquality( Equality inversionEquality ) {
        this.inversionEquality = inversionEquality;
    }

    public Equality getOrderEquality() {
        return orderEquality;
    }

    public void setOrderEquality( Equality orderEquality ) {
        this.orderEquality = orderEquality;
    }

    public Equality getRhythmEquality() {
        return rhythmEquality;
    }

    public void setRhythmEquality(Equality rhythmEquality ) {
        this.rhythmEquality = rhythmEquality;
    }
}
