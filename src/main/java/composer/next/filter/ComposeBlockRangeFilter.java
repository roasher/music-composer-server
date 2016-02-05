package composer.next.filter;

import composer.step.CompositionStep;
import jm.music.data.Note;
import model.ComposeBlock;
import org.springframework.stereotype.Component;
import utils.ModelUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by wish on 02.02.2016.
 * Filter declines all compose blocks that will go out of range after transposing
 */
@Component
public class ComposeBlockRangeFilter extends AbstractComposeBlockFilter {

	private int lowestNotePitch;
	private int highestNotePitch;

	public ComposeBlockRangeFilter() {
	}

	public ComposeBlockRangeFilter( int lowestNotePitch, int highestNotePitch, ComposeBlockFilter composeBlockFilter ) {
		super( composeBlockFilter );
		this.lowestNotePitch = lowestNotePitch;
		this.highestNotePitch = highestNotePitch;
	}

	public ComposeBlockRangeFilter( int lowestNotePitch, int highestNotePitch ) {
		this.lowestNotePitch = lowestNotePitch;
		this.highestNotePitch = highestNotePitch;
	}

	@Override
	public List<ComposeBlock> filterIt( List<ComposeBlock> possibleNextComposeBlocks, List<CompositionStep> previousCompositionSteps ) {
		List<ComposeBlock> out = new ArrayList<>();
		ComposeBlock lastTrasposedComposeBlock = previousCompositionSteps.get( previousCompositionSteps.size() - 1 ).getTransposeComposeBlock();
		for ( ComposeBlock possibleNext : possibleNextComposeBlocks ) {
			int trasposePitch = ModelUtils.getTransposePitch( Optional.of( lastTrasposedComposeBlock ), possibleNext );
			ComposeBlock trasposedBlock = possibleNext.transposeClone( trasposePitch );
			List<Integer> pitches = trasposedBlock.getMelodyList().stream().flatMap( melody -> melody.getNoteList().stream() )
					.mapToInt( value -> ( ( Note ) value ).getPitch() ).filter( value -> value != Note.REST ).boxed().collect( Collectors.toList() );
			if ( pitches.size() == 0 || ( Collections.max( pitches ) <= highestNotePitch && Collections.min( pitches ) >= lowestNotePitch ) ) {
				out.add( possibleNext );
			}
		}
		return out;
	}
}
