package ru.pavelyurkin.musiccomposer.core.decomposer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import jm.JMC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.persistance.dao.LexiconDAO;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionLoader;

/**
 * Created by pyurkin on 17.04.2015.
 */
public class CompositionDecomposerUsingStoringTest extends AbstractSpringTest {
  @Autowired
  private CompositionDecomposer compositionDecomposer;
  @Autowired
  private CompositionLoader compositionLoader;
  @Autowired
  @Qualifier("lexiconDAO_mapdb")
  private LexiconDAO lexiconDAO;

  @Before
  public void init() throws Exception {
    lexiconDAO.clear();
  }

  @Test
  public void decomposeWithOrWithoutStoringEqual() throws IOException {
    List<Composition> compositionList =
        compositionLoader.getCompositionsFromFolder(new File("src/test/resources/simpleMelodies"));
    // first decompose with no persistance
    Lexicon lexiconWithoutDB = compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
    lexiconDAO.persist(lexiconWithoutDB);

    // second decompose with file
    Lexicon lexiconWithDB = compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
    assertThat(lexiconWithDB, is(lexiconWithoutDB));
  }

  @Test
  public void decomposeWithOrWithoutStoring_intersect() throws IOException {
    List<Composition> compositionList =
        compositionLoader.getCompositionsFromFolder(new File("src/test/resources/simpleMelodies"));
    // Decompose all melodies
    Lexicon lexiconFull = compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);

    // Decompose first melody and storing it into DB
    Lexicon lexiconWithoutDB = compositionDecomposer.decompose(compositionList.get(0), JMC.WHOLE_NOTE);
    lexiconDAO.persist(lexiconWithoutDB);

    // Decompose all melodies using DB with only one melody
    Lexicon lexiconWithDB = compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
    assertThat(lexiconWithDB, is(lexiconFull));
  }

  @Test
  public void decomposeWithOrWithoutStoring_nonoverlapping() throws IOException {
    List<Composition> compositionList =
        compositionLoader.getCompositionsFromFolder(new File("src/test/resources/simpleMelodies"));
    // Decompose second
    Lexicon lexiconSecond = compositionDecomposer.decompose(compositionList.get(1), JMC.WHOLE_NOTE);

    // Decompose first melody and storing it into DB
    Lexicon lexiconFirst = compositionDecomposer.decompose(compositionList.get(0), JMC.WHOLE_NOTE);
    lexiconDAO.persist(lexiconFirst);

    // Decompose second melody using DB with only first melody
    Lexicon lexiconSecondWithDB = compositionDecomposer.decompose(compositionList.get(1), JMC.WHOLE_NOTE);
    assertThat(lexiconSecondWithDB, is(lexiconSecond));
  }

  @Test
  public void failDecompose() throws IOException {
    List<Composition> compositionList =
        compositionLoader.getCompositionsFromFolder(new File("src/test/resources/simpleMelodies"));
    // Decompose second
    Lexicon lexiconSecond = compositionDecomposer.decompose(compositionList.get(1), JMC.WHOLE_NOTE);

    // Decompose first melody and storing it into DB
    Lexicon lexiconFirst = compositionDecomposer.decompose(compositionList.get(0), JMC.WHOLE_NOTE);
    lexiconDAO.persist(lexiconFirst);

    // Decompose second melody using DB with only first melody
    Lexicon lexiconSecondWithDB = compositionDecomposer.decompose(compositionList.get(0), JMC.WHOLE_NOTE);
    assertNotEquals(lexiconSecondWithDB, lexiconSecond);

    Lexicon lexiconAllWithDB = compositionDecomposer.decompose(compositionList, JMC.WHOLE_NOTE);
    assertNotEquals(lexiconAllWithDB, lexiconSecond);
  }

}
