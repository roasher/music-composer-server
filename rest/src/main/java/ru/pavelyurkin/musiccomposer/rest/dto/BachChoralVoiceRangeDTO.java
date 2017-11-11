package ru.pavelyurkin.musiccomposer.rest.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepVoiceRangeFilter;

import javax.validation.constraints.NotNull;

@Data
public class BachChoralVoiceRangeDTO {

    @NotNull
    private ComposeStepVoiceRangeFilter.Range range1;
    @NotNull
    private ComposeStepVoiceRangeFilter.Range range2;
    @NotNull
    private ComposeStepVoiceRangeFilter.Range range3;
    @NotNull
    private ComposeStepVoiceRangeFilter.Range range4;

}
