package composer.next.filter;

import composer.step.CompositionStep;
import jm.music.data.Note;
import model.ComposeBlock;
import org.springframework.stereotype.Component;
import utils.ModelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Created by wish on 02.02.2016.
 * Filter declines all compose blocks that will go out of range after transposing
 */
@Component
public class ComposeBlockRangeFilter implements ComposeBlockFilter {

	private ComposeBlockFilter composeBlockFilter;
	private int lowestNotePitch;
	private int highestNotePitch;

	public ComposeBlockRangeFilter() {
	}

	public ComposeBlockRangeFilter( int lowestNotePitch, int highestNotePitch, ComposeBlockFilter composeBlockFilter ) {
		this.composeBlockFilter = composeBlockFilter;
		this.lowestNotePitch = lowestNotePitch;
		this.highestNotePitch = highestNotePitch;
	}

	public ComposeBlockRangeFilter( int lowestNotePitch, int highestNotePitch ) {
		this.lowestNotePitch = lowestNotePitch;
		this.highestNotePitch = highestNotePitch;
	}

	@Override
	public List<ComposeBlock> filter( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		List<ComposeBlock> filteredPrevously = composeBlockFilter != null ?
				composeBlockFilter.filter( possibleNextComposeBlocks, previousCompositionSteps ) :
				new ArrayList<>( possibleNextComposeBlocks );
		List<ComposeBlock> out = new ArrayList<>();
		ComposeBlock lastTrasposedComposeBlock = previousCompositionSteps.get( previousCompositionSteps.size() - 1 ).getTransposeComposeBlock();
		for ( ComposeBlock possibleNext : filteredPrevously ) {
			int trasposePitch = ModelUtils.getTransposePitch( Optional.of( lastTrasposedComposeBlock ), possibleNext );
			ComposeBlock trasposedBlock = possibleNext.transposeClone( trasposePitch );
			OptionalInt max = trasposedBlock.getMelodyList().stream().flatMap( melody -> melody.getNoteList().stream() )
					.mapToInt( value -> ( ( Note ) value ).getPitch() ).max();
			OptionalInt min = trasposedBlock.getMelodyList().stream().flatMap( melody -> melody.getNoteList().stream() )
					.mapToInt( value -> ( ( Note ) value ).getPitch() ).min();
			if ( max.getAsInt() <= highestNotePitch && min.getAsInt() >= lowestNotePitch ) {
				out.add( possibleNext );
			}
		}
		return out;
	}
}
