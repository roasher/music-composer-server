package decomposer.analyzer.form;

import decomposer.analyzer.melody.equality.EqualityTest;
import jm.music.data.Note;
import model.Key;
import model.Signature;
import org.springframework.stereotype.Component;

import java.util.List;

import static model.Keys.*;

/**
 * Created by night wish on 01.11.14.
 */
public class KeyEqualityTest implements EqualityTest {

	private int maxNumberOfNotesOutOfKey;

	@Override
	public boolean test( Signature firstSignature, Signature secondSignature ) {
		List< Key > firstSignaturePossibleKeys = getPossibleKeys( firstSignature.getNoteArray(), maxNumberOfNotesOutOfKey );
		List< Key > secondSignaturePossibleKeys = getPossibleKeys( secondSignature.getNoteArray(), maxNumberOfNotesOutOfKey );

		for ( Key key : firstSignaturePossibleKeys ) {
			if ( secondSignaturePossibleKeys.contains( key ) ) {
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
