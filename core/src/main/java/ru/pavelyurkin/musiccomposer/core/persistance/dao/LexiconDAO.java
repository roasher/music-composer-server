package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

import java.io.IOException;

/**
 * Created by pyurkin on 29.04.2015.
 */
public interface LexiconDAO {

	void persist( Lexicon lexicon ) throws IOException;

	void clear();

	Lexicon fetch();

}
