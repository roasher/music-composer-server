package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

/**
 * Created by wish on 03.02.2016.
 * Filter restricts going out of range for compose block's melodies
 */
@Data
@AllArgsConstructor
public class VoiceRangeFilter implements MusicBlockFilter {

  private List<Range> melodyRange;

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class Range {

    private int lowPitch;
    private int highPitch;

  }

  @Override
  public boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks) {
    if (block.getInstrumentParts().size() > melodyRange.size()) {
      throw new RuntimeException("Number of melodies is greater than number of ranges");
    }
    for (int instrumentPartNumber = 0; instrumentPartNumber < block.getInstrumentParts().size();
         instrumentPartNumber++) {
      InstrumentPart instrumentPart = block.getInstrumentParts().get(instrumentPartNumber);
      if (instrumentPart.isRest()) {
        continue;
      }
      if (instrumentPart.getMaxPitch() > melodyRange.get(instrumentPartNumber).highPitch
          || instrumentPart.getMinNonRestPitch() < melodyRange.get(instrumentPartNumber).lowPitch) {
        return false;
      }
    }
    return true;
  }

}
