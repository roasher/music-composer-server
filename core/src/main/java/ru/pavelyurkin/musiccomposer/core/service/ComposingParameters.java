package ru.pavelyurkin.musiccomposer.core.service;

import ru.pavelyurkin.musiccomposer.core.composer.ComposeBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Holds parameters of Composing
 */
public class ComposingParameters {

	private Lexicon lexicon;
	private ComposeBlockProvider composeBlockProvider;
	private List<CompositionStep> previousCompositionSteps = Collections.emptyList();

	public List<CompositionStep> getPreviousCompositionSteps() {
		return previousCompositionSteps;
	}

	public void setPreviousCompositionSteps( List<CompositionStep> previousCompositionSteps ) {
		this.previousCompositionSteps = previousCompositionSteps;
	}

	public Lexicon getLexicon() {
		return lexicon;
	}

	public void setLexicon( Lexicon lexicon ) {
		this.lexicon = lexicon;
	}

	public ComposeBlockProvider getComposeBlockProvider() {
		return composeBlockProvider;
	}

	public void setComposeBlockProvider( ComposeBlockProvider composeBlockProvider ) {
		this.composeBlockProvider = composeBlockProvider;
	}

}
