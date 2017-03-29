package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.List;

/**
 * Created by wish on 02.02.2016.
 */
public interface ComposeStepFilter {
	// we assuming that possibleNexts are already transposed
	List<CompositionStep> filter( List<CompositionStep> possibleNextComposeSteps, List<CompositionStep> previousCompositionSteps );
}
