package decomposer.melody.analyzer;

import helper.AbstractSpringComposerTest;
import jm.music.data.Note;
import jm.music.data.Rest;
import model.melody.Melody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static jm.JMC.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MetricMelodyEqualityAnalyzerTest extends AbstractSpringComposerTest {

	@Autowired
	private MetricMelodyEqualityAnalyzer metricMelodyEqualityAnalyzer;

	@Test
	public void getEqualityMetric() throws Exception {
		Melody etalon = new Melody( Arrays.asList(
				new Note( 60, HALF_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertEquals( Double.compare( metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, etalon ), 1 ), 0 );

		// very little pitch change
		Melody melody0 = new Melody( Arrays.asList( new Note( 60, HALF_NOTE ), new Note( 64, HALF_NOTE ) ) );
		// pitch change
		Melody melody1 = new Melody( Arrays.asList( new Note( 60, HALF_NOTE ), new Note( 62, HALF_NOTE ) ) );
		// change direction (big change)
		Melody melody2 = new Melody( Arrays.asList( new Note( 60, HALF_NOTE ), new Note( 58, HALF_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody0 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody1 )
		);
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody1 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody2 )
		);

		// melism first note weak time
		Melody melody3 = new Melody( Arrays.asList(
				new Note( 60, EIGHTH_NOTE ), new Note( 62, EIGHTH_NOTE ), new Note( 60, QUARTER_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody3) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody0)
		);
		// melism first note strong time
		Melody melody4 = new Melody( Arrays.asList(
				new Note( 60, QUARTER_NOTE ), new Note( 62, EIGHTH_NOTE ), new Note( 60, EIGHTH_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody3 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody4 )
		);
		// melism both notes notes strong time
		Melody melody5 = new Melody( Arrays.asList(
				new Note( 60, EIGHTH_NOTE ), new Note( 62, EIGHTH_NOTE ), new Note( 60, QUARTER_NOTE ),
				new Note( 65, EIGHTH_NOTE ), new Note( 64, EIGHTH_NOTE ), new Note( 65, QUARTER_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody4 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody5 )
		);
		// pauses weak time
		Melody melody6 = new Melody( Arrays.asList(
				new Note( 60, QUARTER_NOTE ), new Rest( EIGHTH_NOTE ),
				new Note( 65, EIGHTH_NOTE ), new Rest( EIGHTH_NOTE ) ) );
		// pauses strong time
		Melody melody7 = new Melody( Arrays.asList(
				new Rest( EIGHTH_NOTE ), new Note( 60, QUARTER_NOTE ),
				new Rest( EIGHTH_NOTE ), new Note( 65, EIGHTH_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody6) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody7)
		);

		//alter first note on weak time
		Melody melody8 = new Melody( Arrays.asList(
				new Note( 60, QUARTER_NOTE ), new Note( 63, QUARTER_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		//alter first note on DOUBLE weak time
		Melody melody9 = new Melody( Arrays.asList(
				new Note( 60, DOTTED_QUARTER_NOTE ), new Note( 63, EIGHTH_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody9 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody8 )
		);

		//split first note into 3
		Melody melody10 = new Melody( Arrays.asList(
				new Note( 60, QUARTER_NOTE ), new Note( 63, EIGHTH_NOTE ), new Note( 64, EIGHTH_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		//split first note into 4
		Melody melody11 = new Melody( Arrays.asList(
				new Note( 60, EIGHTH_NOTE ), new Note( 59, EIGHTH_NOTE ), new Note( 62, EIGHTH_NOTE ), new Note( 61, EIGHTH_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody10 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody11 )
		);

		//One note longer than first
		Melody melody12 = new Melody( Arrays.asList(
				new Note( 60, EIGHTH_NOTE ), new Note( 59, DOTTED_QUARTER_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody1 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody12 )
		);

		//Change first note (strong time)
		Melody melody13 = new Melody( Arrays.asList(
				new Note( 58, EIGHTH_NOTE ), new Note( 60, DOTTED_QUARTER_NOTE ),
				new Note( 65, HALF_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody1 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody13 )
		);

		//2 notes change
		Melody melody14 = new Melody( Arrays.asList(
				new Note( 56, QUARTER_NOTE ), new Note( 60, QUARTER_NOTE ),
				new Note( 64, HALF_NOTE ) ) );
		assertTrue(
				metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody13 ) >
						metricMelodyEqualityAnalyzer.getEqualityMetric( etalon, melody14 )
		);
	}

}