package ru.pavelyurkin.musiccomposer.core.model;

import jm.music.data.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NoteGroup;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Notes that are performed by one instrument
 */
public class InstrumentPart {

	private List<NoteGroup> noteGroups;
	private int instrument;

	public InstrumentPart( Note... notes ) {

	}

	public InstrumentPart( List<NoteGroup> noteGroups ) {
		this.noteGroups = noteGroups;
	}

	public InstrumentPart( NoteGroup... noteGroups ) {
		this.noteGroups = Arrays.asList( noteGroups );
	}

	public double getRythmValue() {
		return 0;
	}

	public void add( InstrumentPart instrumentPartToAdd ) {
	}

	public InstrumentPart transposeClone( int transposePitch ) {
		return null;
	}

	public int getMaxPitch() {
		return 0;
	}

	/**
	 * Without rests
	 * @return
	 */
	public int getMinPitch() {
		return 0;
	}

	public boolean startsWithRest() {
		return false;
	}

	public List<Integer> getFirstVerticalPitches() {
		return null;
	}

	public List<Integer> getLastVerticalPitches() {
		return null;
	}

	public InstrumentPart clone() {
		return null;
	}

	public boolean isRest() {
		return true;
	}
}
