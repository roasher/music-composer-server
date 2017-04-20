package ru.pavelyurkin.musiccomposer.rest.dto;

import java.util.List;

/**
 * DTO to be send to the front
 */
public class CompositionDTO {

	private List<List<NoteDTO>> notes;

	public List<List<NoteDTO>> getNotes() {
		return notes;
	}

	public void setNotes( List<List<NoteDTO>> notes ) {
		this.notes = notes;
	}
}
