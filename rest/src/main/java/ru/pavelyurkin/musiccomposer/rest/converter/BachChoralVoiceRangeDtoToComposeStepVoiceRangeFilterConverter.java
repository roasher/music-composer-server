package ru.pavelyurkin.musiccomposer.rest.converter;

import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.range.VoiceRangeFilter;
import ru.pavelyurkin.musiccomposer.rest.dto.filter.BachChoralVoiceRangeDTO;

@Component
public class BachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter
    implements Converter<BachChoralVoiceRangeDTO, MusicBlockFilter> {
  @Override
  public VoiceRangeFilter convert(BachChoralVoiceRangeDTO bachChoralVoiceRangeDTO) {
    if (bachChoralVoiceRangeDTO == null) {
      return null;
    }
    return new VoiceRangeFilter(Arrays.asList(
        bachChoralVoiceRangeDTO.getRange1(),
        bachChoralVoiceRangeDTO.getRange2(),
        bachChoralVoiceRangeDTO.getRange3(),
        bachChoralVoiceRangeDTO.getRange4()
    ));
  }
}
