package ru.pavelyurkin.musiccomposer.core.persistance.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

/**
 * Created by pyurkin on 06.05.2015.
 */
@Entity( name = "BLOCK_MOVEMENT" )
@SequenceGenerator( name="SEQ",sequenceName="BLOCK_MOVEMENT_SEQ", initialValue = 1, allocationSize = 1 )
public class BlockMovement extends AbstractPersistanceModel {

    @Column( name = "VOICE_MOVEMENTS" )
    public String voiceMovements;

    @Override
    public boolean equals( Object o ) {
        if ( this == o )
            return true;
        if ( !( o instanceof BlockMovement ) )
            return false;

        BlockMovement that = ( BlockMovement ) o;

        return voiceMovements.equals( that.voiceMovements );

    }

    @Override
    public int hashCode() {
        return voiceMovements.hashCode();
    }
}
