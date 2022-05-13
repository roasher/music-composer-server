package ru.pavelyurkin.musiccomposer.core.equality.equalityMetric;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;

@RequiredArgsConstructor
public class FormEqualityMetricAnalyzer implements EqualityMetricAnalyzer<List<InstrumentPart>> {

  private final EqualityMetricAnalyzer<InstrumentPart> equalityMetricAnalyzer;

  @Override
  /**
   * Returns average of ru.pavelyurkin.musiccomposer.equality metrics from each part
   * @param firstMusicBlockInstrumentParts
   * @param secondMusicBlockInstrumentParts
   * @return
   */
  public double getEqualityMetric(List<InstrumentPart> firstMusicBlockInstrumentParts,
                                  List<InstrumentPart> secondMusicBlockInstrumentParts) {
    if (firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size()) {
      throw new RuntimeException("Input collections of melodies has different sizes");
    }
    List<Double> equalityMetrics = new ArrayList<>();
    for (int instrumentPartNumber = 0; instrumentPartNumber < firstMusicBlockInstrumentParts.size();
         instrumentPartNumber++) {
      equalityMetrics.add(equalityMetricAnalyzer
          .getEqualityMetric(firstMusicBlockInstrumentParts.get(instrumentPartNumber),
              secondMusicBlockInstrumentParts.get(instrumentPartNumber)));
    }

    return equalityMetrics.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
  }

}
