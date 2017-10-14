package ru.pavelyurkin.musiccomposer.rest.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.pavelyurkin.musiccomposer.core.exception.ComposeException;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.rest.converter.CompositionConverter;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.service.ComposeService;
import ru.pavelyurkin.musiccomposer.rest.dto.CompositionDTO;

import javax.annotation.PostConstruct;
import javax.jws.WebResult;

@RestController
@Slf4j
public class MidiController {

	private final ComposeService composeService;

	private final CompositionConverter compositionConverter;

	@Autowired
	public MidiController( ComposeService composeService, CompositionConverter compositionConverter ) {
		this.composeService = composeService;
		this.compositionConverter = compositionConverter;
	}

	@ApiOperation( "Note generation" )
	@RequestMapping( path = "/getBars", method = RequestMethod.GET )
	@CrossOrigin
	public CompositionDTO getNotes(
			@ApiParam(name = "compositionId", required = true, value = "Unique Id of generation session")
			@RequestParam String compositionId,
			@ApiParam(name = "numberOfBars", required = true, value = "Number of bars that you wan't to be generated")
			@RequestParam int numberOfBars ) {
		CompositionFrontDTO nextBarsFromComposition = composeService.getNextBarsFromComposition( compositionId, numberOfBars );
		return compositionConverter.convert( nextBarsFromComposition );
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String commonExceptionHandler(Throwable throwable) {
		log.error("Exception while request", throwable);
		return throwable.getMessage();
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String compseException(ComposeException exception) {
		log.warn("Exception during composing", exception);
		return exception.getMessage();
	}

	@PostConstruct
	public void init() {
		composeService.loadDefaultLexicon();
	}

}
