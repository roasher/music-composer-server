package composer.next.filter;

import composer.step.CompositionStep;
import jm.music.data.Note;
import model.ComposeBlock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by wish on 02.02.2016.
 * Filter declines all blocks that are rests and their rhythm value is longer than x
 */
@Component
public class ComposeBlockRestFilter implements ComposeBlockFilter {

	private ComposeBlockFilter composeBlockFilter;
	private double maxRestRhythmValue;

	public ComposeBlockRestFilter() {
	}

	public ComposeBlockRestFilter( double maxRestRhythmValue, ComposeBlockFilter composeBlockFilter ) {
		this.composeBlockFilter = composeBlockFilter;
		this.maxRestRhythmValue = maxRestRhythmValue;
	}

	public ComposeBlockRestFilter( double maxRestRhythmValue ) {
		this.maxRestRhythmValue = maxRestRhythmValue;
	}

	@Override
	public List<ComposeBlock> filter( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		List<ComposeBlock> filteredPrevously = composeBlockFilter != null ?
				composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps ) :
				new ArrayList<>( possibleNextComposeBlocks );
		List<ComposeBlock> out = new ArrayList<>();
		for ( ComposeBlock composeBlock : filteredPrevously ) {
			boolean hasNonRestInIt = composeBlock.getMelodyList().stream().flatMap( melody -> melody.getNoteList().stream() )
					.filter( note -> !( ( Note ) note ).isRest() ).findAny().isPresent();
			if ( hasNonRestInIt || composeBlock.getRhythmValue() <= maxRestRhythmValue ) {
				out.add( composeBlock );
			}
		}
		return out;
	}
}
