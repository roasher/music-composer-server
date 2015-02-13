package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

/**
 * Class represents wrapper to Music Block collection
 * Created by pyurkin on 13.02.15.
 */
public class Lexicon {
	private List<MusicBlock> musicBlockList;
	private TreeSet<Double> sortedRhythmValuesSet = new TreeSet<>(  );

	public Lexicon( List<MusicBlock> musicBlockList ) {
		if ( musicBlockList == null ) { throw new IllegalArgumentException( "Input music block list is null" ); }
		this.musicBlockList = musicBlockList;
		for ( MusicBlock musicBlock : musicBlockList ) {
			sortedRhythmValuesSet.add( musicBlock.getRhythmValue() );
		}
	}

	public List<MusicBlock> getMusicBlockList( double rhythmValue ) {
		List<MusicBlock> certainRhythmValueLexicon = new ArrayList<>(  );
		for ( MusicBlock musicBlock : musicBlockList ) {
			if ( musicBlock.getRhythmValue() == rhythmValue ) {
				certainRhythmValueLexicon.add( musicBlock );
			}
		}
		return certainRhythmValueLexicon;
	}

	public List<Double> getSortedRhythmValues() {
		return Arrays.asList( sortedRhythmValuesSet.toArray( new Double[]{} ) );
	}

	public List<MusicBlock> getMusicBlockList() {
		return musicBlockList;
	}

}
