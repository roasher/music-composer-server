package ru.pavelyurkin.musiccomposer.equality.melody;

import ru.pavelyurkin.musiccomposer.model.melody.Melody;

public interface Equality {

    /**
     * Tests if two melodies can be considered equal
     * @param firstMelody
     * @param secondMelody
     * @return
     */
    boolean test( Melody firstMelody, Melody secondMelody );

    /**
     * Returns maximum number of not suitable notes that allows this Equality test
     * @return
     */
    int getMaxNumberOfDiversedNotes();

}