package ru.pavelyurkin.musiccomposer.core.service.equality.form;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.service.decomposer.melody.analyzer.MelodyEqualityAnalyzer;
import ru.pavelyurkin.musiccomposer.core.service.equality.equalityMetric.EqualityMetricAnalyzer;

/**
 * Class analyzes if two melodies can belong to one form element
 */
//@Component
@Slf4j
@RequiredArgsConstructor
public class MelodyFormEqualityAnalyzer implements MelodyEqualityAnalyzer {

  private final EqualityMetricAnalyzer<Melody> equalityMetricAnalyzer;
  /**
   * Min percentage of passed sub tests necessary to consider ru.pavelyurkin.musiccomposer.equality of two melodies
   */
  @Value("${melodyFormEqualityAnalyzer.equalityTestPassThreshold}")
  private double equalityTestPassThreshold;

  public boolean isEqual(Melody firstMelody, Melody secondMelody) {

    double positivePersentage = equalityMetricAnalyzer.getEqualityMetric(firstMelody, secondMelody);
    log.debug("Percent of positive tests = {}, pass threshold = {}", positivePersentage,
        this.equalityTestPassThreshold);

    if (equalityTestPassThreshold <= positivePersentage) {
      log.debug("Melodies considered to belong to same form element");
      return true;
    } else {
      log.debug("Melodies considered different in term of form");
      return false;
    }
  }

}
