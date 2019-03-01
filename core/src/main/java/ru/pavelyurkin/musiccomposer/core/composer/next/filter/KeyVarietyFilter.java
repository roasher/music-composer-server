package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.Key;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.utils.ModelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getNumberOfNotesOutOfKey;
import static ru.pavelyurkin.musiccomposer.core.utils.KeyUtils.getPossibleKeys;

/**
 * Filters musicblocks that are not from previous blocks key
 */
public class KeyVarietyFilter extends AbstractComposeStepFilter {

	private final int maxNotesNumberOutOfKey;
	private final double rhythmValue;

	public KeyVarietyFilter( int maxNotesNumberOutOfKey, double rhythmValue, AbstractComposeStepFilter composeStepFilter ) {
		super( composeStepFilter );
		this.maxNotesNumberOutOfKey = maxNotesNumberOutOfKey;
		this.rhythmValue = rhythmValue;
	}

	public KeyVarietyFilter( int maxNotesNumberOutOfKey, double rhythmValue ) {
		this.maxNotesNumberOutOfKey = maxNotesNumberOutOfKey;
		this.rhythmValue = rhythmValue;
	}

	@Override
	public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
		List<MusicBlock> musicBlocksToTrim = new ArrayList<>();
		double sumRhythmValue = 0;
		for ( int blockNumber = previousBlocks.size() - 1; blockNumber >= 0; blockNumber-- ) {
			musicBlocksToTrim.add( previousBlocks.get( blockNumber ) );
			sumRhythmValue = ModelUtils.sumAllRhythmValues( musicBlocksToTrim );
			if ( sumRhythmValue >= rhythmValue )
				break;
		}
		if ( sumRhythmValue < rhythmValue ) {
			// not much composed yet
			return true;
		}

		MusicBlock musicBlockToTrim = new MusicBlock( musicBlocksToTrim );

		List<InstrumentPart> previousParts = ModelUtils
				.trimToTime( musicBlockToTrim.getInstrumentParts(), sumRhythmValue - rhythmValue, sumRhythmValue );
		Set<Integer> allPreviousPitches = previousParts.stream()
				.flatMap( instrumentPart -> instrumentPart.getNoteGroups().stream() )
				.flatMap( noteGroup -> noteGroup.getAllPitches().stream() )
				.collect( Collectors.toSet() );

		Set<Integer> allPossibleNextPitches = block.getInstrumentParts().stream()
				.flatMap( instrumentPart -> instrumentPart.getNoteGroups().stream() )
				.flatMap( noteGroup -> noteGroup.getAllPitches().stream() )
				.collect( Collectors.toSet() );

		List<Key> previousPossibleKeys = getPossibleKeys( 0, allPreviousPitches );
		if ( previousPossibleKeys.isEmpty() ) {
			// If there is hell - all we can do is continue hoping that further notes will form new key variant
			return true;
		}
		OptionalInt minNumberOfOutNotesOutOfKey = previousPossibleKeys.stream()
				.mapToInt( value -> getNumberOfNotesOutOfKey( value, allPossibleNextPitches ) ).min();
		return minNumberOfOutNotesOutOfKey.getAsInt() <= maxNotesNumberOutOfKey;

	}

}
