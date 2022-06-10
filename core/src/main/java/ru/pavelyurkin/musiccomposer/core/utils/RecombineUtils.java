package ru.pavelyurkin.musiccomposer.core.utils;

import static com.google.common.collect.Iterables.getLast;
import static ru.pavelyurkin.musiccomposer.core.utils.ModelUtils.trimToTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NoteGroup;

/**
 * Class handles all recombination between melody lists
 */
@UtilityClass
public class RecombineUtils {

  /**
   * Recombines melody lists.
   * In the result, all melodies has only one note within
   *
   * @param instrumentParts
   * @return
   */
  public static List<List<InstrumentPart>> recombine(List<InstrumentPart> instrumentParts) {
    List<Double> rhythmEdges = instrumentParts.stream().map(InstrumentPart::getNoteGroups)
        .flatMap(noteGroups -> getRhythmEdgeList(noteGroups).stream())
        .distinct()
        .sorted()
        .collect(Collectors.toList());

    List<List<InstrumentPart>> recombineList = new ArrayList<>();
    double previousEdge = 0;
    for (double edge : rhythmEdges) {
      recombineList.add(trimToTime(instrumentParts, previousEdge, edge));
      previousEdge = edge;
    }
    return recombineList;
  }

  /**
   * Returns the edge list of melody noteGroups
   * Sets first edge to rhythm value of the first note, and incrementing this value by
   * rhythm value of all noteGroups one by one
   *
   * @param noteGroups
   * @return
   */
  public static List<Double> getRhythmEdgeList(List<NoteGroup> noteGroups) {
    List<Double> edgeList = new ArrayList<>();
    double lastEdge = 0;
    for (int noteGroupNumber = 0; noteGroupNumber < noteGroups.size(); noteGroupNumber++) {
      NoteGroup noteGroup = noteGroups.get(noteGroupNumber);
      List<Double> rhythmEdgeList = noteGroup.getRhythmEdgeList();
      for (Double aDouble : rhythmEdgeList) {
        edgeList.add(lastEdge + aDouble);
      }
      lastEdge = getLast(edgeList);
    }
    return edgeList;
  }
}
