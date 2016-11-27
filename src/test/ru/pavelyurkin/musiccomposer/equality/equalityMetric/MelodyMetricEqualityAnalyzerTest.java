package ru.pavelyurkin.musiccomposer.equality.equalityMetric;

import helper.AbstractSpringComposerTest;
import jm.music.data.Note;
import jm.music.data.Rest;
import ru.pavelyurkin.musiccomposer.equality.equalityMetric.MelodyMetricEqualityAnalyzer;
import ru.pavelyurkin.musiccomposer.model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static jm.JMC.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.pavelyurkin.musiccomposer.utils.Utils.isEquals;

public class MelodyMetricEqualityAnalyzerTest extends AbstractSpringComposerTest {

	@Autowired
	private MelodyMetricEqualityAnalyzer melodyMetricEqualityAnalyzer;

	@Test
	public void getStrongTimeMetric() throws Exception {
		assertEquals( Arrays.asList( true ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( WHOLE_NOTE ) ) );
		assertEquals( Arrays.asList( true, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( HALF_NOTE, HALF_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, true, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE ) ) );
		assertEquals( Arrays.asList( true, true, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( HALF_NOTE, QUARTER_NOTE, QUARTER_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( QUARTER_NOTE, QUARTER_NOTE,  HALF_NOTE ) ) );
		assertEquals( Arrays.asList( true, true, false, true, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( HALF_NOTE, EIGHTH_NOTE,  EIGHTH_NOTE, EIGHTH_NOTE, EIGHTH_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( DOTTED_QUARTER_NOTE, EIGHTH_NOTE,  HALF_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, true, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( DOTTED_QUARTER_NOTE, EIGHTH_NOTE,  EIGHTH_NOTE, EIGHTH_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, true, false, true, false, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( EIGHTH_NOTE, EIGHTH_NOTE,  SIXTEENTH_NOTE, SIXTEENTH_NOTE, SIXTEENTH_NOTE, SIXTEENTH_NOTE,
						HALF_NOTE ) ) );

		assertEquals( Arrays.asList( true, false, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE,  QUARTER_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE,  WHOLE_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE,  HALF_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, true, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE,  2 * WHOLE_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, true, false, true, false, true, false, true, false ),
				melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE,
						QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE, QUARTER_NOTE,
						QUARTER_NOTE, 2 * WHOLE_NOTE ) ) );

		assertEquals( Arrays.asList( true, false, true, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( EIGHTH_NOTE, DOTTED_QUARTER_NOTE, EIGHTH_NOTE, DOTTED_QUARTER_NOTE ) ) );
		assertEquals( Arrays.asList( true, true, false, true, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( WHOLE_NOTE, EIGHTH_NOTE, DOTTED_QUARTER_NOTE, EIGHTH_NOTE, DOTTED_QUARTER_NOTE ) ) );
		assertEquals( Arrays.asList( true, false, false, false ), melodyMetricEqualityAnalyzer.getStrongTimeMetric(
				Arrays.asList( DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE, DOTTED_QUARTER_NOTE ) ) );
	}

	@Test
	public void getEqualityMetric() throws Exception {
		Melody etalon = new Melody( Arrays.asList(
				new Note( 60, HALF_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue( isEquals( melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, etalon ), 1 ) );

		// very little pitch change
		Melody melody0 = new Melody( Arrays.asList( new Note( 60, HALF_NOTE ), new Note( 64, HALF_NOTE ) ) );
		// pitch change
		Melody melody1 = new Melody( Arrays.asList( new Note( 60, HALF_NOTE ), new Note( 62, HALF_NOTE ) ) );
		// change direction (big change)
		Melody melody2 = new Melody( Arrays.asList( new Note( 60, HALF_NOTE ), new Note( 58, HALF_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody0 ) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody1 )
		);
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody1 ) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody2 )
		);

		// melism first note weak time
		Melody melody3 = new Melody( Arrays.asList(
				new Note( 60, EIGHTH_NOTE ), new Note( 62, EIGHTH_NOTE ), new Note( 60, QUARTER_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody3) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody0)
		);
		// melism first note strong time
		Melody melody4 = new Melody( Arrays.asList(
				new Note( 60, QUARTER_NOTE ), new Note( 62, EIGHTH_NOTE ), new Note( 60, EIGHTH_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody3 ) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody4 )
		);
		// melism both notes notes strong time
		Melody melody5 = new Melody( Arrays.asList(
				new Note( 60, EIGHTH_NOTE ), new Note( 62, EIGHTH_NOTE ), new Note( 60, QUARTER_NOTE ),
				new Note( 65, EIGHTH_NOTE ), new Note( 64, EIGHTH_NOTE ), new Note( 65, QUARTER_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody4 ) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody5 )
		);
		// pauses weak time
		Melody melody6 = new Melody( Arrays.asList(
				new Note( 60, QUARTER_NOTE ), new Rest( QUARTER_NOTE ),
				new Note( 65, QUARTER_NOTE ), new Rest( QUARTER_NOTE ) ) );
		// pauses strong time
		Melody melody7 = new Melody( Arrays.asList(
				new Rest( QUARTER_NOTE ), new Note( 60, QUARTER_NOTE ),
				new Rest( QUARTER_NOTE ), new Note( 65, QUARTER_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody6) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody7)
		);

		//split first note into 3
		Melody melody10 = new Melody( Arrays.asList(
				new Note( 60, QUARTER_NOTE ), new Note( 63, EIGHTH_NOTE ), new Note( 64, EIGHTH_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		//split first note into 4
		Melody melody11 = new Melody( Arrays.asList(
				new Note( 60, EIGHTH_NOTE ), new Note( 59, EIGHTH_NOTE ), new Note( 62, EIGHTH_NOTE ), new Note( 63, EIGHTH_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody10 ) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody11 )
		);

		//One note longer than first
		Melody melody12 = new Melody( Arrays.asList(
				new Note( 60, EIGHTH_NOTE ), new Note( 59, DOTTED_QUARTER_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody12 ) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody1 )
		);

		//Change first note (strong time)
		Melody melody13 = new Melody( Arrays.asList(
				new Note( 58, EIGHTH_NOTE ), new Note( 60, DOTTED_QUARTER_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody13 ) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody1 )
		);

		//2 notes change
		Melody melody14 = new Melody( Arrays.asList(
				new Note( 56, QUARTER_NOTE ), new Note( 60, QUARTER_NOTE ),
				new Note( 64, HALF_NOTE ) ) );
		assertTrue(
				melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody13 ) >
						melodyMetricEqualityAnalyzer.getEqualityMetric( etalon, melody14 )
		);
	}

	@Test
	public void transformMelodyToNewRhythmValues() throws Exception {
		List<Double> newRhythmValues = Arrays.asList(
				0.25,
				0.25,
				0.5,
				0.33333333333333326,
				0.16666666666666674,
				2.5
		);
		List<Note> notes = Arrays.asList(
			new Note( 72, 0.25 ),
				new Note( 71, 0.25 ),
				new Note( 72, 0.8333333333333333 ),
				new Rest( 2.6666666666666665 )
		);
		// asserting no exception
		melodyMetricEqualityAnalyzer.transformMelodyToNewRhythmValues( notes, newRhythmValues );
	}

}