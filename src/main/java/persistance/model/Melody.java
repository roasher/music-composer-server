package persistance.model;

import utils.ModelUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
class Melody {
	@Id @GeneratedValue
	long id;
	@ManyToMany
	List<Note> noteList;

	Melody() {}
	Melody( List<Note> noteList ) { this.noteList = noteList; }
	Melody( Note... notes ) { this( Arrays.asList( notes ) ); }

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