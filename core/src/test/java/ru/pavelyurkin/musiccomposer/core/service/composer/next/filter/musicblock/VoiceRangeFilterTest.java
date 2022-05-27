package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock;

import static jm.constants.Pitches.A2;
import static jm.constants.Pitches.B2;
import static jm.constants.Pitches.B3;
import static jm.constants.Pitches.C3;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.CS3;
import static jm.constants.Pitches.D3;
import static jm.constants.Pitches.E3;
import static jm.constants.Pitches.G3;
import static jm.constants.Pitches.GS3;
import static jm.constants.Pitches.REST;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import jm.music.data.Note;
import org.junit.jupiter.api.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.range.VoiceRangeFilter;

public class VoiceRangeFilterTest {

  @Test
  public void test() {
    List<MusicBlock> previousBlocks = Collections.singletonList(new MusicBlock(0, Arrays.asList(
        new InstrumentPart(new Note(C3, Note.DEFAULT_RHYTHM_VALUE)),
        new InstrumentPart(new Note(C4, Note.DEFAULT_RHYTHM_VALUE))), null));

    VoiceRangeFilter composeBlockVoiceRangeFilter = new VoiceRangeFilter(Arrays.asList(
        new VoiceRangeFilter.Range(C3, G3),
        new VoiceRangeFilter.Range(A2, D3)
    ));

    assertTrue(composeBlockVoiceRangeFilter
        .filterIt(getTestBlock(Arrays.asList(C3, REST, E3), Arrays.asList(B2, CS3, REST)), previousBlocks));
    assertFalse(composeBlockVoiceRangeFilter
        .filterIt(getTestBlock(Arrays.asList(B2, C3, REST), Arrays.asList(B3, REST, REST)), previousBlocks));
    assertFalse(composeBlockVoiceRangeFilter
        .filterIt(getTestBlock(Arrays.asList(C3, B2), Arrays.asList(A2, A2)), previousBlocks));
    assertFalse(composeBlockVoiceRangeFilter
        .filterIt(getTestBlock(Arrays.asList(G3, GS3, REST), Arrays.asList(REST, REST, REST)), previousBlocks));
    assertTrue(composeBlockVoiceRangeFilter
        .filterIt(getTestBlock(Arrays.asList(G3, E3), Arrays.asList(D3, C3)), previousBlocks));
    assertTrue(composeBlockVoiceRangeFilter
        .filterIt(getTestBlock(Arrays.asList(REST, REST), Arrays.asList(REST, REST)), previousBlocks));
  }

  private MusicBlock getTestBlock(List<Integer> firstNotePitches, List<Integer> secondNotePitches) {
    InstrumentPart melody0 = new InstrumentPart(
        new NewMelody(firstNotePitches.stream()
            .map(pitch -> new Note(pitch, Note.DEFAULT_RHYTHM_VALUE))
            .collect(Collectors.toList()))
    );
    InstrumentPart melody1 = new InstrumentPart(
        new NewMelody(secondNotePitches.stream()
            .map(pitch -> new Note(pitch, Note.DEFAULT_RHYTHM_VALUE))
            .collect(Collectors.toList()))
    );
    return new MusicBlock(0, Arrays.asList(melody0, melody1), null);
  }
}
