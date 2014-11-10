package decomposer.analyzer.melody.equality;

import decomposer.analyzer.form.KeyEqualityTest;
import jm.music.data.Note;
import model.Signature;
import org.junit.Test;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by night wish on 02.11.14.
 */
public class TestKeyEqualityTest {

	private EqualityTest test = new KeyEqualityTest();

	@Test
	public void testCase1() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( B4, SIXTEENTH_NOTE ),
			new Note( D5, SIXTEENTH_NOTE ),
			new Note( G5, SIXTEENTH_NOTE ),
			new Note( B5, QUARTER_NOTE ),
			new Note( A5, DOTTED_EIGHTH_NOTE ),
			new Note( B5, SIXTEENTH_NOTE ),
			new Note( G5, DOTTED_QUARTER_NOTE ),
			new Note( D5, EIGHTH_NOTE ),
			new Note( B4, EIGHTH_NOTE ),
			new Note( A4, EIGHTH_NOTE ),
		  }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( GS4, SIXTEENTH_NOTE ),
		  }
		);

		assertFalse( test.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase2() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( B4, SIXTEENTH_NOTE ),
			new Note( D5, SIXTEENTH_NOTE ),
			new Note( G5, SIXTEENTH_NOTE ),
			new Note( B5, QUARTER_NOTE ),
			new Note( A5, DOTTED_EIGHTH_NOTE ),
			new Note( B5, SIXTEENTH_NOTE ),
			new Note( G5, DOTTED_QUARTER_NOTE ),
			new Note( D5, EIGHTH_NOTE ),
			new Note( B4, EIGHTH_NOTE ),
			new Note( A4, EIGHTH_NOTE ),
		  }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( D4, SIXTEENTH_NOTE ),
			new Note( E5, SIXTEENTH_NOTE ),
			new Note( G5, SIXTEENTH_NOTE ),
		  }
		);

		assertTrue( test.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase3() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( CS4, SIXTEENTH_NOTE ),
		  }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( D4, SIXTEENTH_NOTE ),
		  }
		);

		assertFalse( test.test( testSignature1, testSignature2 ) );
	}

	@Test
	public void testCase4() {
		Signature testSignature1 = new Signature(
		  new Note[] {
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( BF0, SIXTEENTH_NOTE ),
			new Note( D4, SIXTEENTH_NOTE ),
			new Note( F4, SIXTEENTH_NOTE ),
			new Note( E4, SIXTEENTH_NOTE ),
		  }
		);
		Signature testSignature2 = new Signature(
		  new Note[] {
			new Note( EF4, SIXTEENTH_NOTE ),
		  }
		);

		assertFalse( test.test( testSignature1, testSignature2 ) );
	}
}
