package ru.pavelyurkin.musiccomposer.rest.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO to be send to the front
 */
@Data
public class CompositionDTO {

	private Header header;
	private List<NoteDTO> notes;

}
