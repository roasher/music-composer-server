package ru.pavelyurkin.musiccomposer.core.model.composition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
@AllArgsConstructor
public class CompositionFrontDTO {

  @Delegate
  private Composition composition;
  private double startRhythmValues;

}
