package utils;

import jm.music.data.Note;
import utils.ModelUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Pitches.C0;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Pavel Yurkin on 20.07.14.
 */
public class ModelUtilsTest {

	@Test
    public void testGetIntervalPattern() throws Exception {
        List< Integer > pitches = new ArrayList< Integer >( );
        // 60 62 64 64 70 89 100
        pitches.add( 100 );
        pitches.add( 60 );
        pitches.add( 62 );
        pitches.add( 89 );
        pitches.add( 64 );
        pitches.add( 64 );
        pitches.add( 70 );

        List< Integer > intervalPattern = new ArrayList< Integer >( pitches.size() - 1 );
        // 60 <2> 62 <2> 64 <0> 64 <6> 70 <19> 89 <11> 100
        intervalPattern.add( 2 );
        intervalPattern.add( 2 );
        intervalPattern.add( 0 );
        intervalPattern.add( 6 );
        intervalPattern.add( 19 );
        intervalPattern.add( 11 );

        assertEquals( intervalPattern, ModelUtils.getIntervalPattern( pitches ) );
    }

	@Test
	public void testSumAllRhythmValues() {
		List<Note> slice10 =  new ArrayList<>();
		slice10.add( new Note( C0, QUARTER_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );
		slice10.add( new Note( C0, SIXTEENTH_NOTE ) );

		assertEquals( ModelUtils.sumAllRhytmValues( slice10 ), HALF_NOTE );
	}
}
