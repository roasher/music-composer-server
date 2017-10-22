package ru.pavelyurkin.musiccomposer.rest.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import ru.pavelyurkin.musiccomposer.core.composer.next.filter.ComposeStepVoiceRangeFilter;

@Data
public class BachChoralVoiceRangeDTO {

    @NotEmpty
    private ComposeStepVoiceRangeFilter.Range range1;
    @NotEmpty
    private ComposeStepVoiceRangeFilter.Range range2;
    @NotEmpty
    private ComposeStepVoiceRangeFilter.Range range3;
    @NotEmpty
    private ComposeStepVoiceRangeFilter.Range range4;

}
