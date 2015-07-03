package persistance.model.note;

import persistance.model.AbstractPersistanceModel;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
public class Note extends AbstractPersistanceModel {

	@Column
	public int pitch;
	@Column
	public double rhythmValue;
	@Column
	public int dynamic;
	@Column
	public double pan;

	Note() {}

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof Note ) )
			return false;

		Note note = ( Note ) o;

		if ( dynamic != note.dynamic )
			return false;
		if ( Double.compare( note.pan, pan ) != 0 )
			return false;
		if ( pitch != note.pitch )
			return false;
		if ( Double.compare( note.rhythmValue, rhythmValue ) != 0 )
			return false;

		return true;
	}

	@Override public int hashCode() {
		int result;
		long temp;
		result = pitch;
		temp = Double.doubleToLongBits( rhythmValue );
		result = 31 * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		result = 31 * result + dynamic;
		temp = Double.doubleToLongBits( pan );
		result = 31 * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		return result;
	}
}
