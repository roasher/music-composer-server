package composer.next;

import composer.step.CompositionStep;
import model.ComposeBlock;
import model.Lexicon;

import java.util.List;

/**
 * Created by Wish on 22.12.2015.
 * Handles logic of getting next step
 */
public interface NextBlockProvider {

    ComposeBlock getNextBlock( Lexicon lexicon, List<CompositionStep> previousCompositionSteps );

}
