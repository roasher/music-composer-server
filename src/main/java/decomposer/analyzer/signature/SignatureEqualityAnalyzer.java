package decomposer.analyzer.signature;

import model.Signature;

/**
 * Created by Pavel Yurkin on 08.08.14.
 */
public interface SignatureEqualityAnalyzer {
    public boolean isEqual( Signature firstSignature, Signature secondSignature );
}
