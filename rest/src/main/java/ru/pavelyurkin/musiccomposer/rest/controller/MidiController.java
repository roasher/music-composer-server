package ru.pavelyurkin.musiccomposer.rest.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pavelyurkin.musiccomposer.rest.converter.CompositionConverter;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.service.ComposeService;
import ru.pavelyurkin.musiccomposer.rest.dto.CompositionDTO;

@RestController
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
	public CompositionDTO getNotes(
			@ApiParam(name = "compositionId", required = true, value = "Unique Id of generation session")
			@RequestParam String compositionId,
			@ApiParam(name = "numberOfBars", required = true, value = "Number of bars that you wan't to be generated")
			@RequestParam int numberOfBars ) {
		Composition nextBarsFromComposition = composeService.getNextBarsFromComposition( compositionId, numberOfBars );
		return compositionConverter.convert( nextBarsFromComposition );
	}

}
