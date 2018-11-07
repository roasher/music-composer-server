package ru.pavelyurkin.musiccomposer.rest.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepVoiceRangeFilter;
import ru.pavelyurkin.musiccomposer.rest.dto.BachChoralVoiceRangeDTO;

import java.util.Arrays;

@Component
public class BachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter implements Converter<BachChoralVoiceRangeDTO, ComposeStepVoiceRangeFilter> {
	@Override
	public ComposeStepVoiceRangeFilter convert( BachChoralVoiceRangeDTO bachChoralVoiceRangeDTO ) {
		if ( bachChoralVoiceRangeDTO == null ) return null;
			return new ComposeStepVoiceRangeFilter( Arrays.asList(
				bachChoralVoiceRangeDTO.getRange1(),
				bachChoralVoiceRangeDTO.getRange2(),
				bachChoralVoiceRangeDTO.getRange3(),
				bachChoralVoiceRangeDTO.getRange4()
		) );
	}
}
