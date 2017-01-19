package ru.pavelyurkin.musiccomposer.core.equality.melody;

import jm.music.data.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import static ru.pavelyurkin.musiccomposer.core.model.Keys.allKeys;

/**
 * Created by night wish on 01.11.14.
 */
@Component
public class KeyEquality implements Equality {

	@Value( "${maxNumberOfNotesOutOfKey:0}" )
	private int maxNumberOfNotesOutOfKey;

	@Override
	public boolean test( Melody firstMelody, Melody secondMelody ) {
		double equalityMetrics = getEqualityMetric( firstMelody, secondMelody );
		double numberOfNotesOutOfKey = ( 1 - equalityMetrics ) * secondMelody.size();
		return numberOfNotesOutOfKey <= maxNumberOfNotesOutOfKey;
	}

	private double getEqualityMetric( Melody firstMelody, Melody secondMelody ) {
		List<Key> firstMelodyPossibleKeys = getPossibleKeys( firstMelody, 0 );
		if ( firstMelodyPossibleKeys.isEmpty() ) {
			return 1;
		}
		OptionalInt minNumberOfOutNotes = firstMelodyPossibleKeys.stream().mapToInt( value -> getNumberOfNotesOutOfKey( value, secondMelody ) ).min();
		return ( secondMelody.size() - minNumberOfOutNotes.getAsInt() ) * 1. / secondMelody.size();
	}

	private int getNumberOfNotesOutOfKey( Key key, Melody melody ) {
		return melody.getNoteList().stream()
				.filter( o -> !( ( Note ) o ).isRest() )
				.mapToInt( note -> ( ( Note ) note ).getPitch() % 12 )
				.filter( notePitch -> !key.getNotePitches().contains( notePitch ) )
				.toArray().length;
	}

	private List<Key> getPossibleKeys( Melody melody, int maxNumberOfNotesOutOfKey ) {
		List<Key> possibleKeys = new ArrayList<>();
		for ( Key key : allKeys ) {
			int numberOfNotesOutOfKey = 0;
			for ( Note note : ( List<Note> ) melody.getNoteList() ) {
				if ( note.getPitch() != Note.REST && !key.getNotePitches().contains( note.getPitch() % 12 ) ) {
					numberOfNotesOutOfKey++;
				}
			}
			if ( numberOfNotesOutOfKey <= maxNumberOfNotesOutOfKey ) {
				possibleKeys.add( key );
			}
		}

		return possibleKeys;
	}

	@Override
	public int getMaxNumberOfDiversedNotes() {
		return maxNumberOfNotesOutOfKey;
	}

	public int getMaxNumberOfNotesOutOfKey() {
		return maxNumberOfNotesOutOfKey;
	}

	public void setMaxNumberOfNotesOutOfKey( int maxNumberOfNotesOutOfKey ) {
		this.maxNumberOfNotesOutOfKey = maxNumberOfNotesOutOfKey;
	}
}
