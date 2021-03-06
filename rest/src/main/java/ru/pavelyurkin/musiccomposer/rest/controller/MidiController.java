package ru.pavelyurkin.musiccomposer.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.key.SameKeyFilter;
import ru.pavelyurkin.musiccomposer.core.service.exception.ComposeException;
import ru.pavelyurkin.musiccomposer.core.service.multipleclients.MultipleClientsComposeService;
import ru.pavelyurkin.musiccomposer.rest.converter.CompositionConverter;
import ru.pavelyurkin.musiccomposer.rest.dto.CompositionDTO;
import ru.pavelyurkin.musiccomposer.rest.dto.filter.BachChoralVoiceRangeDTO;

@RestController
@Slf4j
public class MidiController {

  private final MultipleClientsComposeService multipleClientsComposeService;

  private final CompositionConverter compositionConverter;
  private final Converter<BachChoralVoiceRangeDTO, MusicBlockFilter> rangeDtoToFilterConverter;

  @Autowired
  public MidiController(MultipleClientsComposeService multipleClientsComposeService,
                        CompositionConverter compositionConverter,
                        Converter<BachChoralVoiceRangeDTO, MusicBlockFilter> rangeDtoToFilterConverter) {
    this.multipleClientsComposeService = multipleClientsComposeService;
    this.compositionConverter = compositionConverter;
    this.rangeDtoToFilterConverter = rangeDtoToFilterConverter;
  }

  @Operation(description = "Notes generation")
  @PostMapping(path = "/getBars")
  @CrossOrigin
  public CompositionDTO getBachNotes(
      @Parameter(name = "compositionId", required = true, description = "Unique Id of generation session")
      @RequestParam String compositionId,
      @Parameter(name = "numberOfBars", required = true, description = "Number of bars that you wan't to be generated")
      @RequestParam int numberOfBars,
      @Parameter(name = "Voice range settings", description = "Four voice ranges to compose within")
      @Validated @RequestBody(required = false) BachChoralVoiceRangeDTO bachChoralVoiceRangeDTO,
      @Parameter(name = "Key setting", description = "Key to compose within")
      @RequestParam(required = false) Key key) {
    List<MusicBlockFilter> composeStepFiltersToReplace = new ArrayList<>();
    if (bachChoralVoiceRangeDTO != null) {
      composeStepFiltersToReplace.add(rangeDtoToFilterConverter.convert(bachChoralVoiceRangeDTO));
    }
    if (key != null) {
      composeStepFiltersToReplace.add(new SameKeyFilter(key));
    }

    Instant time = Instant.now();
    CompositionFrontDTO nextBarsFromComposition = multipleClientsComposeService
        .getNextBarsFromComposition(compositionId, numberOfBars,
            composeStepFiltersToReplace);
    CompositionDTO compositionDTO = compositionConverter.convert(nextBarsFromComposition);
    log.info("Composed {}", compositionDTO);
    log.info("Composed time: {} millis", Duration.between(time, Instant.now()).toMillis());
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

}
