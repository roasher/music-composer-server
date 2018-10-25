package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import jm.music.data.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class NewMelody extends NoteGroup {

	private List<Note> notes;

	public NewMelody( Note... notes ) {
		this.notes = Arrays.asList( notes );
	}

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
