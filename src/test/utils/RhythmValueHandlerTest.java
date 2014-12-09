package utils;

import helper.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static jm.constants.Durations.*;
import static jm.constants.Durations.EIGHTH_NOTE_TRIPLET;
import static jm.constants.Durations.QUARTER_NOTE_TRIPLET;
import static junit.framework.Assert.assertEquals;

/**
 * Created by pyurkin on 08.12.14.
 */
public class RhythmValueHandlerTest extends AbstractSpringTest {

	@Autowired
	private RhythmValueHandler rhythmValueHandler;

	@Test
	public void roundRhythmTest() {

		assertEquals( rhythmValueHandler.roundRhythmValue( 0.9833333333333 + WHOLE_NOTE * 4 ), QUARTER_NOTE + WHOLE_NOTE*4 );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.5166666666666 + WHOLE_NOTE * 4 ), EIGHTH_NOTE + WHOLE_NOTE*4 );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.4833333333333 ), EIGHTH_NOTE );
		assertEquals( rhythmValueHandler.roundRhythmValue( 1.4833333333333 ), DOTTED_QUARTER_NOTE );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.49999999999999906 ), EIGHTH_NOTE );

		assertEquals( rhythmValueHandler.roundRhythmValue( 0.01666666666667993 ), 0. );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.083 ), THIRTYSECOND_NOTE_TRIPLET );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.043 ), THIRTYSECOND_NOTE_TRIPLET );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.041 ), 0. );

		assertEquals( rhythmValueHandler.roundRhythmValue( 0.34 ), EIGHTH_NOTE_TRIPLET);
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.32 ), EIGHTH_NOTE_TRIPLET);

		assertEquals( rhythmValueHandler.roundRhythmValue( 0.34 + WHOLE_NOTE * 11 ), EIGHTH_NOTE_TRIPLET + WHOLE_NOTE*11 );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.32 + WHOLE_NOTE * 10 ), EIGHTH_NOTE_TRIPLET  + WHOLE_NOTE*10 );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.333333333302 + WHOLE_NOTE * 5 ), EIGHTH_NOTE_TRIPLET + WHOLE_NOTE*5);

		assertEquals( rhythmValueHandler.roundRhythmValue( 0.9833333333333333 ), 1. );
		assertEquals( rhythmValueHandler.roundRhythmValue( 0.9999999999999999 ), 1. );
	}

	@Test
	public void closestListElementTest() {
		assertEquals( rhythmValueHandler.getClosestListElement( 0.9833333333333, rhythmValueHandler.rhythmValues ), QUARTER_NOTE );
		assertEquals( rhythmValueHandler.getClosestListElement( 0.5166666666666, rhythmValueHandler.rhythmValues ), EIGHTH_NOTE );
		assertEquals( rhythmValueHandler.getClosestListElement( 0.4833333333333, rhythmValueHandler.rhythmValues ), EIGHTH_NOTE );
		assertEquals( rhythmValueHandler.getClosestListElement( 1.4833333333333, rhythmValueHandler.rhythmValues ), DOTTED_QUARTER_NOTE );
		assertEquals( rhythmValueHandler.getClosestListElement( 0.49999999999999906, rhythmValueHandler.rhythmValues ), EIGHTH_NOTE );

		assertEquals( rhythmValueHandler.getClosestListElement( 1.0166666666666657, rhythmValueHandler.rhythmValues ), QUARTER_NOTE );
		assertEquals( rhythmValueHandler.getClosestListElement( 0.999999999999999, rhythmValueHandler.rhythmValues ), QUARTER_NOTE );
		assertEquals( rhythmValueHandler.getClosestListElement( 0.9875, rhythmValueHandler.rhythmValues ), QUARTER_NOTE );
		assertEquals( rhythmValueHandler.getClosestListElement( 1.5000000000000029, rhythmValueHandler.rhythmValues ), DOTTED_QUARTER_NOTE );
		assertEquals( rhythmValueHandler.getClosestListElement( 1.4875, rhythmValueHandler.rhythmValues ), DOTTED_QUARTER_NOTE );

		assertEquals( rhythmValueHandler.getClosestListElement( 0.666664, rhythmValueHandler.rhythmValues ), QUARTER_NOTE_TRIPLET );
		assertEquals( rhythmValueHandler.getClosestListElement( 0.665, rhythmValueHandler.rhythmValues ), QUARTER_NOTE_TRIPLET );

		assertEquals( rhythmValueHandler.getClosestListElement( 0.34, rhythmValueHandler.rhythmValues ), EIGHTH_NOTE_TRIPLET );
		assertEquals( rhythmValueHandler.getClosestListElement( 0.32, rhythmValueHandler.rhythmValues ), EIGHTH_NOTE_TRIPLET );
		assertEquals( rhythmValueHandler.getClosestListElement( 0.333333333302, rhythmValueHandler.rhythmValues ), EIGHTH_NOTE_TRIPLET );
	}
}
