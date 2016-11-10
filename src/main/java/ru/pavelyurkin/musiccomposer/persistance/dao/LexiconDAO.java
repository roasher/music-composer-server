package ru.pavelyurkin.musiccomposer.persistance.dao;

import ru.pavelyurkin.musiccomposer.model.Lexicon;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Component
public interface LexiconDAO {

	void persist( Lexicon lexicon ) throws IOException;

	Lexicon fetch();

}
