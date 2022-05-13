package ru.pavelyurkin.musiccomposer.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
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
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.MusicBlockFilter;
import ru.pavelyurkin.musiccomposer.core.exception.ComposeException;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.core.service.ComposeService;
import ru.pavelyurkin.musiccomposer.rest.converter.CompositionConverter;
import ru.pavelyurkin.musiccomposer.rest.dto.BachChoralVoiceRangeDTO;
import ru.pavelyurkin.musiccomposer.rest.dto.CompositionDTO;

@RestController
@Slf4j
public class MidiController {

  private final ComposeService composeService;

  private final CompositionConverter compositionConverter;
  private final Converter<BachChoralVoiceRangeDTO, MusicBlockFilter>
      bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter;

  @Autowired
  public MidiController(ComposeService composeService, CompositionConverter compositionConverter,
                        Converter<BachChoralVoiceRangeDTO, MusicBlockFilter> bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter) {
    this.composeService = composeService;
    this.compositionConverter = compositionConverter;
    this.bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter =
        bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter;
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
      @Validated @RequestBody(required = false) BachChoralVoiceRangeDTO bachChoralVoiceRangeDTO) {
    List<MusicBlockFilter> composeStepFiltersToReplace = bachChoralVoiceRangeDTO != null ?
        Collections.singletonList(bachChoralVoiceRangeDtoToComposeStepVoiceRangeFilterConverter
            .convert(bachChoralVoiceRangeDTO)) :
        Collections.emptyList();

    Instant time = Instant.now();
    CompositionFrontDTO nextBarsFromComposition = composeService.getNextBarsFromComposition(compositionId, numberOfBars,
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

  @PostConstruct
  public void init() {
    composeService.loadDefaultLexicon();
  }

}
