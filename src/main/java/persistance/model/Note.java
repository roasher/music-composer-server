package persistance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
class Note {
	@Id @GeneratedValue
	long id;
	@Column
	int pitch;
	@Column
	double rhythmValue;
	@Column
	int dynamic;
	@Column
	double pan;

	Note() {}
	Note( int pitch, double rhythmValue ) { this.pitch = pitch; this.rhythmValue = rhythmValue; }
	Note( int pitch, double rhythmValue, int dynamic, double pan ) { this( pitch, rhythmValue ); this.dynamic = dynamic; this.pan = pan; }

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
