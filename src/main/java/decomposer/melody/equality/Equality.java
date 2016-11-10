package decomposer.melody.equality;

import model.melody.Melody;

public interface Equality {

    /**
     * Tests if two melodies can be considered equal
     * @param firstMelody
     * @param secondMelody
     * @return
     */
    boolean test( Melody firstMelody, Melody secondMelody );

    // TODO delete default and create new interface for that
	/**
	 * Returns discrete metric of equality between who melodies
     * @param firstMelody
     * @param secondMelody
     * @return
     */
    default double getEqualityMetric( Melody firstMelody, Melody secondMelody ) {
        throw new RuntimeException( "we don't support that)" );
    }

    /**
     * Returns maximum number of not suitable notes that allows this Equality test
     * @return
     */
    int getMaxNumberOfDiversedNotes();

}