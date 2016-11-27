package ru.pavelyurkin.musiccomposer.equality.equalityMetric;

public interface EqualityMetricAnalyzer<T> {

	double getEqualityMetric( T firstObject, T secondObject );

}
