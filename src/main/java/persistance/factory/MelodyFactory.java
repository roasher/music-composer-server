package persistance.factory;

import org.springframework.stereotype.Component;
import persistance.jpa.Melody;
import persistance.jpa.Note;

import java.util.List;

/**
 * Created by Wish on 03.07.2015.
 */
@Component
public class MelodyFactory extends AbstractFactory<Melody> {

    public Melody getInstance(List<Note> noteList) {
        Melody melody = new Melody();
        melody.notes = noteList;
        return getUniqueInstance( melody );
    }

}
