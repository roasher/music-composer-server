package ru.pavelyurkin.musiccomposer.core.utils;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.Chord;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NewMelody;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NoteGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getLast;

/**
 * Slices composition into smallest parts
 * Created by pyurkin on 10.12.14.
 */
@Component
public class CompositionRecombinator {

	/**
	 * Recombines melody lists.
	 * In the result, all melodies has only one note within
	 * @param composition
	 * @return
	 */
	public List< InstrumentPart > slice( Composition composition ) {
		return (List< InstrumentPart >) composition.getPartList().stream()
				.map( part -> convert( ( Part ) part ) )
				.collect( Collectors.toList() );
	}

	private InstrumentPart convert( Part part ) {
		List<Phrase> phrases = part.getPhraseList();
		List<Double> rhythmEdges = phrases.stream()
				.flatMap( phrase -> getRhythmEdgeList( phrase ).stream() )
				.distinct()
				.sorted().collect( Collectors.toList() );

		double previousEdge = 0;
		InstrumentPart instrumentPart = new InstrumentPart();
		instrumentPart.setInstrument( part.getInstrument() );
		for ( double edge : rhythmEdges ) {
			List<Note> notesAtATime = phrases.stream()
					.flatMap( phrase -> getNotesAtTheEdge( phrase, edge ).stream() )
					.collect( Collectors.toList() );
			List<NoteGroup> noteGroups = instrumentPart.getNoteGroups();
			if (notesAtATime.size() == 1) {
				Note note = notesAtATime.get( 0 );
				if ( noteGroups.isEmpty() || getLast( noteGroups ) instanceof Chord ) {
					noteGroups.add( new NewMelody( new Note( note.getPitch(), edge - previousEdge ) ) );
				} else {
					NewMelody melody = ( NewMelody ) getLast( noteGroups );
					melody.addNoteToTheEnd( note );
				}
			} else {
				List<Integer> chordToAdd = notesAtATime.stream().map( Note::getPitch ).collect( Collectors.toList() );
				if ( noteGroups.isEmpty() ||
						getLast( noteGroups ) instanceof NewMelody ||
						!( ( Chord ) getLast( noteGroups ) ).samePitches( chordToAdd ) ) {

					instrumentPart.getNoteGroups().add( new Chord(
							chordToAdd,
							edge - previousEdge ) );

				} else {
					Chord lastChord = ( Chord ) getLast( noteGroups );
					lastChord.setRhythmValue( lastChord.getRhythmValue() + edge - previousEdge );
				}
			}
			previousEdge = edge;
		}
		return instrumentPart;
	}

	/**
	 * Returns the edge list of phrases notes
	 * Sets first edge to rhythm value of the first note, and incrementing this value by
	 * rhythm value of all noteGroups one by one
	 * @param phrase
	 * @return
	 */
	public List< Double > getRhythmEdgeList( Phrase phrase ) {
		List< Double > edgeList = new ArrayList<>();
		if (phrase.getStartTime() > 0) edgeList.add( phrase.getStartTime() );
		double lastEdge = phrase.getStartTime();
		for ( int noteNumber = 0; noteNumber < phrase.size(); noteNumber ++ ) {
			Note note = phrase.getNote( noteNumber );
			edgeList.add( lastEdge + note.getRhythmValue() );
			lastEdge = edgeList.get( edgeList.size() - 1 );
		}
		return edgeList;
	}

	/**
	 * Returns note from phrase that sounds in particular time
	 * If input time is finish time to one and start time to another, the first one will be returned
	 * @param phrase - note array
	 * @param time
	 * @return
	 * TODO refactor using binary search
	 */
	public List<Note> getNotesAtTheEdge( Phrase phrase, double time ) {
		List<Note> out = new ArrayList<>(  );
		double startTime = phrase.getStartTime();
		for ( int currentNoteNumber = 0; currentNoteNumber < phrase.size(); currentNoteNumber ++ ) {
			double rhythm = phrase.getNote( currentNoteNumber ).getRhythmValue();
			if (Double.compare( rhythm, 0.0 ) == 0) {throw new RuntimeException( "Note with zero rhythm value" );}
			if ( startTime < time && time <= startTime + rhythm ) {
				out.add( ( Note ) phrase.getNoteList().get( currentNoteNumber ) );
			}
			startTime += rhythm;
		}
		return out;
	}
}
