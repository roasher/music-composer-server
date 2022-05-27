package ru.pavelyurkin.musiccomposer.core.model;

import java.util.List;
import lombok.Getter;

public enum Key {

  C_MAJOR(0, 2, 4, 5, 7, 9, 11),
  G_MAJOR(0, 2, 4, 6, 7, 9, 11),
  D_MAJOR(1, 2, 4, 6, 7, 9, 11),
  A_MAJOR(1, 2, 4, 6, 8, 9, 11),
  E_MAJOR(1, 3, 4, 6, 8, 9, 11),
  B_MAJOR(1, 3, 4, 6, 8, 10, 11),
  F_SHARP_MAJOR(1, 3, 5, 6, 8, 10, 11),
  C_SHARP_MAJOR(1, 3, 5, 6, 8, 10, 0),
  F_MAJOR(0, 2, 4, 5, 7, 9, 10),
  B_FLAT_MAJOR(0, 2, 3, 5, 7, 9, 10),
  E_FLAT_MAJOR(0, 2, 3, 5, 7, 8, 10),
  A_FLAT_MAJOR(0, 1, 3, 5, 7, 8, 10),
  D_FLAT_MAJOR(0, 1, 3, 5, 6, 8, 10),
  G_FLAT_MAJOR(11, 1, 3, 5, 6, 8, 10),
  C_FLAT_MAJOR(11, 1, 3, 4, 6, 8, 10);

  @Getter
  private List<Integer> keyPitches;

  Key(Integer... keyPitches) {
    this.keyPitches = List.of(keyPitches);
  }

}
