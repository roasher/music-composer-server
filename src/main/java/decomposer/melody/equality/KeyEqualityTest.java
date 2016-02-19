package decomposer.melody.equality;

import model.Key;
import model.melody.Melody;

import java.util.List;

import static model.Keys.*;

/**
 * Created by night wish on 01.11.14.
 */
public class KeyEqualityTest implements EqualityTest {

	private int maxNumberOfNotesOutOfKey;

	@Override
	public boolean test( Melody firstMelody, Melody secondMelody ) {
		List< Key > firstMelodyPossibleKeys = getPossibleKeys( firstMelody.getNoteList(), maxNumberOfNotesOutOfKey );
		List< Key > secondMelodyPossibleKeys = getPossibleKeys( secondMelody.getNoteList(), maxNumberOfNotesOutOfKey );

		for ( Key key : firstMelodyPossibleKeys ) {
			if ( secondMelodyPossibleKeys.contains( key ) ) {
				return true;
			}
		}
		return false;
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
