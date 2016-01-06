package composer.step;

import model.ComposeBlock;
import model.melody.Form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class represents step that program makes in order to create new composition
 * One step - one added form block to composition
 *
 * Created by pyurkin on 15.01.15.
 */
public class FormCompositionStep {

	private List<ComposeBlock> composeBlocks;
	private Form form;
	/**
	 * Valid Music Blocks which can come next to this, but their usage leads to dead end in future cause of small lexicon.
	 */
	private Set<List<ComposeBlock>> nextMusicBlockExclusions = new HashSet<>(  );

	public FormCompositionStep( List<ComposeBlock> composeBlocks, Form form ) {
		this.composeBlocks = composeBlocks;
		this.form = form;
	}

	public FormCompositionStep( List<ComposeBlock> composeBlocks ) {
		this.composeBlocks = composeBlocks;
	}

	public FormCompositionStep() {}

	public void addNextExclusion( List<ComposeBlock> musicBlocks ) {
		this.nextMusicBlockExclusions.add( musicBlocks );
	}

	public List<ComposeBlock> getComposeBlocks() {
		return composeBlocks;
	}

	public void setComposeBlocks( List<ComposeBlock> composeBlocks ) {
		this.composeBlocks = composeBlocks;
	}

	public Form getForm() {
		return form;
	}

	public void setForm( Form form ) {
		this.form = form;
	}

	public Set<List<ComposeBlock>> getNextMusicBlockExclusions() {
		return nextMusicBlockExclusions;
	}

}