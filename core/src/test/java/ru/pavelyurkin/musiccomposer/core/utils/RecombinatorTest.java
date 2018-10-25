package ru.pavelyurkin.musiccomposer.core.utils;

import jm.music.data.Note;
import jm.music.data.Rest;
import org.junit.Test;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

import java.util.Arrays;
import java.util.List;

import static jm.JMC.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Created by pyurkin on 25.11.14.
 */
public class RecombinatorTest {

	@Test
	public void testRebuildingMelodyBlockList() {
		// input
		List< InstrumentPart > inputMelodyBlock = Arrays.asList(
				new InstrumentPart(
						new Rest( QUARTER_NOTE ),
						new Note( 60, DOTTED_QUARTER_NOTE ),
						new Note( 62, SIXTEENTH_NOTE ),
						new Note( 60, SIXTEENTH_NOTE ),
						new Note( 64, QUARTER_NOTE )
				),
				new InstrumentPart(
						new Note( 58, HALF_NOTE ),
						new Note( 56, EIGHTH_NOTE ),
						new Note( 58, EIGHTH_NOTE ),
						new Note( 50, DOTTED_EIGHTH_NOTE ),
						new Note( 51, SIXTEENTH_NOTE )
				)
		);

		// testList
		List< List< InstrumentPart > > testList = Recombinator.recombineMelodyBlock( inputMelodyBlock );

		// etalon output
		List< InstrumentPart > melodyList0 = Arrays.asList(
				new InstrumentPart( new Rest( QUARTER_NOTE ) ),
				new InstrumentPart( new Note( 58, QUARTER_NOTE ) )
		);

		List< InstrumentPart > melodyList1 = Arrays.asList(
				new InstrumentPart( new Note( 60, QUARTER_NOTE ) ),
				new InstrumentPart( new Note( 58, QUARTER_NOTE ) )
		);

		List< InstrumentPart > melodyList10 = Arrays.asList(
				new InstrumentPart( new Note( 60, EIGHTH_NOTE ) ),
				new InstrumentPart( new Note( 56, EIGHTH_NOTE ) )
		);

		List< InstrumentPart > melodyList2 = Arrays.asList(
				new InstrumentPart( new Note( 62, SIXTEENTH_NOTE ) ),
				new InstrumentPart( new Note( 58, SIXTEENTH_NOTE ) )
		);

		List< InstrumentPart > melodyList3 = Arrays.asList(
				new InstrumentPart( new Note( 60, SIXTEENTH_NOTE ) ),
				new InstrumentPart( new Note( 58, SIXTEENTH_NOTE ) )
		);

		List< InstrumentPart > melodyList4 = Arrays.asList(
				new InstrumentPart( new Note( 64, DOTTED_EIGHTH_NOTE ) ),
				new InstrumentPart( new Note( 50, DOTTED_EIGHTH_NOTE ) )
		);

		List< InstrumentPart > melodyList5 = Arrays.asList(
				new InstrumentPart( new Note( 64, SIXTEENTH_NOTE ) ),
				new InstrumentPart( new Note( 51, SIXTEENTH_NOTE ) )
		);

		List< List< InstrumentPart > > etalonList = Arrays.asList(
			melodyList0,
			melodyList1,
			melodyList10,
			melodyList2,
			melodyList3,
			melodyList4,
			melodyList5
		);

		assertThat( etalonList, is( testList ) );
	}

	@Test
	public void testRecombine() {
		List< InstrumentPart > melodyList0 = Arrays.asList(
				new InstrumentPart(
						new Rest( QUARTER_NOTE ),
						new Note( 60, DOTTED_QUARTER_NOTE ),
						new Note( 62, SIXTEENTH_NOTE ),
						new Note( 60, SIXTEENTH_NOTE )
				),
				new InstrumentPart(
						new Note( 58, HALF_NOTE ),
						new Note( 56, EIGHTH_NOTE ),
						new Note( 58, EIGHTH_NOTE )
				)
		);

		List< InstrumentPart > melodyList1 = Arrays.asList(
				new InstrumentPart( new Note( 64, QUARTER_NOTE ) ),
				new InstrumentPart(
						new Note( 50, DOTTED_EIGHTH_NOTE ),
						new Note( 51, SIXTEENTH_NOTE )
				)
		);

		List< InstrumentPart > melodyList2 = Arrays.asList(
				new InstrumentPart(
						new Note( 67, SIXTEENTH_NOTE ),
						new Note( 65, SIXTEENTH_NOTE ),
						new Note( 67, SIXTEENTH_NOTE ),
						new Note( 65, SIXTEENTH_NOTE )
				),
				new InstrumentPart(
						new Note( 60, EIGHTH_NOTE ),
						new Note( 62, EIGHTH_NOTE )
				)
		);

		List< InstrumentPart > melodyList3 = Arrays.asList(
				new InstrumentPart(
						new Note( 67, SIXTEENTH_NOTE ),
						new Note( 68, DOUBLE_DOTTED_QUARTER_NOTE ),
						new Rest( QUARTER_NOTE )
				),
				new InstrumentPart(
						new Note( 60, QUARTER_NOTE ),
						new Note( 60, QUARTER_NOTE ),
						new Rest( QUARTER_NOTE )
				)
		);

		List< List< InstrumentPart > > melodyBlockList = Arrays.asList(
				melodyList0,
				melodyList1,
				melodyList2,
				melodyList3
		);

		List< List < InstrumentPart > > recombineList = Recombinator.recombine( melodyBlockList );

		assertThat( recombineList, hasSize( 15 ) );
	}

	@Test
	public void recombineChords() throws Exception {
		// input
		List< InstrumentPart > inputMelodyBlock = Arrays.asList(
				new InstrumentPart(
						new NewMelody( new Rest( QUARTER_NOTE ) ),
						new Chord( Arrays.asList( 60, 61 ), QUARTER_NOTE )
				),
				new InstrumentPart(
						new Note( 58, HALF_NOTE )
				)
		);

		assertThat( Recombinator.recombineMelodyBlock( inputMelodyBlock ), is( Arrays.asList(
				Arrays.asList(
						new InstrumentPart( new NewMelody( new Rest( QUARTER_NOTE ) ) ),
						new InstrumentPart( new Note( 58, QUARTER_NOTE ) )
				),
				Arrays.asList(
						new InstrumentPart( new Chord( Arrays.asList( 60, 61 ), QUARTER_NOTE ) ),
						new InstrumentPart( new Note( 58, QUARTER_NOTE ) )
				)
		) ) );
	}
}
