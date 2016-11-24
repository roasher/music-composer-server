package ru.pavelyurkin.musiccomposer.persistance.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import static ru.pavelyurkin.musiccomposer.utils.Utils.isEquals;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity( name = "COMPOSITION_INFO" )
@SequenceGenerator( name="SEQ",sequenceName="COMPOSITION_INFO_SEQ", initialValue = 1, allocationSize = 1 )
public class CompositionInfo extends AbstractPersistanceModel {

	@Column
	public String author;
	@Column
	public String title;
	@Column
	public double tempo;

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof CompositionInfo ) )
			return false;

		CompositionInfo that = ( CompositionInfo ) o;

		if ( !isEquals( that.tempo, tempo ) )
			return false;
		if ( author != null ? !author.equals( that.author ) : that.author != null )
			return false;
		if ( title != null ? !title.equals( that.title ) : that.title != null )
			return false;

		return true;
	}

	@Override public int hashCode() {
		int result;
		long temp;
		result = author != null ? author.hashCode() : 0;
		result = 31 * result + ( title != null ? title.hashCode() : 0 );
		temp = Double.doubleToLongBits( tempo );
		result = 31 * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		return result;
	}
}
