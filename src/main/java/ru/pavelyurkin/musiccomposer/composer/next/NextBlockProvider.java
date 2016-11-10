package ru.pavelyurkin.musiccomposer.composer.next;

import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;

import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 * Handles logic of getting next step
 */
public interface NextBlockProvider {

    Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps, List<FormCompositionStep> differentFormSteps,
            double length );

}
