package composer.step;

import model.ComposeBlock;
import model.melody.Form;

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

	private List<ComposeBlock> originComposeBlocks;
	private List<ComposeBlock> trasposedComposeBlocks;
	private Form form;
	/**
	 * Valid Music Blocks which can come next to this, but their usage leads to dead end in future cause of small lexicon.
	 */
	private Set<List<ComposeBlock>> nextMusicBlockExclusions = new HashSet<>(  );

	public FormCompositionStep( List<ComposeBlock> originComposeBlocks, List<ComposeBlock> trasposedComposeBlocks, Form form ) {
		this.originComposeBlocks = originComposeBlocks;
		this.trasposedComposeBlocks = trasposedComposeBlocks;
		this.form = form;
	}

	public FormCompositionStep() {}

	public List<ComposeBlock> getTrasposedComposeBlocks() {
		return trasposedComposeBlocks;
	}

	public void setTrasposedComposeBlocks( List<ComposeBlock> trasposedComposeBlocks ) {
		this.trasposedComposeBlocks = trasposedComposeBlocks;
	}

	public void addNextExclusion( List<ComposeBlock> musicBlocks ) {
		this.nextMusicBlockExclusions.add( musicBlocks );
	}

	public List<ComposeBlock> getOriginComposeBlocks() {
		return originComposeBlocks;
	}

	public void setOriginComposeBlocks( List<ComposeBlock> originComposeBlocks ) {
		this.originComposeBlocks = originComposeBlocks;
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