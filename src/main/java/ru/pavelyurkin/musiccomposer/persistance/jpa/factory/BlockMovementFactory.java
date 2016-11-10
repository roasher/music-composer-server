package ru.pavelyurkin.musiccomposer.persistance.jpa.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.persistance.jpa.BlockMovement;

import java.util.List;

/**
 * Created by Wish on 03.07.2015.
 */
@Component
public class BlockMovementFactory extends AbstractFactory<BlockMovement> {

    public BlockMovement getInstance( List<Integer> voiceMovements ) {
        BlockMovement blockMovement = new BlockMovement();
        blockMovement.voiceMovements = StringUtils.join( voiceMovements , "," );
        return getUniqueInstance( blockMovement );
    }

}
