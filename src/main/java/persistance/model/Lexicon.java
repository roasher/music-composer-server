package persistance.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyurkin on 29.04.2015.
 */
@Entity
public class Lexicon {
	@Id @GeneratedValue
	long id;
	@Column
	double minRhythmValue;
	@ManyToMany
	List<ComposeBlock> composeBlockList = new ArrayList<>(  );

	Lexicon() {}
	Lexicon( List<ComposeBlock> composeBlockList ) { this.composeBlockList = composeBlockList; }
	Lexicon( List<ComposeBlock> composeBlockList, double minRhythmValue ) { this( composeBlockList ); this.minRhythmValue = minRhythmValue; }

	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( !( o instanceof Lexicon ) )
			return false;

		Lexicon lexicon = ( Lexicon ) o;

		if ( Double.compare( lexicon.minRhythmValue, minRhythmValue ) != 0 )
			return false;
		if ( !composeBlockList.equals( lexicon.composeBlockList ) )
			return false;

		return true;
	}

	@Override public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits( minRhythmValue );
		result = ( int ) ( temp ^ ( temp >>> 32 ) );
		result = 31 * result + composeBlockList.hashCode();
		return result;
	}
}
