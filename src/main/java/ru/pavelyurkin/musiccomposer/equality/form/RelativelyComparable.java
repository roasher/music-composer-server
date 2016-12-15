package ru.pavelyurkin.musiccomposer.equality.form;

import javafx.util.Pair;

/**
 * Interface that holds triple comparable logic
 * @param <T>
 */
public interface RelativelyComparable<T> {

	enum ResultOfComparison {
		EQUAL,
		DIFFERENT,
		UNDEFINED
	}

	/**
	 * Returning not only result of comparison but diff measure as well
	 * @param first
	 * @param second
	 * @return
	 */
	Pair<ResultOfComparison, Double> isEqual(T first, T second);
}
