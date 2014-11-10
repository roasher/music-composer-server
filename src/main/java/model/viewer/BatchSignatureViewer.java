package model.viewer;

import jm.util.View;
import model.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import utils.Utils;

import java.util.Map;
import java.util.Set;

/**
 * Created by night wish on 11.10.14.
 */
@Component
public class BatchSignatureViewer {

    Logger logger = LoggerFactory.getLogger( getClass() );

    public void view( Map< Signature, Set<Signature> > signatures ) {
        for ( Map.Entry<Signature, Set<Signature> > currentEntry : signatures.entrySet() ) {
            Signature signatureKey = currentEntry.getKey();
            Set<Signature> signatureSet = currentEntry.getValue();

            notate(signatureKey);
            for ( Signature currentSignature : signatureSet ) {
                notate(currentSignature);
            }
            Utils.pauseToAnalyzeView();
        }
    }

    private void notate( Signature signature ) {
        logger.info( "Notating signature: {}", signature.toString() );
        View.notate(signature);
    }
}
