package ru.pavelyurkin.musiccomposer.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO to be send to the front
 * <p>
 * {
 * midi: Number,               // midi number, e.g. 60
 * time: Number,               // time in seconds
 * note: String,               // note name, e.g. "C4"
 * velocity: Number,           // normalized 0-1 velocity
 * duration: Number,           // duration between noteOn and noteOff
 * }
 */
@Data
public class NoteDTO {

	@JsonProperty("midiNote")
	private int pitch;
	@JsonProperty("note")
	private String stringRepresentation;

	private String time;
	private int velocity;
	private String duration;
	private int partNumber;
}
