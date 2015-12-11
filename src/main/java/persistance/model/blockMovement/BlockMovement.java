package persistance.model.blockMovement;

import persistance.model.AbstractPersistanceModel;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by pyurkin on 06.05.2015.
 */
@Entity( name = "BLOCK_MOVEMENT" )
public class BlockMovement extends AbstractPersistanceModel {
    @Column( name = "TOP_VOICE_MOVEMENT" )
    public int topVoiceMovement;
    @Column( name = "BOTTOM_VOICE_MOVEMENT" )
    public int bottomVoiceMovement;

    BlockMovement() {}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BlockMovement))
            return false;

        BlockMovement that = (BlockMovement) o;

        if (topVoiceMovement != that.topVoiceMovement)
            return false;
        return bottomVoiceMovement == that.bottomVoiceMovement;

    }

    @Override
    public int hashCode() {
        int result = topVoiceMovement;
        result = 31 * result + bottomVoiceMovement;
        return result;
    }
}
