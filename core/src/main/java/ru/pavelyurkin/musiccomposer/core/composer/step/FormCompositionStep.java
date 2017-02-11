package ru.pavelyurkin.musiccomposer.core.composer.step;

import ru.pavelyurkin.musiccomposer.core.model.MusicBlock;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionSlicer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class represents step that program makes in order to create new composition
 * One step - one added form block to composition
 * <p>
 * Created by pyurkin on 15.01.15.
 */
public class FormCompositionStep {

	private List<CompositionStep> compositionSteps;
	private Form form;

	/**
	 * Returns empty composition step using just for tracking new block exceptions
	 * @return
	 */
	public static FormCompositionStep getEmptyStep() {
		return new FormCompositionStep( Arrays.asList( new CompositionStep( null, null ) ), null );
	}

	public FormCompositionStep( List<CompositionStep> compositionSteps, Form form ) {
		this.compositionSteps = compositionSteps;
		this.form = form;
	}

	public List<CompositionStep> getCompositionSteps() {
		return compositionSteps;
	}

	public void setCompositionSteps( List<CompositionStep> compositionSteps ) {
		this.compositionSteps = compositionSteps;
	}

	public Form getForm() {
		return form;
	}

	public void setForm( Form form ) {
		this.form = form;
	}

}