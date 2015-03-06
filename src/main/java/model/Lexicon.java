package model;

import composer.MusicBlockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class represents wrapper to Music Block collection
 * Created by pyurkin on 13.02.15.
 */
public class Lexicon {

	private List<ComposeBlock> composeBlockList = new ArrayList<>();
	private Map<Double,List<ComposeBlock>> rhythmValueComposeBlockMap = new HashMap<>(  );

	private Map<Double,Integer> rhythmValueQuantityMap = new HashMap<>(  );

	public Lexicon( List<ComposeBlock> composeBlockList ) {
        this.composeBlockList = composeBlockList;
        // filling rhythm value map
        for ( ComposeBlock composeBlock : composeBlockList ) {
            Integer quantity = rhythmValueQuantityMap.get( composeBlock.getRhythmValue() ) != null ? rhythmValueQuantityMap.get( composeBlock.getRhythmValue() ) : 0;
            rhythmValueQuantityMap.put( composeBlock.getRhythmValue(), quantity + 1 );

            List<ComposeBlock> musicBlocks = rhythmValueComposeBlockMap.get( composeBlock.getRhythmValue() );
            if ( musicBlocks != null ) {
                musicBlocks.add( composeBlock );
            } else {
                musicBlocks = new ArrayList<>();
                musicBlocks.add( composeBlock );
                this.rhythmValueComposeBlockMap.put( composeBlock.getRhythmValue(), musicBlocks );
            }
        }
	}

	public List< ComposeBlock > getMusicBlockList( double rhythmValue ) {
		return rhythmValueComposeBlockMap.get( rhythmValue ) != null ? rhythmValueComposeBlockMap.get( rhythmValue ) : Collections.<ComposeBlock>emptyList();
	}

	public ComposeBlock get( int number ) {
		return this.composeBlockList.get( number );
	}

	public Map<Double,Integer> getRhythmValueQuantityMap() {
		return rhythmValueQuantityMap;
	}

	public List<ComposeBlock> getComposeBlockList() {
		return composeBlockList;
	}
}
