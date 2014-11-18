package model;

import jm.music.data.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by night wish on 01.11.14.
 */
public class Keys {
																   		//C  D  E  F  G  A  B
	public static final Key C_MAJOR = new Key( "C_MAJOR", Arrays.asList(  0, 2, 4, 5, 7, 9, 11 ) );
	public static final Key G_MAJOR = new Key( "G_MAJOR", Arrays.asList(  0, 2, 4, 6, 7, 9, 11 ) );
	public static final Key D_MAJOR = new Key( "D_MAJOR", Arrays.asList(  1, 2, 4, 6, 7, 9, 11 ) );
	public static final Key A_MAJOR = new Key( "A_MAJOR", Arrays.asList(  1, 2, 4, 6, 8, 9, 11 ) );
	public static final Key E_MAJOR = new Key( "E_MAJOR", Arrays.asList(  1, 3, 4, 6, 8, 9, 11 ) );
	public static final Key B_MAJOR = new Key( "B_MAJOR", Arrays.asList(  1, 3, 4, 6, 8, 10, 11 ) );
	public static final Key F_SHARP_MAJOR = new Key( "F_SHARP_MAJOR", Arrays.asList(  1, 3, 5, 6, 8, 10, 11 ) );
	public static final Key C_SHARP_MAJOR = new Key( "C_SHARP_MAJOR", Arrays.asList(  1, 3, 5, 6, 8, 10, 0 ) );
											 								 //C  D  E  F  G  A  B
	public static final Key F_MAJOR =      new Key( "F_MAJOR", Arrays.asList(  0, 2, 4, 5, 7, 9, 10 ) );
	public static final Key B_FLAT_MAJOR = new Key( "B_FLAT_MAJOR", Arrays.asList(  0, 2, 3, 5, 7, 9, 10 ) );
	public static final Key E_FLAT_MAJOR = new Key( "E_FLAT_MAJOR", Arrays.asList(  0, 2, 3, 5, 7, 8, 10 ) );
	public static final Key A_FLAT_MAJOR = new Key( "A_FLAT_MAJOR", Arrays.asList(  0, 1, 3, 5, 7, 8, 10 ) );
	public static final Key D_FLAT_MAJOR = new Key( "D_FLAT_MAJOR", Arrays.asList(  0, 1, 3, 5, 6, 8, 10 ) );
	public static final Key G_FLAT_MAJOR = new Key( "G_FLAT_MAJOR", Arrays.asList(  11, 1, 3, 5, 6, 8, 10 ) );
	public static final Key C_FLAT_MAJOR = new Key( "C_FLAT_MAJOR", Arrays.asList(  11, 1, 3, 4, 6, 8, 10 ) );

	public static final List< Key > allKeys = new ArrayList< Key >();
	static {
		allKeys.add( C_MAJOR );
		allKeys.add( G_MAJOR );
		allKeys.add( D_MAJOR );
		allKeys.add( A_MAJOR );
		allKeys.add( E_MAJOR );
		allKeys.add( B_MAJOR );
		allKeys.add( F_SHARP_MAJOR );
		allKeys.add( C_SHARP_MAJOR );
		allKeys.add( F_MAJOR );
		allKeys.add( B_FLAT_MAJOR );
		allKeys.add( E_FLAT_MAJOR );
		allKeys.add( A_FLAT_MAJOR );
		allKeys.add( D_FLAT_MAJOR );
		allKeys.add( G_FLAT_MAJOR );
		allKeys.add( C_FLAT_MAJOR );
	};

	public static List< Key > getPossibleKeys( Note[] notes, int maxNumberOfNotesOutOfKey ) {
		List< Key > possibleKeys = new ArrayList<>();
		for ( Key key : allKeys ) {
			int numberOfNotesOutOfKey = 0;
			for ( Note note : notes ) {
				if ( note.getPitch() != Integer.MIN_VALUE && !key.getNotes().contains( note.getPitch()%12 ) ) {
					numberOfNotesOutOfKey++;
				}
			}
			if ( numberOfNotesOutOfKey <= maxNumberOfNotesOutOfKey ) {
				possibleKeys.add( key );
			}
		}

		return possibleKeys;
	}
}
