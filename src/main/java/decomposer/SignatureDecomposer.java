package decomposer;

import decomposer.analyzer.signature.SignatureEqualityAnalyzer;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import model.*;
import model.composition.Composition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.List;

/**
 * Class searches Signatures in the composition
 * Created by Pavel Yurkin on 26.07.14.
 */
public class SignatureDecomposer {

    private Logger logger = LoggerFactory.getLogger( getClass() );

    private int minSignatureLength;
    private int maxSignatureLength;
    private SignatureEqualityAnalyzer signatureEqualityAnalyzer;

    /**
     * Nested class used for holding information about equality pare presence
     * If hasEqual is true it means that we have already searched such type of
     * Signatures and found some similar, so there is no need in search again
     */
    private class TracedSignature extends Signature {
        private boolean hasEqual = false;
        private TracedSignature( Note[] notes ) { super( notes );}

        @Override
        public boolean equals( Object signature ) {
            return super.equals( signature );
        }
    }

    /**
     * Construct all possible signatures that can be made from list of notes
     * @param composition
     * @return
     */
    private Set< TracedSignature > getAllPossibleSignatures( Composition composition ) {
        logger.debug( "Getting all possible signatures from {}", composition.getCompositionInfo().getTitle() );
        // We will be searching signatures only in the top voice for now.
        Part currentPart = composition.getPart( 0 );

        // In the import composition will be only one phase that includes all the notes played by instrument
        Phrase currentPhrase = currentPart.getPhrase( 0 );
        Note[] notes = currentPhrase.getNoteArray();

        Set< TracedSignature > signatures = new HashSet<>();
        for ( int signatureLength = minSignatureLength; signatureLength <= maxSignatureLength; signatureLength++ ) {
            for ( int startIndex = 0, endIndex = signatureLength; endIndex < notes.length; startIndex++, endIndex++ ) {
                PlaceInTheComposition placeInTheComposition = new PlaceInTheComposition(
                        composition.getCompositionInfo(),
                        // first start time
                        currentPhrase.getNoteStartTime( startIndex ),
                        // last note end time
                        currentPhrase.getNoteStartTime( endIndex ) + notes[ endIndex ].getRhythmValue() );
                Note[] signatureNotes = Arrays.copyOfRange( notes, startIndex, endIndex );
                TracedSignature newSignature = new TracedSignature( signatureNotes );
                newSignature.setPlaceInTheComposition( placeInTheComposition );
                signatures.add( newSignature );
            }
        }
        return signatures;
    }

    /**
     * Analyzes list of compositions and come out with list of signatures
     * @param compositionList
     * @return
     */
    public Map< Signature, Set< Signature > > analyzeSignatures( List< Composition > compositionList ) {
        logger.info( "Starting analyzing composition for signatures" );
        Map< Signature, Set< Signature > > signatures = new HashMap<>();

        logger.info( "Step 1: creating all possible signatures" );
        // lists of signatures inside list corresponding to compositions
        List< Set< TracedSignature > > signaturesToAnalyze = new ArrayList<>();
        for ( int currentCompositionNumber = 0; currentCompositionNumber < compositionList.size(); currentCompositionNumber ++ ) {
            Composition currentComposition = compositionList.get( currentCompositionNumber );
            Set< TracedSignature > possibleSignatures = getAllPossibleSignatures( currentComposition );
            signaturesToAnalyze.add( possibleSignatures );
        }
//        logger.info( "Number of analyzing compositions = {}, total amount of possible signatures = {}", signaturesToAnalyze.size(), getTotalSignaturesNumber( signaturesToAnalyze ) );

        logger.info( "Step 2: finding signatures that can be considered equal" );
        for ( int currentCompositionNumberHavingEtalonSignature = 0; currentCompositionNumberHavingEtalonSignature < signaturesToAnalyze.size(); currentCompositionNumberHavingEtalonSignature ++ ) {
            logger.info( "Analyzing {} composition", currentCompositionNumberHavingEtalonSignature + 1 );
            int currentEtalonSignatureNumber = 0;
            for ( Signature etalonSignature : signaturesToAnalyze.get( currentCompositionNumberHavingEtalonSignature ) ) {
                logger.debug( String.format( "Analyzing %d out of %d signatures in %d out of %d compositions",
                        ++currentEtalonSignatureNumber,
                        signaturesToAnalyze.get( currentCompositionNumberHavingEtalonSignature ).size(),
                        currentCompositionNumberHavingEtalonSignature + 1,
                        signaturesToAnalyze.size() ) );
                // Etalon and Comparing signatures can't be in one composition
                for ( int currentCompositionNumberHavingComparingSignature = currentCompositionNumberHavingEtalonSignature + 1;
                      currentCompositionNumberHavingComparingSignature < signaturesToAnalyze.size(); currentCompositionNumberHavingComparingSignature ++ ) {
                    for ( TracedSignature compareSignature : signaturesToAnalyze.get( currentCompositionNumberHavingComparingSignature ) ) {
                        // We are going to compare only if compareSignature hasn't been considered equal with other one
                        if ( !compareSignature.hasEqual ) {
                            boolean equalityTestPassed = signatureEqualityAnalyzer.isEqual( etalonSignature, compareSignature );
                            if ( equalityTestPassed ) {
                                logger.debug( "{} signature has been considered equal with {}", etalonSignature, compareSignature );
                                compareSignature.hasEqual = true;
                                // Organizing signatures into a block of equal signatures
                                Set<Signature> signatureSet = signatures.get( etalonSignature );
                                if ( signatureSet == null ) {
                                    signatureSet = new HashSet<>();
                                    signatureSet.add( compareSignature );
                                    signatures.put( etalonSignature, signatureSet );
                                } else {
                                    signatureSet.add( compareSignature );
                                }

                            }
                        }
                    }
                }
            }
        }
        return signatures;
    }

    /**
     * Returns total signatures number from lists of list
     * @param lists
     * @return
     */
    private int getTotalSignaturesNumber( List< List< TracedSignature > > lists ) {
        int sum = 0;
        for ( List currentList : lists ) {
            sum+=currentList.size();
        }
        return sum;
    }

    public int getMinSignatureLength() {
        return minSignatureLength;
    }

    public void setMinSignatureLength( int minSignatureLength ) {
        this.minSignatureLength = minSignatureLength;
    }

    public int getMaxSignatureLength() {
        return maxSignatureLength;
    }

    public void setMaxSignatureLength( int maxSignatureLength ) {
        this.maxSignatureLength = maxSignatureLength;
    }

    public SignatureEqualityAnalyzer getSignatureEqualityAnalyzer() {
        return signatureEqualityAnalyzer;
    }

    public void setSignatureEqualityAnalyzer( SignatureEqualityAnalyzer signatureEqualityAnalyzer ) {
        this.signatureEqualityAnalyzer = signatureEqualityAnalyzer;
    }
}
