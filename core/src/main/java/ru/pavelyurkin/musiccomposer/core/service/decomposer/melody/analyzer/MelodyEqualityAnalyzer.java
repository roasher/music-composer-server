package ru.pavelyurkin.musiccomposer.core.service.decomposer.melody.analyzer;

import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

public interface MelodyEqualityAnalyzer {
  boolean isEqual(Melody firstMelody, Melody secondMelody);
}
