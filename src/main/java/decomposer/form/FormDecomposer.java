package decomposer.form;

import decomposer.form.analyzer.FormEqualityAnalyzerImpl;
import model.melody.Form;
import model.melody.Melody;
import model.composition.Composition;
import utils.CompositionSlicer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides form analyses
 * Created by night wish on 08.11.14.
 */
@Component
public class FormDecomposer {
	@Autowired
	private CompositionSlicer compositionSlicer;
//	@Autowired
//	private FormAnalyzer formAnalyzer;
	@Autowired
	private FormEqualityAnalyzerImpl formEqualityAnalyzer;

	private Logger logger = LoggerFactory.getLogger( getClass() );

	/**
	 * Decomposing composition into List of Melody Blocks that has rhythmValue length, setting form into each melody
	 * @param composition
	 * @param rhythmValue
	 * @return
	 */
	public List< List< Melody > > decompose( Composition composition, double rhythmValue ) {

		List< List< Melody > > melodyBlockList = compositionSlicer.slice( composition, rhythmValue );
		
		char[] highestFormValues = new char[ melodyBlockList.get( 0 ).size() ];
		for ( int currentHighestFormValue = 0; currentHighestFormValue < highestFormValues.length; currentHighestFormValue ++ ) {
			highestFormValues[ currentHighestFormValue ] = 'A';
		}
		// Iterating through all melody blocks
		for ( int melodyBlockNumber = 0; melodyBlockNumber < melodyBlockList.size(); melodyBlockNumber++ ) {
			List< Melody > melodyBlock = melodyBlockList.get( melodyBlockNumber );
			boolean[] hasFoundEqualFormValue = new boolean[ melodyBlock.size() ];
			// Comparing current music block with all previous music blocks searching for equality - setting value if equality occurs and highest value + 1 if not
			for ( int melodyBlockToCompareWithNumber = 0; melodyBlockToCompareWithNumber < melodyBlockNumber; melodyBlockToCompareWithNumber++ ) {
				List< Melody > melodyBlockToCompareWith = melodyBlockList.get( melodyBlockToCompareWithNumber );
				// We are checking equality for every melody
				for ( int instrumentNumber = 0; instrumentNumber < melodyBlock.size(); instrumentNumber ++ ) {
					if ( hasFoundEqualFormValue[ instrumentNumber ] ) {
						// once equality has been found there is no more search necessary
					} else {
						char formInstrumentValue = melodyBlockToCompareWith.get( instrumentNumber ).getForm().getValue();
						if ( formEqualityAnalyzer.isEqual( melodyBlock.get( instrumentNumber ), melodyBlockToCompareWith.get( instrumentNumber ) ) ) {
							melodyBlock.get( instrumentNumber ).getForm().setValue( formInstrumentValue );
							hasFoundEqualFormValue[ instrumentNumber ] = true;
						} else {
							// if we found local maximum - it means that we have tested equality for all of the chars include max
							// and we didn't find one equal - so setting the next char value and renewing the state
							if ( formInstrumentValue == highestFormValues[ instrumentNumber ] ) {
								highestFormValues[ instrumentNumber ]++;
								melodyBlock.get( instrumentNumber ).getForm().setValue( highestFormValues[instrumentNumber] );
								hasFoundEqualFormValue[ instrumentNumber ] = true;
							}
						}
					}
				}
			}
		}
		return melodyBlockList;
	}

	public List<Form> getInstrumentForm( int instrumentNumber, Composition composition, double rhythmValue ) {
		List<Form> formList = new ArrayList<>();
		List< List< Melody > > melodyBlockList = decompose( composition, rhythmValue );
		for ( List< Melody > melodyBlock : melodyBlockList ) {
			formList.add( melodyBlock.get( instrumentNumber ).getForm() );
		}
		return formList;
	}

	/**
	 * Divides music melody block into blocks that has at least one note without pitch change.
	 * @param melodyList
	 * @return
	 */
	public List<List<Melody>> divideMusicMelodyBlock ( List<Melody> melodyList ) {
		return null;
	}

}
