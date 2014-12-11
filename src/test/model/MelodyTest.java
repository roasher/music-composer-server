package model;

import jm.music.data.Note;
import model.melody.Melody;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;

/**
 * Created by night wish on 11.10.14.
 */
public class MelodyTest {

    @Test
    public void testEquality2() {
        Note[] notes1 = new Note[] {
                new Note( 76, 0.5 ),
                new Note( 74, 0.5 ),
                new Note( 71, 0.5 ),
                new Note( 69, 0.25 ),
                new Note( 67, 0.25 ),
                new Note( 69, 1 ),
                new Note( 71, 0.25 ),
                new Note( 72, 0.25 )
        };
        Melody melody1 = new Melody( notes1 );

        Note[] notes2 = new Note[] {
                new Note( 76, 0.5 ),
                new Note( 74, 0.5 ),
                new Note( 71, 0.5 ),
                new Note( 69, 0.25 ),
                new Note( 67, 0.25 ),
                new Note( 69, 1 ),
                new Note( 71, 0.25 ),
                new Note( 72, 0.25 ),
        };
        Melody melody2 = new Melody( notes2 );

        Note[] notes3 = new Note[] {
                new Note( 76, 0.5 ),
                new Note( 74, 0.5 ),
                new Note( 71, 0.5 ),
                new Note( 69, 0.25 ),
                new Note( 67, 0.25 ),
                new Note( 69, 1. ),
                new Note( 71, 0.25 ),
                new Note( 72, 0.25 ),
        };
        Melody melody3 = new Melody( notes3 );

        assertEquals( melody1, melody2 );
        assertEquals( melody1, melody3 );
    }

	@Test
	public void testEqualityByStartTime() {
		Note[] notes0 = new Note[] {
		  new Note( 76, 0.5 ),
		  new Note( 74, 0.5 ),
		  new Note( 71, 0.5 ),
		};

		Melody melody1 = new Melody( notes0 );
		melody1.setStartTime( 0 );
		Melody melody2 = new Melody( notes0 );
		melody2.setStartTime( 0 );
		Melody melody3 = new Melody( notes0 );
		melody3.setStartTime( 1 );

		assertEquals( melody1, melody2 );
		assertFalse( melody1.equals( melody3 ) );
	}
}
