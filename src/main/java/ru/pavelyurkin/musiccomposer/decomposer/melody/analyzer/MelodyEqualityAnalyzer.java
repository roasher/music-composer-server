package ru.pavelyurkin.musiccomposer.decomposer.melody.analyzer;

import ru.pavelyurkin.musiccomposer.model.melody.Melody;

/**
 * Created by Pavel Yurkin on 08.08.14.
 */
public interface MelodyEqualityAnalyzer {
    public boolean isEqual( Melody firstMelody, Melody secondMelody );
}
