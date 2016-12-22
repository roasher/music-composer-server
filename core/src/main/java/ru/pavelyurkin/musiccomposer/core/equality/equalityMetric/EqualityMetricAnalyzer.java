package ru.pavelyurkin.musiccomposer.core.equality.equalityMetric;

public interface EqualityMetricAnalyzer<T> {

	double getEqualityMetric( T firstObject, T secondObject );

}
