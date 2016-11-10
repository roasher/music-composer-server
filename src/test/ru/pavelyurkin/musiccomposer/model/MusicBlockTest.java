package ru.pavelyurkin.musiccomposer.model;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jm.music.data.Note;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;


/**
 * Created by pyurkin on 13.01.15.
 */
public class MusicBlockTest {
	@Test
	public void testSettingPreviousNext() {
		List<Melody> melodyList1 = new ArrayList<>(  );
		melodyList1.add( new Melody( new Note(74, 0.5 ) ) );
		melodyList1.add( new Melody( new Note(48, 0.5 ) ) );
		MusicBlock musicBlock1 = new MusicBlock( melodyList1, null );

		List<Melody> melodyList2 = new ArrayList<>(  );
		melodyList2.add( new Melody( new Note( 76, 0.5 ) ) );
		melodyList2.add( new Melody( new Note( 48, 0.5 ) ) );
		MusicBlock musicBlock2 = new MusicBlock( melodyList2, null );

		musicBlock1.setBlockMovementFromPreviousToThis( new BlockMovement( musicBlock1.getMelodyList(), musicBlock2.getMelodyList() ) );
		assertTrue( musicBlock1.getBlockMovementFromPreviousToThis().getVoiceMovements().get( 0 ) == 2 );
		assertTrue( musicBlock1.getBlockMovementFromPreviousToThis().getVoiceMovements().get( 1 ) == 0 );
	}
}
