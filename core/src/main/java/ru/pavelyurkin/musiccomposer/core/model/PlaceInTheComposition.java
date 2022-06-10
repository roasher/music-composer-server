package ru.pavelyurkin.musiccomposer.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;

@Data
@AllArgsConstructor
public class PlaceInTheComposition {

  private CompositionInfo compositionInfo;
  private double firstNoteStartTime;
  private double lastNoteEndTime;

}
