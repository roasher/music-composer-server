package composer;

import decomposer.form.analyzer.MusicBlockFormEqualityAnalyser;
import model.ComposeBlock;
import model.Lexicon;
import model.MusicBlock;
import model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class provides form element
 * Created by pyurkin on 16.01.15.
 */
@Component
public class FormBlockProvider {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	@Autowired
	private MusicBlockProvider musicBlockProvider;

	@Autowired
	private MusicBlockFormEqualityAnalyser formEqualityAnalyser;

	/**
	 * Generates new form block considering previously generated blocks and it's form.
	 * @param form - form, from part of witch are going to be generated new Music Block
	 * @param lexicon - Music Block's database
	 * @return
	 */
	public ComposeBlock getFormElement( Form form, double length, List<CompositionStep> previousSteps, Lexicon lexicon ) {
		logger.info( "Composing new form element : {}, length : {}", form.getValue(), length );
		List<ComposeBlock> previouslyComposedMusicBlocksHavingCertainForm = getComposeBlocksHavingCertainForm( previousSteps, form );
		// TODO implementation
		return null;
	}

	/**
	 * Retrieves composed music blocks having particular input form
	 * @param compositionSteps
	 * @param form
	 * @return
	 */
	private List<ComposeBlock> getComposeBlocksHavingCertainForm( List<CompositionStep> compositionSteps, Form form ) {
		List<ComposeBlock> composeBlocksHavingCertainForm = new ArrayList<>(  );
		for ( CompositionStep compositionStep : compositionSteps ) {
			if ( compositionStep.getForm().equals( form ) ) {
				composeBlocksHavingCertainForm.add( compositionStep.getComposeBlock() );
			}
		}
		return composeBlocksHavingCertainForm;
	}
}
