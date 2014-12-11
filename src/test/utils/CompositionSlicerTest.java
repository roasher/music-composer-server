package utils;

import helper.AbstractSpringTest;
import jm.music.data.*;
import model.melody.Melody;
import model.composition.Composition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static jm.JMC.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompositionSlicerTest extends AbstractSpringTest {

	@Autowired
	private CompositionSlicer compositionSlicer;
	@Autowired
	private CompositionLoader compositionLoader;

	@Test
	public void testCase1() {
		Melody noteListToSlice = new Melody();
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
		List<Melody> sliceHalfNote = new ArrayList<>();

		Melody slice1 = new Melody();
		slice1.add( new Note( C0, HALF_NOTE ) );
		sliceHalfNote.add( slice1 );

		Melody slice2 = new Melody();
		slice2.add( new Note( C0, HALF_NOTE ) );
		slice2.setStartTime( slice1.getEndTime() );
		sliceHalfNote.add( slice2 );

		Melody slice3 = new Melody();
		slice3.add( new Note( C0, HALF_NOTE ) );
		slice3.setStartTime( slice2.getEndTime() );
		sliceHalfNote.add( slice3 );

		Melody slice4 = new Melody();
		slice4.add( new Note( C0, HALF_NOTE ) );
		slice4.setStartTime( slice3.getEndTime() );
		sliceHalfNote.add( slice4 );

		Melody slice5 = new Melody();
		slice5.add( new Note( C0, HALF_NOTE ) );
		slice5.setStartTime( slice4.getEndTime() );
		sliceHalfNote.add( slice5 );

		Melody slice6 = new Melody();
		slice6.add( new Note( C0, EIGHTH_NOTE ) );
		slice6.add( new Note( C0, EIGHTH_NOTE ) );
		slice6.add( new Note( C0, EIGHTH_NOTE ) );
		slice6.add( new Note( C0, EIGHTH_NOTE ) );
		slice6.setStartTime( slice5.getEndTime() );
		sliceHalfNote.add( slice6 );

		Melody slice7 = new Melody();
		slice7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice7.add( new Note( C0, QUARTER_NOTE ) );
		slice7.setStartTime( slice6.getEndTime() );
		sliceHalfNote.add( slice7 );

		Melody slice8 = new Melody();
		slice8.add( new Note( C0, QUARTER_NOTE ) );
		slice8.add( new Note( C0, QUARTER_NOTE ) );
		slice8.setStartTime( slice7.getEndTime() );
		sliceHalfNote.add( slice8 );

		Melody slice9 = new Melody();
		slice9.add( new Note( C0, HALF_NOTE ) );
		slice9.setStartTime( slice8.getEndTime() );
		sliceHalfNote.add( slice9 );

		Melody slice10 = new Melody();
		slice10.add( new Note( C0, QUARTER_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.setStartTime( slice9.getEndTime() );
		sliceHalfNote.add( slice10 );

		Phrase phrase = new Phrase();
		for ( Note note : noteListToSlice.getNoteArray() ) {
			phrase.add( note );
		}
		List<Melody> sliceToTest = compositionSlicer.slice( phrase, HALF_NOTE );
		assertTrue( Utils.listOfMelodiesAreEquals( sliceToTest, sliceHalfNote ) );

		// QUARTER NOTE
		List<Melody> sliceQuarterNote = new ArrayList<>();

		Melody slice11 = new Melody();
		slice11.add( new Note( C0, QUARTER_NOTE ) );
		sliceQuarterNote.add( slice11 );

		Melody slice12 = new Melody();
		slice12.add( new Note( C0, QUARTER_NOTE ) );
		slice12.setStartTime( slice11.getEndTime() );
		sliceQuarterNote.add( slice12 );

		Melody slice13 = new Melody();
		slice13.add( new Note( C0, QUARTER_NOTE ) );
		slice13.setStartTime( slice12.getEndTime() );
		sliceQuarterNote.add( slice13 );

		Melody slice14 = new Melody();
		slice14.add( new Note( C0, QUARTER_NOTE ) );
		slice14.setStartTime( slice13.getEndTime() );
		sliceQuarterNote.add( slice14 );

		Melody slice21 = new Melody();
		slice21.add( new Note( C0, QUARTER_NOTE ) );
		slice21.setStartTime( slice14.getEndTime() );
		sliceQuarterNote.add( slice21 );

		Melody slice22 = new Melody();
		slice22.add( new Note( C0, QUARTER_NOTE ) );
		slice22.setStartTime( slice21.getEndTime() );
		sliceQuarterNote.add( slice22 );

		Melody slice31 = new Melody();
		slice31.add( new Note( C0, QUARTER_NOTE ) );
		slice31.setStartTime( slice22.getEndTime() );
		sliceQuarterNote.add( slice31 );

		Melody slice32 = new Melody();
		slice32.add( new Note( C0, QUARTER_NOTE ) );
		slice32.setStartTime( slice31.getEndTime() );
		sliceQuarterNote.add( slice32 );

		Melody slice41 = new Melody();
		slice41.add( new Note( C0, QUARTER_NOTE ) );
		slice41.setStartTime( slice32.getEndTime() );
		sliceQuarterNote.add( slice41 );

		Melody slice42 = new Melody();
		slice42.add( new Note( C0, QUARTER_NOTE ) );
		slice42.setStartTime( slice41.getEndTime() );
		sliceQuarterNote.add( slice42 );

		Melody slice61 = new Melody();
		slice61.add( new Note( C0, EIGHTH_NOTE ) );
		slice61.add( new Note( C0, EIGHTH_NOTE ) );
		slice61.setStartTime( slice42.getEndTime() );
		sliceQuarterNote.add( slice61 );

		Melody slice62 = new Melody();
		slice62.add( new Note( C0, EIGHTH_NOTE ) );
		slice62.add( new Note( C0, EIGHTH_NOTE ) );
		slice62.setStartTime( slice61.getEndTime() );
		sliceQuarterNote.add( slice62 );

		Melody slice71 = new Melody();
		slice71.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice71.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice71.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		slice71.setStartTime( slice62.getEndTime() );
		sliceQuarterNote.add( slice71 );

		Melody slice72 = new Melody();
		slice72.add( new Note( C0, QUARTER_NOTE ) );
		slice72.setStartTime( slice71.getEndTime() );
		sliceQuarterNote.add( slice72 );

		Melody slice81 = new Melody();
		slice81.add( new Note( C0, QUARTER_NOTE ) );
		slice81.setStartTime( slice72.getEndTime() );
		sliceQuarterNote.add( slice81 );

		Melody slice82 = new Melody();
		slice82.add( new Note( C0, QUARTER_NOTE ) );
		slice82.setStartTime( slice81.getEndTime() );
		sliceQuarterNote.add( slice82 );

		Melody slice91 = new Melody();
		slice91.add( new Note( C0, QUARTER_NOTE ) );
		slice91.setStartTime( slice82.getEndTime() );
		sliceQuarterNote.add( slice91 );

		Melody slice92 = new Melody();
		slice92.add( new Note( C0, QUARTER_NOTE ) );
		slice92.setStartTime( slice91.getEndTime() );
		sliceQuarterNote.add( slice92 );

		Melody slice93 = new Melody();
		slice93.add( new Note( C0, QUARTER_NOTE ) );
		slice93.setStartTime( slice92.getEndTime() );
		sliceQuarterNote.add( slice93 );

		Melody slice101 = new Melody();
		slice101.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice101.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice101.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice101.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice101.setStartTime( slice93.getEndTime() );
		sliceQuarterNote.add( slice101 );

		Phrase phraseQuarter = new Phrase();
		for ( Note note : noteListToSlice.getNoteArray() ) {
			phraseQuarter.add( note );
		}
		List<Melody> sliceToTestQuarter = compositionSlicer.slice( phraseQuarter, QUARTER_NOTE );
		assertTrue( Utils.listOfMelodiesAreEquals( sliceToTestQuarter, sliceQuarterNote ) );

		// WHOLE NOTE
		List<Melody> sliceWholefNote = new ArrayList<>(  );

		Melody sliceW1 = new Melody();
		sliceW1.add( new Note( C0, WHOLE_NOTE ) );
		sliceWholefNote.add( sliceW1 );

		Melody sliceW3 = new Melody();
		sliceW3.add( new Note( C0, HALF_NOTE ) );
		sliceW3.add( new Note( C0, HALF_NOTE ) );
		sliceW3.setStartTime( sliceW1.getEndTime() );
		sliceWholefNote.add( sliceW3 );

		Melody sliceW5 = new Melody();
		sliceW5.add( new Note( C0, HALF_NOTE ) );
		sliceW5.add( new Note( C0, EIGHTH_NOTE ) );
		sliceW5.add( new Note( C0, EIGHTH_NOTE ) );
		sliceW5.add( new Note( C0, EIGHTH_NOTE ) );
		sliceW5.add( new Note( C0, EIGHTH_NOTE ) );
		sliceW5.setStartTime( sliceW3.getEndTime() );
		sliceWholefNote.add( sliceW5 );

		Melody sliceW7 = new Melody();
		sliceW7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		sliceW7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		sliceW7.add( new Note( C0, EIGHTH_NOTE_TRIPLET ) );
		sliceW7.add( new Note( C0, HALF_NOTE ) );
		sliceW7.add( new Note( C0, QUARTER_NOTE ) );
		sliceW7.setStartTime( sliceW5.getEndTime() );
		sliceWholefNote.add( sliceW7 );

		Melody sliceW9 = new Melody();
		sliceW9.add( new Note( C0, DOTTED_HALF_NOTE ) );
		sliceW9.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceW9.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceW9.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceW9.add( new Note( C0, SIXTEENTH_NOTE ) );
		sliceW9.setStartTime( sliceW7.getEndTime() );
		sliceWholefNote.add( sliceW9 );

		Phrase phraseW = new Phrase();
		for ( Note note : noteListToSlice.getNoteArray() ) {
			phraseW.add( note );
		}
		List<Melody> sliceToTestW = compositionSlicer.slice( phraseW, WHOLE_NOTE );
		assertTrue( Utils.listOfMelodiesAreEquals( sliceToTestW, sliceWholefNote ) );

		phraseW.getNoteArray()[0].setRhythmValue( HALF_NOTE );
		List<Melody> sliceToTestW1 = compositionSlicer.slice( phraseW, WHOLE_NOTE );
		assertFalse( Utils.listOfMelodiesAreEquals( sliceToTestW1, sliceWholefNote ) );
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

		List<List<Melody>> testList = compositionSlicer.slice( new Composition( score ), DOTTED_HALF_NOTE );

		// first
		Melody firstBlockfirstList = new Melody();
		firstBlockfirstList.add( new Note( C5, DOTTED_HALF_NOTE ) );
		Melody firstBlocksecondList = new Melody();
		firstBlocksecondList.add( new Note( C4, DOTTED_HALF_NOTE ) );
		Melody firstBlockthirdList = new Melody();
		firstBlockthirdList.add( new Note( C3, HALF_NOTE ) );
		firstBlockthirdList.add( new Note( C3, QUARTER_NOTE ) );

		List<Melody> firstBlock = new ArrayList<>();
		firstBlock.add( firstBlockfirstList );
		firstBlock.add( firstBlocksecondList );
		firstBlock.add( firstBlockthirdList );

		// second
		Melody secondBlockfirstList = new Melody();
		secondBlockfirstList.add( new Note( C5, QUARTER_NOTE ) );
		secondBlockfirstList.add( new Note( D5, HALF_NOTE ) );
		secondBlockfirstList.setStartTime( firstBlockfirstList.getEndTime() );

		Melody secondBlocksecondList = new Melody();
		secondBlocksecondList.add( new Note( C4, QUARTER_NOTE ) );
		secondBlocksecondList.add( new Note( B3, HALF_NOTE ) );
		secondBlocksecondList.setStartTime( firstBlocksecondList.getEndTime() );

		Melody secondBlockthirdList = new Melody();
		secondBlockthirdList.add( new Note( C3, HALF_NOTE ) );
		secondBlockthirdList.add( new Note( C3, QUARTER_NOTE ) );
		secondBlockthirdList.setStartTime( firstBlockthirdList.getEndTime() );

		List<Melody> secondBlock = new ArrayList<>();
		secondBlock.add( secondBlockfirstList );
		secondBlock.add( secondBlocksecondList );
		secondBlock.add( secondBlockthirdList );

		// third
		Melody thirdBlockfirstList = new Melody();
		thirdBlockfirstList.add( new Note( D5, QUARTER_NOTE ) );
		thirdBlockfirstList.add( new Note( E5, HALF_NOTE ) );
		thirdBlockfirstList.setStartTime( secondBlockfirstList.getEndTime() );

		Melody thirdBlocksecondList = new Melody();
		thirdBlocksecondList.add( new Note( A3, DOTTED_HALF_NOTE ) );
		thirdBlocksecondList.setStartTime( secondBlocksecondList.getEndTime() );

		Melody thirdBlockthirdList = new Melody();
		thirdBlockthirdList.add( new Note( C3, DOTTED_HALF_NOTE ) );
		thirdBlockthirdList.setStartTime( secondBlockthirdList.getEndTime() );

		List<Melody> thirdBlock = new ArrayList<>();
		thirdBlock.add( thirdBlockfirstList );
		thirdBlock.add( thirdBlocksecondList );
		thirdBlock.add( thirdBlockthirdList );

		List<List<Melody>> etalonList = new ArrayList<>();
		etalonList.add( firstBlock );
		etalonList.add( secondBlock );
		etalonList.add( thirdBlock );

		assertEquals( etalonList, testList );
	}

	@Test
	public void testCase3() {
		// notes to slice
		List<Note> noteListToSlice = new ArrayList<>();

		noteListToSlice.add( new Note( C0, HALF_NOTE ) );
		noteListToSlice.add( new Note( C0, HALF_NOTE ) );

		noteListToSlice.add( new Note( C0, QUARTER_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );
		noteListToSlice.add( new Note( C0, SIXTEENTH_NOTE ) );

		// already sliced notes + rest in the end
		List<Melody> sliceEtalon = new ArrayList<>();

		Melody slice1 = new Melody();
		slice1.add( new Note( C0, HALF_NOTE ) );
		slice1.add( new Note( C0, HALF_NOTE ) );
		sliceEtalon.add( slice1 );

		Melody slice2 = new Melody();
		slice2.add( new Note( C0, QUARTER_NOTE ) );
		slice2.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice2.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice2.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice2.add( new Note( REST, WHOLE_NOTE - QUARTER_NOTE - 3 * SIXTEENTH_NOTE ) );
		slice2.setStartTime( slice1.getEndTime() );
		sliceEtalon.add( slice2 );

		Phrase phrase = new Phrase();
		for ( Note note : noteListToSlice ) {
			phrase.add( note );
		}
		List<Melody> sliceToTest = compositionSlicer.slice( phrase, WHOLE_NOTE );

		assertTrue( Utils.listOfMelodiesAreEquals( sliceToTest, sliceEtalon ) );
	}

	@Test
	public void restSlice() {
		Phrase phrase = new Phrase();
		phrase.add( new Note( REST, WHOLE_NOTE ) );
		phrase.add( new Note( REST, QUARTER_NOTE ) );
		phrase.add( new Note( REST, QUARTER_NOTE ) );
		phrase.add( new Note( REST, HALF_NOTE ) );
		phrase.add( new Note( REST, EIGHTH_NOTE ) );
		phrase.add( new Note( REST, EIGHTH_NOTE ) );

		List<Melody> sliceToTest = compositionSlicer.slice( phrase, WHOLE_NOTE );
		assertEquals( sliceToTest.size(), 3 );
	}

	@Test
	// expect no exceptions
	public void badNoteRhythmValues() {
		Phrase phrase = new Phrase();
		phrase.add( new Note( 64, 0.5 ) );
		phrase.add( new Note( 60, 0.5 ) );
		phrase.add( new Note( 62, 0.9666666666666667 ) );
		phrase.add( new Note( 57, 0.9666666666666667 ) );
		phrase.add( new Note( 59, 0.5 ) );
		phrase.add( new Note( 60, 0.5 ) );
		phrase.add( new Note( 60, 0.08333333333333333 ) );

		List<Melody> sliceToTest = compositionSlicer.slice( phrase, WHOLE_NOTE );
		assertEquals( sliceToTest.size(), 2 );
	}

	@Test
	public void restInTheBeginningTestSlice() {
		Composition composition = compositionLoader.getComposition( new File( "src\\test\\decomposer\\form\\formDecomposer\\quartets\\2.Scarecrow's song (midi).mid" ) );

//		View.notate( composition );
//		suspend();

		List<List<Melody>> musicBlockList = compositionSlicer.slice( composition, WHOLE_NOTE );

		assertEquals( musicBlockList.get( 0 ).get( 3 ).size(), 1 );
		assertTrue( musicBlockList.get( 0 ).get( 3 ).getNote( 0 ).equals( new Rest( WHOLE_NOTE ) ) );
	}

	@Test
	public void startNotFromTheBeginnigPhraseSliceTest() {
		Phrase phrase = new Phrase();
		phrase.setStartTime( WHOLE_NOTE + QUARTER_NOTE );
		phrase.add( new Note( C4, WHOLE_NOTE ) );
		phrase.add( new Note( D4, QUARTER_NOTE ) );

//		View.notate( phrase );
//		suspend();

		List<Melody> sliceToTest = compositionSlicer.slice( phrase, WHOLE_NOTE );
		List<Melody> etalonSlice = new ArrayList<>();

		Melody slice1 = new Melody();
		slice1.add( new Note( REST, WHOLE_NOTE ) );
		etalonSlice.add( slice1 );

		Melody slice2 = new Melody();
		slice2.add( new Note( REST, QUARTER_NOTE ) );
		slice2.add( new Note( C4, DOTTED_HALF_NOTE ) );
		slice2.setStartTime( slice1.getEndTime() );
		etalonSlice.add( slice2 );

		Melody slice3 = new Melody();
		slice3.add( new Note( C4, QUARTER_NOTE ) );
		slice3.add( new Note( D4, QUARTER_NOTE ) );
		slice3.add( new Note( REST, HALF_NOTE ) );
		slice3.setStartTime( slice2.getEndTime() );
		etalonSlice.add( slice3 );

		assertTrue( Utils.listOfMelodiesAreEquals( sliceToTest, etalonSlice ) );
	}
}
