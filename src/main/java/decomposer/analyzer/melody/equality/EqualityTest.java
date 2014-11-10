package decomposer.analyzer.melody.equality;

import model.Signature;

public interface EqualityTest {

    /**
     * Tests if two signatures can be considered equal
     * @param firstSignature
     * @param secondSignature
     * @return
     */
    public boolean test( Signature firstSignature, Signature secondSignature );

    /**
     * Returns maximum number of not suitable notes that allows this Equality test
     * @return
     */
    public int getMaxNumberOfDiversedNotes();

}