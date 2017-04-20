package ru.pavelyurkin.musiccomposer.rest.dto;

/**
 * DTO to be send to the front
 */
public class NoteDTO {

	private int pitch;
	private double rhythmValue;
	private int dynamic;
	private double pan;

	public int getPitch() {
		return pitch;
	}

	public void setPitch( int pitch ) {
		this.pitch = pitch;
	}

	public double getRhythmValue() {
		return rhythmValue;
	}

	public void setRhythmValue( double rhythmValue ) {
		this.rhythmValue = rhythmValue;
	}

	public int getDynamic() {
		return dynamic;
	}

	public void setDynamic( int dynamic ) {
		this.dynamic = dynamic;
	}

	public double getPan() {
		return pan;
	}

	public void setPan( double pan ) {
		this.pan = pan;
	}
}
