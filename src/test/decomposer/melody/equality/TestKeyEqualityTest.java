package decomposer.melody.equality;

import jm.music.data.Note;
import model.melody.Melody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static jm.constants.Durations.*;
import static jm.constants.Pitches.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by night wish on 02.11.14.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = "classpath:spring.configuration.xml" )
public class TestKeyEqualityTest {

	@Autowired @Qualifier("formKeyEqualityTest")
	private EqualityTest test;

	@Test
	public void testCase1() {
		Melody testMelody1 = new Melody(
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
			new Note( A4, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( GS4, SIXTEENTH_NOTE )
		);

		assertFalse( test.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase2() {
		Melody testMelody1 = new Melody(
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
			new Note( A4, EIGHTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( D4, SIXTEENTH_NOTE ),
			new Note( E5, SIXTEENTH_NOTE ),
			new Note( G5, SIXTEENTH_NOTE )
		);

		assertTrue( test.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase3() {
		Melody testMelody1 = new Melody(
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( CS4, SIXTEENTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( D4, SIXTEENTH_NOTE )
		);

		assertFalse( test.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase4() {
		Melody testMelody1 = new Melody(
			new Note( C4, SIXTEENTH_NOTE ),
			new Note( BF0, SIXTEENTH_NOTE ),
			new Note( D4, SIXTEENTH_NOTE ),
			new Note( F4, SIXTEENTH_NOTE ),
			new Note( E4, SIXTEENTH_NOTE )
		);
		Melody testMelody2 = new Melody(
			new Note( EF4, SIXTEENTH_NOTE )
		);

		assertFalse( test.test( testMelody1, testMelody2 ) );
	}

	@Test
	public void testCase5() {
		Melody testMelody1 = new Melody(
			new Note( 60, 0.5 ),
			new Note( 62, 0.5 ),
			new Note( 64, 0.5 ),
			new Note( 65, 0.5 ),
			new Note( 67, 0.5 ),
			new Note( 69, 0.5 ),
			new Note( -2147483648, 1 )
		);
		Melody testMelody2 = new Melody(
			new Note( 57, 0.5 ),
			new Note( 59, 0.5 ),
			new Note( 60, 0.5 ),
			new Note( 62, 0.5 ),
			new Note( 64, 0.5 ),
			new Note( 65, 0.5 ),
			new Note( -2147483648, 1 )
		);

		assertTrue( test.test( testMelody1, testMelody2 ) );
	}
}
