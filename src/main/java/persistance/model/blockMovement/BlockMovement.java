package persistance.model.blockMovement;

import persistance.model.AbstractPersistanceModel;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by pyurkin on 06.05.2015.
 */
@Entity
public class BlockMovement extends AbstractPersistanceModel {
    @Column
    public int topVoiceMovement;
    @Column
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
