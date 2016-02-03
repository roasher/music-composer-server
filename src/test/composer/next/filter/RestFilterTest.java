package composer.next.filter;

import jm.music.data.Note;
import jm.music.data.Rest;
import model.ComposeBlock;
import model.melody.Melody;
import org.junit.Test;

import java.util.*;

import static jm.JMC.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by wish on 03.02.2016.
 */
public class RestFilterTest {
	@Test
	public void test() {
		List<ComposeBlock> composeBlocks = Arrays.asList(
				getRestComposeBlock( QUARTER_NOTE ),
				getRestComposeBlock( QUARTER_NOTE_TRIPLET ),
				getRestComposeBlock( HALF_NOTE ),
				getRestComposeBlock( WHOLE_NOTE ),
				getRestComposeBlock( EIGHTH_NOTE ),
				getNonRestComposeBlock( HALF_NOTE_TRIPLET ),
				getNonRestComposeBlock( WHOLE_NOTE )
		);
		ComposeBlockRestFilter composeBlockRestFilter = new ComposeBlockRestFilter( QUARTER_NOTE );
		List<ComposeBlock> filteredBlocks = composeBlockRestFilter.filter( composeBlocks, null );
		assertEquals( 5, filteredBlocks.size() );
		// rests
		assertEquals( QUARTER_NOTE, filteredBlocks.get( 0 ).getRhythmValue(), 0 );
		assertEquals( QUARTER_NOTE_TRIPLET, filteredBlocks.get( 1 ).getRhythmValue(), 0 );
		assertEquals( EIGHTH_NOTE, filteredBlocks.get( 2 ).getRhythmValue(), 0 );
		// non rests
		assertEquals( HALF_NOTE_TRIPLET, filteredBlocks.get( 3 ).getRhythmValue(), 0 );
		assertEquals( WHOLE_NOTE, filteredBlocks.get( 4 ).getRhythmValue(), 0 );
	}

	private ComposeBlock getRestComposeBlock( double restRhtyhmValue ) {
		return new ComposeBlock( 0, null, Arrays.asList(
				new Melody( new Rest( restRhtyhmValue ) ),
				new Melody( new Rest( restRhtyhmValue ) ),
				new Melody( new Rest( restRhtyhmValue ) )
		), null);
	}

	private ComposeBlock getNonRestComposeBlock( double rhtyhmValue ) {
		return new ComposeBlock( 0, null, Arrays.asList(
				new Melody( new Rest( rhtyhmValue ) ),
				new Melody( new Rest( rhtyhmValue ) ),
				new Melody( new Note( C0, rhtyhmValue ) )
		), null);
	}
}
