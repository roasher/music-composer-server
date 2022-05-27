package ru.pavelyurkin.musiccomposer.core.model.composition;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.utils.Utils;

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
