package decomposer.analyzer.melody;

import jm.util.View;
import model.Melody;
import utils.Utils;

/**
 * Class wraps MelodyEqualityAnalyzer adding functionality to view successed melodies
 * Created by Pavel Yurkin on 08.08.14.
 */
public class MelodyEqualityAnalyzerNotator implements MelodyEqualityAnalyzer {

    private MelodyEqualityAnalyzerImpl analyzer;
    private boolean notateSuccessful;

    public MelodyEqualityAnalyzerImpl getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer( MelodyEqualityAnalyzerImpl analyzer ) {
        this.analyzer = analyzer;
    }

    public boolean isNotateSuccessful() {
        return notateSuccessful;
    }

    public void setNotateSuccessful( boolean notateSuccessful ) {
        this.notateSuccessful = notateSuccessful;
    }

    @Override
    public boolean isEqual( Melody firstMelody, Melody secondMelody ) {
        boolean isEqual = analyzer.isEqual( firstMelody, secondMelody );
        if ( notateSuccessful && isEqual ) {
            View.notate( firstMelody );
            View.notate( secondMelody );
            Utils.suspend();
        }
        return isEqual;
    }
}
