package composer.next;

import composer.step.CompositionStep;
import model.ComposeBlock;
import model.Lexicon;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Wish on 22.12.2015.
 * Handles logic of getting next step
 */
public interface NextBlockProvider {

    Optional<ComposeBlock> getNextBlock( List<CompositionStep> previousCompositionSteps );

}
