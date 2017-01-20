package ru.pavelyurkin.musiccomposer.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pavelyurkin.musiccomposer.converter.CompositionConverter;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.service.ComposeService;
import ru.pavelyurkin.musiccomposer.dto.CompositionDTO;

@RestController
public class MidiProvider {

	@Autowired
	private ComposeService composeService;

	@Autowired
	private CompositionConverter compositionConverter;

	@RequestMapping( path = "/getBars", method = RequestMethod.GET )
	public CompositionDTO getNotes( @RequestParam String compositionId, @RequestParam int numberOfBars ) {
		Composition nextBarsFromComposition = composeService.getNextBarsFromComposition( compositionId, numberOfBars );
		return compositionConverter.convert( nextBarsFromComposition );
	}

}
