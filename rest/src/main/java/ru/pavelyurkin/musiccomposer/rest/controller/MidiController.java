package ru.pavelyurkin.musiccomposer.rest.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepVoiceRangeFilter;
import ru.pavelyurkin.musiccomposer.core.exception.ComposeException;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.rest.converter.CompositionConverter;
import ru.pavelyurkin.musiccomposer.core.service.ComposeService;
import ru.pavelyurkin.musiccomposer.rest.dto.BachChoralVoiceRangeDTO;
import ru.pavelyurkin.musiccomposer.rest.dto.CompositionDTO;

import javax.annotation.PostConstruct;
import java.util.Collections;

@RestController
@Slf4j
public class MidiController {

	private final ComposeService composeService;

	private final CompositionConverter compositionConverter;
	private final Converter<BachChoralVoiceRangeDTO, ComposeStepVoiceRangeFilter> bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter;

	@Autowired
	public MidiController( ComposeService composeService, CompositionConverter compositionConverter,
			Converter<BachChoralVoiceRangeDTO, ComposeStepVoiceRangeFilter> bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter ) {
		this.composeService = composeService;
		this.compositionConverter = compositionConverter;
		this.bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter = bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter;
	}

	@ApiOperation( "Note generation" )
	@PostMapping( path = "/getBars")
	@CrossOrigin
	public CompositionDTO getNotes(
			@ApiParam(name = "compositionId", required = true, value = "Unique Id of generation session")
			@RequestParam String compositionId,
			@ApiParam(name = "numberOfBars", required = true, value = "Number of bars that you wan't to be generated")
			@RequestParam int numberOfBars,
			@ApiParam(name = "Voice range settings", value = "Four voice ranges to compose within")
			@Validated @RequestBody BachChoralVoiceRangeDTO bachChoralVoiceRangeDTO ) {
		ComposeStepVoiceRangeFilter composeStepVoiceRangeFilter = bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter
				.convert( bachChoralVoiceRangeDTO );
		CompositionFrontDTO nextBarsFromComposition = composeService.getNextBarsFromComposition( compositionId, numberOfBars, Collections.singletonList( composeStepVoiceRangeFilter ) );
		return compositionConverter.convert( nextBarsFromComposition );
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String commonExceptionHandler(Throwable throwable) {
		log.error("Exception while request", throwable);
		return throwable.getMessage();
	}

	@ExceptionHandler(ComposeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String composeException(ComposeException exception) {
		log.warn("Exception during composing", exception);
		return exception.getMessage();
	}

	@PostConstruct
	public void init() {
		composeService.loadDefaultLexicon();
	}

}
