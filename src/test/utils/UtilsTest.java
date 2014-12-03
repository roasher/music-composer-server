package utils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import static jm.JMC.*;
import static utils.Utils.*;

/**
 * Created by pyurkin on 25.11.14.
 */
public class UtilsTest {
	@Test
	public void roundRhythmTest() {

		assertEquals( roundRhythmValue( 0.9833333333333 + WHOLE_NOTE*4 ), QUARTER_NOTE + WHOLE_NOTE*4 );
		assertEquals( roundRhythmValue( 0.5166666666666 + WHOLE_NOTE*4 ), EIGHTH_NOTE + WHOLE_NOTE*4 );
		assertEquals( roundRhythmValue( 0.4833333333333 ), EIGHTH_NOTE );
		assertEquals( roundRhythmValue( 1.4833333333333 ), DOTTED_QUARTER_NOTE );
		assertEquals( roundRhythmValue( 0.49999999999999906 ), EIGHTH_NOTE );

		assertEquals( roundRhythmValue( 0.01666666666667993 ), 0. );
		assertEquals( roundRhythmValue( 0.083 ), THIRTYSECOND_NOTE_TRIPLET );
		assertEquals( roundRhythmValue( 0.043 ), THIRTYSECOND_NOTE_TRIPLET );
		assertEquals( roundRhythmValue( 0.041 ), 0. );

		assertEquals( roundRhythmValue( 0.34 ), EIGHTH_NOTE_TRIPLET);
		assertEquals( roundRhythmValue( 0.32 ), EIGHTH_NOTE_TRIPLET);

		assertEquals( roundRhythmValue( 0.34 + WHOLE_NOTE*11 ), EIGHTH_NOTE_TRIPLET + WHOLE_NOTE*11 );
		assertEquals( roundRhythmValue( 0.32 + WHOLE_NOTE*10 ), EIGHTH_NOTE_TRIPLET  + WHOLE_NOTE*10 );
		assertEquals( roundRhythmValue( 0.333333333302 + WHOLE_NOTE*5 ), EIGHTH_NOTE_TRIPLET + WHOLE_NOTE*5);

		assertEquals( roundRhythmValue( 0.9833333333333333 ), 1. );
		assertEquals( roundRhythmValue( 0.9999999999999999 ), 1. );
	}

	@Test
	public void closestListElementTest() {
		assertEquals( getClosestListElement( 0.9833333333333, rhythmValues ), QUARTER_NOTE );
		assertEquals( getClosestListElement( 0.5166666666666, rhythmValues ), EIGHTH_NOTE );
		assertEquals( getClosestListElement( 0.4833333333333, rhythmValues ), EIGHTH_NOTE );
		assertEquals( getClosestListElement( 1.4833333333333, rhythmValues ), DOTTED_QUARTER_NOTE );
		assertEquals( getClosestListElement( 0.49999999999999906, rhythmValues ), EIGHTH_NOTE );

		assertEquals( getClosestListElement( 1.0166666666666657, rhythmValues ), QUARTER_NOTE );
		assertEquals( getClosestListElement( 0.999999999999999, rhythmValues ), QUARTER_NOTE );
		assertEquals( getClosestListElement( 0.9875, rhythmValues ), QUARTER_NOTE );
		assertEquals( getClosestListElement( 1.5000000000000029, rhythmValues ), DOTTED_QUARTER_NOTE );
		assertEquals( getClosestListElement( 1.4875, rhythmValues ), DOTTED_QUARTER_NOTE );

		assertEquals( getClosestListElement( 0.666664, rhythmValues ), QUARTER_NOTE_TRIPLET );
		assertEquals( getClosestListElement( 0.665, rhythmValues ), QUARTER_NOTE_TRIPLET );

		assertEquals( getClosestListElement( 0.34, rhythmValues ), EIGHTH_NOTE_TRIPLET );
		assertEquals( getClosestListElement( 0.32, rhythmValues ), EIGHTH_NOTE_TRIPLET );
		assertEquals( getClosestListElement( 0.333333333302, rhythmValues ), EIGHTH_NOTE_TRIPLET );
	}
}
