package persistance.model.melody;

import org.springframework.stereotype.Component;
import persistance.factory.AbstractFactory;
import persistance.model.note.Note;

import java.util.List;

/**
 * Created by Wish on 03.07.2015.
 */
@Component
public class MelodyFactory extends AbstractFactory<Melody> {

    public Melody getInstance(List<Note> noteList) {
        Melody melody = new Melody();
        melody.noteList = noteList;
        return getUniqueInstance( melody );
    }

}
