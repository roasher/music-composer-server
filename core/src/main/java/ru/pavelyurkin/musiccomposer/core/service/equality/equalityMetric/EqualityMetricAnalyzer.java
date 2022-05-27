package ru.pavelyurkin.musiccomposer.core.service.equality.equalityMetric;

public interface EqualityMetricAnalyzer<T> {

  double getEqualityMetric(T firstObject, T secondObject);

}
