package persistance.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
public class PersistConverter {

	public static model.Lexicon convertToLexicon( persistance.model.Lexicon lexicon ) {
		// TODO impl
		return null;
	}

	public static persistance.model.Lexicon convertToPersistanceLexicon( model.Lexicon lexicon ) {
		// TODO impl
		return null;
	}

	public static List<model.melody.Melody> getMelodyList( List<persistance.model.Melody> persistanceMelodyList ) {
		List<model.melody.Melody> melodyList = new ArrayList<>(  );
		for ( persistance.model.Melody melody : persistanceMelodyList ) {
			melodyList.add( getMelody( melody ) );
		}
		return melodyList;
	}

	public static model.melody.Melody getMelody( persistance.model.Melody persistanceMelody ) {
		return new model.melody.Melody( getNoteList( persistanceMelody.noteList ) );
	}

	public static jm.music.data.Note getNote( persistance.model.Note persistanceNote ) {
		return new jm.music.data.Note( persistanceNote.pitch, persistanceNote.rhythmValue, persistanceNote.dynamic, persistanceNote.pan );
	}

	public static List<jm.music.data.Note> getNoteList( List<persistance.model.Note> persistanceNoteList ) {
		List<jm.music.data.Note> noteList = new ArrayList<>(  );
		for ( persistance.model.Note note : persistanceNoteList ) {
			noteList.add( getNote( note ) );
		}
		return noteList;
	}

}
