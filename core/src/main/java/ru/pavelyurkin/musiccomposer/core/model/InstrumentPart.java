package ru.pavelyurkin.musiccomposer.core.model;

import jm.music.data.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NoteGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getLast;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Notes that are performed by one instrument
 */
public class InstrumentPart {

	private List<NoteGroup> noteGroups = new ArrayList<>();
	private Integer instrument;

	public InstrumentPart( Note... notes ) {
		noteGroups.add(new NewMelody( notes ) );
	}

	public InstrumentPart( List<NoteGroup> noteGroups ) {
		this.noteGroups = noteGroups;
	}

	public InstrumentPart( NoteGroup... noteGroups ) {
		this.noteGroups.addAll( Arrays.asList( noteGroups ) );
	}

	public double getRythmValue() {
		return noteGroups.stream().mapToDouble( NoteGroup::getRhythmValue ).sum();
	}

	public void add( InstrumentPart instrumentPartToAdd ) {
		if ( !( this.instrument == null && instrumentPartToAdd.instrument == null ) ||
				!Objects.equals( this.instrument, instrumentPartToAdd.getInstrument() ) ) {
			throw new RuntimeException( "Adding parts for different instruments is forbidden" );
		}
		this.noteGroups.addAll( instrumentPartToAdd.getNoteGroups() );
	}

	public InstrumentPart transposeClone( int transposePitch ) {
		List<NoteGroup> transposedNoteGroups = this.noteGroups.stream()
				.map( noteGroup -> noteGroup.transposeClone( transposePitch ) ).collect( Collectors.toList() );
		return new InstrumentPart( transposedNoteGroups, this.instrument );
	}

	public InstrumentPart clone() {
		List<NoteGroup> transposedNoteGroups = this.noteGroups.stream()
				.map( noteGroup -> noteGroup.transposeClone( 0 ) ).collect( Collectors.toList() );
		return new InstrumentPart( transposedNoteGroups, this.instrument );
	}

	public int getMaxPitch() {
		return noteGroups.stream()
				.mapToInt( NoteGroup::getMaxPitch )
				.max()
				.getAsInt();
	}

	public int getMinNonRestPitch() {
		return noteGroups.stream()
				.filter( noteGroup -> !noteGroup.isRest() )
				.mapToInt( NoteGroup::getMinNonRestPitch )
				.min()
				.getAsInt();
	}

	public boolean startsWithRest() {
		return noteGroups.get( 0 ).isRest();
	}

	public List<Integer> getFirstVerticalPitches() {
		return noteGroups.get( 0 ).getFirstVerticalPitches();
	}

	public List<Integer> getLastVerticalPitches() {
		return getLast(noteGroups).getLastVerticalPitches();
	}

	public boolean isRest() {
		return noteGroups.stream().allMatch( NoteGroup::isRest );
	}
}
