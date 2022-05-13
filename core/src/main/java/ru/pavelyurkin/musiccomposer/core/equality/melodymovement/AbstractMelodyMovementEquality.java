package ru.pavelyurkin.musiccomposer.core.equality.melodymovement;

import ru.pavelyurkin.musiccomposer.core.equality.melody.Equality;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.model.melody.MelodyMovement;

/**
 * @author Pavel Yurkin
 * @date 28.06.2014.
 */
public abstract class AbstractMelodyMovementEquality implements Equality {

  @Override
  public boolean test(Melody firstMelody, Melody secondMelody) {
    MelodyMovement firstMelodyMovement = new MelodyMovement(firstMelody.getNoteList());
    MelodyMovement secondMelodyMovement = new MelodyMovement(secondMelody.getNoteList());
    // Test on ru.pavelyurkin.musiccomposer.equality to save some time
    if (firstMelodyMovement.equals(secondMelodyMovement)) {
      return true;
    } else {
      return testEqualityByLogic(firstMelodyMovement, secondMelodyMovement);
    }
  }

  /**
   * Actual test, unique for each of children
   *
   * @param firstMelodyMovement
   * @param secondMelodyMovement
   */
  abstract boolean testEqualityByLogic(MelodyMovement firstMelodyMovement, MelodyMovement secondMelodyMovement);
}
