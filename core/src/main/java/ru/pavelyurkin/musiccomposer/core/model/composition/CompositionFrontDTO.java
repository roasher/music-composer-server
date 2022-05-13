package ru.pavelyurkin.musiccomposer.core.model.composition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Delegate;

/**
 * Created by Wish on 13.07.2017.
 */
@Data
@AllArgsConstructor
public class CompositionFrontDTO {

  @Delegate
  private Composition composition;
  private double previousSumRhythmValues;

}
