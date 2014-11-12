package model;

import jm.music.data.Note;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by night wish on 11.10.14.
 */
public class MelodyTest {

    @Test
    public void testEquality2() {
        Note[] notes1 = new Note[] {
                new Note( 76, 0.49999999999999906 ),
                new Note( 74, 0.49999999999999906 ),
                new Note( 71, 0.49999999999999906 ),
                new Note( 69, 0.24999999999999906 ),
                new Note( 67, 0.24999999999999906 ),
                new Note( 69, 1.0000000000000029 ),
                new Note( 71, 0.24999999999999906 ),
                new Note( 72, 0.24999999999999906 )
        };
        Melody melody1 = new Melody( notes1 );

        Note[] notes2 = new Note[] {
                new Note( 76, 0.49999999999999906 ),
                new Note( 74, 0.49999999999999906 ),
                new Note( 71, 0.49999999999999906 ),
                new Note( 69, 0.24999999999999906 ),
                new Note( 67, 0.24999999999999906 ),
                new Note( 69, 1.0000000000000029 ),
                new Note( 71, 0.24999999999999906 ),
                new Note( 72, 0.24999999999999906 ),
        };
        Melody melody2 = new Melody( notes2 );

        Note[] notes3 = new Note[] {
                new Note( 76, 0.49999999999998485 ),
                new Note( 74, 0.49999999999998485 ),
                new Note( 71, 0.49999999999998485 ),
                new Note( 69, 0.24999999999998485 ),
                new Note( 67, 0.24999999999998485 ),
                new Note( 69, 0.9999999999999887 ),
                new Note( 71, 0.24999999999998485 ),
                new Note( 72, 0.24999999999998485 ),
        };
        Melody melody3 = new Melody( notes3 );

        assertEquals( melody1, melody2 );
        assertEquals( melody1, melody3 );
    }
}
