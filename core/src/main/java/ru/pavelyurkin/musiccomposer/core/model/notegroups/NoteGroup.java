package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public abstract class NoteGroup {

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
}
