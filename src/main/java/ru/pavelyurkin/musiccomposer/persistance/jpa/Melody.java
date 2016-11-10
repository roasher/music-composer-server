package ru.pavelyurkin.musiccomposer.persistance.jpa;

import ru.pavelyurkin.musiccomposer.utils.ModelUtils;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity( name = "MELODY" )
@SequenceGenerator( name="SEQ",sequenceName="MELODY_SEQ", initialValue = 1, allocationSize = 1 )
public class Melody extends AbstractPersistanceModel {

	@ManyToMany( cascade = CascadeType.ALL )
	@JoinTable( name = "MELODY_NOTE", joinColumns = {@JoinColumn( name = "MELODY_ID" )}, inverseJoinColumns = {@JoinColumn( name = "NOTE_ID") })
	public List<Note> notes;

	@Column
	public char form;

	public Melody() {}

	public Melody( List<Note> notes, char form ) {
		this.notes = notes;
		this.form = form;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof Melody ) )
			return false;

		Melody melody = ( Melody ) o;

		if ( form != melody.form )
			return false;
		return notes.equals( melody.notes );

	}

	@Override
	public int hashCode() {
		int result = notes.hashCode();
		result = 31 * result + ( int ) form;
		return result;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for ( Note note : this.notes ) {
			stringBuilder.append( String.format( "{%d %s|%.3f}", note.pitch, ModelUtils.getNoteNameByPitch( note.pitch ), note.rhythmValue ) );
		}
		return stringBuilder.toString();
	}
}
