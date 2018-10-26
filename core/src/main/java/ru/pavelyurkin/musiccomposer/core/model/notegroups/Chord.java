package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import jm.music.data.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
	public Chord clone() {
		return new Chord( pitches, rhythmValue );
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

	@Override
	public Pair<NoteGroup, NoteGroup> divideByRhythmValue( double rhythmValue ) {
		if ( rhythmValue >= this.rhythmValue ) {
			throw new IllegalArgumentException( "Input rhythmValue can't be greater or equal than chords rhythmValue" );
		}
		Chord left = this.clone();
		left.setRhythmValue( rhythmValue );

		Chord right = this.clone();
		right.setRhythmValue( this.rhythmValue - rhythmValue );
		return Pair.of( left, right );
	}

	@Override
	public NoteGroup cloneRange( double startTime, double endTime ) {
		if ( startTime < 0 || endTime > this.getRhythmValue() || startTime >= endTime )
			throw new IllegalArgumentException("Can't clone range");
		return new Chord( this.getPitches(), BigDecimal.valueOf( endTime ).subtract( BigDecimal.valueOf( startTime ) ).doubleValue() );
	}

	@Override
	public List<Double> getRhythmEdgeList() {
		return Collections.singletonList( this.rhythmValue );
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		Chord chord = ( Chord ) o;
		if ( Double.compare( chord.rhythmValue, rhythmValue ) != 0 )
			return false;
		return samePitches( chord.pitches );
	}

	@Override
	public int hashCode() {
		return Objects.hash( pitches, rhythmValue );
	}

	public boolean samePitches(List<Integer> pitches) {
		if (this.pitches.size() != pitches.size()) return false;
		for ( int noteNumber = 0; noteNumber < this.pitches.size(); noteNumber++ ) {
			if ( !this.pitches.get( noteNumber ).equals( pitches.get( noteNumber ) ) )
				return false;
		}
		return true;
	}
}
