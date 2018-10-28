package ru.pavelyurkin.musiccomposer.core.utils;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Rest;
import jm.music.data.Score;
import jm.util.View;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

import java.util.Arrays;

import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.*;

@RunWith( MockitoJUnitRunner.class )
public class InstrumentPartToPartConverterTest {

	@InjectMocks
	private InstrumentPartToPartConverter converter;

	@Mock
	private CompositionParser compositionParser;

	@Test
	@Ignore
	public void testConvert() throws Exception {
		InstrumentPart instrumentPart = new InstrumentPart( Arrays.asList(
				new NewMelody( new Note( C3, QUARTER_NOTE ), new Rest( QUARTER_NOTE ) ),
				new Chord( Arrays.asList( G3, D4 ), HALF_NOTE ),
				new NewMelody( new Note( D5, QUARTER_NOTE ) )
		) );

		Part convert = converter.convert( instrumentPart );

		Score score = new Score( convert );
		View.notate( score );
		Utils.suspend();
	}
}