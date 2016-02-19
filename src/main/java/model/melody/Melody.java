package model.melody;

import jm.music.data.Note;
import jm.music.data.Phrase;
import model.PlaceInTheComposition;
import utils.ModelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Class represents Melody entity
 * Melodies considered as simple single-voice note moves
 * Created by Pavel Yurkin on 26.07.14.
 */
public class Melody extends Phrase {

    private PlaceInTheComposition placeInTheComposition;
	private Form form = new Form();

    public Melody( List<Note> notes ) {
        this((Note[]) notes.toArray( new Note[]{}));
    }

    public Melody( Note... notes ) {
        super(notes);
    }

    public Melody( char form, Note... notes ) {
        super(notes);
        this.form = new Form( form );
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

		if ( !this.form.equals( melody.form ) ) {
			return false;
		}

        List<Note> thisNoteArray = this.getNoteList();
        List<Note> melodyNoteArray = melody.getNoteList();

        if ( thisNoteArray.size() != melodyNoteArray.size() ) return false;

        for ( int currentNoteNumber = 0; currentNoteNumber < thisNoteArray.size() ; currentNoteNumber ++ ) {
            Note currentNoteFromThis = thisNoteArray.get( currentNoteNumber );
            Note currentNoteFromMelody = melodyNoteArray.get( currentNoteNumber );
            double rhythm1 = currentNoteFromMelody.getRhythmValue();
            double rhythm2 = currentNoteFromThis.getRhythmValue();
            if ( currentNoteFromMelody.getPitch() != currentNoteFromThis.getPitch() || rhythm1 != rhythm2 ) {
                return false;
            }
        }
        return true;
    }

	/**
	 * Returns transposed clone of melody
	 * @param transposePitch
	 * @return
	 */
	public Melody transposeClone( int transposePitch ) {
		List<Note> notes = new ArrayList<>( this.getNoteList().size() );
		for ( Note note : ( List<Note> ) this.getNoteList() ) {
			notes.add( new Note( note.getPitch() == Note.REST ? Note.REST : note.getPitch() + transposePitch, note.getRhythmValue(), note.getDynamic(), note.getPan() ) );
		}
		Melody newMelody = new Melody( notes );
		return newMelody;
	}

	@Override
    public int hashCode() {
        List<Note> notes = this.getNoteList();
        return this.getNote( 0 ).getPitch() - this.getNote( notes.size() - 1 ).getPitch();
    }

	@Override
		 public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append( this.getStartTime() ).append( " " );
		for ( Note note : ( List<Note> ) this.getNoteList() ) {
			stringBuilder.append( String.format( "{%d %s|%.3f}", note.getPitch(), ModelUtils.getNoteNameByPitch( note.getPitch() ), note.getRhythmValue() ) );
		}
		stringBuilder.append(" ").append(this.getEndTime() );
		return stringBuilder.toString();
	}
}
