package composer;

import model.ComposeBlock;
import model.MusicBlock;
import model.melody.Form;

import java.util.*;

/**
 * Class represents step that program makes in order to create new composition
 * One step - one added form block to the composition
 *
 * Created by pyurkin on 15.01.15.
 */
public class CompositionStep {

	private ComposeBlock composeBlock;
	private Form form;
	/**
	 * Valid Music Blocks which can come next to this, but their usage leads to dead end in future cause of small lexicon.
	 */
	private List<ComposeBlock> nextMusicBlockExclusion = new ArrayList<>(  );

	public CompositionStep( ComposeBlock composeBlock, Form form ) {
		this.composeBlock = composeBlock;
		this.form = form;
	}

	public CompositionStep( ComposeBlock composeBlock ) {
		this.composeBlock = composeBlock;
	}

	public CompositionStep() {}

	public void addNextExclusion( ComposeBlock musicBlock ) {
		this.nextMusicBlockExclusion.add( musicBlock );
	}

	public ComposeBlock getComposeBlock() {
		return composeBlock;
	}

	public void setComposeBlock( ComposeBlock composeBlock ) {
		this.composeBlock = composeBlock;
	}

	public Form getForm() {
		return form;
	}

	public void setForm( Form form ) {
		this.form = form;
	}

	public List<ComposeBlock> getNextMusicBlockExclusion() {
		return nextMusicBlockExclusion;
	}
}