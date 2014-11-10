package decomposer.analyzer.signature;

import jm.util.View;
import model.Signature;
import utils.Utils;

/**
 * Class wraps SignatureEqualityAnalyzer adding functionality to view successed signatures
 * Created by Pavel Yurkin on 08.08.14.
 */
public class SignatureEqualityAnalyzerNotator implements SignatureEqualityAnalyzer {

    private SignatureEqualityAnalyzerImpl analyzer;
    private boolean notateSuccessful;

    public SignatureEqualityAnalyzerImpl getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer( SignatureEqualityAnalyzerImpl analyzer ) {
        this.analyzer = analyzer;
    }

    public boolean isNotateSuccessful() {
        return notateSuccessful;
    }

    public void setNotateSuccessful( boolean notateSuccessful ) {
        this.notateSuccessful = notateSuccessful;
    }

    @Override
    public boolean isEqual( Signature firstSignature, Signature secondSignature ) {
        boolean isEqual = analyzer.isEqual( firstSignature, secondSignature );
        if ( notateSuccessful && isEqual ) {
            View.notate( firstSignature );
            View.notate( secondSignature );
            Utils.pauseToAnalyzeView();
        }
        return isEqual;
    }
}
