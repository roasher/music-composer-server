package ru.pavelyurkin.musiccomposer.core.utils;

import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.notegroups.NoteGroup;

import java.util.*;

/**
 * Class handles all recombination between melody lists
 * Created by pyurkin on 10.12.14.
 */
public class Recombinator {

	/**
	 * Recombines melody lists.
	 * In the result, all melodies has only one note within
	 * @param instrumentPartsCollection
	 * @return
	 */
	public static List< List< InstrumentPart > > recombine( List< List<InstrumentPart> > instrumentPartsCollection ) {
		List< List< InstrumentPart > > recombineList = new ArrayList<>();
		for ( List< InstrumentPart > melodyBlock : instrumentPartsCollection ) {
			List< List< InstrumentPart > > melodyBlockList = recombineMelodyBlock( melodyBlock );
			recombineList.addAll( melodyBlockList );
		}
		return recombineList;
	}

	/**
	 * Recombining single melody block into smaller lists (blocks) .
	 * In result this ends with <b>list</b> of music blocks that has exact notes that input block has
	 * but melodies will be divided note by note so there will be no pitch changing in any of result melody blocks
	 *
	 * @param inputMelodyBlock
	 * @return
	 */
	public static List<List< InstrumentPart >> recombineMelodyBlock( List<InstrumentPart> inputMelodyBlock ) {

		// The edge set
		Set< Double > edgeSet = new HashSet<>(  );
		for ( InstrumentPart instrumentPart : inputMelodyBlock ) {
			edgeSet.addAll( getRhythmEdgeList( instrumentPart.getNoteGroups() ) );
		}

		Double[] edgeArray = edgeSet.toArray( new Double[0] );
		Arrays.sort( edgeArray );

		List< List< InstrumentPart > > melodyBlockList = new ArrayList<>(  );
		double previousEdge = 0;
		for ( double edge : edgeArray ) {
			List< InstrumentPart > instrumentPartsBlock = new ArrayList<>(  );
			for ( InstrumentPart instrumentPart : inputMelodyBlock ) {
				int noteGroupNumber = getNoteNumber( instrumentPart.getNoteGroups(), edge );
				NoteGroup noteGroupPlayingAtThisMoment = instrumentPart.getNoteGroups().get( noteGroupNumber );
				NoteGroup newNoteGroup = noteGroupPlayingAtThisMoment.cloneWithRhythmValue( edge - previousEdge );

				ArrayList<NoteGroup> newNoteGroups = new ArrayList<>();
				newNoteGroups.add( newNoteGroup );
				instrumentPartsBlock.add( new InstrumentPart( newNoteGroups, instrumentPart.getInstrument() ) );
			}
			previousEdge = edge;
			melodyBlockList.add( instrumentPartsBlock );
		}

		return melodyBlockList;
	}

	/**
	 * Returns the edge list of melody noteGroups
	 * Sets first edge to rhythm value of the first note, and incrementing this value by
	 * rhythm value of all noteGroups one by one
	 * @param noteGroups
	 * @return
	 */
	public static List< Double > getRhythmEdgeList( List<NoteGroup> noteGroups ) {
		List< Double > edgeList = new ArrayList<>();
		double lastEdge = 0;
		for ( int noteGroupNumber = 0; noteGroupNumber < noteGroups.size(); noteGroupNumber ++ ) {
			NoteGroup noteGroup = noteGroups.get( noteGroupNumber );
			edgeList.add( lastEdge + noteGroup.getRhythmValue() );
			lastEdge = edgeList.get( edgeList.size() - 1 );
		}
		return edgeList;
	}

	/**
	 * Returns number of the note in the Note Array that sounds in particular time
	 * If input time is finish time to one and start time to another, the first one will be returned
	 * @param noteGroups - note array
	 * @param time
	 * @return
	 * TODO refactor using binary search
	 */
	public static int getNoteNumber( List<NoteGroup> noteGroups, double time ) {
		double startTime = 0;
		for ( int currentNoteNumber = 0; currentNoteNumber < noteGroups.size(); currentNoteNumber ++ ) {
			double rhythm = noteGroups.get( currentNoteNumber ).getRhythmValue();
			if ( startTime < time && time <= startTime + rhythm ) {
				return currentNoteNumber;
			}
			startTime += rhythm;
		}
		return noteGroups.size() + 1;
	}
}
