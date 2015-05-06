package persistance.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
public class PersistConverter {

	public static model.Lexicon convertLexicon( persistance.model.Lexicon lexicon ) {

		List<model.ComposeBlock> composeBlockList = new ArrayList<>(  );
		for ( persistance.model.ComposeBlock persistanceComposeBlock : lexicon.composeBlockList ) {
			composeBlockList.add( convertComposeBlock( persistanceComposeBlock ) );
		}

		for ( int composeBlockNumber = 0; composeBlockNumber < lexicon.composeBlockList.size(); composeBlockNumber++ ) {
			List<persistance.model.ComposeBlock> persistancePossibleNextList = lexicon.composeBlockList.get( composeBlockNumber ).possibleNextComposeBlocks;
			for ( persistance.model.ComposeBlock persistancePossibleNext : persistancePossibleNextList ) {
				model.ComposeBlock composeBlock = composeBlockList.get( composeBlockNumber );
				int index = lexicon.composeBlockList.lastIndexOf( persistancePossibleNext );
				composeBlock.getPossibleNextComposeBlocks().add( composeBlockList.get( index ) );
			}

			List<persistance.model.ComposeBlock> persistancePossiblePrevoiusList = lexicon.composeBlockList.get( composeBlockNumber ).possiblePreviousComposeBlocks;
			for ( persistance.model.ComposeBlock persistancePossiblePrevious : persistancePossiblePrevoiusList ) {
				model.ComposeBlock composeBlock = composeBlockList.get( composeBlockNumber );
				int index = lexicon.composeBlockList.lastIndexOf( persistancePossiblePrevious );
				composeBlock.getPossiblePreviousComposeBlocks().add( composeBlockList.get( index ) );
			}
		}

		return new model.Lexicon( composeBlockList );
	}

	public static persistance.model.Lexicon convertLexicon( model.Lexicon lexicon ) {

		List<persistance.model.ComposeBlock> persistanceComposeBlockList = new ArrayList<>(  );
		for ( model.ComposeBlock composeBlock : lexicon.getComposeBlockList() ) {
			persistanceComposeBlockList.add( convertComposeBlock( composeBlock ) );
		}

		for ( int composeBlockNumber = 0; composeBlockNumber < lexicon.getComposeBlockList().size(); composeBlockNumber++ ) {
			List<model.ComposeBlock> possibleNextList = lexicon.getComposeBlockList().get( composeBlockNumber ).getPossibleNextComposeBlocks();
			for ( model.ComposeBlock possibleNext : possibleNextList ) {
				persistance.model.ComposeBlock composeBlock = persistanceComposeBlockList.get( composeBlockNumber );
				int index = lexicon.getComposeBlockList().lastIndexOf( possibleNext );
				composeBlock.possibleNextComposeBlocks.add( persistanceComposeBlockList.get( index ) );
			}

			List<model.ComposeBlock> possiblePrevoiusList = lexicon.getComposeBlockList().get( composeBlockNumber ).getPossiblePreviousComposeBlocks();
			for ( model.ComposeBlock possiblePrevious : possiblePrevoiusList ) {
				persistance.model.ComposeBlock composeBlock = persistanceComposeBlockList.get( composeBlockNumber );
				int index = lexicon.getComposeBlockList().lastIndexOf( possiblePrevious );
				composeBlock.possiblePreviousComposeBlocks.add( persistanceComposeBlockList.get( index ) );
			}
		}

		persistance.model.Lexicon persitanceLexicon = new persistance.model.Lexicon( persistanceComposeBlockList );
		persitanceLexicon.minRhythmValue = lexicon.getMinRhythmValue();
		return persitanceLexicon;
	}

	public static model.ComposeBlock convertComposeBlock( persistance.model.ComposeBlock persistanceComposeBlock ) {
		model.ComposeBlock musicBlock = new model.ComposeBlock(
				persistanceComposeBlock.rhythmValue,
				persistanceComposeBlock.startTime,
				convertCompositionInfo( persistanceComposeBlock.compositionInfo ),
				convertMelodyList( persistanceComposeBlock.melodyList ),
				persistanceComposeBlock.startIntervalPattern,
				convertBlockMovement( persistanceComposeBlock.blockMovementFromPreviousToThis ) );
		return musicBlock;
	}

	public static persistance.model.ComposeBlock convertComposeBlock( model.ComposeBlock composeBlock ) {
		List<persistance.model.Melody> melodyList = convertToPersistMelodyList( composeBlock.getMelodyList() );
		persistance.model.ComposeBlock persistanceMusicBlock = new persistance.model.ComposeBlock(
				composeBlock.getRhythmValue(),
				composeBlock.getStartTime(),
				convertCompositionInfo( composeBlock.getCompositionInfo() ),
				convertToPersistMelodyList( composeBlock.getMelodyList() ),
				composeBlock.getStartIntervalPattern(),
				convertBlockMovement( composeBlock.getBlockMovementFromPreviousToThis() )
		);
		return persistanceMusicBlock;
	}

	public static persistance.model.BlockMovement convertBlockMovement( model.BlockMovement blockMovement ) {
		//TODO impl
		return null;
	}

	public static model.BlockMovement convertBlockMovement( persistance.model.BlockMovement persitanceBlockMovement ) {
		//TODO impl
		return null;
	}

	public static List<model.melody.Melody> convertMelodyList( List<persistance.model.Melody> persistanceMelodyList ) {
		List<model.melody.Melody> melodyList = new ArrayList<>(  );
		for ( persistance.model.Melody melody : persistanceMelodyList ) {
			melodyList.add( convertMelody( melody ) );
		}
		return melodyList;
	}

	public static List<persistance.model.Melody> convertToPersistMelodyList( List<model.melody.Melody> melodyList ) {
		List<persistance.model.Melody> persistanceMelodyList = new ArrayList<>(  );
		for (model.melody.Melody melody : melodyList ) {
			persistanceMelodyList.add( convertMelody( melody ) );
		}
		return persistanceMelodyList;
	}

	public static model.composition.CompositionInfo convertCompositionInfo( persistance.model.CompositionInfo persistanceCompositionInfo ) {
		if ( persistanceCompositionInfo != null ) {
			model.composition.CompositionInfo compositionInfo = new model.composition.CompositionInfo();
			compositionInfo.setAuthor( persistanceCompositionInfo.author );
			compositionInfo.setMetre( null );
			compositionInfo.setTempo( persistanceCompositionInfo.tempo );
			compositionInfo.setTitle( persistanceCompositionInfo.title );
			return compositionInfo;
		} else {
			return null;
		}
	}

	public static persistance.model.CompositionInfo convertCompositionInfo( model.composition.CompositionInfo compositionInfo ) {
		if ( compositionInfo != null ) {
			persistance.model.CompositionInfo persistanceCompositionInfo = new persistance.model.CompositionInfo();
			persistanceCompositionInfo.author = compositionInfo.getAuthor();
			// not implemented yet
//			persistanceCompositionInfo.setMetre( compositionInfo );
			persistanceCompositionInfo.tempo = compositionInfo.getTempo();
			persistanceCompositionInfo.title = compositionInfo.getTitle();
			return persistanceCompositionInfo;
		} else {
			return null;
		}
	}

	public static model.melody.Melody convertMelody( persistance.model.Melody persistanceMelody ) {
		return new model.melody.Melody( convertNoteList( persistanceMelody.noteList ) );
	}

	public static persistance.model.Melody convertMelody( model.melody.Melody melody ) {
		return new persistance.model.Melody( convertToPersistNoteList( melody.getNoteList() ) );
	}

	public static List<jm.music.data.Note> convertNoteList( List<persistance.model.Note> persistanceNoteList ) {
		List<jm.music.data.Note> noteList = new ArrayList<>(  );
		for ( persistance.model.Note note : persistanceNoteList ) {
			noteList.add( convertNote( note ) );
		}
		return noteList;
	}

	public static List<persistance.model.Note> convertToPersistNoteList( List<jm.music.data.Note> noteList ) {
		List<persistance.model.Note> persistanceNoteList = new ArrayList<>(  );
		for ( jm.music.data.Note note : noteList ) {
			persistanceNoteList.add( convertNote( note ) );
		}
		return persistanceNoteList;
	}

	public static jm.music.data.Note convertNote( persistance.model.Note persistanceNote ) {
		return new jm.music.data.Note( persistanceNote.pitch, persistanceNote.rhythmValue, persistanceNote.dynamic, persistanceNote.pan );
	}

	public static persistance.model.Note convertNote( jm.music.data.Note note ) {
		return new persistance.model.Note( note.getPitch(), note.getRhythmValue(), note.getDynamic(), note.getPan() );
	}
}
