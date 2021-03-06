package ru.pavelyurkin.musiccomposer.core.model;

import static ru.pavelyurkin.musiccomposer.core.utils.ParallelUtils.getPitchDistanceIfParallel;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.core.utils.ParallelUtils;

@Data
@NoArgsConstructor
/**
 * Class represents music MusicBlock Music Block is the cut from the one's partition with some characteristics - the
 * blocks with which new generated composition will be made from.
 */
public class MusicBlock implements Serializable {

  // Origin Self Information
  private List<InstrumentPart> instrumentParts;
  private CompositionInfo compositionInfo;
  // todo refactor to Optional?
  private List<Integer> previousBlockEndPitches;
  private double startTime;

  public MusicBlock(double startTime, List<InstrumentPart> instrumentParts, CompositionInfo compositionInfo,
                    List<Integer> previousBlockEndPitches) {
    this(startTime, instrumentParts, compositionInfo);
    this.previousBlockEndPitches = previousBlockEndPitches;
  }

  public MusicBlock(double startTime, List<InstrumentPart> instrumentParts, CompositionInfo inputCompositionInfo) {
    // check if inputMelody have same rhythm value
    if (instrumentParts.stream().mapToDouble(InstrumentPart::getRythmValue).distinct().count() != 1) {
      throw new IllegalArgumentException(
          "Can't create Music Block: input melody list has melodies with different rhythm value");
    }
    this.instrumentParts = instrumentParts;
    this.compositionInfo = inputCompositionInfo;
    this.startTime = startTime;
    this.previousBlockEndPitches = null;
  }

  public MusicBlock(List<MusicBlock> musicBlocks) {
    long count = musicBlocks.stream()
        .mapToInt(musicBlock -> musicBlock.instrumentParts.size())
        .distinct()
        .count();
    if (count != 1) {
      throw new RuntimeException("Music blocks has different part numbers");
    }

    MusicBlock firstMusicBlock = musicBlocks.get(0);
    // Cloning instrument parts from first block
    List<InstrumentPart> instrumentParts = firstMusicBlock.getInstrumentParts()
        .stream()
        .map(InstrumentPart::clone)
        .collect(Collectors.toList());

    for (int musicBlockNumber = 1; musicBlockNumber < musicBlocks.size(); musicBlockNumber++) {
      MusicBlock currentMusicBlock = musicBlocks.get(musicBlockNumber);
      for (int instrumentPartNumber = 0; instrumentPartNumber < currentMusicBlock.getInstrumentParts().size();
           instrumentPartNumber++) {
        instrumentParts.get(instrumentPartNumber).add(currentMusicBlock.getInstrumentParts().get(instrumentPartNumber));
      }
    }

    this.instrumentParts = instrumentParts;
    this.compositionInfo = null;
    this.previousBlockEndPitches = firstMusicBlock.getPreviousBlockEndPitches().orElse(null);
    this.startTime = firstMusicBlock.getStartTime();

  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int melodyNumber = 0; melodyNumber < this.getInstrumentParts().size(); melodyNumber++) {
      stringBuilder.append(" |part " + melodyNumber + ": ");
      InstrumentPart instrumentPart = this.getInstrumentParts().get(melodyNumber);
      this.getPreviousBlockEndPitches().ifPresent(integers -> stringBuilder.append("[").append(integers).append("]"));
      stringBuilder.append(instrumentPart.toString());
      if (this.compositionInfo != null) {
        stringBuilder.append(" from ");
        stringBuilder.append(this.compositionInfo.getTitle());
      }
    }
    return stringBuilder.toString();
  }

  //TODO get rid of music block here - should return List<InstrumentPart> only
  public MusicBlock transposeClone(MusicBlock previousBlock) {
    if (this.getPreviousBlockEndPitches().isEmpty()) {
      throw new RuntimeException("Can't calculate transpose pitch. Previous block end pitches does not exist");
    }
    return ParallelUtils
        .getSinglePitchDifferenceIfExist(this.getPreviousBlockEndPitches().get(), previousBlock.getEndPitches())
        .map(transposePitch -> {
          List<InstrumentPart> transposedInstrumentParts = this.instrumentParts.stream()
              .map(instrumentPart -> instrumentPart.transposeClone(transposePitch))
              .collect(Collectors.toList());
          MusicBlock transposedBlock = new MusicBlock();
          transposedBlock.setInstrumentParts(transposedInstrumentParts);
          transposedBlock.setCompositionInfo(this.compositionInfo);
          return transposedBlock;
        })
        .orElseThrow(() -> new RuntimeException("Can't calculate transpose pitch"));

  }

  public MusicBlock clone() {
    MusicBlock clone = new MusicBlock(
        this.startTime,
        this.instrumentParts.stream()
            .map(InstrumentPart::clone)
            .collect(Collectors.toList()),
        this.compositionInfo
    );
    clone.setPreviousBlockEndPitches(this.previousBlockEndPitches);

    return clone;
  }

  public boolean isStartsWithRest() {
    return this.instrumentParts.stream()
        .allMatch(InstrumentPart::startsWithRest);
  }

  public List<Integer> getEndPitches() {
    return this.instrumentParts.stream()
        .flatMap(instrumentPart -> instrumentPart.getLastVerticalPitches().stream())
        .collect(Collectors.toList());
  }

  public boolean isRest() {
    return this.instrumentParts.stream()
        .allMatch(InstrumentPart::isRest);
  }

  public double getRhythmValue() {
    List<Double> rhythmValues = this.getInstrumentParts()
        .stream()
        .map(InstrumentPart::getRythmValue).distinct()
        .collect(Collectors.toList());

    if (rhythmValues.size() != 1) {
      throw new RuntimeException("Several instrument parts has different rhytmValues");
    }
    return rhythmValues.get(0);
  }

  public Optional<List<Integer>> getPreviousBlockEndPitches() {
    return Optional.ofNullable(previousBlockEndPitches);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MusicBlock that = (MusicBlock) o;

    if (this.instrumentParts.size() != that.getInstrumentParts().size()) {
      return false;
    }

    Integer instrumentPartTransposePitch = null;
    for (int instrumentPartNumber = 0; instrumentPartNumber < this.instrumentParts.size(); instrumentPartNumber++) {
      InstrumentPart instrumentPart1 = this.instrumentParts.get(instrumentPartNumber);
      InstrumentPart instrumentPart2 = that.getInstrumentParts().get(instrumentPartNumber);
      Optional<Integer> transposePitch =
          getPitchDistanceIfParallel(instrumentPart1.getNoteGroups(), instrumentPart2.getNoteGroups());
      if (transposePitch.isEmpty()
          || (instrumentPartTransposePitch != null && !instrumentPartTransposePitch.equals(transposePitch.get()))) {
        return false;
      }
      if (instrumentPartTransposePitch == null) {
        instrumentPartTransposePitch = transposePitch.get();
      }
    }
    // if false wasn't returned here, both music blocks instrument parts are parallel with
    // instrumentPartTransposePitch metric

    Optional<List<Integer>> previousBlockEndPitches1 = this.getPreviousBlockEndPitches();
    Optional<List<Integer>> previousBlockEndPitches2 = that.getPreviousBlockEndPitches();
    if ((previousBlockEndPitches1.isEmpty() && previousBlockEndPitches2.isPresent())
        || previousBlockEndPitches1.isPresent() && previousBlockEndPitches2.isEmpty()) {
      return false;
    }

    if (previousBlockEndPitches1.isPresent() && previousBlockEndPitches2.isPresent()) {
      Optional<Integer> previousEndPitches =
          ParallelUtils.getSinglePitchDifferenceIfExist(previousBlockEndPitches1.get(), previousBlockEndPitches2.get());
      if (previousEndPitches.isEmpty() || !previousEndPitches.get().equals(instrumentPartTransposePitch)) {
        return false;
      }
    }

    return ModelUtils.isTimeCorrelated(that.startTime, startTime)
           && Objects.equals(compositionInfo, that.compositionInfo);
  }

  @Override
  public int hashCode() {
    // everything else might be not equal but parallel
    return Objects.hash(compositionInfo);
  }
}
