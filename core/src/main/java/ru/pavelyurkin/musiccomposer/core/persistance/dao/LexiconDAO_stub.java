package ru.pavelyurkin.musiccomposer.core.persistance.dao;

import java.io.IOException;
import org.springframework.stereotype.Repository;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

/**
 * Created by night wish on 09.05.2015.
 */
@Repository
public class LexiconDAO_stub implements LexiconDAO {
  @Override
  public void persist(Lexicon lexicon) throws IOException {
    // doing nothing
  }

  @Override
  public void clear() {
    // doing nothing
  }

  @Override
  public Lexicon fetch() {
    return Lexicon.getBlankLexicon();
  }
}
