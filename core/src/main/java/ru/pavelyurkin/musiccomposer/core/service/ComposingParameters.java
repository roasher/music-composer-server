package ru.pavelyurkin.musiccomposer.core.service;

import ru.pavelyurkin.musiccomposer.core.composer.ComposeBlockProvider;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

/**
 * Holds parameters of Composing
 */
public class ComposingParameters {

	private Lexicon lexicon;
	private ComposeBlockProvider composeBlockProvider;

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
