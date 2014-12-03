package model.melody;

/**
 * Created by pyurkin on 03.12.14.
 */
public class Form {

	private char value = 'A';

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof Form ) ) {
			return false;
		}

		Form that = ( Form ) o;

		if ( value != that.value ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return ( int ) value;
	}

	public char getValue() {
		return value;
	}

	public void setValue( char value ) {
		this.value = value;
	}
}
