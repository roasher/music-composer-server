package utils;

import jm.music.data.Note;
import model.melody.Melody;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class handles all recombination between melody lists
 * Created by pyurkin on 10.12.14.
 */
@Component
public class Recombinator {

	/**
	 * Recombines melody lists.
	 * In the result, all melodies has only one note within
	 * @param inputMelodyBlockList
	 * @return
	 */
	public List< List< Melody > > recombine( List< List< Melody > > inputMelodyBlockList ) {
		List< List< Melody > > recombineList = new ArrayList<>();
		for ( List< Melody > melodyBlock : inputMelodyBlockList ) {
			List< List< Melody > > melodyBlockList = recombineMelodyBlock( melodyBlock );
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
	public List<List< Melody >> recombineMelodyBlock( List<Melody> inputMelodyBlock ) {

		// The edge set
		Set< Double > edgeSet = new HashSet<>(  );
		for ( Melody melody : inputMelodyBlock ) {
			edgeSet.addAll( getEdgeList( melody ) );
		}

		Double[] edgeArray = edgeSet.toArray( new Double[0] );
		Arrays.sort( edgeArray );

		List< List< Melody > > melodyBlockList = new ArrayList<>(  );
		double prevoiusEdge = 0;
		for ( double edge : edgeArray ) {
			List< Melody > melodyBlock = new ArrayList<>(  );
			for ( Melody melody : inputMelodyBlock ) {
				int noteNumber = Utils.getNoteNumber( melody.getNoteArray(), edge );
				Note notePlayingAtThisMoment = melody.getNote( noteNumber );

				Melody newMelody = new Melody( new Note[]{ new Note( notePlayingAtThisMoment.getPitch(), edge - prevoiusEdge ) } );
				newMelody.setForm( melody.getForm() );
				newMelody.setStartTime( prevoiusEdge + melody.getStartTime() );
				melodyBlock.add( newMelody );
			}
			prevoiusEdge = edge;
			melodyBlockList.add( melodyBlock );
		}

		return melodyBlockList;
	}

	/**
	 * Returns the edge list of melody notes
	 * Sets first edge to rhythm value of the first note, and incrementing this value by
	 * rhythm value of all notes one by one
	 * @param melody
	 * @return
	 */
	public List< Double > getEdgeList( Melody melody ) {
		List< Double > edgeList = new ArrayList<>();
		double lastEdge = 0;
		for ( int noteNumber = 0; noteNumber < melody.size(); noteNumber ++ ) {
			Note note = melody.getNote( noteNumber );
			edgeList.add( lastEdge + note.getRhythmValue() );
			lastEdge = edgeList.get( edgeList.size() - 1 );
		}
		return edgeList;
	}
}
