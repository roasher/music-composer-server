package persistance;

import model.Lexicon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import persistance.jpa.*;
import persistance.jpa.BlockMovement;
import persistance.jpa.ComposeBlock;
import persistance.jpa.factory.BlockMovementFactory;
import persistance.jpa.factory.CompositionInfoFactory;
import persistance.jpa.factory.MelodyFactory;
import persistance.jpa.factory.NoteFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public Lexicon convertPersistComposeBlockList( List<ComposeBlock> persistanceComposeBlockList ) {

		List<model.ComposeBlock> composeBlocks = new ArrayList<>(  );
		Map<Integer, List<Integer>> possibleNexts = new HashMap<>(  );
		for ( ComposeBlock persistanceComposeBlock : persistanceComposeBlockList ) {
			model.ComposeBlock composeBlock = convertComposeBlock( persistanceComposeBlock );
			composeBlocks.add( composeBlock );
			List<Integer> possibleNextToCurrent = persistanceComposeBlock.possibleNextComposeBlocks.stream()
					.map( composeBlock1 -> composeBlock1.getId().intValue() ).collect( Collectors.toList());
			possibleNexts.put( persistanceComposeBlock.getId().intValue(), possibleNextToCurrent );
		}

		for ( int composeBlockNumber = 0; composeBlockNumber < composeBlocks.size(); composeBlockNumber++ ) {
			model.ComposeBlock composeBlock = composeBlocks.get( composeBlockNumber );
			for ( Integer possibleNextComposeBlockNumber : possibleNexts.get( composeBlockNumber ) ) {
				model.ComposeBlock possibleNextComposeBlock = composeBlocks.get( possibleNextComposeBlockNumber );
				composeBlock.getPossibleNextComposeBlocks().add( possibleNextComposeBlock );
				// we should check if we need to add previous at first place
				if ( composeBlockNumber + 1 != possibleNextComposeBlockNumber ) {
					possibleNextComposeBlock.getPossiblePreviousComposeBlocks().add( composeBlock );
				} else {
					possibleNextComposeBlock.getPossiblePreviousComposeBlocks().add( 0, composeBlock );
				}
			}
		}

		return new Lexicon( composeBlocks, possibleNexts);
	}

	public List<ComposeBlock> convertComposeBlockList( Lexicon lexicon ) {

		List<ComposeBlock> persistanceComposeBlocks = lexicon.getComposeBlockList().stream().map( this::convertComposeBlock ).collect( Collectors.toList() );

		for ( int composeBlockNumber = 0; composeBlockNumber < persistanceComposeBlocks.size(); composeBlockNumber++ ) {
			ComposeBlock composeBlock = persistanceComposeBlocks.get( composeBlockNumber );
			for ( int possibleNextComposeBlockNumber : lexicon.getPossibleNextMusicBlockNumbers().get( composeBlockNumber ) ) {
				ComposeBlock possibleNextComposeBlock = persistanceComposeBlocks.get( possibleNextComposeBlockNumber );
				composeBlock.possibleNextComposeBlocks.add( possibleNextComposeBlock );
				// we should check if we need to add previous at first place
				if ( composeBlockNumber + 1 != possibleNextComposeBlockNumber ) {
					possibleNextComposeBlock.possiblePreviousComposeBlocks.add( composeBlock );
				} else {
					possibleNextComposeBlock.possiblePreviousComposeBlocks.add( 0, composeBlock );
				}
			}
		}

		return persistanceComposeBlocks;
	}

	public model.ComposeBlock convertComposeBlock( ComposeBlock persistanceComposeBlock ) {
		model.ComposeBlock musicBlock = new model.ComposeBlock(
				persistanceComposeBlock.startTime,
				convertCompositionInfo( persistanceComposeBlock.compositionInfo ),
				convertMelodyList( persistanceComposeBlock.melodies ),
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
		model.melody.Melody melody = new model.melody.Melody( convertNoteList( persistanceMelody.notes ) );
		melody.setForm( new model.melody.Form( persistanceMelody.form ) );
		return melody;
	}

	public Melody convertMelody( model.melody.Melody melody ) {
		Melody persistMelody = melodyFactory.getInstance( convertToPersistNoteList(melody.getNoteList()), melody.getForm().getValue() );
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
