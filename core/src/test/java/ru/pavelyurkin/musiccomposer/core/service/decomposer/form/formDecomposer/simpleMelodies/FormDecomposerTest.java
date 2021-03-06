package ru.pavelyurkin.musiccomposer.core.service.decomposer.form.formDecomposer.simpleMelodies;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import jm.JMC;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.service.composition.loader.CompositionLoader;
import ru.pavelyurkin.musiccomposer.core.service.decomposer.form.FormDecomposer;

@Disabled("From related. Not ready yet")
public class FormDecomposerTest extends AbstractSpringTest {

  @Autowired
  protected FormDecomposer formDecomposer;
  @Autowired
  protected CompositionLoader compositionLoader;

  @Test
  public void simpleMelodyAABC1() {
    Composition composition =
        compositionLoader.getComposition(new File(this.getClass().getResource("AABC1.mid").getFile()));

    List<Form> formList = formDecomposer.getInstrumentForm(0, composition, JMC.WHOLE_NOTE);

    assertTrue(formList.get(0).getValue() == 'A');
    assertTrue(formList.get(1).getValue() == 'A');
    assertTrue(formList.get(2).getValue() == 'B');
    assertTrue(formList.get(3).getValue() == 'C');
    //		View.notate( score );
    //		Utils.suspend();
  }

  @Test
  public void simpleMelodyABAB1() {
    Composition composition =
        compositionLoader.getComposition(new File(this.getClass().getResource("ABAB1.mid").getFile()));

    List<Form> formList = formDecomposer.getInstrumentForm(0, composition, JMC.WHOLE_NOTE);

    assertTrue(formList.get(0).getValue() == 'A');
    assertTrue(formList.get(1).getValue() == 'B');
    assertTrue(formList.get(2).getValue() == 'A');
    assertTrue(formList.get(3).getValue() == 'B');
    //		View.notate( score );
    //		Utils.suspend();
  }

  @Test
  public void simpleMelodyABAC1() {
    Composition composition =
        compositionLoader.getComposition(new File(this.getClass().getResource("ABAC1.mid").getFile()));

    List<Form> formList = formDecomposer.getInstrumentForm(0, composition, JMC.WHOLE_NOTE);

    assertTrue(formList.get(0).getValue() == 'A');
    assertTrue(formList.get(1).getValue() == 'B');
    assertTrue(formList.get(2).getValue() == 'A');
    assertTrue(formList.get(3).getValue() == 'C');
    //		View.notate( score );
    //		Utils.suspend();
  }
}
