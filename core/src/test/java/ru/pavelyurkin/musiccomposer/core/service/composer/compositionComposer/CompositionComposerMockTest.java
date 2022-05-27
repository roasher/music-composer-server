package ru.pavelyurkin.musiccomposer.core.service.composer.compositionComposer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jm.JMC;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pavelyurkin.musiccomposer.core.service.composer.CompositionComposer;
import ru.pavelyurkin.musiccomposer.core.service.composer.FormBlockProvider;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.exception.ComposeException;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;

@ExtendWith(MockitoExtension.class)
public class CompositionComposerMockTest {

  @InjectMocks
  private CompositionComposer compositionComposer;

  @Mock
  private FormBlockProvider formBlockProvider;

  @Test
  @DisplayName("Check that empty composition steps would trigger adding last step to exclusion and further composing")
  public void composeStepsTest() {
    List<Optional<FormCompositionStep>> formCompositionSteps = getMockFormCompositionSteps(10);
    when(formBlockProvider.getFormElement(any(Double.class), any(), any(), any(), any(), any()))
        .thenReturn(formCompositionSteps.get(0))
        .thenReturn(formCompositionSteps.get(1))
        .thenReturn(formCompositionSteps.get(2))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.empty())
        .thenReturn(formCompositionSteps.get(3))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.empty())
        .thenReturn(formCompositionSteps.get(4))
        .thenReturn(formCompositionSteps.get(5))
        .thenReturn(formCompositionSteps.get(6))
        .thenReturn(formCompositionSteps.get(7))
        .thenReturn(formCompositionSteps.get(8));

    List<FormCompositionStep> compositionSteps = compositionComposer.composeSteps(null, null, "ABCD", JMC.WHOLE_NOTE);
    assertEquals(4, compositionSteps.size());
    assertEquals(4, compositionSteps.get(0).getForm().getValue());
    assertEquals(5, compositionSteps.get(1).getForm().getValue());
    assertEquals(6, compositionSteps.get(2).getForm().getValue());
    assertEquals(7, compositionSteps.get(3).getForm().getValue());

  }

  @Test
  public void composeStepsTest2() {

    List<Optional<FormCompositionStep>> formCompositionSteps = getMockFormCompositionSteps(10);

    when(formBlockProvider.getFormElement(any(Double.class), any(), any(), any(), any(), any()))
        .thenReturn(formCompositionSteps.get(0))
        .thenReturn(formCompositionSteps.get(1))
        .thenReturn(formCompositionSteps.get(2))
        .thenReturn(Optional.empty())
        .thenReturn(formCompositionSteps.get(3))
        .thenReturn(Optional.empty())
        .thenReturn(formCompositionSteps.get(4))
        .thenReturn(formCompositionSteps.get(5))
        .thenReturn(formCompositionSteps.get(6))
        .thenReturn(formCompositionSteps.get(7))
        .thenReturn(formCompositionSteps.get(8));

    List<FormCompositionStep> compositionSteps =
        compositionComposer.composeSteps(null, null, "ABCD", 2 * JMC.WHOLE_NOTE);
    assertEquals(4, compositionSteps.size());
    assertEquals(0, compositionSteps.get(0).getForm().getValue());
    assertEquals(1, compositionSteps.get(1).getForm().getValue());
    assertEquals(4, compositionSteps.get(2).getForm().getValue());
    assertEquals(5, compositionSteps.get(3).getForm().getValue());

  }

  @Test
  @DisplayName("Check that exception is thrown if no way to compose")
  public void composeStepsFailing() {

    List<Optional<FormCompositionStep>> formCompositionSteps = getMockFormCompositionSteps(10);

    when(formBlockProvider.getFormElement(any(Double.class), any(), any(), any(), any(), any()))
        .thenReturn(formCompositionSteps.get(0))
        .thenReturn(formCompositionSteps.get(1))
        .thenReturn(formCompositionSteps.get(2))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.empty())
        .thenReturn(Optional.empty())
        .thenReturn(Optional.empty())
        .thenReturn(formCompositionSteps.get(3))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.empty())
        .thenReturn(formCompositionSteps.get(4))
        .thenReturn(formCompositionSteps.get(5))
        .thenReturn(formCompositionSteps.get(6))
        .thenReturn(formCompositionSteps.get(7))
        .thenReturn(formCompositionSteps.get(8));

    assertThrows(ComposeException.class, () -> compositionComposer.composeSteps(null, null, "ABCD", JMC.WHOLE_NOTE));
  }

  private List<Optional<FormCompositionStep>> getMockFormCompositionSteps(int number) {
    List<Optional<FormCompositionStep>> formCompositionSteps = new ArrayList<>();
    for (char formCompositionStepNumber = 0; formCompositionStepNumber < number; formCompositionStepNumber++) {
      formCompositionSteps.add(
          Optional.of(new FormCompositionStep(List.of(
              new CompositionStep(new ComposeBlock(new MusicBlock()), null)
          ), new Form(formCompositionStepNumber))));
    }
    return formCompositionSteps;
  }
}
