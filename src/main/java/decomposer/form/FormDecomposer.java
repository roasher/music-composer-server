package decomposer.form;

import decomposer.form.analyzer.FormEqualityAnalyzerImpl;
import model.melody.Form;
import model.melody.Melody;
import model.MusicBlock;
import model.composition.Composition;
import decomposer.form.slicer.CompositionSlicer;
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
	 * Decomposing composition into music blocks that has rhythmValue length, setting form into each music block
	 * @param composition
	 * @param rhythmValue
	 * @return
	 */
	public List<MusicBlock> decompose( Composition composition, double rhythmValue ) {
		int formCounter = 0;
		List<MusicBlock> musicBlocks = compositionSlicer.slice( composition, rhythmValue );
		char[] highestFormValues = new char[ musicBlocks.get( 0 ).getMelodyList().size() ];
		for ( int currentHighestFormValue = 0; currentHighestFormValue < highestFormValues.length; currentHighestFormValue ++ ) {
			highestFormValues[ currentHighestFormValue ] = 'A';
		}
		// Iterating through all music blocks
		for ( int musicBlockNumber = 0; musicBlockNumber < musicBlocks.size(); musicBlockNumber++ ) {
			MusicBlock musicBlock = musicBlocks.get( musicBlockNumber );
			boolean[] hasFoundEqualFormValue = new boolean[ musicBlock.getMelodyList().size() ];
			// Comparing current music block with all previous music blocks searching for equality - setting value if equality occrus and highest value + 1 if not
			for ( int musicBlockToCompareWithNumber = 0; musicBlockToCompareWithNumber < musicBlockNumber; musicBlockToCompareWithNumber++ ) {
				MusicBlock musicBlockToCompareWith = musicBlocks.get( musicBlockToCompareWithNumber );
				// We are checking equality for every melody
				List<Melody> melodies = musicBlock.getMelodyList();
				List<Melody> melodiesToCompareWith = musicBlockToCompareWith.getMelodyList();
				for ( int currentMelodyNumber = 0; currentMelodyNumber < melodies.size(); currentMelodyNumber ++ ) {
					if ( hasFoundEqualFormValue[ currentMelodyNumber ] ) {
						// once equality has been found there is no more search necessary
					} else {
						char formInstrumentValue = melodiesToCompareWith.get( currentMelodyNumber ).getForm().getValue();
						if ( formEqualityAnalyzer.isEqual( melodies.get( currentMelodyNumber ), melodiesToCompareWith.get( currentMelodyNumber ) ) ) {
							melodies.get( currentMelodyNumber ).getForm().setValue( formInstrumentValue );
							hasFoundEqualFormValue[ currentMelodyNumber ] = true;
						} else {
							// if we found local maximum - it means that we have tested equality for all of the chars include max
							// and we didn't find one equal - so setting the next char value and renewing the state
							if ( formInstrumentValue == highestFormValues[ currentMelodyNumber ] ) {
								highestFormValues[ currentMelodyNumber ]++;
								melodies.get( currentMelodyNumber ).getForm().setValue( highestFormValues[currentMelodyNumber] );
								hasFoundEqualFormValue[ currentMelodyNumber ] = true;
							}
						}
					}
				}
			}
		}
		return musicBlocks;
	}

	public List<Form> getInstrumentForm( int instrumentNumber, Composition composition, double rhythmValue ) {
		List<Form> formList = new ArrayList<>();
		List<MusicBlock> musicBlockList = decompose( composition, rhythmValue );
		for ( MusicBlock musicBlock : musicBlockList ) {
			formList.add( musicBlock.getMelodyList().get( instrumentNumber ).getForm() );
		}
		return formList;
	}
}
