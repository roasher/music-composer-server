package persistance.factory;

import org.springframework.stereotype.Component;
import persistance.factory.AbstractFactory;
import persistance.jpa.Note;

/**
 * Created by Wish on 03.07.2015.
 */
@Component
public class NoteFactory extends AbstractFactory<Note> {

    public Note getInstance( int pitch, double rhythmValue ) {
        return getInstance( pitch, rhythmValue, 0, 0 );
    }

    public Note getInstance( int pitch, double rhythmValue, int dynamic, double pan ) {
        Note note = new Note( pitch, rhythmValue, dynamic, pan );
        return getUniqueInstance( note );
    }

}
