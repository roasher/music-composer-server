package ru.pavelyurkin.musiccomposer.core.decomposer.form;

import lombok.RequiredArgsConstructor;
import ru.pavelyurkin.musiccomposer.core.equality.form.MelodyFormEqualityAnalyzer;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;
import ru.pavelyurkin.musiccomposer.core.model.composition.Composition;
import ru.pavelyurkin.musiccomposer.core.model.melody.Form;
import ru.pavelyurkin.musiccomposer.core.model.melody.Melody;
import ru.pavelyurkin.musiccomposer.core.utils.CompositionSlicer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides form analyses
 * Created by night wish on 08.11.14.
 */
//@Component
@RequiredArgsConstructor
public class FormDecomposer {

	private final CompositionSlicer compositionSlicer;
	private final MelodyFormEqualityAnalyzer formEqualityAnalyzer;

	/**
	 * Decomposing composition into List of Melody Blocks that has rhythmValue length, setting form into each melody
	 *
	 * @param composition
	 * @param rhythmValue
	 * @return
	 */
	public List<List<InstrumentPart>> decompose( Composition composition, double rhythmValue ) {

//		List<List<InstrumentPart>> instrumentPartsCollection = compositionSlicer.slice( composition, rhythmValue );
//
//		char[] highestFormValues = new char[instrumentPartsCollection.get( 0 ).size()];
//		for ( int currentHighestFormValue = 0;
//			  currentHighestFormValue < highestFormValues.length; currentHighestFormValue++ ) {
//			highestFormValues[currentHighestFormValue] = 'A';
//		}
//		// Iterating through all melody blocks
//		for ( int melodyBlockNumber = 0; melodyBlockNumber < instrumentPartsCollection.size(); melodyBlockNumber++ ) {
//			List<InstrumentPart> block = instrumentPartsCollection.get( melodyBlockNumber );
//			boolean[] hasFoundEqualFormValue = new boolean[block.size()];
//			// Comparing current music block with all previous music blocks searching for ru.pavelyurkin.musiccomposer.equality - setting value if ru.pavelyurkin.musiccomposer.equality occurs and highest value + 1 if not
//			for ( int melodyBlockToCompareWithNumber = 0;
//				  melodyBlockToCompareWithNumber < melodyBlockNumber; melodyBlockToCompareWithNumber++ ) {
//				List<InstrumentPart> melodyBlockToCompareWith = instrumentPartsCollection.get( melodyBlockToCompareWithNumber );
//				// We are checking ru.pavelyurkin.musiccomposer.equality for every melody
//				for ( int instrumentNumber = 0; instrumentNumber < block.size(); instrumentNumber++ ) {
//					if ( hasFoundEqualFormValue[instrumentNumber] ) {
//						// once equality has been found there is no more search necessary
//					} else {
//						char formInstrumentValue = melodyBlockToCompareWith.get( instrumentNumber ).getForm()
//								.getValue();
//						if ( formEqualityAnalyzer.isEqual( block.get( instrumentNumber ),
//								melodyBlockToCompareWith.get( instrumentNumber ) ) ) {
//							block.get( instrumentNumber ).getForm().setValue( formInstrumentValue );
//							hasFoundEqualFormValue[instrumentNumber] = true;
//						} else {
//							// if we found local maximum - it means that we have tested ru.pavelyurkin.musiccomposer.equality for all of the chars include max
//							// and we didn't find one equal - so setting the next char value and renewing the state
//							if ( formInstrumentValue == highestFormValues[instrumentNumber] ) {
//								highestFormValues[instrumentNumber]++;
//								block.get( instrumentNumber ).getForm()
//										.setValue( highestFormValues[instrumentNumber] );
//								hasFoundEqualFormValue[instrumentNumber] = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		return instrumentPartsCollection;
		//todo
		return null;
	}

	public List<Form> getInstrumentForm( int instrumentNumber, Composition composition, double rhythmValue ) {
		List<Form> formList = new ArrayList<>();
		// TODO impl
//		List<List<Melody>> melodyBlockList = decompose( composition, rhythmValue );
//		for ( List<Melody> melodyBlock : melodyBlockList ) {
//			formList.add( melodyBlock.get( instrumentNumber ).getForm() );
//		}
		return formList;
	}

	/**
	 * Divides music melody block into blocks that has at least one note without pitch change.
	 *
	 * @param melodyList
	 * @return
	 */
	public List<List<Melody>> divideMusicMelodyBlock( List<Melody> melodyList ) {
		return null;
	}

}
