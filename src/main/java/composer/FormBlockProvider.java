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
	 * @param form - form, form part of witch are going to be generated
	 * @param lexicon - Music Block's database
	 * @return
	 */
	public MusicBlock getFormElement( Form form, double length, List<CompositionStep> previousSteps, Lexicon lexicon ) {
		List<MusicBlock> exclusion = new ArrayList<>();
		while ( true ) {
			MusicBlock musicBlock = getMusicBlock( previousSteps.get( previousSteps.size() - 1 ).getMusicBlock(), length, lexicon, exclusion );
			if ( musicBlock != null ) {
				if ( formEqualityAnalyser.isEqualToAnyMusicBlock( musicBlock, getMusicBlocksHavingCertainForm( previousSteps, form ) ) ) {
					return musicBlock;
				} else {
					exclusion.add( musicBlock );
					continue;
				}
			} else {
				logger.warn( "Can't get music block: not enough lexicon" );
			}
		}
	}

	public MusicBlock getMusicBlock( MusicBlock previousMusicBlock, double length,  Lexicon lexicon, List<MusicBlock> exclusion ) {
		/**
		 * Calculating variants: how one can put up length with elements of rhythmValues array
		 * Iterating through this variants choosing first convenient
		 */
		List<Double> rhythmValues = lexicon.getSortedRhythmValues();
		List<List<Double>> variants = Utils.getVariantsOfDistribution( rhythmValues, length );
variant:for ( List<Double> variant : variants ) {
			// Variant handling
			List<CompositionStep> compositionSteps = new ArrayList<>(  );
			for ( int rhythmValueNumber = 0; rhythmValueNumber < variant.size(); rhythmValueNumber++ ) {

				CompositionStep lastCompositionStep = rhythmValueNumber != 0 ? compositionSteps.get( compositionSteps.size() - 1 ) : new CompositionStep( previousMusicBlock );

				MusicBlock nextMusicBlock = musicBlockProvider.getFirstConvenientMusicBlock(
				  compositionSteps.get( rhythmValueNumber ).getMusicBlock(),
				  lexicon.getMusicBlockList( variant.get( rhythmValueNumber ) ),
				  compositionSteps.get( rhythmValueNumber ).getNextMusicBlockExclusion() );

				CompositionStep nextStep = new CompositionStep( nextMusicBlock );

				if ( nextStep.getMusicBlock() == null ) {
					if ( rhythmValueNumber != 0 ) {
						// there is no pre last step if we can't create second element
						if ( rhythmValueNumber != 1 ) {
							CompositionStep preLastCompositionStep = compositionSteps.get( rhythmValueNumber - 2 );
							preLastCompositionStep.addNextExclusion( lastCompositionStep.getMusicBlock() );
						}
						// subtracting 2 because on the next iteration formElementNumber will be added one and we need to work with previous
						compositionSteps.remove( rhythmValueNumber -1 );
						rhythmValueNumber = rhythmValueNumber - 2;
						continue;
					} else {
						logger.info( "There is no possible ways to compose new MisicBlock considering this variant: {}", variant );
						break variant;
					}
				} else {
					compositionSteps.add( nextStep );
				}
			}

			List<MusicBlock> resultMusicBlockList = Utils.getMusicBlocksList( compositionSteps );
			MusicBlock resultMusicBlock = Utils.gatherMusicBlocks( resultMusicBlockList, null );
			if ( !exclusion.contains( resultMusicBlock ) ) {
				return resultMusicBlock;
			} else {
				logger.warn( "Music block has been composed, but was found in exclusion list" );
			}
		}
		return null;
	}

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
