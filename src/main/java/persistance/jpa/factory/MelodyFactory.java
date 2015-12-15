package persistance.jpa.factory;

import org.springframework.stereotype.Component;
import persistance.jpa.Melody;
import persistance.jpa.Note;

import java.util.List;

/**
 * Created by Wish on 03.07.2015.
 */
@Component
public class MelodyFactory extends AbstractFactory<Melody> {

	public Melody getInstance( List<Note> noteList, char form ) {
		Melody melody = new Melody( noteList, form );
		return getUniqueInstance( melody );
	}

}
