package ru.pavelyurkin.musiccomposer.core.client.lexicon;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapdb.DB;
import org.mapdb.Serializer;
import org.mapdb.serializer.GroupSerializer;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.client.lexicon.dto.LexiconDB;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

@Component
@RequiredArgsConstructor
@Slf4j
public class LexiconDAO_mapdb implements LexiconDAO {

  private static final GroupSerializer SERIALIZER = Serializer.JAVA;
  private static final String COMPOSE_BLOCKS_COLLECTION_NAME = "lexicon";
  private final DB db;

  @Override
  public void persist(Lexicon lexicon) {
    log.info("Persisting Lexicon");
    List<LexiconDB> lexiconDbs =
        (List<LexiconDB>) db.indexTreeList(COMPOSE_BLOCKS_COLLECTION_NAME, SERIALIZER).createOrOpen();
    if (lexiconDbs.size() > 0) {
      lexiconDbs.clear();
    }

    List<MusicBlock> musicBlocks = lexicon.getComposeBlocks().stream()
        .map(ComposeBlock::getMusicBlock)
        .collect(Collectors.toList());
    lexiconDbs.add(new LexiconDB(musicBlocks, lexicon.getPossibleNextMusicBlockNumbers()));

    db.commit();
    log.info("Persistence done");
  }

  @Override
  public Lexicon fetch() {
    log.info("Fetching lexicon");
    List<LexiconDB> lexiconDbs =
        (List<LexiconDB>) db.indexTreeList(COMPOSE_BLOCKS_COLLECTION_NAME, SERIALIZER).createOrOpen();
    if (lexiconDbs.isEmpty()) {
      log.info("Fetched Lexicon is empty");
      return Lexicon.getBlankLexicon();
    }

    LexiconDB lexiconDB = lexiconDbs.get(0);
    log.info("Fetched lexicon contains {} blocks", lexiconDB.getMusicBlocks().size());
    return new Lexicon(lexiconDB.getPossibleNextMusicBlockNumbers(), lexiconDB.getMusicBlocks());
  }

  @Override
  public void clear() {
    List<LexiconDB> lexiconDbs =
        (List<LexiconDB>) db.indexTreeList(COMPOSE_BLOCKS_COLLECTION_NAME, SERIALIZER).createOrOpen();
    lexiconDbs.clear();

    db.commit();
  }
}
