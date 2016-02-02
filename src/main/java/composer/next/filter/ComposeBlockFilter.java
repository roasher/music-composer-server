package composer.next.filter;

import composer.step.CompositionStep;
import model.ComposeBlock;

import java.util.List;

/**
 * Created by wish on 02.02.2016.
 */
public interface ComposeBlockFilter {
	List<ComposeBlock> filter( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps );
}
