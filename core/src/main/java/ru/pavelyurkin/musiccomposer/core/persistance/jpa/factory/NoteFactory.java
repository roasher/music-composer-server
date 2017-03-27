package ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory;

import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.Note;

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