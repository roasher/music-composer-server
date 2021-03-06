package ru.pavelyurkin.musiccomposer.core.service.composer.next;

import static jm.JMC.A3;
import static jm.JMC.B3;
import static jm.JMC.BF4;
import static jm.JMC.BF5;
import static jm.JMC.BF6;
import static jm.JMC.C3;
import static jm.JMC.D3;
import static jm.JMC.E3;
import static jm.JMC.EIGHTH_NOTE;
import static jm.JMC.F3;
import static jm.JMC.G3;
import static jm.JMC.QUARTER_NOTE;
import static jm.JMC.SIXTEENTH_NOTE;
import static jm.JMC.WHOLE_NOTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.composer.step.FormCompositionStep;
import ru.pavelyurkin.musiccomposer.core.service.equality.equalityMetric.EqualityMetricAnalyzer;

@ExtendWith(MockitoExtension.class)
public class NextStepProviderTest {

  @InjectMocks
  private NextStepProviderImpl nextBlockProvider;

  @Mock
  private EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer;

  @Test
  @Disabled("Form related")
  public void highestEqualityMetricBlockReturnedAsNextWhenComposingWithForm() {

    // Already composed blocks
    ComposeBlock composeBlock0 = new ComposeBlock(new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(C3, QUARTER_NOTE)),
        new InstrumentPart(new Note(F3, QUARTER_NOTE))),
        null));
    ComposeBlock composeBlock1 = new ComposeBlock(new MusicBlock(1, Arrays.asList(
        new InstrumentPart(new Note(D3, EIGHTH_NOTE)),
        new InstrumentPart(new Note(G3, EIGHTH_NOTE))),
        null));
    ComposeBlock composeBlock2 = new ComposeBlock(new MusicBlock(2, Arrays.asList(
        new InstrumentPart(new Note(E3, EIGHTH_NOTE), new Note(F3, EIGHTH_NOTE)),
        new InstrumentPart(new Note(A3, EIGHTH_NOTE), new Note(B3, EIGHTH_NOTE))),
        null));
    ComposeBlock alreadyComposedBlock = new ComposeBlock(Arrays.asList(composeBlock0, composeBlock1, composeBlock2));

    // Possible next blocks
    ComposeBlock composeBlock20 = new ComposeBlock(new MusicBlock(20, Arrays.asList(
        new InstrumentPart(new Note(BF4, SIXTEENTH_NOTE)),
        new InstrumentPart(new Rest(SIXTEENTH_NOTE))),
        null, List.of(F3, B3)));
    ComposeBlock composeBlock21 = new ComposeBlock(new MusicBlock(21, Arrays.asList(
        new InstrumentPart(new Note(BF5, SIXTEENTH_NOTE)),
        new InstrumentPart(new Rest(SIXTEENTH_NOTE))),
        null, List.of(F3, B3)));
    ComposeBlock composeBlock22 = new ComposeBlock(new MusicBlock(22, Arrays.asList(
        new InstrumentPart(new Note(BF6, SIXTEENTH_NOTE)),
        new InstrumentPart(new Rest(SIXTEENTH_NOTE))),
        null, List.of(F3, B3)));

    composeBlock2.setPossibleNextComposeBlocks(Arrays.asList(
        composeBlock20,
        composeBlock21,
        composeBlock22
    ));

    List<InstrumentPart> melodyList20 =
        sumMelodies(alreadyComposedBlock.getInstrumentParts(), composeBlock20.getInstrumentParts());
    when(equalityMetricAnalyzer.getEqualityMetric(any(), eq(melodyList20))).thenReturn(0.41);
    List<InstrumentPart> melodyList21 =
        sumMelodies(alreadyComposedBlock.getInstrumentParts(), composeBlock21.getInstrumentParts());
    when(equalityMetricAnalyzer.getEqualityMetric(any(), eq(melodyList21))).thenReturn(0.51);
    List<InstrumentPart> melodyList22 =
        sumMelodies(alreadyComposedBlock.getInstrumentParts(), composeBlock22.getInstrumentParts());
    when(equalityMetricAnalyzer.getEqualityMetric(any(), eq(melodyList22))).thenReturn(0.4);

    // Actually we don't care about similarFormSteps as long we mocked equalityMetricAnalyzer
    List<ComposeBlock> originComposeBlocks = List.of(new ComposeBlock(
        new MusicBlock(0, List.of(new InstrumentPart(new Rest(WHOLE_NOTE))), null)));
    List<FormCompositionStep> similarFormSteps = List.of(
        new FormCompositionStep(originComposeBlocks.stream()
            .map(composeBlock -> new CompositionStep(composeBlock, composeBlock.getMusicBlock()))
            .collect(Collectors.toList()), null)
    );

    List<CompositionStep> previousCompositionSteps = List.of(
        new CompositionStep(composeBlock0, composeBlock0.getMusicBlock()),
        new CompositionStep(composeBlock1, composeBlock1.getMusicBlock()),
        new CompositionStep(composeBlock2, composeBlock2.getMusicBlock())
    );

    Optional<CompositionStep> nextBlock =
        nextBlockProvider
            .getNext(previousCompositionSteps, Collections.emptyList(), Optional.of(new Form('A')), WHOLE_NOTE);

    assertEquals(composeBlock21, nextBlock.get().getOriginComposeBlock());
  }

  private List<InstrumentPart> sumMelodies(List<InstrumentPart> melodies, List<InstrumentPart> melodiesToAdd) {
    List<InstrumentPart> out = new ArrayList<>();
    for (int melodyNumber = 0; melodyNumber < melodies.size(); melodyNumber++) {
      InstrumentPart melody = new InstrumentPart();
      melody.add(melodies.get(melodyNumber));
      melody.add(melodiesToAdd.get(melodyNumber));
      out.add(melody);
    }
    return out;
  }

}
