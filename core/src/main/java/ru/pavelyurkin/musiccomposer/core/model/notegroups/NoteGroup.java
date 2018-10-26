package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public abstract class NoteGroup implements Cloneable {

	public abstract double getRhythmValue();

	public NoteGroup cloneWithRhythmValue( double doubleValue ) {
		throw new RuntimeException( "Unsupported operation" );
	}

	public abstract NoteGroup clone();

	public abstract boolean isRest();

	public abstract int getMaxPitch();

	public abstract int getMinNonRestPitch();

	public abstract List<Integer> getFirstVerticalPitches();

	public abstract List<Integer> getLastVerticalPitches();

	public abstract NoteGroup transposeClone( int transposePitch );

	public abstract Pair<NoteGroup, NoteGroup> divideByRhythmValue( double rhythmValue );
}
