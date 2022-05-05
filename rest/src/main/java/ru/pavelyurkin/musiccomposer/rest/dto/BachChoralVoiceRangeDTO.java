package ru.pavelyurkin.musiccomposer.rest.dto;

import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.musicblock.VoiceRangeFilter;

import javax.validation.constraints.NotNull;

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
