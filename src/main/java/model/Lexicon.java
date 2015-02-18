package model;

import java.util.*;

/**
 * Class represents wrapper to Music Block collection
 * Created by pyurkin on 13.02.15.
 */
public class Lexicon {
	private List<MusicBlock> musicBlockList;
	private Map<Double,List<MusicBlock>> rhythmValueMusicBlockMap = new HashMap<>(  );

	private Map<Double,Integer> rhythmValueQuantityMap = new HashMap<>(  );

	public Lexicon( List<MusicBlock> musicBlockList ) {
		if ( musicBlockList == null ) { throw new IllegalArgumentException( "Input music block list is null" ); }
		this.musicBlockList = musicBlockList;
		for ( MusicBlock musicBlock : musicBlockList ) {
			Integer quantity = rhythmValueQuantityMap.get( musicBlock.getRhythmValue() ) != null ? rhythmValueQuantityMap.get( musicBlock.getRhythmValue() ) : 0;
			rhythmValueQuantityMap.put( musicBlock.getRhythmValue(), quantity + 1 );

			List<MusicBlock> musicBlocks = rhythmValueMusicBlockMap.get( musicBlock.getRhythmValue() );
			if ( musicBlocks != null ) {
				musicBlocks.add( musicBlock );
			} else {
				musicBlocks = new ArrayList<MusicBlock>(  );
				musicBlocks.add( musicBlock );
				this.rhythmValueMusicBlockMap.put( musicBlock.getRhythmValue(), musicBlocks );
			}
		}
	}

	public List<MusicBlock> getMusicBlockList( double rhythmValue ) {
//		List<MusicBlock> certainRhythmValueLexicon = new ArrayList<>(  );
//		for ( MusicBlock musicBlock : musicBlockList ) {
//			if ( musicBlock.getRhythmValue() == rhythmValue ) {
//				certainRhythmValueLexicon.add( musicBlock );
//			}
//		}
//		return certainRhythmValueLexicon;
		return rhythmValueMusicBlockMap.get( rhythmValue ) != null ? rhythmValueMusicBlockMap.get( rhythmValue ) : Collections.<MusicBlock>emptyList();
	}

	public MusicBlock get( int number ) {
		return this.musicBlockList.get( number );
	}

	public Map<Double,Integer> getRhythmValueQuantityMap() {
		return rhythmValueQuantityMap;
	}

	public List<MusicBlock> getMusicBlockList() {
		return musicBlockList;
	}
}
