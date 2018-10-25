package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import jm.music.data.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Chord extends NoteGroup {

	private List<Integer> pitches;
	private double rhythmValue;

	@Override
	public double getRhythmValue() {
		return rhythmValue;
	}

	@Override
	public boolean isRest() {
		return pitches.stream().allMatch( integer -> integer.equals( Note.REST ) );
	}

	@Override
	public int getMaxPitch() {
		return pitches.stream()
				.mapToInt( Integer::intValue )
				.max()
				.getAsInt();
	}

	@Override
	public int getMinNonRestPitch() {
		return pitches.stream()
				.filter( integer -> integer != Note.REST )
				.mapToInt( Integer::intValue )
				.min()
				.getAsInt();
	}

	@Override
	public List<Integer> getFirstVerticalPitches() {
		return pitches;
	}

	@Override
	public List<Integer> getLastVerticalPitches() {
		return pitches;
	}

	@Override
	public NoteGroup transposeClone( int transposePitch ) {
		List<Integer> tranposedPitches = pitches.stream()
				.map( integer -> integer + transposePitch )
				.collect( Collectors.toList() );
		return new Chord( tranposedPitches, this.rhythmValue );
	}
}
