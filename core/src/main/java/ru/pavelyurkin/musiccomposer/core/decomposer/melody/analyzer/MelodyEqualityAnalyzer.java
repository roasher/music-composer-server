package ru.pavelyurkin.musiccomposer.core.decomposer.melody.analyzer;

import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Created by Pavel Yurkin on 08.08.14.
 */
public interface MelodyEqualityAnalyzer {
  boolean isEqual(Melody firstMelody, Melody secondMelody);
}
