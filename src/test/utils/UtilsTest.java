package utils;

import jm.music.data.Note;
import jm.music.data.Rest;
import model.melody.Melody;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

import static jm.JMC.*;
import static junit.framework.Assert.assertTrue;

/**
 * Created by pyurkin on 25.11.14.
 */
public class UtilsTest {

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

		List< Melody > inputMelodyBlockList = new ArrayList<>(  );
		inputMelodyBlockList.add( melodyInput0 );
		inputMelodyBlockList.add( melodyInput1 );

		// testList
		List< List< Melody > > testList = Utils.recombineMelodyBlock( inputMelodyBlockList );

		// etalon output
		// item
		Melody melody00 = new Melody( new Note[] {
		  new Rest( QUARTER_NOTE ),
		} );

		Melody melody01 = new Melody( new Note[] {
		  new Note( 58, QUARTER_NOTE ),
		} );

		List< Melody > melodyList0 = new ArrayList<>(  );
		melodyList0.add( melody00 );
		melodyList0.add( melody01 );

		// item
		Melody melody10 = new Melody( new Note[] {
		  new Note( 60, QUARTER_NOTE ),
		} );

		Melody melody11 = new Melody( new Note[] {
		  new Note( 58, QUARTER_NOTE )
		} );

		List< Melody > melodyList1 = new ArrayList<>(  );
		melodyList1.add( melody10 );
		melodyList1.add( melody11 );

		// item
		Melody melody100 = new Melody( new Note[] {
		  new Note( 60, EIGHTH_NOTE ),
		} );

		Melody melody110 = new Melody( new Note[] {
		  new Note( 56, EIGHTH_NOTE )
		} );

		List< Melody > melodyList10 = new ArrayList<>(  );
		melodyList10.add( melody100 );
		melodyList10.add( melody110 );

		// item
		Melody melody20 = new Melody( new Note[] {
		  new Note( 62, SIXTEENTH_NOTE )
		} );

		Melody melody21 = new Melody( new Note[] {
		  new Note( 58, SIXTEENTH_NOTE ),
		} );

		List< Melody > melodyList2 = new ArrayList<>(  );
		melodyList2.add( melody20 );
		melodyList2.add( melody21 );

		// item
		Melody melody30 = new Melody( new Note[] {
		  new Note( 60, SIXTEENTH_NOTE )
		} );

		Melody melody31 = new Melody( new Note[] {
		  new Note( 58, SIXTEENTH_NOTE ),
		} );

		List< Melody > melodyList3 = new ArrayList<>(  );
		melodyList3.add( melody30 );
		melodyList3.add( melody31 );

		// item
		Melody melody40 = new Melody( new Note[] {
		  new Note( 64, DOTTED_EIGHTH_NOTE ),
		} );

		Melody melody41 = new Melody( new Note[] {
		  new Note( 50, DOTTED_EIGHTH_NOTE ),
		} );

		List< Melody > melodyList4 = new ArrayList<>(  );
		melodyList4.add( melody40 );
		melodyList4.add( melody41 );

		// item
		Melody melody50 = new Melody( new Note[] {
		  new Note( 64, SIXTEENTH_NOTE ),
		} );

		Melody melody51 = new Melody( new Note[] {
		  new Note( 51, SIXTEENTH_NOTE ),
		} );

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
}
