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

    private MelodyMovement topVoiceMelodyMovement;
    private MelodyMovement bottomVoiceMelodyMovement;

    public BlockMovement( MusicBlock firstMusicBlock, MusicBlock secondMusicBlock ) {

		int firstMusicBlockTopNotePitch = Integer.MIN_VALUE;
		int firstMusicBlockBottomNotePitch = Integer.MAX_VALUE;

		for ( Melody melody : firstMusicBlock.getMelodyList() ) {
			int noteNumber = melody.size();
			if ( melody.getNote( noteNumber - 1 ).getPitch() > firstMusicBlockTopNotePitch ) {
				firstMusicBlockTopNotePitch = melody.getNote( 0 ).getPitch();
			}
			if ( melody.getNote( noteNumber - 1 ).getPitch() < firstMusicBlockBottomNotePitch ) {
				firstMusicBlockBottomNotePitch = melody.getNote( 0 ).getPitch();
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

        topVoiceMelodyMovement = new MelodyMovement( new int[] { firstMusicBlockTopNotePitch, secondMusicBlockTopNotePitch } ) ;
        bottomVoiceMelodyMovement = new MelodyMovement( new int[] { firstMusicBlockBottomNotePitch, secondMusicBlockBottomNotePitch } ) ;
    }

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof BlockMovement ) ) {
			return false;
		}

		BlockMovement that = ( BlockMovement ) o;

		if ( !bottomVoiceMelodyMovement.equals( that.bottomVoiceMelodyMovement ) ) {
			return false;
		}
		if ( !topVoiceMelodyMovement.equals( that.topVoiceMelodyMovement ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = topVoiceMelodyMovement.hashCode();
		result = 31 * result + bottomVoiceMelodyMovement.hashCode();
		return result;
	}

	// Getters & Setters
    public MelodyMovement getTopVoiceMelodyMovement() {
        return topVoiceMelodyMovement;
    }

    public void setTopVoiceMelodyMovement( MelodyMovement topVoiceMelodyMovement ) {
        this.topVoiceMelodyMovement = topVoiceMelodyMovement;
    }

    public MelodyMovement getBottomVoiceMelodyMovement() {
        return bottomVoiceMelodyMovement;
    }

    public void setBottomVoiceMelodyMovement( MelodyMovement bottomVoiceMelodyMovement ) {
        this.bottomVoiceMelodyMovement = bottomVoiceMelodyMovement;
    }
}
