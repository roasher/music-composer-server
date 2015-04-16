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
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof CompositionInfo ) ) {
			return false;
		}

		CompositionInfo that = ( CompositionInfo ) o;

		if ( Double.compare( that.tempo, tempo ) != 0 ) {
			return false;
		}
		if ( author != null ? !author.equals( that.author ) : that.author != null ) {
			return false;
		}
		if ( metre != null ? !metre.equals( that.metre ) : that.metre != null ) {
			return false;
		}
		if ( title != null ? !title.equals( that.title ) : that.title != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = author != null ? author.hashCode() : 0;
		result = 31 * result + ( title != null ? title.hashCode() : 0 );
		temp = Double.doubleToLongBits( tempo );
		result = 31 * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		result = 31 * result + ( metre != null ? metre.hashCode() : 0 );
		return result;
	}

	@Override public String toString() {
		return "CompositionInfo{" +
				"author='" + author + '\'' +
				", title='" + title + '\'' +
				", tempo=" + tempo +
				", metre=" + metre +
				'}';
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
