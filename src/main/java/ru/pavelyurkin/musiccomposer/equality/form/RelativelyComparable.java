package ru.pavelyurkin.musiccomposer.equality.form;

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

	ResultOfComparison isEqual(T first, T second);
}
