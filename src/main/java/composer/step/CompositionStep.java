package composer.step;

import model.ComposeBlock;
import model.melody.Form;

import java.util.*;

/**
 * Class represents step that program makes in order to create new form block
 * One step - one added composeBlock to the form block
 *
 * Created by pyurkin on 15.01.15.
 */
public class CompositionStep {

	private ComposeBlock composeBlock;
	/**
	 * Valid Music Blocks which can come next to this, but their usage leads to dead end in future cause of small lexicon.
	 */
	private List<ComposeBlock> nextMusicBlockExclusions = new ArrayList<>(  );

	public CompositionStep( ComposeBlock composeBlock ) {
		this.composeBlock = composeBlock;
	}

	public CompositionStep() {
	}

	public void addNextExclusion( ComposeBlock musicBlock ) {
		this.nextMusicBlockExclusions.add( musicBlock );
	}

	public ComposeBlock getComposeBlock() {
		return composeBlock;
	}

	public void setComposeBlock( ComposeBlock composeBlock ) {
		this.composeBlock = composeBlock;
	}

	public List<ComposeBlock> getNextMusicBlockExclusions() {
		return nextMusicBlockExclusions;
	}

	public void setNextMusicBlockExclusions( List<ComposeBlock> nextMusicBlockExclusions ) {
		this.nextMusicBlockExclusions = nextMusicBlockExclusions;
	}

}