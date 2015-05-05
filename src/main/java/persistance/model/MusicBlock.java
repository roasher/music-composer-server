package persistance.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
class MusicBlock {
	@Id @GeneratedValue
	long id;
	@ManyToMany
	List<Melody> melodyList;
	@ManyToMany
	MusicBlock next;
	@ManyToMany
	MusicBlock previous;
	@Column
	CompositionInfo compositionInfo;

	MusicBlock() {}
	MusicBlock( List<Melody> melodyList ) { this.melodyList = melodyList; }
	MusicBlock( List<Melody> melodyList, CompositionInfo compositionInfo ) { this( melodyList ); this.compositionInfo = compositionInfo; }
	MusicBlock( Melody... melodies ) { this( Arrays.asList( melodies ) ); }

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof MusicBlock ) )
			return false;

		MusicBlock that = ( MusicBlock ) o;

		if ( compositionInfo != null ? !compositionInfo.equals( that.compositionInfo ) : that.compositionInfo != null )
			return false;
		if ( !melodyList.equals( that.melodyList ) )
			return false;
		if ( next != null ? !next.equals( that.next ) : that.next != null )
			return false;
		if ( previous != null ? !previous.equals( that.previous ) : that.previous != null )
			return false;

		return true;
	}

	@Override public int hashCode() {
		int result = melodyList.hashCode();
//		result = 31 * result + ( next != null ? next.hashCode() : 0 );
//		result = 31 * result + ( previous != null ? previous.hashCode() : 0 );
		result = 31 * result + ( compositionInfo != null ? compositionInfo.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(  );
		for ( persistance.model.Melody melody : this.melodyList ) {
			stringBuilder.append('|').append( melody.toString() );
		}
		return stringBuilder.toString();
	}
}
