package ru.pavelyurkin.musiccomposer.rest.dto;

import java.util.List;
import lombok.Data;

/**
 * "header": {
 * "tempo": 64,
 * "timeSignature": [ 4, 4 ]
 * }
 */
@Data
public class Header {

  private double tempo;
  private List<Integer> timeSignature;

}