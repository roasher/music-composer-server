package ru.pavelyurkin.musiccomposer.core.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Optional;
import jm.music.data.Note;
import org.junit.jupiter.api.Test;

class ParallelUtilsTest {

  @Test
  public void returnZeroIfAllPitchesAreRests() throws Exception {
    assertThat(
        ParallelUtils.getSinglePitchDifferenceIfExist(List.of(Note.REST, Note.REST), List.of(Note.REST, Note.REST)),
        is(Optional.of(0)));
  }

  @Test
  public void noTransposePitchIfDifferentPitchLengths() throws Exception {
    assertThat(
        ParallelUtils.getSinglePitchDifferenceIfExist(List.of(Note.REST, Note.REST, 8), List.of(Note.REST, Note.REST)),
        is(Optional.empty()));
  }

  @Test
  public void noTransposePitchIfSwappedPlaces() throws Exception {
    assertThat(ParallelUtils.getSinglePitchDifferenceIfExist(List.of(10, Note.REST), List.of(Note.REST, 10)),
        is(Optional.empty()));
  }

  @Test
  public void noTransposePitchIfNotParallel() throws Exception {
    assertThat(ParallelUtils.getSinglePitchDifferenceIfExist(List.of(10, 11), List.of(10, 12)), is(Optional.empty()));
  }

  @Test
  public void calculateTransposePitchIfParallel() throws Exception {
    assertThat(ParallelUtils.getSinglePitchDifferenceIfExist(List.of(10, 11), List.of(20, 21)), is(Optional.of(10)));
  }

  @Test
  public void calculateTransposePitchIfParallelWithRests() throws Exception {
    assertThat(ParallelUtils.getSinglePitchDifferenceIfExist(List.of(10, Note.REST, 11), List.of(20, Note.REST, 21)),
        is(Optional.of(10)));
  }


}
