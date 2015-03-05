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
		/**
		 * Calculating variants: how one can put up length with elements of rhythmValues array
		 * Iterating through this variants choosing first convenient
		 */
		List<List<Double>> variants = Utils.getVariantsOfDistribution( lexicon.getRhythmValueQuantityMap(), length );
		for ( List<Double> variant : variants ) {
			MusicBlock musicBlock = handleVariant( variant, previousMusicBlock, lexicon );
			if ( musicBlock == null ) {
				logger.info( "There is no possible ways to compose new MusicBlock considering this variant: {}", variant );
				continue;
			}
			if ( !Utils.containsMelody( musicBlock, exclusion ) ) {
				return musicBlock;
			} else {
				logger.warn( "Music block has been composed, but was found in exclusion list" );
			}
		}
		return null;
	}

	/**
	 * Gets music block that can be put next to previousMusicBlock that inside has rhythmValueVariant structure
	 * @param variant
	 * @param previousMusicBlock
	 * @param lexicon
	 * @return
	 */
	public MusicBlock handleVariant( List<Double> variant, MusicBlock previousMusicBlock, Lexicon lexicon ) {
		// Variant handling
		List<CompositionStep> compositionSteps = new ArrayList<>(  );
		compositionSteps.add( new CompositionStep( previousMusicBlock ) );
		for ( int rhythmValueNumber = 0; rhythmValueNumber < variant.size(); rhythmValueNumber++ ) {

			CompositionStep lastCompositionStep = compositionSteps.get( compositionSteps.size() - 1 );

			MusicBlock nextMusicBlock = musicBlockProvider.getFirstConvenientMusicBlock(
			  lastCompositionStep.getMusicBlock(),
			  lexicon.getMusicBlockList( variant.get( rhythmValueNumber ) ),
			  lastCompositionStep.getNextMusicBlockExclusion() );

			CompositionStep nextStep = new CompositionStep( nextMusicBlock );

			if ( nextStep.getMusicBlock() == null ) {
				if ( rhythmValueNumber != 0 ) {
					CompositionStep preLastCompositionStep = compositionSteps.get( compositionSteps.size() - 2 );
					preLastCompositionStep.addNextExclusion( lastCompositionStep.getMusicBlock() );
					// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
					compositionSteps.remove( compositionSteps.size() -1 );
					rhythmValueNumber = rhythmValueNumber - 2;
					continue;
				} else {
					break;
				}
			} else {
				compositionSteps.add( nextStep );
			}
		}
		compositionSteps.remove( 0 );
		if ( compositionSteps.size() == 0 ) return null;
		// gathering MusicBlock
		return new MusicBlock( null, Utils.getMusicBlocksList( compositionSteps ) );
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
