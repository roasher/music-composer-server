package utils;

import helper.AbstractSpringTest;
import jm.music.data.Note;
import jm.music.data.Rest;
import model.melody.Form;
import model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

import static jm.JMC.*;
import static junit.framework.Assert.assertTrue;

/**
 * Created by pyurkin on 25.11.14.
 */
public class RecombinatorTest extends AbstractSpringTest {

	@Autowired
	private Recombinator recombinator;

	@Test
	public void testRebuildingMelodyBlockList() {
		// input
		Melody melodyInput0 = new Melody( new Note[]{
		  new Rest( QUARTER_NOTE ),
		  new Note( 60, DOTTED_QUARTER_NOTE ),
	      new Note( 62, SIXTEENTH_NOTE ),
		  new Note( 60, SIXTEENTH_NOTE ),
		  new Note( 64, QUARTER_NOTE ),

//		  new Note( 67, SIXTEENTH_NOTE ),
//		  new Note( 65, SIXTEENTH_NOTE ),
//		  new Note( 67, SIXTEENTH_NOTE ),
//		  new Note( 65, SIXTEENTH_NOTE ),
//		  new Note( 67, SIXTEENTH_NOTE ),
//		  new Note( 68, DOUBLE_DOTTED_QUARTER_NOTE ),
//		  new Rest( QUARTER_NOTE )
		}
		);
		melodyInput0.setForm( new Form( 'B' ) );

		Melody melodyInput1 = new Melody( new Note[]{
		  new Note( 58, HALF_NOTE ),
		  new Note( 56, EIGHTH_NOTE ),
		  new Note( 58, EIGHTH_NOTE ),
		  new Note( 50, DOTTED_EIGHTH_NOTE ),
		  new Note( 51, SIXTEENTH_NOTE ),

//		  new Note( 60, EIGHTH_NOTE ),
//		  new Note( 62, EIGHTH_NOTE ),
//		  new Note( 60, QUARTER_NOTE ),
//		  new Note( 60, QUARTER_NOTE ),
//		  new Rest( QUARTER_NOTE ),
		}
		);
		melodyInput1.setForm( new Form( 'C' ) );

		List< Melody > inputMelodyBlock = new ArrayList<>(  );
		inputMelodyBlock.add( melodyInput0 );
		inputMelodyBlock.add( melodyInput1 );

		// testList
		List< List< Melody > > testList = recombinator.recombineMelodyBlock( inputMelodyBlock );

		// etalon output
		// item
		Melody melody00 = new Melody( new Note[] {
		  new Rest( QUARTER_NOTE ),
		} );
		melody00.setForm( new Form( 'B' ) );

		Melody melody01 = new Melody( new Note[] {
		  new Note( 58, QUARTER_NOTE ),
		} );
		melody01.setForm( new Form( 'C' ) );

		List< Melody > melodyList0 = new ArrayList<>(  );
		melodyList0.add( melody00 );
		melodyList0.add( melody01 );

		// item
		Melody melody10 = new Melody( new Note[] {
		  new Note( 60, QUARTER_NOTE ),
		} );
		melody10.setStartTime( melody00.getEndTime() );
		melody10.setForm( new Form( 'B' ) );

		Melody melody11 = new Melody( new Note[] {
		  new Note( 58, QUARTER_NOTE )
		} );
		melody11.setStartTime( melody00.getEndTime() );
		melody11.setForm( new Form( 'C' ) );

		List< Melody > melodyList1 = new ArrayList<>(  );
		melodyList1.add( melody10 );
		melodyList1.add( melody11 );

		// item
		Melody melody100 = new Melody( new Note[] {
		  new Note( 60, EIGHTH_NOTE ),
		} );
		melody100.setStartTime( melody10.getEndTime() );
		melody100.setForm( new Form( 'B' ) );

		Melody melody110 = new Melody( new Note[] {
		  new Note( 56, EIGHTH_NOTE )
		} );
		melody110.setStartTime( melody10.getEndTime() );
		melody110.setForm( new Form( 'C' ) );

		List< Melody > melodyList10 = new ArrayList<>(  );
		melodyList10.add( melody100 );
		melodyList10.add( melody110 );

		// item
		Melody melody20 = new Melody( new Note[] {
		  new Note( 62, SIXTEENTH_NOTE )
		} );
		melody20.setStartTime( melody100.getEndTime() );
		melody20.setForm( new Form( 'B' ) );

		Melody melody21 = new Melody( new Note[] {
		  new Note( 58, SIXTEENTH_NOTE ),
		} );
		melody21.setStartTime( melody110.getEndTime() );
		melody21.setForm( new Form( 'C' ) );

		List< Melody > melodyList2 = new ArrayList<>(  );
		melodyList2.add( melody20 );
		melodyList2.add( melody21 );

		// item
		Melody melody30 = new Melody( new Note[] {
		  new Note( 60, SIXTEENTH_NOTE )
		} );
		melody30.setStartTime( melody20.getEndTime() );
		melody30.setForm( new Form( 'B' ) );

		Melody melody31 = new Melody( new Note[] {
		  new Note( 58, SIXTEENTH_NOTE ),
		} );
		melody31.setStartTime( melody20.getEndTime() );
		melody31.setForm( new Form( 'C' ) );

		List< Melody > melodyList3 = new ArrayList<>(  );
		melodyList3.add( melody30 );
		melodyList3.add( melody31 );

		// item
		Melody melody40 = new Melody( new Note[] {
		  new Note( 64, DOTTED_EIGHTH_NOTE ),
		} );
		melody40.setStartTime( melody30.getEndTime() );
		melody40.setForm( new Form( 'B' ) );

		Melody melody41 = new Melody( new Note[] {
		  new Note( 50, DOTTED_EIGHTH_NOTE ),
		} );
		melody41.setStartTime( melody30.getEndTime() );
		melody41.setForm( new Form( 'C' ) );

		List< Melody > melodyList4 = new ArrayList<>(  );
		melodyList4.add( melody40 );
		melodyList4.add( melody41 );

		// item
		Melody melody50 = new Melody( new Note[] {
		  new Note( 64, SIXTEENTH_NOTE ),
		} );
		melody50.setStartTime( melody40.getEndTime() );
		melody50.setForm( new Form( 'B' ) );

		Melody melody51 = new Melody( new Note[] {
		  new Note( 51, SIXTEENTH_NOTE ),
		} );
		melody51.setStartTime( melody40.getEndTime() );
		melody51.setForm( new Form( 'C' ) );

		List< Melody > melodyList5 = new ArrayList<>(  );
		melodyList5.add( melody50 );
		melodyList5.add( melody51 );

		List< List< Melody > > etalonList = new ArrayList<>(  );
		etalonList.add( melodyList0 );
		etalonList.add( melodyList1 );
		etalonList.add( melodyList10 );
		etalonList.add( melodyList2 );
		etalonList.add( melodyList3 );
		etalonList.add( melodyList4 );
		etalonList.add( melodyList5 );

		assertTrue( Utils.ListOfMelodyBlocksIsEquals( testList, etalonList ) );
	}

	@Test
	public void testRecombine() {
		// input
		Melody melodyInput00 = new Melody( new Note[]{
		  new Rest( QUARTER_NOTE ),
		  new Note( 60, DOTTED_QUARTER_NOTE ),
		  new Note( 62, SIXTEENTH_NOTE ),
		  new Note( 60, SIXTEENTH_NOTE ),
		} );
		melodyInput00.setForm( new Form( 'B' ) );

		Melody melodyInput01 = new Melody( new Note[]{
		  new Note( 58, HALF_NOTE ),
		  new Note( 56, EIGHTH_NOTE ),
		  new Note( 58, EIGHTH_NOTE ),
		} );
		melodyInput01.setForm( new Form( 'I' ) );

		List< Melody > melodyList0 = new ArrayList<>(  );
		melodyList0.add( melodyInput00 );
		melodyList0.add( melodyInput01 );

		Melody melodyInput10 = new Melody( new Note[]{
		  new Note( 64, QUARTER_NOTE ),
		} );
		melodyInput10.setForm( new Form( 'C' ) );

		Melody melodyInput11 = new Melody( new Note[]{
		  new Note( 50, DOTTED_EIGHTH_NOTE ),
		  new Note( 51, SIXTEENTH_NOTE ),
		} );
		melodyInput11.setForm( new Form( 'J' ) );

		List< Melody > melodyList1 = new ArrayList<>(  );
		melodyList1.add( melodyInput10 );
		melodyList1.add( melodyInput11 );

		Melody melodyInput20 = new Melody( new Note[]{
		  new Note( 67, SIXTEENTH_NOTE ),
		  new Note( 65, SIXTEENTH_NOTE ),
		  new Note( 67, SIXTEENTH_NOTE ),
		  new Note( 65, SIXTEENTH_NOTE ),
		} );
		melodyInput20.setForm( new Form( 'D' ) );

		Melody melodyInput21 = new Melody( new Note[]{
		  new Note( 60, EIGHTH_NOTE ),
		  new Note( 62, EIGHTH_NOTE ),
		} );
		melodyInput21.setForm( new Form( 'K' ) );

		List< Melody > melodyList2 = new ArrayList<>(  );
		melodyList2.add( melodyInput20 );
		melodyList2.add( melodyInput21 );

		Melody melodyInput30 = new Melody( new Note[]{
		  new Note( 67, SIXTEENTH_NOTE ),
		  new Note( 68, DOUBLE_DOTTED_QUARTER_NOTE ),
		  new Rest( QUARTER_NOTE )
		} );
		melodyInput30.setForm( new Form( 'E' ) );

		Melody melodyInput31 = new Melody( new Note[]{
		  new Note( 60, QUARTER_NOTE ),
		  new Note( 60, QUARTER_NOTE ),
		  new Rest( QUARTER_NOTE ),
		} );
		melodyInput31.setForm( new Form( 'L' ) );

		List< Melody > melodyList3 = new ArrayList<>(  );
		melodyList3.add( melodyInput30 );
		melodyList3.add( melodyInput31 );

		List< List< Melody > > melodyBlockList = new ArrayList<>(  );
		melodyBlockList.add( melodyList0 );
		melodyBlockList.add( melodyList1 );
		melodyBlockList.add( melodyList2 );
		melodyBlockList.add( melodyList3 );

		List< List < Melody > > recombineList = recombinator.recombine( melodyBlockList );

		assertEquals( recombineList.size(), 15 );
	}
}
