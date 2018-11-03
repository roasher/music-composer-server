package ru.pavelyurkin.musiccomposer.core.utils;

import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;

@Component
@RequiredArgsConstructor
public class InstrumentPartToPartConverter implements Converter<InstrumentPart, Part> {

	private final CompositionParser compositionParser;

	@Override
	public Part convert( InstrumentPart instrumentPart ) {
		Part part = new Part();
		instrumentPart.getNoteGroups().stream()
				.forEach( noteGroup -> {
					if ( noteGroup instanceof NewMelody ) {
						NewMelody newMelody = ( NewMelody ) noteGroup;
						Phrase phrase = new Phrase( newMelody.clone().getNotes().toArray( new Note[] {} ) );
						phrase.setAppend( true );
						part.add( phrase );
					} else {
						Chord chord = ( Chord ) noteGroup;
						int[] pitches = chord.getPitches().stream().mapToInt( Integer::intValue ).toArray();
						CPhrase cPhrase = new CPhrase();
						cPhrase.setAppend( true );
						cPhrase.addChord( pitches, chord.getRhythmValue() );
						part.addCPhrase( cPhrase );
					}
				} );
		return part;
	}

	public InstrumentPart convertTo( Part part ) {
		return compositionParser.convert( part );
	}
}
