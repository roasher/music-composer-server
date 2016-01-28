package model;

import jm.music.data.Note;
import model.melody.Melody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Class represents movement from one musical block to another
 * Created by Pavel Yurkin on 20.07.14.
 */
public class BlockMovement implements Serializable {

	private List<Integer> voiceMovements = new ArrayList<>();

	public BlockMovement( List<Integer> movements ) {
		this.voiceMovements = movements;
	}

	public BlockMovement( Integer... movements ) {
		this( Arrays.asList( movements ) );
	}

	public BlockMovement( List<Melody> firstMelodies, List<Melody> secondMelodies ) {

		if ( firstMelodies.size() != secondMelodies.size() ) {
			throw new RuntimeException( "Can't create BlockMovement from different amount of input melody collections" );
		}

		for ( int melodyNumber = 0; melodyNumber < firstMelodies.size(); melodyNumber++ ) {
			Vector firstMelodyNoteList = firstMelodies.get( melodyNumber ).getNoteList();
			Note lastNoteFromFirstMelody = ( Note ) firstMelodyNoteList.get( firstMelodyNoteList.size() - 1 );
			Vector secondMelodyNoteList = secondMelodies.get( melodyNumber ).getNoteList();
			Note firstNoteFromSecondMelody = ( Note ) secondMelodyNoteList.get( 0 );
			voiceMovements.add( firstNoteFromSecondMelody.getPitch() - lastNoteFromFirstMelody.getPitch() );
		}

    }

	public List<Integer> getVoiceMovements() {
		return voiceMovements;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof BlockMovement ) )
			return false;

		BlockMovement that = ( BlockMovement ) o;

		if ( !voiceMovements.equals( that.voiceMovements ) )
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return voiceMovements.hashCode();
	}
}
