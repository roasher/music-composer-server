package composer;

import model.Lexicon;
import model.MusicBlock;
import model.composition.Composition;
import model.melody.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handles composition of new piece using lexicon
 * Created by pyurkin on 15.12.14.
 */
@Component
public class CompositionComposer {
	private Logger logger = LoggerFactory.getLogger( getClass() );

	@Autowired
	private FormBlockProvider formBlockProvider;

	/**
	 * Composing piece considering given lexicon, form pattern and composition compositionLength
	 * @param lexicon
	 * @param form
	 * @param compositionLength
	 * @return
	 */
	public Composition compose( Lexicon lexicon, String form, double compositionLength ) {
		List<CompositionStep> compositionSteps = composeSteps( lexicon, form, compositionLength );
		List<MusicBlock> musicBlocks = new ArrayList<>(  );
		for ( CompositionStep compositionStep : compositionSteps ) {
			musicBlocks.add( compositionStep.getMusicBlock() );
		}
		return Utils.gatherComposition( musicBlocks );
	}

	/**
	 * Main composing function.
	 * Assuming we are on k-th step of composing.
	 * Composing k+1 music block.
	 * If it is impossible, than recomposing k-th music block.
	 * If it is impossible, going back to k-1 and so on.
	 * @param lexicon
	 * @param form
	 * @param compositionLength
	 * @return
	 */
	public List<CompositionStep> composeSteps( Lexicon lexicon, String form, double compositionLength ) {
		List<CompositionStep> compositionSteps = new ArrayList<>(  );
		for ( int formElementNumber = 0; formElementNumber < form.length(); formElementNumber++ ) {

			CompositionStep lastCompositionStep = formElementNumber != 0 ? compositionSteps.get( compositionSteps.size() - 1 ) : null;
			CompositionStep nextStep = composeNext( new Form( form.charAt( formElementNumber ) ), compositionLength/form.length(), compositionSteps,  lexicon );

			if ( nextStep.getMusicBlock() == null ) {
				if ( formElementNumber != 0 ) {
					// there is no pre last step if we can't create second element
					if ( formElementNumber != 1 ) {
						CompositionStep preLastCompositionStep = compositionSteps.get( formElementNumber - 2 );
						preLastCompositionStep.addNextExclusion( lastCompositionStep.getMusicBlock() );
					}
					// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
					compositionSteps.remove( formElementNumber -1 );
					formElementNumber = formElementNumber - 2;
					continue;
				} else {
					logger.warn( "There is no possible ways to composeSteps new piece considering such parameters" );
					break;
				}
			} else {
				compositionSteps.add( nextStep );
			}
		}
		return compositionSteps;
	}

	public CompositionStep composeNext( Form form, double length, List<CompositionStep> previousSteps, Lexicon lexicon ) {
		MusicBlock musicBlock = formBlockProvider.getFormElement( form, length, previousSteps, lexicon );
		// FIXME figure out legality of using "new" operator
		return new CompositionStep( musicBlock );
	}
}