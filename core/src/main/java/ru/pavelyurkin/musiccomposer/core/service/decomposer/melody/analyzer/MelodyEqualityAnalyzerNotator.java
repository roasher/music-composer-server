package ru.pavelyurkin.musiccomposer.core.service.decomposer.melody.analyzer;

import jm.util.View;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.utils.Utils;

/**
 * Class wraps MelodyEqualityAnalyzer adding functionality to view successed melodies
 */
public class MelodyEqualityAnalyzerNotator implements MelodyEqualityAnalyzer {

  private MelodyEqualityAnalyzerImpl analyzer;
  private boolean notateSuccessful;

  public MelodyEqualityAnalyzerImpl getAnalyzer() {
    return analyzer;
  }

  public void setAnalyzer(MelodyEqualityAnalyzerImpl analyzer) {
    this.analyzer = analyzer;
  }

  public boolean isNotateSuccessful() {
    return notateSuccessful;
  }

  public void setNotateSuccessful(boolean notateSuccessful) {
    this.notateSuccessful = notateSuccessful;
  }

  @Override
  public boolean isEqual(Melody firstMelody, Melody secondMelody) {
    boolean isEqual = analyzer.isEqual(firstMelody, secondMelody);
    if (notateSuccessful && isEqual) {
      View.notate(firstMelody);
      View.notate(secondMelody);
      Utils.suspend();
    }
    return isEqual;
  }
}
