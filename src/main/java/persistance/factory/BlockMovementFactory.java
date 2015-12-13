package persistance.factory;

import org.springframework.stereotype.Component;
import persistance.factory.AbstractFactory;
import persistance.jpa.BlockMovement;

/**
 * Created by Wish on 03.07.2015.
 */
@Component
public class BlockMovementFactory extends AbstractFactory<BlockMovement> {

    public BlockMovement getInstance( int topVoiceMovement, int bottomVoiceMovement ) {
        BlockMovement blockMovement = new BlockMovement();
        blockMovement.topVoiceMovement = topVoiceMovement;
        blockMovement.bottomVoiceMovement = bottomVoiceMovement;
        return getUniqueInstance( blockMovement );
    }

}
