package ru.pavelyurkin.musiccomposer.rest.http;

import jm.JMC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pavelyurkin.musiccomposer.core.persistance.jpa.Note;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MidiProvider {

	@RequestMapping( path = "/getNotes", method = RequestMethod.GET )
	public List<Note> getNotes( @RequestParam int number ) {
		List<Note> notes = new ArrayList<>( number );
		for ( int noteNumber = 0; noteNumber < number; noteNumber++ ) {
			notes.add( new Note( 60 + noteNumber, JMC.WHOLE_NOTE, 0, 0 ) );
		}
		return notes;
	}
}
