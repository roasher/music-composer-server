package ru.pavelyurkin.musiccomposer.core.persistance;

import ru.pavelyurkin.musiccomposer.core.model.Lexicon;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.*;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory.BlockMovementFactory;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory.CompositionInfoFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.BlockMovement;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory.MelodyFactory;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.factory.NoteFactory;

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

	public Lexicon convertPersistComposeBlockList( List<ComposeBlock> persistanceComposeBlocks ) {
		// persitance block can have id seq with gaps or start not with 0
		Map<ComposeBlock, Integer> remap = new HashMap<>();
		int id = 0;
		for ( ComposeBlock persistanceComposeBlock : persistanceComposeBlocks ) {
			remap.put( persistanceComposeBlock, id++ );
		}

		List<ru.pavelyurkin.musiccomposer.core.model.ComposeBlock> composeBlocks = new ArrayList<>();
		Map<Integer, List<Integer>> possibleNexts = new HashMap<>();

		for ( int persistanceComposeBlockNumber = 0; persistanceComposeBlockNumber < persistanceComposeBlocks.size(); persistanceComposeBlockNumber++ ) {
			ComposeBlock persistanceComposeBlock = persistanceComposeBlocks.get( persistanceComposeBlockNumber );
			ru.pavelyurkin.musiccomposer.core.model.ComposeBlock composeBlock = convertComposeBlock( persistanceComposeBlock );
			composeBlocks.add( composeBlock );
			List<Integer> possibleNextToCurrent = persistanceComposeBlock.possibleNextComposeBlocks.stream().map( remap::get ).collect( Collectors.toList() );
			possibleNexts.put( persistanceComposeBlockNumber, possibleNextToCurrent );
		}

		for ( int composeBlockNumber = 0; composeBlockNumber < composeBlocks.size(); composeBlockNumber++ ) {
			ru.pavelyurkin.musiccomposer.core.model.ComposeBlock composeBlock = composeBlocks.get( composeBlockNumber );
			for ( Integer possibleNextComposeBlockNumber : possibleNexts.get( composeBlockNumber ) ) {
				ru.pavelyurkin.musiccomposer.core.model.ComposeBlock possibleNextComposeBlock = composeBlocks.get( possibleNextComposeBlockNumber );
				composeBlock.getPossibleNextComposeBlocks().add( possibleNextComposeBlock );
				// we should check if we need to add previous at first place
				if ( composeBlockNumber + 1 != possibleNextComposeBlockNumber ) {
					possibleNextComposeBlock.getPossiblePreviousComposeBlocks().add( composeBlock );
				} else {
					possibleNextComposeBlock.getPossiblePreviousComposeBlocks().add( 0, composeBlock );
				}
			}
		}

		return new Lexicon( composeBlocks, possibleNexts );
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

	public ru.pavelyurkin.musiccomposer.core.model.ComposeBlock convertComposeBlock( ComposeBlock persistanceComposeBlock ) {
		ru.pavelyurkin.musiccomposer.core.model.ComposeBlock musicBlock = new ru.pavelyurkin.musiccomposer.core.model.ComposeBlock( persistanceComposeBlock.startTime,
				convertCompositionInfo( persistanceComposeBlock.compositionInfo ), convertMelodyList( persistanceComposeBlock.melodies ),
				convertBlockMovement( persistanceComposeBlock.blockMovementFromPreviousToThis ) );
		return musicBlock;
	}

	public ComposeBlock convertComposeBlock( ru.pavelyurkin.musiccomposer.core.model.ComposeBlock composeBlock ) {
		ComposeBlock persistanceMusicBlock = new ComposeBlock( composeBlock.getStartTime(), convertCompositionInfo( composeBlock.getCompositionInfo() ),
				convertToPersistMelodyList( composeBlock.getMelodyList() ), convertBlockMovement( composeBlock.getBlockMovementFromPreviousToThis() ) );
		return persistanceMusicBlock;
	}

	public BlockMovement convertBlockMovement( ru.pavelyurkin.musiccomposer.core.model.BlockMovement blockMovement ) {
		BlockMovement persistBlockMovement = null;
		if ( blockMovement != null ) {
			persistBlockMovement = blockMovementFactory.getInstance( blockMovement.getVoiceMovements() );
		}
		return persistBlockMovement;
	}

	public ru.pavelyurkin.musiccomposer.core.model.BlockMovement convertBlockMovement( BlockMovement persitanceBlockMovement ) {
		ru.pavelyurkin.musiccomposer.core.model.BlockMovement blockMovement = null;
		if ( persitanceBlockMovement != null ) {
			List<Integer> voiceMovements = new ArrayList<>();
			for ( String str : StringUtils.split( persitanceBlockMovement.voiceMovements, "," ) ) {
				voiceMovements.add( Integer.valueOf( str ) );
			}
			blockMovement = new ru.pavelyurkin.musiccomposer.core.model.BlockMovement( voiceMovements );
		}
		return blockMovement;
	}

	public List<ru.pavelyurkin.musiccomposer.core.model.melody.Melody> convertMelodyList( List<Melody> persistanceMelodyList ) {
		List<ru.pavelyurkin.musiccomposer.core.model.melody.Melody> melodyList = new ArrayList<>();
		for ( Melody melody : persistanceMelodyList ) {
			melodyList.add( convertMelody( melody ) );
		}
		return melodyList;
	}

	public List<Melody> convertToPersistMelodyList( List<ru.pavelyurkin.musiccomposer.core.model.melody.Melody> melodyList ) {
		List<Melody> persistanceMelodyList = new ArrayList<>();
		for ( ru.pavelyurkin.musiccomposer.core.model.melody.Melody melody : melodyList ) {
			persistanceMelodyList.add( convertMelody( melody ) );
		}
		return persistanceMelodyList;
	}

	public CompositionInfo convertCompositionInfo( ru.pavelyurkin.musiccomposer.core.persistance.jpa.CompositionInfo persistanceCompositionInfo ) {
		if ( persistanceCompositionInfo != null ) {
			CompositionInfo compositionInfo = new CompositionInfo();
			compositionInfo.setAuthor( persistanceCompositionInfo.author );
			compositionInfo.setMetre( null );
			compositionInfo.setTempo( persistanceCompositionInfo.tempo );
			compositionInfo.setTitle( persistanceCompositionInfo.title );
			return compositionInfo;
		} else {
			return null;
		}
	}

	public ru.pavelyurkin.musiccomposer.core.persistance.jpa.CompositionInfo convertCompositionInfo( CompositionInfo compositionInfo ) {
		if ( compositionInfo != null ) {
			ru.pavelyurkin.musiccomposer.core.persistance.jpa.CompositionInfo persistanceCompositionInfo = compositionInfoFactory
					.getInstance( compositionInfo.getAuthor(), compositionInfo.getTitle(), compositionInfo.getTempo() );
			return persistanceCompositionInfo;
		} else {
			return null;
		}
	}

	public ru.pavelyurkin.musiccomposer.core.model.melody.Melody convertMelody( Melody persistanceMelody ) {
		ru.pavelyurkin.musiccomposer.core.model.melody.Melody melody = new ru.pavelyurkin.musiccomposer.core.model.melody.Melody( convertNoteList( persistanceMelody.notes ) );
		melody.setForm( new Form( persistanceMelody.form ) );
		return melody;
	}

	public Melody convertMelody( ru.pavelyurkin.musiccomposer.core.model.melody.Melody melody ) {
		Melody persistMelody = melodyFactory.getInstance( convertToPersistNoteList( melody.getNoteList() ), melody.getForm().getValue() );
		persistMelody.form = melody.getForm().getValue();
		return persistMelody;
	}

	public List<jm.music.data.Note> convertNoteList( List<Note> persistanceNoteList ) {
		List<jm.music.data.Note> noteList = new ArrayList<>();
		for ( Note note : persistanceNoteList ) {
			noteList.add( convertNote( note ) );
		}
		return noteList;
	}

	public List<Note> convertToPersistNoteList( List<jm.music.data.Note> noteList ) {
		List<Note> persistanceNoteList = new ArrayList<>();
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
