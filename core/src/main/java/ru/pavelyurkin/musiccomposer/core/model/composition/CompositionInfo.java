package ru.pavelyurkin.musiccomposer.core.model.composition;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Additional information about composition
 */
@Data
@NoArgsConstructor
public class CompositionInfo implements Serializable {

  private String author;
  private String title;
  private double tempo;
  private Meter metre;

  public CompositionInfo(String title) {
    this.title = title;
  }

}
