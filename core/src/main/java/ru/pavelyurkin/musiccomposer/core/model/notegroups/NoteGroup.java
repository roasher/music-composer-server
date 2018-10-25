package ru.pavelyurkin.musiccomposer.core.model.notegroups;

public abstract class NoteGroup {

	public abstract long getRhythmValue();


	public abstract NoteGroup cloneWithRhythmValue( double doubleValue );

	public abstract boolean isRest();
}
