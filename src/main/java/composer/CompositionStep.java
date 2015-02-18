package composer;

import model.MusicBlock;
import model.melody.Form;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class represents step that program makes in order to create new composition
 * One step - one added form block to the composition
 *
 * Created by pyurkin on 15.01.15.
 */
public class CompositionStep {

	private MusicBlock musicBlock;
	private Form form;
	/**
	 * Valid Music Blocks which can come next to this, but their usage leads to dead end in future cause of small lexicon.
	 */
	private List<MusicBlock> nextMusicBlockExclusion = new ArrayList<>(  );

	public CompositionStep( MusicBlock musicBlock, Form form ) {
		this.musicBlock = musicBlock;
		this.form = form;
	}

	public CompositionStep( MusicBlock musicBlock ) { this.musicBlock = musicBlock; }

	public CompositionStep() {}

	public void addNextExclusion( MusicBlock musicBlock ) {
		this.nextMusicBlockExclusion.add( musicBlock );
	}

	public List<MusicBlock> getNextMusicBlockExclusion() {
		return nextMusicBlockExclusion;
	}

	public MusicBlock getMusicBlock() {
		return musicBlock;
	}

	public void setMusicBlock( MusicBlock musicBlock ) {
		this.musicBlock = musicBlock;
	}

	public Form getForm() {
		return form;
	}

	public void setForm( Form form ) {
		this.form = form;
	}
}