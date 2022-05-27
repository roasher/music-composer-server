package ru.pavelyurkin.musiccomposer.core.service.equality.form;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.service.equality.equalityMetric.EqualityMetricAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;

/**
 * Class that decides if two Lists fo melodies are form equal
 */
@Component
@Slf4j
public class FormEquality implements RelativelyComparable<List<InstrumentPart>>, InitializingBean {

  /**
   * Min value of equality metric to consider two blocks form equal
   */
  @Value("${formEquality.instrumentEqualityPassThreshold:0.7}")
  private double instrumentEqualityPassThreshold;
  /**
   * Max value of equality metric to consider two blocks form different
   */
  @Value("${formEquality.instrumentEqualityFailThreshold:0.4}")
  private double instrumentEqualityFailThreshold;

  @Autowired
  private EqualityMetricAnalyzer<List<InstrumentPart>> equalityMetricAnalyzer;

  /**
   * Consider if music blocks are form equal.
   * Music blocks considered form equal if enough of their instrument parts is equal.
   *
   * @param firstMusicBlockInstrumentParts
   * @param secondMusicBlockInstrumentParts
   * @return
   */
  @Override
  public Pair<ResultOfComparison, Double> isEqual(List<InstrumentPart> firstMusicBlockInstrumentParts,
                                                  List<InstrumentPart> secondMusicBlockInstrumentParts) {
    if (firstMusicBlockInstrumentParts.size() != secondMusicBlockInstrumentParts.size()) {
      log.info("Input collections of melodies has different sizes so they can't be considered equal");
      return Pair.of(ResultOfComparison.UNDEFINED, Double.MAX_VALUE);
    }

    double successTestPersentage =
        equalityMetricAnalyzer.getEqualityMetric(firstMusicBlockInstrumentParts, secondMusicBlockInstrumentParts);
    double diffMeasure;
    if (successTestPersentage >= instrumentEqualityPassThreshold) {
      diffMeasure = getMeasureOfDifference(successTestPersentage, instrumentEqualityPassThreshold);
      log.debug(
          "Successful tests percentage {} higher than pass threshold {}. Diff measure = {}. Music Blocks considered "
          + "form equal.",
          successTestPersentage, instrumentEqualityPassThreshold, diffMeasure);
      return Pair.of(ResultOfComparison.EQUAL, diffMeasure);
    } else if (successTestPersentage <= instrumentEqualityFailThreshold) {
      diffMeasure = getMeasureOfDifference(successTestPersentage, instrumentEqualityFailThreshold);
      log.debug(
          "Successful tests percentage {} lower than fail threshold {}. Diff measure = {}. Music Blocks considered "
          + "non equal",
          successTestPersentage, instrumentEqualityFailThreshold, diffMeasure);
      return Pair.of(ResultOfComparison.DIFFERENT, diffMeasure);
    } else {
      diffMeasure = Math.min(getMeasureOfDifference(successTestPersentage, instrumentEqualityFailThreshold),
          getMeasureOfDifference(successTestPersentage, instrumentEqualityPassThreshold));
      log.debug(
          "Successful tests percentage {} higher than the fail threshold {} but lower that pass threshold {}. Diff "
          + "measure = {}. Blocks form equality considered undefined",
          successTestPersentage, instrumentEqualityFailThreshold, instrumentEqualityPassThreshold, diffMeasure);
      return Pair.of(ResultOfComparison.UNDEFINED, diffMeasure);
    }
  }

  public static double getMeasureOfDifference(double first, double second) {
    double diff = Math.abs(first - second);
    if (diff > 1) {
      return 0;
    }
    return 1 - diff;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (instrumentEqualityFailThreshold > instrumentEqualityPassThreshold) {
      throw new IllegalArgumentException("Illegal configuration of FormEqualityBean: fail threshold "
                                         + instrumentEqualityFailThreshold
                                         + " is greater than pass threshold "
                                         + instrumentEqualityPassThreshold);
    }
  }

  public void setInstrumentEqualityPassThreshold(double instrumentEqualityPassThreshold) {
    this.instrumentEqualityPassThreshold = instrumentEqualityPassThreshold;
  }

  public void setInstrumentEqualityFailThreshold(double instrumentEqualityFailThreshold) {
    this.instrumentEqualityFailThreshold = instrumentEqualityFailThreshold;
  }
}
