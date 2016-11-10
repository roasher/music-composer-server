package ru.pavelyurkin.musiccomposer.composer.next.filter;

import ru.pavelyurkin.musiccomposer.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.model.ComposeBlock;

import java.util.List;

/**
 * Created by wish on 02.02.2016.
 */
public interface ComposeBlockFilter {
	List<ComposeBlock> filter( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps );
}
