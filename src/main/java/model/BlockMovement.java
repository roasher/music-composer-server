package model;

import model.melody.Melody;
import model.melody.MelodyMovement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class represents movement from one musical block to another
 * As for now comes in two flavors : top voice movement and bottom voice movement
 * We are not saving multiple movements because one instrument can do chord, nevertheless, top( melody ) and bottom( bass )
 * are considered as most important ones
 * Always subtracting first from second ( deltaMovement = second - first )
 * Created by Pavel Yurkin on 20.07.14.
 */
public class BlockMovement implements Serializable {

    private int topVoiceMelodyMovement;
    private int bottomVoiceMelodyMovement;

	public BlockMovement( int topVoiceMelodyMovement, int bottomVoiceMelodyMovement ) { this.topVoiceMelodyMovement = topVoiceMelodyMovement; this.bottomVoiceMelodyMovement = bottomVoiceMelodyMovement; }

    public BlockMovement( MusicBlock firstMusicBlock, MusicBlock secondMusicBlock ) {

		int firstMusicBlockTopNotePitch = Integer.MIN_VALUE;
		int firstMusicBlockBottomNotePitch = Integer.MAX_VALUE;

		for ( Melody melody : firstMusicBlock.getMelodyList() ) {
			int noteNumber = melody.size();
			if ( melody.getNote( noteNumber - 1 ).getPitch() > firstMusicBlockTopNotePitch ) {
				firstMusicBlockTopNotePitch = melody.getNote( noteNumber - 1 ).getPitch();
			}
			if ( melody.getNote( noteNumber - 1 ).getPitch() < firstMusicBlockBottomNotePitch ) {
				firstMusicBlockBottomNotePitch = melody.getNote( noteNumber - 1 ).getPitch();
			}
		}

		int secondMusicBlockTopNotePitch = Integer.MIN_VALUE;
		int secondMusicBlockBottomNotePitch = Integer.MAX_VALUE;

		for ( Melody melody : secondMusicBlock.getMelodyList() ) {
			if ( melody.getNote( 0 ).getPitch() > secondMusicBlockTopNotePitch ) {
				secondMusicBlockTopNotePitch = melody.getNote( 0 ).getPitch();
			}
			if ( melody.getNote( 0 ).getPitch() < secondMusicBlockBottomNotePitch ) {
				secondMusicBlockBottomNotePitch = melody.getNote( 0 ).getPitch();
			}
		}

        topVoiceMelodyMovement = secondMusicBlockTopNotePitch - firstMusicBlockTopNotePitch ;
        bottomVoiceMelodyMovement = secondMusicBlockBottomNotePitch - firstMusicBlockBottomNotePitch;
    }

	public int getTopVoiceMelodyMovement() {
		return topVoiceMelodyMovement;
	}

	public int getBottomVoiceMelodyMovement() {
		return bottomVoiceMelodyMovement;
	}

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof BlockMovement ) )
			return false;

		BlockMovement that = ( BlockMovement ) o;

		if ( topVoiceMelodyMovement != that.topVoiceMelodyMovement )
			return false;
		return bottomVoiceMelodyMovement == that.bottomVoiceMelodyMovement;

	}

	@Override public int hashCode() {
		int result = topVoiceMelodyMovement;
		result = 31 * result + bottomVoiceMelodyMovement;
		return result;
	}
}
