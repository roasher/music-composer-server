package ru.pavelyurkin.musiccomposer.equalityMetric;

public interface EqualityMetricAnalyzer<T> {

	double getEqualityMetric( T firstObject, T secondObject );

}
