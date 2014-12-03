package model.melody;

import jm.music.data.Note;
import jm.music.data.Phrase;
import model.PlaceInTheComposition;

import java.util.List;

/**
 * Class represents Melody entity
 * Melodies considered as simple single-voice note moves
 * Created by Pavel Yurkin on 26.07.14.
 */
public class Melody extends Phrase {

    private PlaceInTheComposition placeInTheComposition;
	private Form form = new Form();

    public Melody( List<Note> notes ) {
        this((Note[]) notes.toArray());
    }

    public Melody( Note[] notes ) {
        super(notes);
    }

    public Melody() {
        super();
    }

    public PlaceInTheComposition getPlaceInTheComposition() {
        return placeInTheComposition;
    }

    public void setPlaceInTheComposition( PlaceInTheComposition placeInTheComposition ) {
        this.placeInTheComposition = placeInTheComposition;
		this.setTitle( getTitle( placeInTheComposition ) );
    }

	public Form getForm() {
		return form;
	}

	public void setForm( Form form ) {
		this.form = form;
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

        Melody melody = ( Melody ) o;

        Note[] thisNoteArray = this.getNoteArray();
        Note[] melodyNoteArray = melody.getNoteArray();

        if ( thisNoteArray.length != melodyNoteArray.length ) return false;

//        int difference = thisNoteArray[ 0 ].getPitch() - melodyNoteArray[ 0 ].getPitch();
        for ( int currentNoteNumber = 0; currentNoteNumber < thisNoteArray.length ; currentNoteNumber ++ ) {
            Note currentNoteFromThis = thisNoteArray[ currentNoteNumber ];
            Note currentNoteFromMelody = melodyNoteArray[ currentNoteNumber ];
//            int currentDifference = currentNoteFromThis.getPitch() - currentNoteFromMelody.getPitch();
            double rhythm1 = currentNoteFromMelody.getRhythmValue();
            double rhythm2 = currentNoteFromThis.getRhythmValue();
            if ( currentNoteFromMelody.getPitch() != currentNoteFromThis.getPitch() || rhythm1 != rhythm2 ) {
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

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for ( Note note : this.getNoteArray() ) {
			stringBuilder.append( String.format( "{%d|%.1f}", note.getPitch(), note.getRhythmValue() ) );
		}
		return stringBuilder.toString();
	}
}