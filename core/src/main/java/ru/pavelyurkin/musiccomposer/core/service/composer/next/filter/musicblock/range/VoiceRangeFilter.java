package ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.range;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.service.composer.next.filter.musicblock.MusicBlockFilter;

/**
 * Filter restricts going out of range for compose block's instrument parts.
 */
@Data
@AllArgsConstructor
public class VoiceRangeFilter implements MusicBlockFilter {

  /**
   * Instrument part ranges that we want to keep music within
   */
  private List<Range> ranges;

  @Override
  public boolean filterIt(MusicBlock block, List<MusicBlock> previousBlocks) {
    if (block.getInstrumentParts().size() > ranges.size()) {
      throw new RuntimeException("Number of melodies is greater than number of ranges");
    }
    for (int instrumentPartNumber = 0; instrumentPartNumber < block.getInstrumentParts().size();
         instrumentPartNumber++) {
      InstrumentPart instrumentPart = block.getInstrumentParts().get(instrumentPartNumber);
      if (instrumentPart.isRest()) {
        continue;
      }
      if (instrumentPart.getMaxPitch() > ranges.get(instrumentPartNumber).highPitch
          || instrumentPart.getMinNonRestPitch() < ranges.get(instrumentPartNumber).lowPitch) {
        return false;
      }
    }
    return true;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class Range {

    private int lowPitch;
    private int highPitch;

  }

}
