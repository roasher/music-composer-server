package persistance.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jm.JMC.*;
import static org.junit.Assert.assertEquals;
import static persistance.model.PersistConverter.convertLexicon;

/**
 * Created by pyurkin on 05.05.2015.
 */
public class PersistConverterTest {

	@Test public void convert() {
		persistance.model.Lexicon persistanceLexicon = getDefaultPersitanceLexicon();
		model.Lexicon lexicon = getDefaultLexicon();
		persistance.model.Lexicon convertedLexicon = convertLexicon( lexicon );
		assertEquals( lexicon, convertLexicon( persistanceLexicon ) );
		assertEquals( persistanceLexicon, convertedLexicon );
	}

	@Test public void doubleConvert() {
		persistance.model.Lexicon persistanceLexicon = getDefaultPersitanceLexicon();
		assertEquals( persistanceLexicon, convertLexicon( convertLexicon( persistanceLexicon ) ) );

		model.Lexicon lexicon = getDefaultLexicon();
		assertEquals( lexicon, convertLexicon( convertLexicon( lexicon ) ) );
	}

	private persistance.model.Lexicon getDefaultPersitanceLexicon() {
		persistance.model.ComposeBlock composeBlock1 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
					new persistance.model.Melody[] {
							new persistance.model.Melody(
									new persistance.model.Note( C4, WHOLE_NOTE, 0, 0 ),
									new persistance.model.Note( D4, HALF_NOTE, 0, 0 ),
									new persistance.model.Note( E4, EIGHTH_NOTE, 0, 0 ),
									new persistance.model.Note( DS4, EIGHTH_NOTE, 0, 0 ) ),
							new persistance.model.Melody(
									new persistance.model.Note( B4, WHOLE_NOTE, 0, 0 ),
									new persistance.model.Note( A4, DOTTED_HALF_NOTE, 0, 0 ) ) } ),
				null
				);

		persistance.model.ComposeBlock composeBlock2 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
						new persistance.model.Melody[] {
								new persistance.model.Melody(
										new persistance.model.Note( C4, DOTTED_HALF_NOTE, 0, 0 ) ),
								new persistance.model.Melody(
										new persistance.model.Note( G4, QUARTER_NOTE, 0, 0 ),
										new persistance.model.Note( GS4, QUARTER_NOTE, 0, 0 ),
										new persistance.model.Note( A4, QUARTER_NOTE, 0, 0 ) ) } ),
				new persistance.model.BlockMovement( G4-A4, C4-DS4 )
		);

		persistance.model.ComposeBlock composeBlock3 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
						new persistance.model.Melody[] {
								new persistance.model.Melody(
										new persistance.model.Note( B4, EIGHTH_NOTE, 0, 0 ) ),
								new persistance.model.Melody(
										new persistance.model.Note( B4, SIXTEENTH_NOTE, 0, 0 ),
										new persistance.model.Note( B4, SIXTEENTH_NOTE, 0, 0 ) ) } ),
				new persistance.model.BlockMovement( B4-A4, B4-C4)
		);

		persistance.model.ComposeBlock composeBlock4 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
						new persistance.model.Melody[] {
								new persistance.model.Melody(
										new persistance.model.Note( CS4, WHOLE_NOTE, 0, 0 ) ),
								new persistance.model.Melody(
										new persistance.model.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ),
										new persistance.model.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ),
										new persistance.model.Note( C3, HALF_NOTE_TRIPLET, 0, 0 ) ) } ),
				new persistance.model.BlockMovement( CS4-B4, C3-B4 )
		);

		persistance.model.ComposeBlock composeBlock5 = new ComposeBlock(
				0,
				null,
				Arrays.asList(
						new persistance.model.Melody[] {
								new persistance.model.Melody(
										new persistance.model.Note( E4, DOTTED_EIGHTH_NOTE, 0, 0 ) ),
								new persistance.model.Melody(
										new persistance.model.Note( G4, SIXTEENTH_NOTE, 0, 0 ),
										new persistance.model.Note( F4, SIXTEENTH_NOTE, 0, 0 ),
										new persistance.model.Note( E4, SIXTEENTH_NOTE, 0, 0 ) ) } ),
				new persistance.model.BlockMovement( G4-CS4, E4-C3)
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

		persistance.model.Lexicon persistanceLexicon = new Lexicon( composeBlockList, EIGHTH_NOTE );
		return persistanceLexicon;
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
