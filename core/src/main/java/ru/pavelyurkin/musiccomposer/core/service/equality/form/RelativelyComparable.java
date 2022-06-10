package ru.pavelyurkin.musiccomposer.core.service.equality.form;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Interface that holds triple comparable logic
 *
 * @param <T>
 */
public interface RelativelyComparable<T> {

  /**
   * Returning not only result of comparison but diff measure as well
   *
   * @param first
   * @param second
   * @return
   */
  Pair<ResultOfComparison, Double> isEqual(T first, T second);

  enum ResultOfComparison {
    EQUAL,
    DIFFERENT,
    UNDEFINED
  }
}
