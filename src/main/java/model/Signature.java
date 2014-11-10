package model;

import jm.music.data.Note;
import jm.music.data.Phrase;
import utils.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Class represents signature entity
 * Signatures is the simple moves witch repeats from composition to composition
 * Created by Pavel Yurkin on 26.07.14.
 */
public class Signature extends Phrase {

    private PlaceInTheComposition placeInTheComposition;

    public Signature( List<Note> notes ) {
        this((Note[]) notes.toArray());
    }

    public Signature( Note[] notes ) {
        super(notes);
    }

    public Signature() {
        super();
    }

    public PlaceInTheComposition getPlaceInTheComposition() {
        return placeInTheComposition;
    }

    public void setPlaceInTheComposition( PlaceInTheComposition placeInTheComposition ) {
        this.placeInTheComposition = placeInTheComposition;
		this.setTitle( getTitle( placeInTheComposition ) );
    }

	private String getTitle( PlaceInTheComposition placeInTheComposition ) {
		StringBuffer out = new StringBuffer();
		out.append( placeInTheComposition.getCompositionInfo().getTitle() ).append( "\n" );
		out.append( " first note start time : " ).append( placeInTheComposition.getFirstNoteStartTime() ).append( "\n" );
		out.append( " last note end time : " ).append( placeInTheComposition.getLastNoteEndTime() ).append( "\n" );
		return out.toString();
	}

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        Signature signature = ( Signature ) o;

        Note[] thisNoteArray = this.getNoteArray();
        Note[] signatureNoteArray = signature.getNoteArray();

        if ( thisNoteArray.length != signatureNoteArray.length ) return false;

//        int difference = thisNoteArray[ 0 ].getPitch() - signatureNoteArray[ 0 ].getPitch();
        for ( int currentNoteNumber = 0; currentNoteNumber < thisNoteArray.length ; currentNoteNumber ++ ) {
            Note currentNoteFromThis = thisNoteArray[ currentNoteNumber ];
            Note currentNoteFromSignature = signatureNoteArray[ currentNoteNumber ];
//            int currentDifference = currentNoteFromThis.getPitch() - currentNoteFromSignature.getPitch();
            double rhythm1 = Utils.round( currentNoteFromSignature.getRhythmValue() );
            double rhythm2 = Utils.round( currentNoteFromThis.getRhythmValue() );
            if ( currentNoteFromSignature.getPitch() != currentNoteFromThis.getPitch() || rhythm1 != rhythm2 ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        Note[] notes = this.getNoteArray();
        return notes[0].getPitch() - notes[ notes.length - 1 ].getPitch();
    }
}
