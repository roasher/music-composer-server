package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import lombok.Data;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getLast;

/**
 * Created by wish on 02.02.2016.
 * Filters next compose block to maintain variety of blocks used in composition.
 * If from previous steps there are already x
 * blocks from one composition, filter will decline blocks from that composition to prevent
 * x + 1 blocks from same composition in the result
 */
@Data
public class ComposeStepVarietyFilter extends AbstractComposeStepFilter {

	private int maxSequentialBlocksFromSameComposition;
	private int minSequentialBlocksFromSameComposition;

	public ComposeStepVarietyFilter() {
	}

	public ComposeStepVarietyFilter( int minSequentialBlocksFromSameComposition,
			int maxSequentialBlocksFromSameComposition,
			AbstractComposeStepFilter composeStepFilter ) {
		super( composeStepFilter );
		this.maxSequentialBlocksFromSameComposition = maxSequentialBlocksFromSameComposition;
		this.minSequentialBlocksFromSameComposition = minSequentialBlocksFromSameComposition;
	}

	public ComposeStepVarietyFilter( int minSequentialBlocksFromSameComposition, int maxSequentialBlocksFromSameComposition ) {
		this.maxSequentialBlocksFromSameComposition = maxSequentialBlocksFromSameComposition;
		this.minSequentialBlocksFromSameComposition = minSequentialBlocksFromSameComposition;
	}

	@Override
	public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
		return isOkMaxSequentialBlocksFromSameComposition( block, previousBlocks ) &&
				isOkMinSequentialBlocksFromSameComposition( block, previousBlocks );
	}

	private boolean isOkMaxSequentialBlocksFromSameComposition( MusicBlock block, List<MusicBlock> previousBlocks ) {
		if ( maxSequentialBlocksFromSameComposition <= 0 ) return true;
		if ( previousBlocks.size() >= maxSequentialBlocksFromSameComposition ) {
			Set<CompositionInfo> compositionInfos = previousBlocks.stream()
					.skip( previousBlocks.size() - maxSequentialBlocksFromSameComposition )
					.map( MusicBlock::getCompositionInfo ).collect( Collectors.toSet() );
			return compositionInfos.size() != 1 || !compositionInfos.contains( block.getCompositionInfo() );
		} else {
			return true;
		}
	}

	private boolean isOkMinSequentialBlocksFromSameComposition( MusicBlock musicBlock, List<MusicBlock> previousBlocks ) {
		if ( minSequentialBlocksFromSameComposition <= 0 ) return true;
		if ( previousBlocks.isEmpty() ) return true;
		if ( getLast( previousBlocks ).getCompositionInfo().equals( musicBlock.getCompositionInfo() ) ) {
			return true;
		} else {
			if ( previousBlocks.size() < minSequentialBlocksFromSameComposition ) return false;
			long countDifferentCompositionInfos = previousBlocks.stream()
					.map( MusicBlock::getCompositionInfo )
					.skip( previousBlocks.size() - minSequentialBlocksFromSameComposition )
					.distinct()
					.count();
			return countDifferentCompositionInfos == 1;
		}

	}

}
