package persistance.model;

import helper.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import persistance.PersistConverter;
import persistance.factory.BlockMovementFactory;
import persistance.jpa.ComposeBlock;
import persistance.jpa.Melody;
import persistance.factory.MelodyFactory;
import persistance.factory.NoteFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jm.JMC.*;
import static org.junit.Assert.assertEquals;

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
		model.Lexicon lexicon = getDefaultLexicon();
		List<ComposeBlock> convertedComposeBlocks = persistConverter.convertComposeBlockList( lexicon.getComposeBlockList() );
		assertEquals( lexicon.getComposeBlockList(), persistConverter.convertPersistComposeBlockList( persitanceMusicBlocks ) );
		assertEquals( persitanceMusicBlocks, convertedComposeBlocks );
	}

	@Test public void doubleConvert() {
		List<ComposeBlock> persitanceMusicBlocks = getDefaultPersitanceMusicBlocks();
		assertEquals( persitanceMusicBlocks, persistConverter.convertComposeBlockList( persistConverter.convertPersistComposeBlockList( persitanceMusicBlocks ) ) );

		model.Lexicon lexicon = getDefaultLexicon();
		assertEquals( lexicon.getComposeBlockList(), persistConverter.convertPersistComposeBlockList(persistConverter.convertComposeBlockList( lexicon.getComposeBlockList() ) ) );
	}

	private List<ComposeBlock> getDefaultPersitanceMusicBlocks() {
		ComposeBlock composeBlock1 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
					new Melody[] {
							melodyFactory.getInstance( Arrays.asList(
									noteFactory.getInstance(C4, WHOLE_NOTE, 0, 0),
									noteFactory.getInstance(D4, HALF_NOTE, 0, 0),
									noteFactory.getInstance(E4, EIGHTH_NOTE, 0, 0),
									noteFactory.getInstance(DS4, EIGHTH_NOTE, 0, 0) ) ),
							melodyFactory.getInstance( Arrays.asList(
									noteFactory.getInstance( B4, WHOLE_NOTE, 0, 0 ),
									noteFactory.getInstance( A4, DOTTED_HALF_NOTE, 0, 0 ) ) ) } ),
				null
				);

		ComposeBlock composeBlock2 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
						new Melody[] {
								melodyFactory.getInstance( Arrays.asList(
										noteFactory.getInstance( C4, DOTTED_HALF_NOTE, 0, 0 ) ) ),
								melodyFactory.getInstance( Arrays.asList(
										noteFactory.getInstance(G4, QUARTER_NOTE, 0, 0),
										noteFactory.getInstance(GS4, QUARTER_NOTE, 0, 0),
										noteFactory.getInstance(A4, QUARTER_NOTE, 0, 0)) ) } ),
				blockMovementFactory.getInstance( G4-A4, C4-DS4 )
		);

		ComposeBlock composeBlock3 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
						new Melody[] {
								melodyFactory.getInstance( Arrays.asList(
										noteFactory.getInstance( B4, EIGHTH_NOTE, 0, 0 ) ) ),
								melodyFactory.getInstance( Arrays.asList(
										noteFactory.getInstance( B4, SIXTEENTH_NOTE, 0, 0 ),
										noteFactory.getInstance( B4, SIXTEENTH_NOTE, 0, 0 ) ) ) } ),
				blockMovementFactory.getInstance( B4-A4, B4-C4)
		);

		ComposeBlock composeBlock4 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
						new Melody[] {
								melodyFactory.getInstance( Arrays.asList(
										noteFactory.getInstance( CS4, WHOLE_NOTE, 0, 0 ) ) ),
								melodyFactory.getInstance( Arrays.asList(
										noteFactory.getInstance( C3, HALF_NOTE_TRIPLET, 0, 0 ),
										noteFactory.getInstance( C3, HALF_NOTE_TRIPLET, 0, 0 ),
										noteFactory.getInstance( C3, HALF_NOTE_TRIPLET, 0, 0 ) ) ) } ),
				blockMovementFactory.getInstance( CS4-B4, C3-B4 )
		);

		ComposeBlock composeBlock5 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
						new Melody[] {
								melodyFactory.getInstance( Arrays.asList(
										noteFactory.getInstance( E4, DOTTED_EIGHTH_NOTE, 0, 0 ) ) ),
								melodyFactory.getInstance( Arrays.asList(
										noteFactory.getInstance( G4, SIXTEENTH_NOTE, 0, 0 ),
										noteFactory.getInstance( F4, SIXTEENTH_NOTE, 0, 0 ),
										noteFactory.getInstance( E4, SIXTEENTH_NOTE, 0, 0 ) ) ) } ),
				blockMovementFactory.getInstance( G4-CS4, E4-C3)
		);

		composeBlock1.possiblePreviousComposeBlocks.add( null );
		composeBlock1.possiblePreviousComposeBlocks.add( composeBlock2 );
		composeBlock1.possibleNextComposeBlocks.add( composeBlock2 );
		composeBlock1.possibleNextComposeBlocks.add( composeBlock4 );
		composeBlock1.possibleNextComposeBlocks.add( composeBlock5 );

		composeBlock2.possiblePreviousComposeBlocks.add( composeBlock1 );
		composeBlock2.possiblePreviousComposeBlocks.add( composeBlock5 );
		composeBlock2.possibleNextComposeBlocks.add( composeBlock3 );
		composeBlock2.possibleNextComposeBlocks.add( composeBlock1 );

		composeBlock3.possiblePreviousComposeBlocks.add( composeBlock2 );
		composeBlock3.possibleNextComposeBlocks.add( composeBlock4 );
		composeBlock3.possibleNextComposeBlocks.add( composeBlock5 );

		composeBlock4.possiblePreviousComposeBlocks.add( composeBlock3 );
		composeBlock4.possiblePreviousComposeBlocks.add( composeBlock1 );
		composeBlock4.possibleNextComposeBlocks.add( composeBlock5 );

		composeBlock5.possiblePreviousComposeBlocks.add( composeBlock4 );
		composeBlock5.possiblePreviousComposeBlocks.add( composeBlock1 );
		composeBlock5.possiblePreviousComposeBlocks.add( composeBlock3 );
		composeBlock5.possibleNextComposeBlocks.add( null );
		composeBlock5.possibleNextComposeBlocks.add( composeBlock2 );

		List<ComposeBlock> composeBlockList = new ArrayList<>();
		composeBlockList.add( composeBlock1 );
		composeBlockList.add( composeBlock2 );
		composeBlockList.add( composeBlock3 );
		composeBlockList.add( composeBlock4 );
		composeBlockList.add( composeBlock5 );

		return composeBlockList;
	}

	private model.Lexicon getDefaultLexicon() {
		List<model.melody.Melody> melodyList1 = new ArrayList<>();
		model.MusicBlock musicBlock1 = new model.MusicBlock( null,
				new model.melody.Melody(
						new jm.music.data.Note( C4, WHOLE_NOTE, 0, 0 ),
						new jm.music.data.Note( D4, HALF_NOTE, 0, 0 ),
						new jm.music.data.Note( E4, EIGHTH_NOTE, 0, 0 ),
						new jm.music.data.Note( DS4, EIGHTH_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						new jm.music.data.Note( B4, WHOLE_NOTE, 0, 0 ),
						new jm.music.data.Note( A4, DOTTED_HALF_NOTE, 0, 0 ) ) );

		model.MusicBlock musicBlock2 = new model.MusicBlock( null,
				new model.melody.Melody(
						new jm.music.data.Note( C4, DOTTED_HALF_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						new jm.music.data.Note( G4, QUARTER_NOTE, 0, 0 ),
						new jm.music.data.Note( GS4, QUARTER_NOTE, 0, 0 ),
						new jm.music.data.Note( A4, QUARTER_NOTE, 0, 0 ) ) );

		model.MusicBlock musicBlock3 = new model.MusicBlock( null,
				new model.melody.Melody(
						new jm.music.data.Note( B4, EIGHTH_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						new jm.music.data.Note( B4, SIXTEENTH_NOTE, 0, 0 ),
						new jm.music.data.Note( B4, SIXTEENTH_NOTE, 0, 0 ) ) );

		model.MusicBlock musicBlock4 = new model.MusicBlock( null,
				new model.melody.Melody(
						new jm.music.data.Note( CS4, WHOLE_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ),
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ),
						new jm.music.data.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ) ) );

		model.MusicBlock musicBlock5 = new model.MusicBlock( null,
				new model.melody.Melody(
						new jm.music.data.Note( E4, DOTTED_EIGHTH_NOTE, 0, 0 ) ),
				new model.melody.Melody(
						new jm.music.data.Note( G4, SIXTEENTH_NOTE, 0, 0 ),
						new jm.music.data.Note( F4, SIXTEENTH_NOTE, 0, 0 ),
						new jm.music.data.Note( E4, SIXTEENTH_NOTE, 0, 0 ) ) );

		musicBlock1.setPrevious( null );
		musicBlock1.setNext( musicBlock2 );
		musicBlock2.setPrevious( musicBlock1 );
		musicBlock2.setNext( musicBlock3 );
		musicBlock3.setPrevious( musicBlock2 );
		musicBlock3.setNext( musicBlock4 );
		musicBlock4.setPrevious( musicBlock3 );
		musicBlock4.setNext( musicBlock5 );
		musicBlock5.setPrevious( musicBlock4 );
		musicBlock5.setNext( null );

		model.ComposeBlock composeBlock1 = new model.ComposeBlock( musicBlock1 );
		model.ComposeBlock composeBlock2 = new model.ComposeBlock( musicBlock2 );
		model.ComposeBlock composeBlock3 = new model.ComposeBlock( musicBlock3 );
		model.ComposeBlock composeBlock4 = new model.ComposeBlock( musicBlock4 );
		model.ComposeBlock composeBlock5 = new model.ComposeBlock( musicBlock5 );

		composeBlock1.getPossiblePreviousComposeBlocks().add( null );
		composeBlock1.getPossiblePreviousComposeBlocks().add( composeBlock2 );
		composeBlock1.getPossibleNextComposeBlocks().add( composeBlock2 );
		composeBlock1.getPossibleNextComposeBlocks().add( composeBlock4 );
		composeBlock1.getPossibleNextComposeBlocks().add( composeBlock5 );

		composeBlock2.getPossiblePreviousComposeBlocks().add( composeBlock1 );
		composeBlock2.getPossiblePreviousComposeBlocks().add( composeBlock5 );
		composeBlock2.getPossibleNextComposeBlocks().add( composeBlock3 );
		composeBlock2.getPossibleNextComposeBlocks().add( composeBlock1 );

		composeBlock3.getPossiblePreviousComposeBlocks().add( composeBlock2 );
		composeBlock3.getPossibleNextComposeBlocks().add( composeBlock4 );
		composeBlock3.getPossibleNextComposeBlocks().add( composeBlock5 );

		composeBlock4.getPossiblePreviousComposeBlocks().add( composeBlock3 );
		composeBlock4.getPossiblePreviousComposeBlocks().add( composeBlock1 );
		composeBlock4.getPossibleNextComposeBlocks().add( composeBlock5 );

		composeBlock5.getPossiblePreviousComposeBlocks().add( composeBlock4 );
		composeBlock5.getPossiblePreviousComposeBlocks().add( composeBlock1 );
		composeBlock5.getPossiblePreviousComposeBlocks().add( composeBlock3 );
		composeBlock5.getPossibleNextComposeBlocks().add( null );
		composeBlock5.getPossibleNextComposeBlocks().add( composeBlock2 );

		List<model.ComposeBlock> composeBlockList = new ArrayList<>();
		composeBlockList.add( composeBlock1 );
		composeBlockList.add( composeBlock2 );
		composeBlockList.add( composeBlock3 );
		composeBlockList.add( composeBlock4 );
		composeBlockList.add( composeBlock5 );

		model.Lexicon persistanceLexicon = new model.Lexicon( composeBlockList );
		return persistanceLexicon;
	}

}
