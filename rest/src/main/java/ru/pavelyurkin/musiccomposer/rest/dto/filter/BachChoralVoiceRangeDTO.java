package ru.pavelyurkin.musiccomposer.rest.dto.filter;

import javax.validation.constraints.NotNull;
import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.range.VoiceRangeFilter;

@Data
public class BachChoralVoiceRangeDTO {

  @NotNull
  private VoiceRangeFilter.Range range1;
  @NotNull
  private VoiceRangeFilter.Range range2;
  @NotNull
  private VoiceRangeFilter.Range range3;
  @NotNull
  private VoiceRangeFilter.Range range4;

}
