package ru.pavelyurkin.musiccomposer.rest.converter;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.rest.dto.CompositionDTO;
import ru.pavelyurkin.musiccomposer.rest.dto.NoteDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts composition to compositionDTO
 */
@Component
public class CompositionConverter {

	public CompositionDTO convert( Composition composition ) {
		List<List<NoteDTO>> compositionNotes = (List<List<NoteDTO>>) composition.getPartList()
				.stream()
				.map( part -> ( ( Part ) part ).getPhraseList()
						.stream()
						.flatMap( phrase -> ( ( Phrase ) phrase ).getNoteList()
								.stream()
								.map( note -> convert( ( Note ) note ) ) )
						.collect( Collectors.toList() ) )
				.collect( Collectors.toList() );
		CompositionDTO compositionDTO = new CompositionDTO();
		compositionDTO.setNotes( compositionNotes );
		return compositionDTO;
	}

	private NoteDTO convert( Note note ) {
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setDynamic( note.getDynamic() );
		noteDTO.setPan( note.getPan() );
		noteDTO.setPitch( note.getPitch() );
		noteDTO.setRhythmValue( note.getRhythmValue() );
		return noteDTO;
	}
}
