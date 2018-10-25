package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Chord extends NoteGroup {

	private List<Integer> pitches;
	private double rhythmValue;

	@Override
	public long getRhythmValue() {
		return 0;
	}

	@Override
	public NoteGroup cloneWithRhythmValue( double doubleValue ) {
		return null;
	}

	@Override
	public boolean isRest() {
		return false;
	}
}
