package model;

import java.io.Serializable;
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
class BlockMovement implements Serializable {

    private MelodyMovement topVoiceMelodyMovement;
    private MelodyMovement bottomVoiceMelodyMovement;

    public BlockMovement( MusicBlock firstMusicBlock, MusicBlock secondMusicBlock ) {

        List< Integer > firstMusicBlockEndIntervalPattern = firstMusicBlock.getEndIntervalPattern();
        int firstMusicBlockTopNotePitch = Collections.max( firstMusicBlockEndIntervalPattern );
        int firstMusicBlockBottomNotePitch = Collections.min( firstMusicBlockEndIntervalPattern );

        List< Integer > secondMusicBlockStartIntervalPattern = secondMusicBlock.getStartIntervalPattern();
        int secondMusicBlockTopNotePitch = Collections.max( secondMusicBlockStartIntervalPattern );
        int secondMusicBlockBottomNotePitch = Collections.min( secondMusicBlockStartIntervalPattern );

        topVoiceMelodyMovement = new MelodyMovement( new int[] { firstMusicBlockTopNotePitch, secondMusicBlockTopNotePitch } ) ;
        bottomVoiceMelodyMovement = new MelodyMovement( new int[] { firstMusicBlockBottomNotePitch, secondMusicBlockBottomNotePitch } ) ;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj instanceof BlockMovement ) {
            BlockMovement inputBlockMovement = ( BlockMovement ) obj;
            if ( this.bottomVoiceMelodyMovement == inputBlockMovement.bottomVoiceMelodyMovement && this.topVoiceMelodyMovement == inputBlockMovement.topVoiceMelodyMovement ) {
                return true;
            }
        }
        return false;
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
