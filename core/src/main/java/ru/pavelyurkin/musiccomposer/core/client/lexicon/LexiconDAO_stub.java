package ru.pavelyurkin.musiccomposer.core.client.lexicon;

import java.io.IOException;
import org.springframework.stereotype.Repository;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;

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
