package model.utils;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.View;
import model.MusicBlock;
import org.junit.Test;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static jm.JMC.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ScoreSlicerTest {

	private ScoreSlicer scoreSlicer = new ScoreSlicer();

	@Test
	public void testCase1() {
		List< Note > noteListToSlice = new ArrayList<>();
		noteListToSlice.add( new Note( C0, WHOLE_NOTE ) );
		noteListToSlice.add( new Note( C0, HALF_NOTE ) );
		noteListToSlice.add( new Note( C0, WHOLE_NOTE ) );
		noteListToSlice.add( new Note( C0, EIGHTH_NOTE ) );
		noteListToSlice.add( new Note( C0, EIGHTH_NOTE ) );
		noteListToSlice.add( new Note( C0, EIGHTH_NOTE ) );
		noteListToSlice.add( new Note( C0, EIGHTH_NOTE ) );

		noteListToSlice.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		noteListToSlice.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		noteListToSlice.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );

		noteListToSlice.add( new Note( C0, HALF_NOTE ) );
		noteListToSlice.add( new Note( C0, WHOLE_NOTE ) );

		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );

		// HALF NOTE
		List< List< Note > > sliceHalfNote = new ArrayList<>();

		List< Note > slice1 =  new ArrayList<>();
		slice1.add( new Note( C0, HALF_NOTE ) );
		sliceHalfNote.add( slice1 );

		List< Note > slice2 =  new ArrayList<>();
		slice2.add( new Note( C0, HALF_NOTE ) );
		sliceHalfNote.add( slice2 );

		List< Note > slice3 =  new ArrayList<>();
		slice3.add( new Note( C0, HALF_NOTE ) );
		sliceHalfNote.add( slice3 );

		List< Note > slice4 =  new ArrayList<>();
		slice4.add( new Note( C0, HALF_NOTE ) );
		sliceHalfNote.add( slice4 );

		List< Note > slice5 =  new ArrayList<>();
		slice5.add( new Note( C0, HALF_NOTE ) );
		sliceHalfNote.add( slice5 );

		List< Note > slice6 =  new ArrayList<>();
		slice6.add( new Note( C0, EIGHTH_NOTE ) );
		slice6.add( new Note( C0, EIGHTH_NOTE ) );
		slice6.add( new Note( C0, EIGHTH_NOTE ) );
		slice6.add( new Note( C0, EIGHTH_NOTE ) );
		sliceHalfNote.add( slice6 );

		List< Note > slice7 =  new ArrayList<>();
		slice7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice7.add( new Note( C0, QUARTER_NOTE ) );
		sliceHalfNote.add( slice7 );

		List< Note > slice8 =  new ArrayList<>();
		slice8.add( new Note( C0, QUARTER_NOTE ) );
		slice8.add( new Note( C0, QUARTER_NOTE ) );
		sliceHalfNote.add( slice8 );

		List< Note > slice9 =  new ArrayList<>();
		slice9.add( new Note( C0, HALF_NOTE ) );
		sliceHalfNote.add( slice9 );

		List< Note > slice10 =  new ArrayList<>();
		slice10.add( new Note( C0, QUARTER_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceHalfNote.add( slice10 );

		Phrase phrase = new Phrase();
		for ( Note note : noteListToSlice ) {
			phrase.add( note );
		}
		List< List< Note> > sliceToTest = scoreSlicer.slice( phrase, HALF_NOTE );
		assertTrue( Utils.ListOfListsIsEquals( sliceToTest, sliceHalfNote ) );

		// QUARTER NOTE
		List< List< Note > > sliceQuarterNote = new ArrayList<>();

		List< Note > slice11 =  new ArrayList<>();
		slice11.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice11 );

		List< Note > slice12 =  new ArrayList<>();
		slice12.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice12 );

		List< Note > slice13 =  new ArrayList<>();
		slice13.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice13 );

		List< Note > slice14 =  new ArrayList<>();
		slice14.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice14 );

		List< Note > slice21 =  new ArrayList<>();
		slice21.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice21 );

		List< Note > slice22 =  new ArrayList<>();
		slice22.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice22 );

		List< Note > slice31 =  new ArrayList<>();
		slice31.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice31 );

		List< Note > slice32 =  new ArrayList<>();
		slice32.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice32 );

		List< Note > slice41 =  new ArrayList<>();
		slice41.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice41 );

		List< Note > slice42 =  new ArrayList<>();
		slice42.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice42 );

		List< Note > slice61 =  new ArrayList<>();
		slice61.add( new Note( C0, EIGHTH_NOTE ) );
		slice61.add( new Note( C0, EIGHTH_NOTE ) );
		sliceQuarterNote.add( slice61 );

		List< Note > slice62 =  new ArrayList<>();
		slice62.add( new Note( C0, EIGHTH_NOTE ) );
		slice62.add( new Note( C0, EIGHTH_NOTE ) );
		sliceQuarterNote.add( slice62 );

		List< Note > slice71 =  new ArrayList<>();
		slice71.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice71.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice71.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		sliceQuarterNote.add( slice71 );

		List< Note > slice72 =  new ArrayList<>();
		slice72.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice72 );

		List< Note > slice81 =  new ArrayList<>();
		slice81.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice81 );

		List< Note > slice82 =  new ArrayList<>();
		slice82.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice82 );

		List< Note > slice91 =  new ArrayList<>();
		slice91.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice91 );

		List< Note > slice92 =  new ArrayList<>();
		slice92.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice92 );

		List< Note > slice93 =  new ArrayList<>();
		slice93.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice93 );

		List< Note > slice101 =  new ArrayList<>();
		slice101.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice101.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice101.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice101.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceQuarterNote.add( slice101 );

		Phrase phraseQuarter = new Phrase();
		for ( Note note : noteListToSlice ) {
			phraseQuarter.add( note );
		}
		List< List< Note> > sliceToTestQuarter = scoreSlicer.slice( phraseQuarter, QUARTER_NOTE );
		assertTrue( Utils.ListOfListsIsEquals( sliceToTestQuarter, sliceQuarterNote ) );


		// WHOLE NOTE
		List< List< Note > > sliceWholefNote = new ArrayList<>();

		List< Note > sliceW1 =  new ArrayList<>();
		sliceW1.add( new Note( C0, WHOLE_NOTE ) );
		sliceWholefNote.add( sliceW1 );

		List< Note > sliceW3 =  new ArrayList<>();
		sliceW3.add( new Note( C0, HALF_NOTE ) );
		sliceW3.add( new Note( C0, HALF_NOTE ) );
		sliceWholefNote.add( sliceW3 );

		List< Note > sliceW5 =  new ArrayList<>();
		sliceW5.add( new Note( C0, HALF_NOTE ) );
		sliceW5.add( new Note( C0, EIGHTH_NOTE ) );
		sliceW5.add( new Note( C0, EIGHTH_NOTE ) );
		sliceW5.add( new Note( C0, EIGHTH_NOTE ) );
		sliceW5.add( new Note( C0, EIGHTH_NOTE ) );
		sliceWholefNote.add( sliceW5 );

		List< Note > sliceW7 =  new ArrayList<>();
		sliceW7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		sliceW7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		sliceW7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		sliceW7.add( new Note( C0, HALF_NOTE ) );
		sliceW7.add( new Note( C0, QUARTER_NOTE ) );
		sliceWholefNote.add( sliceW7 );

		List< Note > sliceW9 =  new ArrayList<>();
		sliceW9.add( new Note( C0, DOTTED_HALF_NOTE ) );
		sliceW9.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceW9.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceW9.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceW9.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceWholefNote.add( sliceW9 );

		Phrase phraseW = new Phrase();
		for ( Note note : noteListToSlice ) {
			phraseW.add( note );
		}
		List< List< Note> > sliceToTestW = scoreSlicer.slice( phraseW, WHOLE_NOTE );
		assertTrue( Utils.ListOfListsIsEquals( sliceToTestW, sliceWholefNote ) );

		phraseW.getNoteArray()[0].setRhythmValue( HALF_NOTE );
		List< List< Note> > sliceToTestW1 = scoreSlicer.slice( phraseW, WHOLE_NOTE );
		assertFalse( Utils.ListOfListsIsEquals( sliceToTestW1, sliceWholefNote ) );
	}

	@Test
	public void testCase2() {
		Score score = new Score();

		Phrase firstInstr = new Phrase();
		firstInstr.add( new Note( C5, WHOLE_NOTE ) );
		firstInstr.add( new Note( D5, DOTTED_HALF_NOTE ) );
		firstInstr.add( new Note( E5, HALF_NOTE ) );
		Part firstPart = new Part( firstInstr );
		score.add( firstPart );

		Phrase secondInstr = new Phrase();
		secondInstr.add( new Note( C4, WHOLE_NOTE ) );
		secondInstr.add( new Note( B3, HALF_NOTE ) );
		secondInstr.add( new Note( A3, DOTTED_HALF_NOTE ) );
		Part secondPart = new Part( secondInstr );
		score.add( secondPart );

		Phrase thirdInstr = new Phrase();
		thirdInstr.add( new Note( C3, HALF_NOTE ) );
		thirdInstr.add( new Note( C3, DOTTED_HALF_NOTE ) );
		thirdInstr.add( new Note( C3, WHOLE_NOTE ) );
		Part thirdPart = new Part( thirdInstr );
		score.add( thirdPart );

		List<MusicBlock> testList = scoreSlicer.slice( score, DOTTED_HALF_NOTE );

		// first
		List< Note > firstBlockfirstList = new ArrayList<>(  );
		firstBlockfirstList.add( new Note( C5, DOTTED_HALF_NOTE ) );
		List< Note > firstBlocksecondList = new ArrayList<>(  );
		firstBlocksecondList.add( new Note( C4, DOTTED_HALF_NOTE ) );
		List< Note > firstBlockthirdList = new ArrayList<>(  );
		firstBlockthirdList.add( new Note( C3, HALF_NOTE ) );
		firstBlockthirdList.add( new Note( C3, QUARTER_NOTE ) );

		List<List<Note>> firstBlock = new ArrayList<>();
		firstBlock.add( firstBlockfirstList );
		firstBlock.add( firstBlocksecondList );
		firstBlock.add( firstBlockthirdList );
		MusicBlock firstMusicBlock = new MusicBlock( firstBlock, null );

		// second
		List< Note > secondBlockfirstList = new ArrayList<>(  );
		secondBlockfirstList.add( new Note( C5, QUARTER_NOTE ) );
		secondBlockfirstList.add( new Note( D5, HALF_NOTE ) );
		List< Note > secondBlocksecondList = new ArrayList<>(  );
		secondBlocksecondList.add( new Note( C4, QUARTER_NOTE ) );
		secondBlocksecondList.add( new Note( B3, HALF_NOTE ) );
		List< Note > secondBlockthirdList = new ArrayList<>(  );
		secondBlockthirdList.add( new Note( C3, HALF_NOTE ) );
		secondBlockthirdList.add( new Note( C3, QUARTER_NOTE ) );

		List<List<Note>> secondBlock = new ArrayList<>();
		secondBlock.add( secondBlockfirstList );
		secondBlock.add( secondBlocksecondList );
		secondBlock.add( secondBlockthirdList );
		MusicBlock secondMusicBlock = new MusicBlock( secondBlock, null );

		// third
		List< Note > thirdBlockfirstList = new ArrayList<>(  );
		thirdBlockfirstList.add( new Note( D5, QUARTER_NOTE ) );
		thirdBlockfirstList.add( new Note( E5, HALF_NOTE ) );
		List< Note > thirdBlocksecondList = new ArrayList<>(  );
		thirdBlocksecondList.add( new Note( A3, DOTTED_HALF_NOTE ) );
		List< Note > thirdBlockthirdList = new ArrayList<>(  );
		thirdBlockthirdList.add( new Note( C3, DOTTED_HALF_NOTE ) );

		List<List<Note>> thirdBlock = new ArrayList<>();
		thirdBlock.add( thirdBlockfirstList );
		thirdBlock.add( thirdBlocksecondList );
		thirdBlock.add( thirdBlockthirdList );
		MusicBlock thirdMusicBlock = new MusicBlock( thirdBlock, null );

		List< MusicBlock > etalonList = new ArrayList<>(  );
		etalonList.add( firstMusicBlock );
		etalonList.add( secondMusicBlock );
		etalonList.add( thirdMusicBlock );

		assertEquals( etalonList, testList );
	}
}
