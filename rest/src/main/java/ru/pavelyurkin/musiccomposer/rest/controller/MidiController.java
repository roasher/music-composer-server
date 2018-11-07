package ru.pavelyurkin.musiccomposer.rest.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.AbstractComposeStepFilter;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepVoiceRangeFilter;
import ru.pavelyurkin.musiccomposer.core.exception.ComposeException;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.rest.converter.CompositionConverter;
import ru.pavelyurkin.musiccomposer.core.service.ComposeService;
import ru.pavelyurkin.musiccomposer.rest.dto.BachChoralVoiceRangeDTO;
import ru.pavelyurkin.musiccomposer.rest.dto.CompositionDTO;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

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

	@ApiOperation( "Notes generation" )
	@PostMapping( path = "/getBars")
	@CrossOrigin
	public CompositionDTO getBachNotes(
			@ApiParam(name = "compositionId", required = true, value = "Unique Id of generation session")
			@RequestParam String compositionId,
			@ApiParam(name = "numberOfBars", required = true, value = "Number of bars that you wan't to be generated")
			@RequestParam int numberOfBars,
			@ApiParam(name = "Voice range settings", value = "Four voice ranges to compose within")
			@Validated @RequestBody(required = false) BachChoralVoiceRangeDTO bachChoralVoiceRangeDTO ) {
		List<AbstractComposeStepFilter> composeStepFiltersToReplace = bachChoralVoiceRangeDTO != null ?
				Collections.singletonList( bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter
						.convert( bachChoralVoiceRangeDTO ) ) :
				Collections.emptyList();

		CompositionFrontDTO nextBarsFromComposition = composeService.getNextBarsFromComposition( compositionId, numberOfBars,
				composeStepFiltersToReplace );
		CompositionDTO compositionDTO = compositionConverter.convert( nextBarsFromComposition );
		log.info( "Composed {}", compositionDTO );
		return compositionDTO;
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
