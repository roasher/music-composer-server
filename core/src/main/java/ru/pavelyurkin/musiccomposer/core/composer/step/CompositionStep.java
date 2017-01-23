package ru.pavelyurkin.musiccomposer.core.composer.step;

import ru.pavelyurkin.musiccomposer.core.model.ComposeBlock;
import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;

import java.util.*;

/**
 * Class represents step that program makes in order to create new form block
 * One step - one added originComposeBlock to the form block
 *
 * Created by pyurkin on 15.01.15.
 */
public class CompositionStep {

	/**
	 * Compose Block that is base for current composition step
	 */
	private ComposeBlock originComposeBlock;
	/**
	 * Compose Block that is part of composed composition
	 */
	private MusicBlock transposedBlock;

	/**
	 * Valid Music Blocks which can come next to this, but their usage leads to dead end in future cause of small lexicon.
	 */
	private List<ComposeBlock> nextMusicBlockExclusions = new ArrayList<>(  );

	public CompositionStep( ComposeBlock originComposeBlock, MusicBlock transposedBlock ) {
		this.originComposeBlock = originComposeBlock;
		this.transposedBlock = transposedBlock;
	}

	public void addNextExclusion( ComposeBlock musicBlock ) {
		this.nextMusicBlockExclusions.add( musicBlock );
	}

	public ComposeBlock getOriginComposeBlock() {
		return originComposeBlock;
	}

	public void setOriginComposeBlock( ComposeBlock originComposeBlock ) {
		this.originComposeBlock = originComposeBlock;
	}

	public List<ComposeBlock> getNextMusicBlockExclusions() {
		return nextMusicBlockExclusions;
	}

	public void setNextMusicBlockExclusions( List<ComposeBlock> nextMusicBlockExclusions ) {
		this.nextMusicBlockExclusions = nextMusicBlockExclusions;
	}

	public MusicBlock getTransposedBlock() {
		return transposedBlock;
	}

	public void setTransposedBlock( MusicBlock transposedBlock ) {
		this.transposedBlock = transposedBlock;
	}

}