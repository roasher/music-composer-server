package ru.pavelyurkin.musiccomposer.core.persistance.model;

import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.helper.AbstractSpringTest;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pavelyurkin.musiccomposer.core.model.BlockMovement;
import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.persistance.PersistConverter;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.Melody;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.Note;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory.BlockMovementFactory;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory.MelodyFactory;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory.NoteFactory;

import java.util.*;

import static jm.JMC.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

	@Test
	public void convert() {
		List<ComposeBlock> persitanceMusicBlocks = getDefaultPersitanceMusicBlocks();
		Lexicon convertedFromPersistanceLexicon = persistConverter.convertPersistComposeBlockList( persitanceMusicBlocks );
		Lexicon lexicon = getDefaultLexicon();
		assertEquals( lexicon, convertedFromPersistanceLexicon );

		List<ComposeBlock> persistanceComposeBlocks = persistConverter.convertComposeBlockList( lexicon );
		assertEquals( persitanceMusicBlocks, persistanceComposeBlocks );
	}

	@Test
	public void doubleConvert() {
		List<ComposeBlock> persitanceMusicBlocks = getDefaultPersitanceMusicBlocks();
		Lexicon persistLexiconFirstConvertion = persistConverter.convertPersistComposeBlockList( persitanceMusicBlocks );
		List<ComposeBlock> persistLexiconSecondConversion = persistConverter.convertComposeBlockList( persistLexiconFirstConvertion );
		assertEquals( persitanceMusicBlocks, persistLexiconSecondConversion );

		Lexicon lexicon = getDefaultLexicon();
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

	private Lexicon getDefaultLexicon() {
		List<ru.pavelyurkin.musiccomposer.core.model.melody.Melody> melodyList1 = new ArrayList<>();
		MusicBlock musicBlock0 = new MusicBlock( null,
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'A',
						new jm.music.data.Note( C4, WHOLE_NOTE, 0, 0 ),
						new jm.music.data.Note( D4, HALF_NOTE, 0, 0 ),
						new jm.music.data.Note( E4, EIGHTH_NOTE, 0, 0 ),
						new jm.music.data.Note( DS4, EIGHTH_NOTE, 0, 0 ) ),
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'B',
						new jm.music.data.Note( B4, WHOLE_NOTE, 0, 0 ),
						new jm.music.data.Note( A4, DOTTED_HALF_NOTE, 0, 0 ) ) );

		MusicBlock musicBlock1 = new MusicBlock( null,
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'C',
						new jm.music.data.Note( C4, DOTTED_HALF_NOTE, 0, 0 ) ),
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'D',
						new jm.music.data.Note( G4, QUARTER_NOTE, 0, 0 ),
						new jm.music.data.Note( GS4, QUARTER_NOTE, 0, 0 ),
						new jm.music.data.Note( A4, QUARTER_NOTE, 0, 0 ) ) );

		MusicBlock musicBlock2 = new MusicBlock( null,
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'A',
						new jm.music.data.Note( B4, EIGHTH_NOTE, 0, 0 ) ),
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'B',
						new jm.music.data.Note( B4, SIXTEENTH_NOTE, 0, 0 ),
						new jm.music.data.Note( B4, SIXTEENTH_NOTE, 0, 0 ) ) );

		MusicBlock musicBlock3 = new MusicBlock( null,
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'C',
						new jm.music.data.Note( CS4, WHOLE_NOTE, 0, 0 ) ),
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'D',
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ),
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ),
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ) ) );

		MusicBlock musicBlock4 = new MusicBlock( null,
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
						'A',
						new jm.music.data.Note( E4, DOTTED_EIGHTH_NOTE, 0, 0 ) ),
				new ru.pavelyurkin.musiccomposer.core.model.melody.Melody(
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
		BlockMovement blockMovement34 = new BlockMovement( musicBlock3.getMelodyList(), musicBlock4.getMelodyList() );
		musicBlock4.setBlockMovementFromPreviousToThis( blockMovement34 );

		ru.pavelyurkin.musiccomposer.core.model.ComposeBlock composeBlock0 = new ru.pavelyurkin.musiccomposer.core.model.ComposeBlock( musicBlock0 );
		ru.pavelyurkin.musiccomposer.core.model.ComposeBlock composeBlock1 = new ru.pavelyurkin.musiccomposer.core.model.ComposeBlock( musicBlock1 );
		ru.pavelyurkin.musiccomposer.core.model.ComposeBlock composeBlock2 = new ru.pavelyurkin.musiccomposer.core.model.ComposeBlock( musicBlock2 );
		ru.pavelyurkin.musiccomposer.core.model.ComposeBlock composeBlock3 = new ru.pavelyurkin.musiccomposer.core.model.ComposeBlock( musicBlock3 );
		ru.pavelyurkin.musiccomposer.core.model.ComposeBlock composeBlock4 = new ru.pavelyurkin.musiccomposer.core.model.ComposeBlock( musicBlock4 );

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

		List<ru.pavelyurkin.musiccomposer.core.model.ComposeBlock> composeBlockList = new ArrayList<>();
		composeBlockList.add( composeBlock0 );
		composeBlockList.add( composeBlock1 );
		composeBlockList.add( composeBlock2 );
		composeBlockList.add( composeBlock3 );
		composeBlockList.add( composeBlock4 );

		Lexicon persistanceLexicon = new Lexicon( composeBlockList, nextMap );
		return persistanceLexicon;
	}

	@Test
	public void wrongFormTest() {
		ComposeBlock composeBlock = new ComposeBlock(  );
		composeBlock.melodies = Arrays.asList( new Melody( Arrays.asList( new Note( C3, HALF_NOTE_TRIPLET, 0, 0 ) ), 'A' ) );

		ru.pavelyurkin.musiccomposer.core.model.ComposeBlock modelComposeBlock = new ru.pavelyurkin.musiccomposer.core.model.ComposeBlock( 0, null, Arrays.asList( new ru.pavelyurkin.musiccomposer.core.model.melody.Melody( 'B', new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ) ) ) ,null );

		ComposeBlock convertedFromModel = persistConverter.convertComposeBlock( modelComposeBlock );
		assertNotEquals( composeBlock, convertedFromModel );
		ru.pavelyurkin.musiccomposer.core.model.ComposeBlock convertedFromPersist = persistConverter.convertComposeBlock( composeBlock );
		assertNotEquals( convertedFromPersist,  modelComposeBlock );

	}

}
