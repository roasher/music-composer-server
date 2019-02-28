package ru.pavelyurkin.musiccomposer.core.model.notegroups;

import jm.music.data.Note;
import jm.music.data.Rest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.*;
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
	public NewMelody clone() {
		return new NewMelody( notes.stream()
				.map( note -> new Note( note.getPitch(), note.getRhythmValue() ) )
				.collect( Collectors.toList() ) );
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
				.map( note -> !note.isRest() ?
						new Note( note.getPitch() + transposePitch, note.getRhythmValue() ) :
						new Rest( note.getRhythmValue() ) )
				.collect( Collectors.toList() );
		return new NewMelody( transposedNotes );
	}

	@Override
	public Pair<NoteGroup, NoteGroup> divideByRhythmValue( double rhythmValue ) {
		if ( rhythmValue >= this.getRhythmValue() ) {
			throw new IllegalArgumentException( "Input rhythmValue can't be greater or equal than Melody rhythmValue" );
		}
		NewMelody left = new NewMelody(  );
		NewMelody right = new NewMelody();

		double noteStartTime = 0;
		for ( Note note : this.notes ) {
			double noteEndTime = noteStartTime + note.getRhythmValue();
			if ( noteEndTime <= rhythmValue ) {
				left.getNotes().add( note );
			} else if ( noteStartTime < rhythmValue && noteEndTime > rhythmValue ) {
				left.getNotes().add( new Note( note.getPitch(), rhythmValue - noteStartTime ) );
				right.getNotes().add( new Note( note.getPitch(), noteEndTime - rhythmValue ) );
			} else {
				right.getNotes().add( note );
			}
			noteStartTime = noteEndTime;
		}
		return Pair.of( left, right );
	}

	@Override
	public NoteGroup cloneRange( double edgeLeft, double edgeRight ) {
		if ( edgeLeft < 0 || edgeRight > this.getRhythmValue() || edgeLeft >= edgeRight )
			throw new IllegalArgumentException("Can't clone range");

		List<Note> cloneNotes =  new ArrayList<>(  );
		BigDecimal noteStartTime = BigDecimal.ZERO;
		for ( Note note : notes ) {
			BigDecimal rhythmValue = BigDecimal.valueOf( note.getRhythmValue() );
			BigDecimal noteEndTime = noteStartTime.add( rhythmValue );
			if ( noteStartTime.doubleValue() <= edgeLeft && noteEndTime.doubleValue() > edgeLeft ) {
				BigDecimal min = BigDecimal.valueOf( Math.min( edgeRight, noteEndTime.doubleValue() ) );
				cloneNotes.add( new Note( note.getPitch(), min.subtract( BigDecimal.valueOf( edgeLeft ) ).doubleValue() ) );
			} else if ( noteStartTime.doubleValue() >= edgeLeft && noteEndTime.doubleValue() <= edgeRight ) {
				cloneNotes.add( new Note( note.getPitch(), rhythmValue.doubleValue() ) );
			} else if ( noteStartTime.doubleValue() < edgeRight && noteEndTime.doubleValue() > edgeRight ) {
				cloneNotes.add( new Note( note.getPitch(), BigDecimal.valueOf( edgeRight ).subtract( noteStartTime ).doubleValue() ) );
				break;
			}
			noteStartTime = noteStartTime.add( rhythmValue );
		}
		return new NewMelody( cloneNotes );
	}

	@Override
	public List<Double> getRhythmEdgeList() {
		List<Double> out = new ArrayList<>(  );
		double previousRhythmEdge = 0;
		for ( int currentNoteNumber = 0; currentNoteNumber < notes.size(); currentNoteNumber ++ ) {
			double rhythm = notes.get( currentNoteNumber ).getRhythmValue();
			out.add( previousRhythmEdge + rhythm );
			previousRhythmEdge += rhythm;
		}
		return out;
	}

	@Override
	public Set<Integer> getAllPitches() {
		return notes.stream()
				.map( Note::getPitch )
				.collect( Collectors.toSet());
	}

	/**
	 * Glues note to the end. If last note has same pitch - it's rhythm value increasing
	 * @param note
	 */
	public void glueNoteToTheEnd( Note note ) {
		if ( this.notes.isEmpty() || !getLast( this.notes ).samePitch( note ) ) {
			this.getNotes().add( note );
		} else {
			Note lastNote = getLast( this.notes );
			lastNote.setRhythmValue( lastNote.getRhythmValue() + note.getRhythmValue(), true );
		}
	}

	public void addNoteToTheEnd( Note note ) {
		this.getNotes().add( note );
	}

	public void addNotesToTheEnd( List<Note> notes ) {
		notes.forEach( this::addNoteToTheEnd );
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		NewMelody melody = ( NewMelody ) o;
		if (this.notes.size() != melody.notes.size()) return false;
		for ( int noteNumber = 0; noteNumber < this.notes.size(); noteNumber++ ) {
			if ( !this.notes.get( noteNumber ).equals( melody.notes.get( noteNumber ) ) )
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash( notes );
	}
}
