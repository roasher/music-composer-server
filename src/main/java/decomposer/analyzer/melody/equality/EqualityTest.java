package decomposer.analyzer.melody.equality;

import model.Melody;

public interface EqualityTest {

    /**
     * Tests if two melodies can be considered equal
     * @param firstMelody
     * @param secondMelody
     * @return
     */
    public boolean test( Melody firstMelody, Melody secondMelody );

    /**
     * Returns maximum number of not suitable notes that allows this Equality test
     * @return
     */
    public int getMaxNumberOfDiversedNotes();

}