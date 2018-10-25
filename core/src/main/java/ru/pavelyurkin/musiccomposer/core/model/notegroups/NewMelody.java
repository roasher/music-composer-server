package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import jm.music.data.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getLast;

@Data
@AllArgsConstructor
public class NewMelody extends NoteGroup {

	private List<Note> notes = new ArrayList<>(  );

	public NewMelody( Note... notes ) {
		this.notes.addAll( Arrays.asList( notes ) );
	}

	@Override
	public double getRhythmValue() {
		return notes.stream()
				.mapToDouble( Note::getRhythmValue )
				.sum();
	}

	@Override
	public boolean isRest() {
		return notes.stream()
				.allMatch( Note::isRest );
	}

	@Override
	public int getMaxPitch() {
		return notes.stream()
				.mapToInt( Note::getPitch )
				.max()
				.getAsInt();
	}

	@Override
	public int getMinNonRestPitch() {
		return notes.stream()
				.filter( note -> !note.isRest() )
				.mapToInt( Note::getPitch )
				.min()
				.getAsInt();
	}

	@Override
	public List<Integer> getFirstVerticalPitches() {
		return Collections.singletonList( notes.get( 0 ).getPitch() );
	}

	@Override
	public List<Integer> getLastVerticalPitches() {
		return Collections.singletonList( getLast( notes ).getPitch() );
	}

	@Override
	public NoteGroup transposeClone( int transposePitch ) {
		List<Note> transposedNotes = this.notes.stream()
				.map( note -> new Note( note.getPitch() + transposePitch, note.getRhythmValue() ) )
				.collect( Collectors.toList() );
		return new NewMelody( transposedNotes );
	}
}
