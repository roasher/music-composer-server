package ru.pavelyurkin.musiccomposer.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.persistance.dao.LexiconDAO;

@Configuration
@Profile("persist-lexicon")
@Slf4j
public class PrecalculateLexicon implements ApplicationRunner {

  @Autowired
  @Qualifier("lexiconDAO_mapdb")
  private LexiconDAO lexiconDAO;

  @Autowired
  private Lexicon lexicon;


  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("Saving lexicon");
    lexiconDAO.persist(lexicon);
  }
}
