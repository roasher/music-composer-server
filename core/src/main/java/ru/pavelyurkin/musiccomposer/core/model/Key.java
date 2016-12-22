package ru.pavelyurkin.musiccomposer.core.model;

import java.util.List;

/**
 * Class represents key
 * Created by night wish on 02.11.14.
 */
public class Key {

	private String name;
	private List< Integer > notePitches;

	public Key( String name, List< Integer > notes ) {
		this.name = name;
		this.notePitches = notes;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof Key ) ) {
			return false;
		}

		Key key = ( Key ) o;

		if ( !name.equals( key.name ) ) {
			return false;
		}
		if ( !notePitches.equals( key.notePitches ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + notePitches.hashCode();
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public List<Integer> getNotePitches() {
		return notePitches;
	}

	public void setNotePitches( List<Integer> notePitches ) {
		this.notePitches = notePitches;
	}
}

