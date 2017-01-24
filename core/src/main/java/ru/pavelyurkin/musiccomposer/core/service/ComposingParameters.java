package ru.pavelyurkin.musiccomposer.core.service;

import ru.pavelyurkin.musiccomposer.core.composer.ComposeBlockProvider;
import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

/**
 * Holds parameters of Composing
 */
public class ComposingParameters {

	private Lexicon lexicon;
	private ComposeBlockProvider composeBlockProvider;
	private CompositionStep previousCompositionStep = new CompositionStep( null, null );

	public CompositionStep getPreviousCompositionStep() {
		return previousCompositionStep;
	}

	public void setPreviousCompositionStep( CompositionStep previousCompositionStep ) {
		this.previousCompositionStep = previousCompositionStep;
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
