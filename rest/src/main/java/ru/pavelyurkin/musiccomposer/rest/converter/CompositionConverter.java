package ru.pavelyurkin.musiccomposer.rest.converter;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionFrontDTO;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;
import ru.pavelyurkin.musiccomposer.rest.dto.CompositionDTO;
import ru.pavelyurkin.musiccomposer.rest.dto.Header;
import ru.pavelyurkin.musiccomposer.rest.dto.NoteDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Converts composition to compositionDTO
 */
@Component
public class CompositionConverter {

	public CompositionDTO convert( CompositionFrontDTO compositionFrontDTO ) {
		Vector partList = compositionFrontDTO.getPartList();
		List<NoteDTO> noteDTOs = IntStream.range( 0, partList.size() )
				.mapToObj( i -> Pair.of( i, partList.get( i ) ) )
				.flatMap( pair -> convert( ( List<Note> )
						( ( Part ) pair.getRight() ).getPhraseList()
								.stream()
								.flatMap( phrase -> ( ( Phrase ) phrase ).getNoteList().stream() )
								.collect( Collectors.toList() ),
						compositionFrontDTO.getPreviousSumRhythmValues(), pair.getLeft() )
						.stream() )
				.collect( Collectors.toList() );
		CompositionDTO compositionDTO = new CompositionDTO();
		compositionDTO.setNotes( noteDTOs );
		Header header = new Header();
		header.setTempo( compositionFrontDTO.getTempo() );
		header.setTimeSignature( Arrays.asList( compositionFrontDTO.getNumerator(), compositionFrontDTO.getDenominator() ) );
		compositionDTO.setHeader( header );
		return compositionDTO;
	}

	private List<NoteDTO> convert( List<Note> compositionNotes, double previousRhythmValueWhereToStartFrom,
			int partNumber ) {
		List<NoteDTO> noteDTOs = new ArrayList<>();
		for ( int noteNumber = 0; noteNumber < compositionNotes.size(); noteNumber++ ) {
			Note compositionNote = compositionNotes.get( noteNumber );
			NoteDTO noteDTO = convert( compositionNote, previousRhythmValueWhereToStartFrom, partNumber );
			noteDTOs.add( noteDTO );
			previousRhythmValueWhereToStartFrom += compositionNote.getRhythmValue();
		}
		return noteDTOs;
	}

	private String convertDurationToStringDuration( double duration ) {
		return Math.round( duration * 24 ) + "i";
	}

	private NoteDTO convert( Note note, double previousSumRhythmValues, int partNumber ) {
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setPitch( note.getPitch() );
		noteDTO.setVelocity( 1 );
		noteDTO.setStringRepresentation( ModelUtils.getNoteNameByPitch( note.getPitch() ) );
		noteDTO.setTime( convertDurationToStringDuration( previousSumRhythmValues ) );
		noteDTO.setDuration( convertDurationToStringDuration( note.getRhythmValue() ) );
		noteDTO.setPartNumber( partNumber );
		return noteDTO;
	}
}
