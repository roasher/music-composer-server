package persistance.model.melody;

import persistance.model.AbstractPersistanceModel;
import persistance.model.note.Note;
import utils.ModelUtils;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity( name = "MELODY" )
public class Melody extends AbstractPersistanceModel {

	@ManyToMany( cascade = CascadeType.ALL )
	@JoinTable( name = "MELODY_NOTE", joinColumns = {@JoinColumn( name = "MELODY_ID" )})
	public List<Note> notes;

	@Column
	public char form;

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof Melody ) )
			return false;

		Melody melody = ( Melody ) o;

		if ( !notes.equals( melody.notes ) )
			return false;

		return true;
	}

	@Override public int hashCode() {
		return notes.hashCode();
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
