package model.composition;

/**
 * Class represents CompositionInfo
 * Created by Pavel Yurkin on 18.07.14.
 */
public class CompositionInfo {
    private String author;
    private String title;
    private double tempo;
    private Meter metre;

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        CompositionInfo that = ( CompositionInfo ) o;

        if ( !metre.equals( that.metre ) ) return false;
        if ( Double.compare( tempo, that.tempo ) != 0 ) return false;
        if ( !author.equals( that.author ) ) return false;
        if ( !title.equals( that.title ) ) return false;

        return true;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor( String author ) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo( double tempo ) {
        this.tempo = tempo;
    }

    public Meter getMetre() {
        return metre;
    }

    public void setMetre( Meter metre ) {
        this.metre = metre;
    }
}
