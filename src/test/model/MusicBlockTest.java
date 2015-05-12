package model;

import jm.music.data.Note;
import model.melody.Melody;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;


/**
 * Created by pyurkin on 13.01.15.
 */
public class MusicBlockTest {
	@Test
	public void testSettingPreviousNext() {
		List<Melody> melodyList1 = new ArrayList<>(  );
		melodyList1.add( new Melody( new Note[] {
		  new Note(74, 0.5 ),
		} ) );
		melodyList1.add( new Melody( new Note[] {
		  new Note(48, 0.5 ),
		} ) );
		MusicBlock musicBlock1 = new MusicBlock( melodyList1, null );

		List<Melody> melodyList2 = new ArrayList<>(  );
		melodyList2.add( new Melody( new Note[] {
		  new Note( 76, 0.5 ),
		} ) );
		melodyList2.add( new Melody( new Note[] {
		  new Note( 48, 0.5 ),
		} ) );
		MusicBlock musicBlock2 = new MusicBlock( melodyList2, null );

		musicBlock1.setNext( musicBlock2 );
		assertTrue( musicBlock1.getBlockMovementFromThisToNext().getTopVoiceMelodyMovement() == 2 );
		assertTrue( musicBlock1.getBlockMovementFromThisToNext().getBottomVoiceMelodyMovement() == 0 );
	}
}
