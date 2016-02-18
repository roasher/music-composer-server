package composer.next;

import composer.step.CompositionStep;
import composer.step.FormCompositionStep;
import model.ComposeBlock;

import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 * Handles logic of getting next step
 */
public interface NextBlockProvider {

    Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps, List<FormCompositionStep> similarFormSteps,
            List<FormCompositionStep> differentFormSteps );

}
