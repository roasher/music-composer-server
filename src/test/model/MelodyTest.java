package model;

import jm.music.data.Note;
import model.melody.Melody;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

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
}
