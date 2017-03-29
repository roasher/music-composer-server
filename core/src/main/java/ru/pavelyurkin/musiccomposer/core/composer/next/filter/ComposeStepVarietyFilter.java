package ru.pavelyurkin.musiccomposer.core.composer.next.filter;

import ru.pavelyurkin.musiccomposer.core.composer.step.CompositionStep;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.composition.CompositionInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by wish on 02.02.2016.
 * Filters next compose block to maintain variety of blocks used in composition.
 * If from previous steps there are already x
 * blocks from one composition, filter will decline blocks from that composition to prevent
 * x + 1 blocks from same composition in the result
 */
@Component
public class ComposeStepVarietyFilter extends AbstractComposeStepFilter {

	private int possibleBlockNumberFromSameCompositionOneByOne;

	public ComposeStepVarietyFilter() {
	}

	public ComposeStepVarietyFilter( int possibleBlockNumberFromSameCompositionOneByOne, ComposeStepFilter composeStepFilter ) {
		super( composeStepFilter );
		this.possibleBlockNumberFromSameCompositionOneByOne = possibleBlockNumberFromSameCompositionOneByOne;
	}

	public ComposeStepVarietyFilter( int possibleBlockNumberFromSameCompositionOneByOne ) {
		this.possibleBlockNumberFromSameCompositionOneByOne = possibleBlockNumberFromSameCompositionOneByOne;
	}

	@Override
	public boolean filterIt( MusicBlock block, List<MusicBlock> previousBlocks ) {
		if ( previousBlocks.size() > possibleBlockNumberFromSameCompositionOneByOne ) {
			Set<CompositionInfo> compositionInfos = previousBlocks.stream()
					.skip( previousBlocks.size() - possibleBlockNumberFromSameCompositionOneByOne )
					.map( MusicBlock::getCompositionInfo ).collect( Collectors.toSet() );
			return compositionInfos.size() != 1 || !compositionInfos.contains( block.getCompositionInfo() );
		} else {
			return true;
		}
	}

}
