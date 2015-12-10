package persistance.model.melody;

import persistance.model.AbstractPersistanceModel;
import persistance.model.note.Note;
import utils.ModelUtils;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
public class Melody extends AbstractPersistanceModel {

	@ManyToMany( cascade = CascadeType.ALL )
	@JoinTable( name = "MELODY_NOTES", joinColumns = {@JoinColumn( name = "MELODY_ID" )})
	public List<Note> noteList;

	@Column
	public char form;

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof Melody ) )
			return false;

		Melody melody = ( Melody ) o;

		if ( !noteList.equals( melody.noteList ) )
			return false;

		return true;
	}

	@Override public int hashCode() {
		return noteList.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for ( Note note : this.noteList ) {
			stringBuilder.append( String.format( "{%d %s|%.3f}", note.pitch, ModelUtils.getNoteNameByPitch( note.pitch ), note.rhythmValue ) );
		}
		return stringBuilder.toString();
	}
}
