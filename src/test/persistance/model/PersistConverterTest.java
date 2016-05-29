package persistance.model;

import static jm.JMC.A4;
import static jm.JMC.B4;
import static jm.JMC.C3;
import static jm.JMC.C4;
import static jm.JMC.CS4;
import static jm.JMC.D4;
import static jm.JMC.DOTTED_EIGHTH_NOTE;
import static jm.JMC.DOTTED_HALF_NOTE;
import static jm.JMC.DS4;
import static jm.JMC.E4;
import static jm.JMC.EIGHTH_NOTE;
import static jm.JMC.F4;
import static jm.JMC.G4;
import static jm.JMC.GS4;
import static jm.JMC.HALF_NOTE;
import static jm.JMC.HALF_NOTE_TRIPLET;
import static jm.JMC.QUARTER_NOTE;
import static jm.JMC.SIXTEENTH_NOTE;
import static jm.JMC.WHOLE_NOTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helper.AbstractSpringTest;
import model.BlockMovement;
import model.Lexicon;
import persistance.PersistConverter;
import persistance.jpa.ComposeBlock;
import persistance.jpa.Melody;
import persistance.jpa.Note;
import persistance.jpa.factory.BlockMovementFactory;
import persistance.jpa.factory.MelodyFactory;
import persistance.jpa.factory.NoteFactory;

/**
 * Created by pyurkin on 05.05.2015.
 */
public class PersistConverterTest extends AbstractSpringTest {

	@Autowired
	private PersistConverter persistConverter;
	@Autowired
	private NoteFactory noteFactory;
	@Autowired
	private BlockMovementFactory blockMovementFactory;
	@Autowired
	private MelodyFactory melodyFactory;

	@Test public void convert() {
		List<ComposeBlock> persitanceMusicBlocks = getDefaultPersitanceMusicBlocks();
		Lexicon convertedFromPersistanceLexicon = persistConverter.convertPersistComposeBlockList( persitanceMusicBlocks );
		model.Lexicon lexicon = getDefaultLexicon();
		assertEquals( lexicon, convertedFromPersistanceLexicon );

		List<ComposeBlock> persistanceComposeBlocks = persistConverter.convertComposeBlockList( lexicon );
		assertEquals( persitanceMusicBlocks, persistanceComposeBlocks );
	}

	@Test public void doubleConvert() {
		List<ComposeBlock> persitanceMusicBlocks = getDefaultPersitanceMusicBlocks();
		Lexicon persistLexiconFirstConvertion = persistConverter.convertPersistComposeBlockList( persitanceMusicBlocks );
		List<ComposeBlock> persistLexiconSecondConversion = persistConverter.convertComposeBlockList( persistLexiconFirstConvertion );
		assertEquals( persitanceMusicBlocks, persistLexiconSecondConversion );

		model.Lexicon lexicon = getDefaultLexicon();
		List<ComposeBlock> lexiconFirstConvertion = persistConverter.convertComposeBlockList( lexicon );
		Lexicon lexoconSecondConvertion = persistConverter.convertPersistComposeBlockList( lexiconFirstConvertion );
		assertEquals( lexicon, lexoconSecondConvertion );
	}

	private List<ComposeBlock> getDefaultPersitanceMusicBlocks() {
		ComposeBlock composeBlock0 = new ComposeBlock(
				10,
				0,
				null,
				Arrays.asList(
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance(C4, WHOLE_NOTE, 0, 0),
								noteFactory.getInstance(D4, HALF_NOTE, 0, 0),
								noteFactory.getInstance(E4, EIGHTH_NOTE, 0, 0),
								noteFactory.getInstance(DS4, EIGHTH_NOTE, 0, 0) ), 'A' ),
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance( B4, WHOLE_NOTE, 0, 0 ),
								noteFactory.getInstance( A4, DOTTED_HALF_NOTE, 0, 0 ) ), 'B' )
				),
				null
				);

		ComposeBlock composeBlock1 = new ComposeBlock(
				20,
				0,
				null,
				Arrays.asList(
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance( C4, DOTTED_HALF_NOTE, 0, 0 ) ), 'C' ),
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance(G4, QUARTER_NOTE, 0, 0),
								noteFactory.getInstance(GS4, QUARTER_NOTE, 0, 0),
								noteFactory.getInstance(A4, QUARTER_NOTE, 0, 0) ), 'D' )
				),
				blockMovementFactory.getInstance( Arrays.asList( C4-DS4, G4-A4 ) )
		);

		ComposeBlock composeBlock2 = new ComposeBlock(
				30,
				0,
				null,
				Arrays.asList(
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance( B4, EIGHTH_NOTE, 0, 0 ) ), 'A' ),
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance( B4, SIXTEENTH_NOTE, 0, 0 ),
								noteFactory.getInstance( B4, SIXTEENTH_NOTE, 0, 0 ) ), 'B' )
				),
				blockMovementFactory.getInstance( Arrays.asList( B4-C4, B4-A4 ) )
		);

		ComposeBlock composeBlock3 = new ComposeBlock(
				31,
				0,
				null,
				Arrays.asList(
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance( CS4, WHOLE_NOTE, 0, 0 ) ), 'C' ),
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance( C3, HALF_NOTE_TRIPLET, 0, 0 ),
								noteFactory.getInstance( C3, HALF_NOTE_TRIPLET, 0, 0 ),
								noteFactory.getInstance( C3, HALF_NOTE_TRIPLET, 0, 0 ) ), 'D' )
				),
				blockMovementFactory.getInstance( Arrays.asList( CS4-B4, C3-B4 ) )
		);

		ComposeBlock composeBlock4 = new ComposeBlock(
				32,
				0,
				null,
				Arrays.asList(
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance( E4, DOTTED_EIGHTH_NOTE, 0, 0 ) ), 'A' ),
						melodyFactory.getInstance( Arrays.asList(
								noteFactory.getInstance( G4, SIXTEENTH_NOTE, 0, 0 ),
								noteFactory.getInstance( F4, SIXTEENTH_NOTE, 0, 0 ),
								noteFactory.getInstance( E4, SIXTEENTH_NOTE, 0, 0 ) ), 'B' ) ),
				blockMovementFactory.getInstance( Arrays.asList( E4-CS4, G4-C3 ) )
		);

		composeBlock0.possibleNextComposeBlocks.add( composeBlock1 );
		composeBlock0.possibleNextComposeBlocks.add( composeBlock3 );
		composeBlock0.possibleNextComposeBlocks.add( composeBlock4 );

		composeBlock1.possiblePreviousComposeBlocks.add( composeBlock0 );
		composeBlock1.possibleNextComposeBlocks.add( composeBlock2 );

		composeBlock2.possiblePreviousComposeBlocks.add( composeBlock1 );
		composeBlock2.possibleNextComposeBlocks.add( composeBlock3 );
		composeBlock2.possibleNextComposeBlocks.add( composeBlock4 );

		composeBlock3.possiblePreviousComposeBlocks.add( composeBlock2 );
		composeBlock3.possiblePreviousComposeBlocks.add( composeBlock0 );
		composeBlock3.possibleNextComposeBlocks.add( composeBlock4 );

		composeBlock4.possiblePreviousComposeBlocks.add( composeBlock3 );
		composeBlock4.possiblePreviousComposeBlocks.add( composeBlock0 );
		composeBlock4.possiblePreviousComposeBlocks.add( composeBlock2 );

		List<ComposeBlock> composeBlockList = new ArrayList<>();
		composeBlockList.add( composeBlock0 );
		composeBlockList.add( composeBlock1 );
		composeBlockList.add( composeBlock2 );
		composeBlockList.add( composeBlock3 );
		composeBlockList.add( composeBlock4 );

		return composeBlockList;
	}

	private model.Lexicon getDefaultLexicon() {
		List<model.melody.Melody> melodyList1 = new ArrayList<>();
		model.MusicBlock musicBlock0 = new model.MusicBlock( null,
				new model.melody.Melody(
						'A',
						new jm.music.data.Note( C4, WHOLE_NOTE, 0, 0 ),
						new jm.music.data.Note( D4, HALF_NOTE, 0, 0 ),
						new jm.music.data.Note( E4, EIGHTH_NOTE, 0, 0 ),
						new jm.music.data.Note( DS4, EIGHTH_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						'B',
						new jm.music.data.Note( B4, WHOLE_NOTE, 0, 0 ),
						new jm.music.data.Note( A4, DOTTED_HALF_NOTE, 0, 0 ) ) );

		model.MusicBlock musicBlock1 = new model.MusicBlock( null,
				new model.melody.Melody(
						'C',
						new jm.music.data.Note( C4, DOTTED_HALF_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						'D',
						new jm.music.data.Note( G4, QUARTER_NOTE, 0, 0 ),
						new jm.music.data.Note( GS4, QUARTER_NOTE, 0, 0 ),
						new jm.music.data.Note( A4, QUARTER_NOTE, 0, 0 ) ) );

		model.MusicBlock musicBlock2 = new model.MusicBlock( null,
				new model.melody.Melody(
						'A',
						new jm.music.data.Note( B4, EIGHTH_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						'B',
						new jm.music.data.Note( B4, SIXTEENTH_NOTE, 0, 0 ),
						new jm.music.data.Note( B4, SIXTEENTH_NOTE, 0, 0 ) ) );

		model.MusicBlock musicBlock3 = new model.MusicBlock( null,
				new model.melody.Melody(
						'C',
						new jm.music.data.Note( CS4, WHOLE_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						'D',
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ),
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ),
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ) ) );

		model.MusicBlock musicBlock4 = new model.MusicBlock( null,
				new model.melody.Melody(
						'A',
						new jm.music.data.Note( E4, DOTTED_EIGHTH_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						'B',
						new jm.music.data.Note( G4, SIXTEENTH_NOTE, 0, 0 ),
						new jm.music.data.Note( F4, SIXTEENTH_NOTE, 0, 0 ),
						new jm.music.data.Note( E4, SIXTEENTH_NOTE, 0, 0 ) ) );

		BlockMovement blockMovement01 = new BlockMovement( musicBlock0.getMelodyList(), musicBlock1.getMelodyList() );
		musicBlock1.setBlockMovementFromPreviousToThis( blockMovement01 );
		BlockMovement blockMovement12 = new BlockMovement( musicBlock1.getMelodyList(), musicBlock2.getMelodyList() );
		musicBlock2.setBlockMovementFromPreviousToThis( blockMovement12 );
		BlockMovement blockMovement23 = new BlockMovement( musicBlock2.getMelodyList(), musicBlock3.getMelodyList() );
		musicBlock3.setBlockMovementFromPreviousToThis( blockMovement23 );
		BlockMovement blockMovement34 = new BlockMovement( musicBlock4.getMelodyList(), musicBlock4.getMelodyList() );
		musicBlock4.setBlockMovementFromPreviousToThis( blockMovement34 );

		model.ComposeBlock composeBlock0 = new model.ComposeBlock( musicBlock0 );
		model.ComposeBlock composeBlock1 = new model.ComposeBlock( musicBlock1 );
		model.ComposeBlock composeBlock2 = new model.ComposeBlock( musicBlock2 );
		model.ComposeBlock composeBlock3 = new model.ComposeBlock( musicBlock3 );
		model.ComposeBlock composeBlock4 = new model.ComposeBlock( musicBlock4 );

		composeBlock0.getPossibleNextComposeBlocks().add( composeBlock1 );
		composeBlock0.getPossibleNextComposeBlocks().add( composeBlock3 );
		composeBlock0.getPossibleNextComposeBlocks().add( composeBlock4 );
		List<Integer> possibleNext0 = Arrays.asList( 1, 3, 4 );

		composeBlock1.getPossiblePreviousComposeBlocks().add( composeBlock0 );
		composeBlock1.getPossibleNextComposeBlocks().add( composeBlock2 );
		List<Integer> possibleNext1 = Arrays.asList( 2 );

		composeBlock2.getPossiblePreviousComposeBlocks().add( composeBlock1 );
		composeBlock2.getPossibleNextComposeBlocks().add( composeBlock3 );
		composeBlock2.getPossibleNextComposeBlocks().add( composeBlock4 );
		List<Integer> possibleNext2 = Arrays.asList( 3, 4 );

		composeBlock3.getPossiblePreviousComposeBlocks().add( composeBlock2 );
		composeBlock3.getPossiblePreviousComposeBlocks().add( composeBlock0 );
		composeBlock3.getPossibleNextComposeBlocks().add( composeBlock4 );
		List<Integer> possibleNext3 = Arrays.asList( 4 );

		composeBlock4.getPossiblePreviousComposeBlocks().add( composeBlock3 );
		composeBlock4.getPossiblePreviousComposeBlocks().add( composeBlock0 );
		composeBlock4.getPossiblePreviousComposeBlocks().add( composeBlock2 );
		List<Integer> possibleNext4 = Collections.emptyList();

		Map<Integer, List<Integer>> nextMap = new HashedMap();
		nextMap.put( 0, possibleNext0 );
		nextMap.put( 1, possibleNext1 );
		nextMap.put( 2, possibleNext2 );
		nextMap.put( 3, possibleNext3 );
		nextMap.put( 4, possibleNext4 );

		List<model.ComposeBlock> composeBlockList = new ArrayList<>();
		composeBlockList.add( composeBlock0 );
		composeBlockList.add( composeBlock1 );
		composeBlockList.add( composeBlock2 );
		composeBlockList.add( composeBlock3 );
		composeBlockList.add( composeBlock4 );

		model.Lexicon persistanceLexicon = new model.Lexicon( composeBlockList, nextMap );
		return persistanceLexicon;
	}

	@Test
	public void wrongFormTest() {
		ComposeBlock composeBlock = new ComposeBlock(  );
		composeBlock.melodies = Arrays.asList( new Melody( Arrays.asList( new Note( C3, HALF_NOTE_TRIPLET, 0, 0 ) ), 'A' ) );

		model.ComposeBlock modelComposeBlock = new model.ComposeBlock( 0, null, Arrays.asList( new model.melody.Melody( 'B', new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ) ) ) ,null );

		ComposeBlock convertedFromModel = persistConverter.convertComposeBlock( modelComposeBlock );
		assertNotEquals( composeBlock, convertedFromModel );
		model.ComposeBlock convertedFromPersist = persistConverter.convertComposeBlock( composeBlock );
		assertNotEquals( convertedFromPersist,  modelComposeBlock );

	}

}
