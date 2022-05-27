package ru.pavelyurkin.musiccomposer.core.client.lexicon;

import java.io.IOException;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

public interface LexiconDAO {

  void persist(Lexicon lexicon) throws IOException;

  void clear();

  Lexicon fetch();

}
