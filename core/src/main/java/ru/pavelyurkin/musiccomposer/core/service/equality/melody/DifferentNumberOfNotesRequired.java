package ru.pavelyurkin.musiccomposer.core.service.equality.melody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

/**
 * Equality test wrapper adding conditions that both comparing melodies must have different number of notes
 * Pattern: chain of responsibility
 * Created by Pavel Yurkin on 18.08.14.
 */
public class DifferentNumberOfNotesRequired implements Equality {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private Equality equality;

  public DifferentNumberOfNotesRequired() {
  }

  public DifferentNumberOfNotesRequired(Equality equality) {
    this.equality = equality;
  }

  @Override
  public boolean test(Melody firstMelody, Melody secondMelody) {
    if (firstMelody.size() == secondMelody.size()) {
      return false;
    } else {
      if (this.equality != null) {
        return this.equality.test(firstMelody, secondMelody);
      } else {
        logger.warn(
            "DifferentNumberOfNotesRequired instance has null Equality member. Test will be considered UNsuccessful");
        return false;
      }
    }
  }

  @Override
  public int getMaxNumberOfDiversedNotes() {
    return equality != null ? equality.getMaxNumberOfDiversedNotes() : 0;
  }

  public Equality getEquality() {
    return equality;
  }

  public void setEquality(Equality equality) {
    this.equality = equality;
  }
}
