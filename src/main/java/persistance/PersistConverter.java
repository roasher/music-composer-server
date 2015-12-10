package persistance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import persistance.model.blockMovement.BlockMovement;
import persistance.model.ComposeBlock;
import persistance.model.blockMovement.BlockMovementFactory;
import persistance.model.compositionInfo.CompositionInfo;
import persistance.model.compositionInfo.CompositionInfoFactory;
import persistance.model.melody.Melody;
import persistance.model.melody.MelodyFactory;
import persistance.model.note.Note;
import persistance.model.note.NoteFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Component
public class PersistConverter {

	@Autowired
	private NoteFactory noteFactory;
	@Autowired
	private MelodyFactory melodyFactory;
	@Autowired
	private CompositionInfoFactory compositionInfoFactory;
	@Autowired
	private BlockMovementFactory blockMovementFactory;

	public model.Lexicon convertLexicon( Lexicon lexicon ) {
		return new model.Lexicon( convertPersistComposeBlockList( lexicon.composeBlockList ) );
	}

	public Lexicon convertLexicon( model.Lexicon lexicon ) {
		Lexicon persitanceLexicon = new Lexicon( convertComposeBlockList( lexicon.getComposeBlockList() ) );
		persitanceLexicon.minRhythmValue = lexicon.getMinRhythmValue();
		return persitanceLexicon;
	}

	public List<model.ComposeBlock> convertPersistComposeBlockList( List<ComposeBlock> persistanceComposeBlockList ) {

		List<model.ComposeBlock> composeBlockList = new ArrayList<>(  );
		for ( ComposeBlock persistanceComposeBlock : persistanceComposeBlockList ) {
			composeBlockList.add( convertComposeBlock( persistanceComposeBlock ) );
		}

		for ( int composeBlockNumber = 0; composeBlockNumber < persistanceComposeBlockList.size(); composeBlockNumber++ ) {
			List<ComposeBlock> persistancePossibleNextList = persistanceComposeBlockList.get( composeBlockNumber ).possibleNextComposeBlocks;
			for ( ComposeBlock persistancePossibleNext : persistancePossibleNextList ) {
				model.ComposeBlock composeBlock = composeBlockList.get( composeBlockNumber );
				int index = persistanceComposeBlockList.lastIndexOf( persistancePossibleNext );
				composeBlock.getPossibleNextComposeBlocks().add( persistancePossibleNext != null ? composeBlockList.get( index ) : null );
			}

			List<ComposeBlock> persistancePossiblePrevoiusList = persistanceComposeBlockList.get( composeBlockNumber ).possiblePreviousComposeBlocks;
			for ( ComposeBlock persistancePossiblePrevious : persistancePossiblePrevoiusList ) {
				model.ComposeBlock composeBlock = composeBlockList.get( composeBlockNumber );
				int index = persistanceComposeBlockList.lastIndexOf( persistancePossiblePrevious );
				composeBlock.getPossiblePreviousComposeBlocks().add( persistancePossiblePrevious != null ? composeBlockList.get( index ) : null );
			}
		}

		return composeBlockList;
	}

	public List<ComposeBlock> convertComposeBlockList( List<model.ComposeBlock> composeBlockList ) {

		List<ComposeBlock> persistanceComposeBlockList = new ArrayList<>(  );
		for ( model.ComposeBlock composeBlock : composeBlockList ) {
			persistanceComposeBlockList.add( convertComposeBlock( composeBlock ) );
		}

		for ( int composeBlockNumber = 0; composeBlockNumber < composeBlockList.size(); composeBlockNumber++ ) {
			List<model.ComposeBlock> possibleNextList = composeBlockList.get( composeBlockNumber ).getPossibleNextComposeBlocks();
			for ( model.ComposeBlock possibleNext : possibleNextList ) {
				ComposeBlock composeBlock = persistanceComposeBlockList.get( composeBlockNumber );
				int index = composeBlockList.lastIndexOf( possibleNext );
				composeBlock.possibleNextComposeBlocks.add( possibleNext != null ? persistanceComposeBlockList.get( index ) : null );
			}

			List<model.ComposeBlock> possiblePrevoiusList = composeBlockList.get( composeBlockNumber ).getPossiblePreviousComposeBlocks();
			for ( model.ComposeBlock possiblePrevious : possiblePrevoiusList ) {
				ComposeBlock composeBlock = persistanceComposeBlockList.get( composeBlockNumber );
				int index = composeBlockList.lastIndexOf( possiblePrevious );
				composeBlock.possiblePreviousComposeBlocks.add( possiblePrevious != null ? persistanceComposeBlockList.get( index ) : null );
			}
		}

		return persistanceComposeBlockList;
	}

	public model.ComposeBlock convertComposeBlock( ComposeBlock persistanceComposeBlock ) {
		model.ComposeBlock musicBlock = new model.ComposeBlock(
				persistanceComposeBlock.startTime,
				convertCompositionInfo( persistanceComposeBlock.compositionInfo ),
				convertMelodyList( persistanceComposeBlock.melodyList ),
				convertBlockMovement( persistanceComposeBlock.blockMovementFromPreviousToThis ) );
		return musicBlock;
	}

	public ComposeBlock convertComposeBlock( model.ComposeBlock composeBlock ) {
		ComposeBlock persistanceMusicBlock = new ComposeBlock(
				composeBlock.getStartTime(),
				convertCompositionInfo( composeBlock.getCompositionInfo() ),
				convertToPersistMelodyList( composeBlock.getMelodyList() ),
				convertBlockMovement( composeBlock.getBlockMovementFromPreviousToThis() )
		);
		return persistanceMusicBlock;
	}

	public BlockMovement convertBlockMovement( model.BlockMovement blockMovement ) {
		BlockMovement persistBlockMovement = null;
		if ( blockMovement != null ) {
			persistBlockMovement = blockMovementFactory.getInstance( blockMovement.getTopVoiceMelodyMovement(), blockMovement.getBottomVoiceMelodyMovement() );
		}
		return persistBlockMovement;
	}

	public model.BlockMovement convertBlockMovement( BlockMovement persitanceBlockMovement ) {
		model.BlockMovement blockMovement = null;
		if ( persitanceBlockMovement != null ) {
			blockMovement = new model.BlockMovement( persitanceBlockMovement.topVoiceMovement, persitanceBlockMovement.bottomVoiceMovement );
		}
		return blockMovement;
	}

	public List<model.melody.Melody> convertMelodyList( List<Melody> persistanceMelodyList ) {
		List<model.melody.Melody> melodyList = new ArrayList<>(  );
		for ( Melody melody : persistanceMelodyList ) {
			melodyList.add( convertMelody( melody ) );
		}
		return melodyList;
	}

	public List<Melody> convertToPersistMelodyList( List<model.melody.Melody> melodyList ) {
		List<Melody> persistanceMelodyList = new ArrayList<>(  );
		for (model.melody.Melody melody : melodyList ) {
			persistanceMelodyList.add( convertMelody( melody ) );
		}
		return persistanceMelodyList;
	}

	public model.composition.CompositionInfo convertCompositionInfo( CompositionInfo persistanceCompositionInfo ) {
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

	public CompositionInfo convertCompositionInfo( model.composition.CompositionInfo compositionInfo ) {
		if ( compositionInfo != null ) {
			CompositionInfo persistanceCompositionInfo = compositionInfoFactory.getInstance( compositionInfo.getAuthor(), compositionInfo.getTitle(), compositionInfo.getTempo() );
			return persistanceCompositionInfo;
		} else {
			return null;
		}
	}

	public model.melody.Melody convertMelody( Melody persistanceMelody ) {
		model.melody.Melody melody = new model.melody.Melody( convertNoteList( persistanceMelody.noteList ) );
		melody.setForm( new model.melody.Form( persistanceMelody.form ) );
		return melody;
	}

	public Melody convertMelody( model.melody.Melody melody ) {
		Melody persistMelody = melodyFactory.getInstance(convertToPersistNoteList(melody.getNoteList()));
		persistMelody.form = melody.getForm().getValue();
		return persistMelody;
	}

	public List<jm.music.data.Note> convertNoteList( List<Note> persistanceNoteList ) {
		List<jm.music.data.Note> noteList = new ArrayList<>(  );
		for ( Note note : persistanceNoteList ) {
			noteList.add( convertNote( note ) );
		}
		return noteList;
	}

	public List<Note> convertToPersistNoteList( List<jm.music.data.Note> noteList ) {
		List<Note> persistanceNoteList = new ArrayList<>(  );
		for ( jm.music.data.Note note : noteList ) {
			persistanceNoteList.add( convertNote( note ) );
		}
		return persistanceNoteList;
	}

	public jm.music.data.Note convertNote( Note persistanceNote ) {
		return new jm.music.data.Note( persistanceNote.pitch, persistanceNote.rhythmValue, persistanceNote.dynamic, persistanceNote.pan );
	}

	public Note convertNote( jm.music.data.Note note ) {
		return noteFactory.getInstance( note.getPitch(), note.getRhythmValue(), note.getDynamic(), note.getPan() );
	}
}
