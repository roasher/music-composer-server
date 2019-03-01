package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public abstract class NoteGroup implements Serializable {

	public static final long serialVersionUID = 1L;

	public abstract double getRhythmValue();

	public abstract NoteGroup clone();

	public abstract boolean isRest();

	public abstract int getMaxPitch();

	public abstract int getMinNonRestPitch();

	public abstract List<Integer> getFirstVerticalPitches();

	public abstract List<Integer> getLastVerticalPitches();

	public abstract NoteGroup transposeClone( int transposePitch );

	public abstract Pair<NoteGroup, NoteGroup> divideByRhythmValue( double rhythmValue );

	public abstract NoteGroup cloneRange( double startTime, double endTime );

	public abstract List<Double> getRhythmEdgeList();

	public abstract Set<Integer> getAllPitches();
}
