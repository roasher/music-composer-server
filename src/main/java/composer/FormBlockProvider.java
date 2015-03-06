package composer;

import decomposer.form.analyzer.MusicBlockFormEqualityAnalyser;
import model.Lexicon;
import model.MusicBlock;
import model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.Utils;

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
	public MusicBlock getFormElement( Form form, double length, List<CompositionStep> previousSteps, Lexicon lexicon ) {
		logger.info( "Composing new form element : {}, length : {}", form.getValue(), length );
		List<MusicBlock> exclusion = new ArrayList<>();
        // TODO refactor
		while ( true ) {
			MusicBlock previousMusicBlock = previousSteps.size() != 0 ? previousSteps.get( previousSteps.size() - 1 ).getMusicBlock() : null;
			MusicBlock musicBlock = getMusicBlock( previousMusicBlock, length, lexicon, exclusion );
			if ( musicBlock != null ) {
				List<MusicBlock> musicBlocksHavingCertainForm = getMusicBlocksHavingCertainForm( previousSteps, form );
				if ( musicBlocksHavingCertainForm.isEmpty() ) {
					logger.info( "First composed music block of this form flavour" );
					return musicBlock;
				}
				if ( formEqualityAnalyser.isEqualToAnyMusicBlock( musicBlock, musicBlocksHavingCertainForm ) ) {
					return musicBlock;
				} else {
					logger.info( "Composed form element was considered NOT appropriate in terms of recently composed form elements" );
					exclusion.add( musicBlock );
					continue;
				}
			} else {
				logger.warn( "Can't get music block: not enough lexicon" );
			}
		}
	}

	public MusicBlock getMusicBlock( MusicBlock previousMusicBlock, double length, Lexicon lexicon, List<MusicBlock> exclusion ) {
		// TODO implementation
        return null;
	}

	/**
	 * Retrieves composed music blocks having particular input form
	 * @param compositionSteps
	 * @param form
	 * @return
	 */
	private List<MusicBlock> getMusicBlocksHavingCertainForm( List<CompositionStep> compositionSteps, Form form ) {
		List<MusicBlock> musicBlocksOfForm = new ArrayList<>(  );
		for ( CompositionStep compositionStep : compositionSteps ) {
			if ( compositionStep.getForm().equals( form ) ) {
				musicBlocksOfForm.add( compositionStep.getMusicBlock() );
			}
		}
		return musicBlocksOfForm;
	}
}
