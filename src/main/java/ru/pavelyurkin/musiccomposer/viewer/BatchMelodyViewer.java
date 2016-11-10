package ru.pavelyurkin.musiccomposer.viewer;

import jm.util.View;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.utils.Utils;

import java.util.Map;
import java.util.Set;

/**
 * Created by night wish on 11.10.14.
 */
@Component
public class BatchMelodyViewer {

    Logger logger = LoggerFactory.getLogger( getClass() );

    public void view( Map<Melody, Set<Melody> > melodies ) {
        for ( Map.Entry<Melody, Set<Melody> > currentEntry : melodies.entrySet() ) {
            Melody melodyKey = currentEntry.getKey();
            Set<Melody> melodySet = currentEntry.getValue();

            notate( melodyKey );
            for ( Melody currentMelody : melodySet ) {
                notate( currentMelody );
            }
            Utils.suspend();
        }
    }

    private void notate( Melody melody ) {
        logger.info( "Notating melody: {}", melody.toString() );
        View.notate( melody );
    }
}
