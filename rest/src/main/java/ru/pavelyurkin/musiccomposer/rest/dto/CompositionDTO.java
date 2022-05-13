package ru.pavelyurkin.musiccomposer.rest.dto;

import java.util.List;
import lombok.Data;

/**
 * DTO to be send to the front
 */
@Data
public class CompositionDTO {

  private Header header;
  private List<NoteDTO> notes;

}
